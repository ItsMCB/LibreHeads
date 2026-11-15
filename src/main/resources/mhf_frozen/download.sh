#!/usr/bin/env bash
set -uo pipefail

OUT="mhf.json"
FAILED="mhf_failed.txt"

MHF_NAMES=(
  "MHF_Alex" "MHF_Steve" "MHF_Herobrine"
  "MHF_Blaze" "MHF_CaveSpider" "MHF_Chicken" "MHF_Cow" "MHF_Creeper"
  "MHF_Enderman" "MHF_Ghast" "MHF_Golem" "MHF_LavaSlime" "MHF_MushroomCow"
  "MHF_Ocelot" "MHF_Pig" "MHF_PigZombie" "MHF_Sheep" "MHF_Skeleton"
  "MHF_Slime" "MHF_Spider" "MHF_Squid" "MHF_Villager" "MHF_WSkeleton" "MHF_Zombie"
  "MHF_Cactus" "MHF_Cake" "MHF_Chest" "MHF_CoconutB" "MHF_CoconutG"
  "MHF_Melon" "MHF_OakLog" "MHF_Present1" "MHF_Present2" "MHF_Pumpkin"
  "MHF_TNT" "MHF_TNT2"
  "MHF_ArrowUp" "MHF_ArrowDown" "MHF_ArrowLeft" "MHF_ArrowRight"
  "MHF_Exclamation" "MHF_Question"
)

# Retry helper: POST/GET with exponential backoff; 404/204 = don't retry.
fetch() {
  local method="$1" url="$2" body="${3:-}" tries=5 delay=1 i out code
  for (( i=0; i<tries; i++ )); do
    if [[ -n "$body" ]]; then
      out=$(curl -sS --max-time 15 -X POST -H 'Content-Type: application/json' \
            -d "$body" -w $'\n%{http_code}' "$url" 2>/dev/null)
    else
      out=$(curl -sS --max-time 15 -w $'\n%{http_code}' "$url" 2>/dev/null)
    fi
    code="${out##*$'\n'}"
    out="${out%$'\n'*}"
    case "$code" in
      200) printf '%s' "$out"; return 0 ;;
      404|204) return 1 ;;
    esac
    sleep "$delay"; (( delay *= 2 )) || true
  done
  return 1
}

# --- Phase 1: bulk name -> UUID lookup (10 names per request) ---------------
# Mojang drops nonexistent names from the response, so a missing name here
# means "no such profile" — it can't be rescued by retrying.
MAP_FILE=$(mktemp)
RESOLVED=$(mktemp)
echo "Bulk-resolving ${#MHF_NAMES[@]} names ..."
for (( i=0; i<${#MHF_NAMES[@]}; i+=10 )); do
  chunk=$(printf '%s\n' "${MHF_NAMES[@]:i:10}" | jq -R . | jq -sc .)
  resp=$(fetch POST "https://api.minecraftservices.com/minecraft/profile/lookup/bulk/byname" "$chunk" \
      || fetch POST "https://api.mojang.com/profiles/minecraft" "$chunk") || true
  [[ -n "$resp" ]] && printf '%s' "$resp" | jq -r '.[] | "\(.name)\t\(.id)"' >> "$MAP_FILE"
  sleep 0.5
done

: > "$FAILED"
echo "[" > "$OUT"
FIRST=1

# --- Phase 2: one session-server call per UUID (only the ~37 that resolved) --
cut -f1 "$MAP_FILE" > "$RESOLVED"
while IFS=$'\t' read -r NAME UUID; do
  echo "Fetching textures for $NAME ..."
  PROFILE=$(fetch GET "https://sessionserver.mojang.com/session/minecraft/profile/${UUID}?unsigned=false") || {
    echo "  -> failed"; echo "$NAME" >> "$FAILED"; continue
  }
  VALUE=$(printf '%s' "$PROFILE" | jq -r '.properties[0].value // empty')
  SIG=$(printf '%s' "$PROFILE" | jq -r '.properties[0].signature // empty')
  if [[ -z "$VALUE" ]]; then
    echo "  -> no texture data"; echo "$NAME" >> "$FAILED"; continue
  fi

  [[ $FIRST -eq 0 ]] && echo "," >> "$OUT"
  FIRST=0
  jq -n --arg name "$NAME" --arg uuid "$UUID" --arg value "$VALUE" --arg sig "$SIG" \
    '{name: $name, uuid: $uuid, value: $value, signature: $sig}' >> "$OUT"
  sleep 0.3
done < "$MAP_FILE"
rm -f "$MAP_FILE"

# Names the bulk lookup never returned are 404s (name does not exist)
while IFS= read -r NAME; do
  grep -qFx "$NAME" "$RESOLVED" || echo "$NAME" >> "$FAILED"
done < <(printf '%s\n' "${MHF_NAMES[@]}")
rm -f "$RESOLVED"

echo "]" >> "$OUT"
echo "Saved $(jq length "$OUT") MHF heads to $OUT"
if [[ -s "$FAILED" ]]; then
  echo "Failed heads:"; sort -u "$FAILED"
fi


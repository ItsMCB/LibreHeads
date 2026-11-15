package org.libregalaxy.libreheads;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.itsmcb.vexelcore.bukkit.api.framework.VexelCommandRegistry;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public final class LibreHeads extends JavaPlugin {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private LibreHeadsContext context;
    private final List<LibreHead> heads = new CopyOnWriteArrayList<>();
    private final List<LibreHead> mhfHeads = new CopyOnWriteArrayList<>();
    private final Map<LibreHead.CATEGORY, List<LibreHead>> headsByCategory = new EnumMap<>(LibreHead.CATEGORY.class);

    private final DecimalFormat df = new DecimalFormat("###,###");

    public DecimalFormat getDF() {
        return df;
    }

    public List<LibreHead> getHeads() {
        return heads;
    }

    public List<LibreHead> getMhfHeads() {
        return mhfHeads;
    }

    public @NotNull List<LibreHead> getCategoryHeads(@NotNull LibreHead.CATEGORY category) {
        return headsByCategory.getOrDefault(category, Collections.emptyList());
    }

    public List<LibreHead> getAlphabetHeads() {
        return getCategoryHeads(LibreHead.CATEGORY.ALPHABET);
    }

    public List<LibreHead> getAnimalHeads() {
        return getCategoryHeads(LibreHead.CATEGORY.ANIMALS);
    }

    public List<LibreHead> getBlockHeads() {
        return getCategoryHeads(LibreHead.CATEGORY.BLOCKS);
    }

    public List<LibreHead> getDecorationHeads() {
        return getCategoryHeads(LibreHead.CATEGORY.DECORATION);
    }

    public List<LibreHead> getFoodDrinkHeads() {
        return getCategoryHeads(LibreHead.CATEGORY.FOOD_DRINKS);
    }

    public List<LibreHead> getHumanHeads() {
        return getCategoryHeads(LibreHead.CATEGORY.HUMANS);
    }

    public List<LibreHead> getHumanoidHeads() {
        return getCategoryHeads(LibreHead.CATEGORY.HUMANOID);
    }

    public List<LibreHead> getMiscellaneousHeads() {
        return getCategoryHeads(LibreHead.CATEGORY.MISCELLANEOUS);
    }

    public List<LibreHead> getMonsterHeads() {
        return getCategoryHeads(LibreHead.CATEGORY.MONSTERS);
    }

    public List<LibreHead> getPlantHeads() {
        return getCategoryHeads(LibreHead.CATEGORY.PLANTS);
    }

    @Override
    public void onEnable() {
        this.context = new LibreHeadsContext(this);

        heads.clear();
        mhfHeads.clear();
        headsByCategory.clear();

        // Load HDB Heads
        loadHeadsDatabaseFile("alphabet", LibreHead.CATEGORY.ALPHABET, LibreHead.DATABASE.HEADDATABASE);
        loadHeadsDatabaseFile("animals", LibreHead.CATEGORY.ANIMALS, LibreHead.DATABASE.HEADDATABASE);
        loadHeadsDatabaseFile("blocks", LibreHead.CATEGORY.BLOCKS, LibreHead.DATABASE.HEADDATABASE);
        loadHeadsDatabaseFile("decoration", LibreHead.CATEGORY.DECORATION, LibreHead.DATABASE.HEADDATABASE);
        loadHeadsDatabaseFile("food-drinks", LibreHead.CATEGORY.FOOD_DRINKS, LibreHead.DATABASE.HEADDATABASE);
        loadHeadsDatabaseFile("humans", LibreHead.CATEGORY.HUMANS, LibreHead.DATABASE.HEADDATABASE);
        loadHeadsDatabaseFile("humanoid", LibreHead.CATEGORY.HUMANOID, LibreHead.DATABASE.HEADDATABASE);
        loadHeadsDatabaseFile("miscellaneous", LibreHead.CATEGORY.MISCELLANEOUS, LibreHead.DATABASE.HEADDATABASE);
        loadHeadsDatabaseFile("monsters", LibreHead.CATEGORY.MONSTERS, LibreHead.DATABASE.HEADDATABASE);
        loadHeadsDatabaseFile("plants", LibreHead.CATEGORY.PLANTS, LibreHead.DATABASE.HEADDATABASE);

        // Load LibreHeads Heads
        loadHeadsDatabaseFile("miscellaneous", LibreHead.CATEGORY.MISCELLANEOUS, LibreHead.DATABASE.LIBREHEADS);

        // Load Marc's Head Format (MHF) Heads
        loadHeadsDatabaseFile("mhf", null, LibreHead.DATABASE.MHF);

        // Register commands
        VexelCommandRegistry registry = new VexelCommandRegistry(context);
        registry.register(new SkullCmd(context));
    }

    /**
     * Resolves a database file.
     * A copy in the plugin data folder takes priority so servers can update head data without having to rebuild the jar.
     * The bundled resource is the fallback.
     */
    private InputStream resolveDatabaseStream(@NotNull String fileName,
                                              @NotNull LibreHead.DATABASE database) throws IOException {
        String folder = switch (database) {
            case HEADDATABASE -> "hdb_frozen/";
            case MHF -> "mhf_frozen/";
            default -> "";
        };
        String relative = folder + fileName + ".json";
        Path dataFile = getDataFolder().toPath().resolve(relative);
        if (Files.exists(dataFile)) {
            getLogger().info("Loading " + relative + " from plugin data folder instead of the plugin's jar resources snapshot.");
            return Files.newInputStream(dataFile);
        }
        return getResource(relative);
    }

    private void loadHeadsDatabaseFile(@NotNull String fileName,
                                       @Nullable LibreHead.CATEGORY category,
                                       @NotNull LibreHead.DATABASE database) {
        try (InputStream inputStream = resolveDatabaseStream(fileName, database)) {
            if (inputStream == null) {
                getLogger().warning("Couldn't find \"" + fileName + "\" in database type \"" + database + "\". Ignoring...");
                return;
            }
            JsonNode root = MAPPER.readTree(inputStream);
            if (root == null || !root.isArray()) {
                getLogger().warning("Invalid JSON array in \"" + fileName + "\" for database type \"" + database + "\". Ignoring...");
                return;
            }
            List<LibreHead> categoryList = category != null
                    ? headsByCategory.computeIfAbsent(category, k -> new ArrayList<>())
                    : null;

            for (JsonNode element : root) {
                if (!element.hasNonNull("name") || !element.hasNonNull("value")) {
                    continue;
                }
                LibreHead head = new LibreHead(element.get("name").asText(), element.get("value").asText())
                        .category(category)
                        .database(database);

                if (element.hasNonNull("signature")) {
                    head.signature(element.get("signature").asText());
                }
                if (element.hasNonNull("tags")) {
                    JsonNode tagsNode = element.get("tags");
                    if (tagsNode.isArray()) {
                        List<String> tagList = new ArrayList<>();
                        tagsNode.forEach(t -> tagList.add(t.asText()));
                        head.tags(tagList);
                    } else if (tagsNode.isTextual()) {
                        head.tags(Arrays.stream(tagsNode.asText().split(","))
                                .map(String::trim)
                                .filter(s -> !s.isEmpty())
                                .toList());
                    }
                }

                heads.add(head);
                if (categoryList != null) {
                    categoryList.add(head);
                }
                if (database == LibreHead.DATABASE.MHF) {
                    mhfHeads.add(head);
                }
            }
        } catch (Exception e) {
            getLogger().warning("Couldn't load \"" + fileName + "\" in database type \"" + database + "\": " + e.getMessage());
        }
    }
}
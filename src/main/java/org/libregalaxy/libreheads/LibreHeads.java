package org.libregalaxy.libreheads;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.itsmcb.vexelcore.bukkit.api.managers.BukkitFeatureManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.codehaus.plexus.util.IOUtil;
import org.libregalaxy.libreheads.features.SkullFeat;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public final class LibreHeads extends JavaPlugin {

    private List<LibreHead> heads = new ArrayList<>();

    private final DecimalFormat df = new DecimalFormat("###,###");

    public DecimalFormat getDF() {
        return df;
    }

    public List<LibreHead> getHeads() {
        return heads;
    }

    public List<LibreHead> getAlphabetHeads() {
        return getHeads().stream().filter(head -> head.getCategory().equals(LibreHead.CATEGORY.ALPHABET)).toList();
    }

    public List<LibreHead> getAnimalHeads() {
        return getHeads().stream().filter(head -> head.getCategory().equals(LibreHead.CATEGORY.ANIMALS)).toList();
    }

    public List<LibreHead> getBlockHeads() {
        return getHeads().stream().filter(head -> head.getCategory().equals(LibreHead.CATEGORY.BLOCKS)).toList();
    }

    public List<LibreHead> getDecorationHeads() {
        return getHeads().stream().filter(head -> head.getCategory().equals(LibreHead.CATEGORY.DECORATION)).toList();
    }

    public List<LibreHead> getFoodDrinkHeads() {
        return getHeads().stream().filter(head -> head.getCategory().equals(LibreHead.CATEGORY.FOOD_DRINKS)).toList();
    }

    public List<LibreHead> getHumanHeads() {
        return getHeads().stream().filter(head -> head.getCategory().equals(LibreHead.CATEGORY.HUMANS)).toList();
    }

    public List<LibreHead> getHumanoidHeads() {
        return getHeads().stream().filter(head -> head.getCategory().equals(LibreHead.CATEGORY.HUMANOID)).toList();
    }

    public List<LibreHead> getMiscellaneousHeads() {
        return getHeads().stream().filter(head -> head.getCategory().equals(LibreHead.CATEGORY.MISCELLANEOUS)).toList();
    }

    public List<LibreHead> getMonsterHeads() {
        return getHeads().stream().filter(head -> head.getCategory().equals(LibreHead.CATEGORY.MONSTERS)).toList();
    }

    public List<LibreHead> getPlantHeads() {
        return getHeads().stream().filter(head -> head.getCategory().equals(LibreHead.CATEGORY.PLANTS)).toList();
    }

    @Override
    public void onEnable() {
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

        // Register features
        BukkitFeatureManager bukkitFeatureManager = new BukkitFeatureManager();
        bukkitFeatureManager.register(new SkullFeat(this));
        bukkitFeatureManager.reload();
    }

    private void loadHeadsDatabaseFile(String fileName, LibreHead.CATEGORY category, LibreHead.DATABASE database) {
        try {
            String resource = "";
            if (database.equals(LibreHead.DATABASE.HEADDATABASE)) {
                resource = "hdb_frozen"+ File.separator;
            }
            String headsJSON = IOUtil.toString(getResource(resource+fileName+".json"));
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(headsJSON);
            for (JsonNode element : root) {
                String name = element.get("name").asText();
                String value = element.get("value").asText();
                heads.add(new LibreHead(name,value).category(category).database(database));
            }
        } catch (Exception e) {
            System.out.println("Couldn't find \"" + fileName + "\" in database type \"" + database + "\". Ignoring...");
        }
    }

}

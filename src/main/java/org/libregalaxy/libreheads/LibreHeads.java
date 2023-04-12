package org.libregalaxy.libreheads;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.itsmcb.vexelcore.bukkit.api.managers.BukkitFeatureManager;
import me.itsmcb.vexelcore.bukkit.api.menuv2.MenuV2Manager;
import org.bukkit.plugin.java.JavaPlugin;
import org.codehaus.plexus.util.IOUtil;
import org.libregalaxy.libreheads.features.SkullFeat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class LibreHeads extends JavaPlugin {

    private BukkitFeatureManager bukkitFeatureManager;

    private MenuV2Manager menuManager;

    public MenuV2Manager getMenuManager() {
        return menuManager;
    }

    public BukkitFeatureManager getBukkitFeatureManager() {
        return bukkitFeatureManager;
    }

    private List<LibreHead> heads = new ArrayList<>();

    public List<LibreHead> getHeads() {
        return heads;
    }

    List<LibreHead> alphabetHeads = new ArrayList<>();

    public List<LibreHead> getAlphabetHeads() {
        return alphabetHeads;
    }

    List<LibreHead> animalHeads = new ArrayList<>();

    public List<LibreHead> getAnimalHeads() {
        return animalHeads;
    }

    List<LibreHead> blockHeads = new ArrayList<>();

    public List<LibreHead> getBlockHeads() {
        return blockHeads;
    }

    List<LibreHead> decorationHeads = new ArrayList<>();

    public List<LibreHead> getDecorationHeads() {
        return decorationHeads;
    }

    List<LibreHead> foodDrinkHeads = new ArrayList<>();

    public List<LibreHead> getFoodDrinkHeads() {
        return foodDrinkHeads;
    }

    List<LibreHead> humanHeads = new ArrayList<>();

    public List<LibreHead> getHumanHeads() {
        return humanHeads;
    }

    List<LibreHead> humanoidHeads = new ArrayList<>();

    public List<LibreHead> getHumanoidHeads() {
        return humanoidHeads;
    }

    List<LibreHead> miscellaneousHeads = new ArrayList<>();

    public List<LibreHead> getMiscellaneousHeads() {
        return miscellaneousHeads;
    }

    List<LibreHead> monsterHeads = new ArrayList<>();

    public List<LibreHead> getMonsterHeads() {
        return monsterHeads;
    }

    List<LibreHead> plantHeads = new ArrayList<>();

    public List<LibreHead> getPlantHeads() {
        return plantHeads;
    }

    List<LibreHead> demoHeads = new ArrayList<>();

    public List<LibreHead> getDemoHeads() {
        return demoHeads;
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

        // Cache heads
        alphabetHeads = getHeads().stream().filter(head -> head.getCategory().equals(LibreHead.CATEGORY.ALPHABET)).toList();
        animalHeads = getHeads().stream().filter(head -> head.getCategory().equals(LibreHead.CATEGORY.ANIMALS)).toList();
        blockHeads = getHeads().stream().filter(head -> head.getCategory().equals(LibreHead.CATEGORY.BLOCKS)).toList();
        decorationHeads = getHeads().stream().filter(head -> head.getCategory().equals(LibreHead.CATEGORY.DECORATION)).toList();
        foodDrinkHeads = getHeads().stream().filter(head -> head.getCategory().equals(LibreHead.CATEGORY.FOOD_DRINKS)).toList();
        humanHeads = getHeads().stream().filter(head -> head.getCategory().equals(LibreHead.CATEGORY.HUMANS)).toList();
        humanoidHeads = getHeads().stream().filter(head -> head.getCategory().equals(LibreHead.CATEGORY.HUMANOID)).toList();
        miscellaneousHeads = getHeads().stream().filter(head -> head.getCategory().equals(LibreHead.CATEGORY.MISCELLANEOUS)).toList();
        monsterHeads = getHeads().stream().filter(head -> head.getCategory().equals(LibreHead.CATEGORY.MONSTERS)).toList();
        plantHeads = getHeads().stream().filter(head -> head.getCategory().equals(LibreHead.CATEGORY.PLANTS)).toList();


        this.menuManager = new MenuV2Manager(this);
        // Register features
        this.bukkitFeatureManager = new BukkitFeatureManager();
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
        } catch (IOException e) {
            System.out.println("Couldn't find \"" + fileName + "\" in database type \"" + database + "\". Ignoring...");
        }
    }

}

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
        loadHeadsDatabaseFile("demo", LibreHead.CATEGORY.DEMO, LibreHead.DATABASE.LIBREHEADS);

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

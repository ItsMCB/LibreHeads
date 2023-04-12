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
        loadHeadsDatabaseFile("alphabet", LibreHead.CATEGORY.ALPHABET, true);
        loadHeadsDatabaseFile("animals", LibreHead.CATEGORY.ANIMALS, true);
        loadHeadsDatabaseFile("blocks", LibreHead.CATEGORY.BLOCKS, true);
        loadHeadsDatabaseFile("decoration", LibreHead.CATEGORY.DECORATION, true);
        loadHeadsDatabaseFile("food-drinks", LibreHead.CATEGORY.FOOD_DRINKS, true);
        loadHeadsDatabaseFile("humans", LibreHead.CATEGORY.HUMANS, true);
        loadHeadsDatabaseFile("humanoid", LibreHead.CATEGORY.HUMANOID, true);
        loadHeadsDatabaseFile("miscellaneous", LibreHead.CATEGORY.MISCELLANEOUS, true);
        loadHeadsDatabaseFile("monsters", LibreHead.CATEGORY.MONSTERS, true);
        loadHeadsDatabaseFile("plants", LibreHead.CATEGORY.PLANTS, true);

        // Load LibreHeads Heads
        loadHeadsDatabaseFile("demo", LibreHead.CATEGORY.DEMO, false);

        this.menuManager = new MenuV2Manager(this);
        // Register features
        this.bukkitFeatureManager = new BukkitFeatureManager();
        bukkitFeatureManager.register(new SkullFeat(this));
        bukkitFeatureManager.reload();
    }

    private void loadHeadsDatabaseFile(String fileName, LibreHead.CATEGORY category, boolean isHDB) {
        try {
            String resource = "";
            if (isHDB) {
                resource = "hdb_frozen"+ File.separator;
            }
            String headsJSON = IOUtil.toString(getResource(resource+fileName+".json"));
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(headsJSON);
            for (JsonNode element : root) {
                String name = element.get("name").asText();
                String value = element.get("value").asText();
                heads.add(new LibreHead(name, value).category(category));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadLibreHeadsHeadsExperiment() {
        try {
            String headsJSON = IOUtil.toString(getResource("heads.json"));
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(headsJSON);
            JsonNode categoriesNode = root.get("categories");
            if (categoriesNode != null) {
                JsonNode alphabetNode = categoriesNode.get("alphabet");
                if (alphabetNode != null && alphabetNode.isArray()) {
                    for (JsonNode elementNode : alphabetNode) {
                        String name = elementNode.get("name").asText();
                        String value = elementNode.get("value").asText();
                        JsonNode tagsNode = elementNode.get("tags");
                        heads.add(new LibreHead(name, value, tagsNode.findValuesAsText("")));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

package org.libregalaxy.libreheads;

import me.itsmcb.vexelcore.bukkit.api.menuv2.MenuV2Item;
import me.itsmcb.vexelcore.bukkit.api.menuv2.SkullBuilder;
import me.itsmcb.vexelcore.bukkit.api.text.BukkitMsgBuilder;

import java.util.List;

public class LibreHead {

    public enum CATEGORY {
        ALPHABET(new HeadCategory("Alphabet").description("Letters, numbers, and special characters.")),
        ANIMALS(new HeadCategory("Animals").description("Real and fictional animals and animal characters.")),
        BLOCKS(new HeadCategory("Blocks").description("Miniature blocks.")),
        DECORATION(new HeadCategory("Decoration").description("Furniture items, props, etc.")),
        FOOD_DRINKS(new HeadCategory("Food and Drink").description("Food and drinks.")),
        HUMANS(new HeadCategory("Humans").description("Real and fictional human characters.")),
        HUMANOID(new HeadCategory("Humanoid").description("Human-like characters.")),
        MISCELLANEOUS(new HeadCategory("Miscellaneous").description("A variety of various things.")),
        MONSTERS(new HeadCategory("Monsters").description("Aliens, ghosts, and other creatures.")),
        PLANTS(new HeadCategory("Plants").description("Shrubbery, fruit, vegetables, etc."));

        private HeadCategory categorty;

        CATEGORY(HeadCategory category) {
            this.categorty = category;
        }

        public HeadCategory get() {
            return categorty;
        }

        @Override
        public String toString() {
            return String.valueOf(get().getName());
        }
    }

    public enum DATABASE {

        UNKNOWN("Unknown Source"),
        LIBREHEADS("LibreHeads"),
        HEADDATABASE("Head Database");

        private String database;

        DATABASE(String database) {
            this.database = database;
        }

        public String get() {
            return database;
        }

        @Override
        public String toString() {
            return String.valueOf(get());
        }
    }

    private String name;
    private String value;
    private List<String> tags;
    private CATEGORY category;
    private DATABASE database = DATABASE.UNKNOWN;
    private MenuV2Item item;

    // empty constructor for Jackson deserialization
    public LibreHead() {}

    public LibreHead(String name, String value) {
        this.name = name;
        this.value = value;
        this.item = new SkullBuilder(value)
                .name("&r&d&l"+getName());
    }

    public LibreHead(String name, String value, List<String> tags) {
        this(name, value);
        this.tags = tags;
    }

    public MenuV2Item getItem() {
        return item;
    }

    public LibreHead category(CATEGORY category) {
        this.category = category;
        this.item.addLore(new BukkitMsgBuilder("&a"+getCategory().toString()).get());
        return this;
    }

    public LibreHead database(DATABASE database) {
        this.database = database;
        this.item.addLore(new BukkitMsgBuilder("&7"+getDatabase().toString()).get());
        return this;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public List<String> getTags() {
        return tags;
    }

    public CATEGORY getCategory() {
        return category;
    }

    public DATABASE getDatabase() {
        return database;
    }
}

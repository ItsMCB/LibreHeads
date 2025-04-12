package org.libregalaxy.libreheads;

import me.itsmcb.vexelcore.bukkit.api.cache.CachedPlayerV2;

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
        HEADDATABASE("Head Database"),
        PLAYER("Player"),
        ONLINE_PLAYER("Online Player");

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
    private String signature;
    private List<String> tags;
    private CATEGORY category;
    private DATABASE database = DATABASE.UNKNOWN;

    // empty constructor for Jackson deserialization
    public LibreHead() {}

    public LibreHead(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public LibreHead(String name, String value, String signature) {
        this.name = name;
        this.value = value;
        this.signature = signature;
    }

    public LibreHead(CachedPlayerV2 p) {
        this("&d&l"+p.getUsername(),p.getPlayerSkinData().getTexture(),p.getPlayerSkinData().getSignature());
    }

    public LibreHead(String name, String value, List<String> tags) {
        this(name, value);
        this.tags = tags;
    }

    public LibreHead category(CATEGORY category) {
        this.category = category;
        return this;
    }

    public LibreHead database(DATABASE database) {
        this.database = database;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getSignature() {
        return signature;
    }

    public boolean hasSignature() {
        return signature != null;
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

package org.libregalaxy.libreheads;

import java.util.List;

public class LibreHead {

    public enum CATEGORY {
        ALPHABET("Alphabet"),
        ANIMALS("Animals"),
        BLOCKS("Blocks"),
        DECORATION("Decoration"),
        FOOD_DRINKS("Food and Drink"),
        HUMANS("Humans"),
        HUMANOID("Humanoid"),
        MISCELLANEOUS("Miscellaneous"),
        MONSTERS("Monsters"),
        PLANTS("Plants"),
        DEMO("Demo");

        private String categorty;

        CATEGORY(String elementState) {
            this.categorty = elementState;
        }

        public String get() {
            return categorty;
        }

        @Override
        public String toString() {
            return String.valueOf(get());
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

    // empty constructor for Jackson deserialization
    public LibreHead() {}

    public LibreHead(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public LibreHead(String name, String value, List<String> tags) {
        this.name = name;
        this.value = value;
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

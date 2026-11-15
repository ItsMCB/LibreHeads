package org.libregalaxy.libreheads;

public class HeadCategory {
    private final String name;
    private String description;

    public HeadCategory(String name) {
        this.name = name;
    }

    public HeadCategory description(String description) {
        this.description = description;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
package org.libregalaxy.libreheads;

public class HeadCategory {
    String name;
    String description;

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

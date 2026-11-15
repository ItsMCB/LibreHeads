package org.libregalaxy.libreheads;

import me.itsmcb.vexelcore.bukkit.api.framework.AbstractVexelContext;
import org.jetbrains.annotations.NotNull;

public class LibreHeadsContext extends AbstractVexelContext {

    private final LibreHeads plugin;

    public LibreHeadsContext(@NotNull LibreHeads plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    public @NotNull LibreHeads getLibreHeads() {
        return plugin;
    }
}
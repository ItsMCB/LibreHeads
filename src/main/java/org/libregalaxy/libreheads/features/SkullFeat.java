package org.libregalaxy.libreheads.features;

import me.itsmcb.vexelcore.bukkit.BukkitFeature;
import org.libregalaxy.libreheads.LibreHeads;

public class SkullFeat extends BukkitFeature {

    public SkullFeat(LibreHeads instance) {
        super("skull","Get a head",null, instance);
        registerCommand(new SkullCmd(instance, "head"));
        registerCommand(new SkullCmd(instance, "heads"));
        registerCommand(new SkullCmd(instance, "skull"));
        registerCommand(new SkullCmd(instance, "skulls"));
    }
}

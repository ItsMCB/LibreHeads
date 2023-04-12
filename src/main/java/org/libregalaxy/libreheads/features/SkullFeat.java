package org.libregalaxy.libreheads.features;

import me.itsmcb.vexelcore.bukkit.BukkitFeature;
import org.libregalaxy.libreheads.LibreHeads;

public class SkullFeat extends BukkitFeature {

    public SkullFeat(LibreHeads instance) {
        super("skull","Get a skull",null, instance);
        registerCommand(new SkullCmd(instance));
    }
}

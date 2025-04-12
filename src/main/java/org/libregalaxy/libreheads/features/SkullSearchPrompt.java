package org.libregalaxy.libreheads.features;

import me.itsmcb.vexelcore.bukkit.VexelCoreBukkitAPI;
import me.itsmcb.vexelcore.bukkit.api.menu.Menu;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.libregalaxy.libreheads.LibreHeads;

public class SkullSearchPrompt extends StringPrompt {
    @Override
    public @NotNull String getPromptText(@NotNull ConversationContext context) {
        return "Please enter a valid player username";
    }

    @Override
    public @Nullable Prompt acceptInput(@NotNull ConversationContext c, @Nullable String input) {
        LibreHeads instance = (LibreHeads) c.getSessionData("lh");
        Menu pm = (Menu) c.getSessionData("pm");
        Player player = (Player) c.getForWhom();
        VexelCoreBukkitAPI.getMenuManager().open(new SkullCmd(instance,"skull").skullSearch(input,pm),player);
        return END_OF_CONVERSATION;
    }
}

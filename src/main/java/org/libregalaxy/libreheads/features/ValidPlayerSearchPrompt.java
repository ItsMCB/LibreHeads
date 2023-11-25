package org.libregalaxy.libreheads.features;

import me.itsmcb.vexelcore.bukkit.api.menuv2.SkullBuilder;
import me.itsmcb.vexelcore.bukkit.api.text.BukkitMsgBuilder;
import me.itsmcb.vexelcore.bukkit.api.utils.PlayerUtils;
import me.itsmcb.vexelcore.bukkit.plugin.CachedPlayer;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.libregalaxy.libreheads.LibreHeads;

public class ValidPlayerSearchPrompt extends ValidatingPrompt {
    @Override
    public @NotNull String getPromptText(@NotNull ConversationContext context) {
        return "Please enter a valid player username";
    }

    @Override
    protected boolean isInputValid(@NotNull ConversationContext context, @NotNull String input) {
        return PlayerUtils.isValid(input);
    }

    @Override
    protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext c, @NotNull String input) {
        Player player = (Player) c.getForWhom();
        CachedPlayer cachedPlayer = ((LibreHeads) c.getSessionData("lh")).getCacheManager().get(input);
        player.getInventory().addItem(
                new SkullBuilder(cachedPlayer)
                        .name("&d&l"+cachedPlayer.getName())
                        .addLore("&7Player Head")
                        .update()
                        .getCleanItemStack()
        );
        new BukkitMsgBuilder("&7Grabbed &d&l"+input).send(player);
        return END_OF_CONVERSATION;
    }
}

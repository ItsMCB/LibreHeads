package org.libregalaxy.libreheads.features;

import me.itsmcb.vexelcore.bukkit.VexelCoreBukkitAPI;
import me.itsmcb.vexelcore.bukkit.api.cache.CacheManagerV2;
import me.itsmcb.vexelcore.bukkit.api.cache.CachedPlayerV2;
import me.itsmcb.vexelcore.bukkit.api.cache.exceptions.PlayerNotFoundException;
import me.itsmcb.vexelcore.bukkit.api.menu.MenuButton;
import me.itsmcb.vexelcore.bukkit.api.text.BukkitMsgBuilder;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ValidPlayerSearchPrompt extends ValidatingPrompt {
    @Override
    public @NotNull String getPromptText(@NotNull ConversationContext context) {
        return "Please enter a valid player username";
    }

    @Override
    protected boolean isInputValid(@NotNull ConversationContext c, @NotNull String input) {
        return CacheManagerV2.isValidUsernameFormat(input);
    }

    @Override
    protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext c, @NotNull String input) {
        Player player = (Player) c.getForWhom();
        CompletableFuture<CachedPlayerV2> cpf = VexelCoreBukkitAPI.getCacheManager().getCachedPlayer(input);
        cpf.thenAccept(p -> {
            player.getInventory().addItem(new MenuButton(p).name("&d&l"+p.getUsername()).addLore("&7Player Head").getCleanItemStack());
            new BukkitMsgBuilder("&7Grabbed &d&l"+input).send(player);
        });
        cpf.exceptionally(e -> {
            if (e instanceof PlayerNotFoundException) {
                new BukkitMsgBuilder("&cInvalid username: "+input);
            }
            return null;
        });
        return END_OF_CONVERSATION;
    }
}

package org.libregalaxy.libreheads;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import me.itsmcb.vexelcore.bukkit.VexelCoreBukkitAPI;
import me.itsmcb.vexelcore.bukkit.api.cache.CacheManagerV2;
import me.itsmcb.vexelcore.bukkit.api.cache.CachedPlayerV2;
import me.itsmcb.vexelcore.bukkit.api.cache.PlayerSkinData;
import me.itsmcb.vexelcore.bukkit.api.cache.exceptions.DataRequestFailure;
import me.itsmcb.vexelcore.bukkit.api.cache.exceptions.PlayerNotFoundException;
import me.itsmcb.vexelcore.bukkit.api.dialog.VexelInputDialog;
import me.itsmcb.vexelcore.bukkit.api.framework.TextUtils;
import me.itsmcb.vexelcore.bukkit.api.framework.VexelArg;
import me.itsmcb.vexelcore.bukkit.api.framework.VexelCommandNode;
import me.itsmcb.vexelcore.bukkit.api.framework.VexelCommandPath;
import me.itsmcb.vexelcore.bukkit.api.framework.VexelPlayerSubCommand;
import me.itsmcb.vexelcore.bukkit.api.framework.VexelRootCommand;
import me.itsmcb.vexelcore.bukkit.api.menu.Menu;
import me.itsmcb.vexelcore.bukkit.api.menu.MenuButton;
import me.itsmcb.vexelcore.bukkit.api.menu.MenuRowSize;
import me.itsmcb.vexelcore.bukkit.api.menu.PaginatedMenu;
import me.itsmcb.vexelcore.bukkit.api.text.BukkitMsgBuilder;
import me.itsmcb.vexelcore.bukkit.api.utils.BukkitUtils;
import me.itsmcb.vexelcore.bukkit.api.utils.SkullBuilderUtil;
import me.itsmcb.vexelcore.common.api.HeadTexture;
import me.itsmcb.vexelcore.common.api.utils.MojangUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

public class SkullCmd extends VexelRootCommand<LibreHeadsContext> {

    private final LibreHeads instance;
    private final VexelArg<String> QUERY_ARG;

    public SkullCmd(@NotNull LibreHeadsContext context) {
        super(context, "skull", "Get a decorative or player head", "libreheads.skull.get", true);
        this.instance = context.getLibreHeads();
        this.QUERY_ARG = VexelArg.greedyString("query", "Optional head name or player username to search for").asOptional();
    }

    @Override
    public @NotNull List<String> getAliases() {
        return List.of("head", "heads", "skulls");
    }

    @Override
    public @NotNull List<VexelArg<?>> getArguments() {
        return List.of(QUERY_ARG);
    }

    @Override
    public @NotNull List<VexelCommandNode> getChildren() {
        return List.of(
                new MenuScmd(context, this),
                new SearchScmd(context, this),
                new PlayerScmd(context, this)
        );
    }

    @Override
    protected void executeAsPlayer(@NotNull CommandContext<CommandSourceStack> ctx,
                                   @NotNull Player player,
                                   @NotNull VexelCommandPath path) {
        String query = optionalArg(ctx, QUERY_ARG);
        if (query == null || query.isBlank()) {
            openMainMenu(player);
            return;
        }
        Menu mainMenu = createMainMenu(player);
        VexelCoreBukkitAPI.getMenuManager().register(mainMenu);
        Menu searchMenu = skullSearch(query.trim(), mainMenu);
        VexelCoreBukkitAPI.getMenuManager().open(searchMenu, player);
    }

    /**
     * Opens the main head categories menu for a player
     */
    public void openMainMenu(@NotNull Player player) {
        Menu mainMenu = createMainMenu(player);
        VexelCoreBukkitAPI.getMenuManager().register(mainMenu);
        VexelCoreBukkitAPI.getMenuManager().open(mainMenu, player);
    }

    /**
     * Creates the main menu with all categories and functions (ex. search)
     */
    public Menu createMainMenu(Player player) {
        PaginatedMenu menu = new PaginatedMenu(MenuRowSize.FOUR, "Head Categories (" + instance.getDF().format(instance.getHeads().size()) + ")");

        // Add all category buttons
        addCategory(menu, HeadTexture.OAK_WOOD_A, LibreHead.CATEGORY.ALPHABET, instance.getAlphabetHeads().size(), 2, player);
        addCategory(menu, HeadTexture.COW, LibreHead.CATEGORY.ANIMALS, instance.getAnimalHeads().size(), 3, player);
        addCategory(menu, HeadTexture.DIAMOND_ORE, LibreHead.CATEGORY.BLOCKS, instance.getBlockHeads().size(), 4, player);
        addCategory(menu, HeadTexture.CHEST, LibreHead.CATEGORY.DECORATION, instance.getDecorationHeads().size(), 5, player);
        addCategory(menu, HeadTexture.BREAD, LibreHead.CATEGORY.FOOD_DRINKS, instance.getFoodDrinkHeads().size(), 6, player);
        addCategory(menu, HeadTexture.PERSON, LibreHead.CATEGORY.HUMANS, instance.getHumanHeads().size(), 11, player);
        addCategory(menu, HeadTexture.VILLAGER, LibreHead.CATEGORY.HUMANOID, instance.getHumanoidHeads().size(), 12, player);
        addCategory(menu, HeadTexture.EMOJI, LibreHead.CATEGORY.MISCELLANEOUS, instance.getMiscellaneousHeads().size(), 13, player);
        addCategory(menu, HeadTexture.ZOMBIE, LibreHead.CATEGORY.MONSTERS, instance.getMonsterHeads().size(), 14, player);
        addCategory(menu, HeadTexture.PLANT, LibreHead.CATEGORY.PLANTS, instance.getPlantHeads().size(), 15, player);

        // Add MHF button
        int mhfCount = !instance.getMhfHeads().isEmpty() ? instance.getMhfHeads().size() : MojangUtils.MHFHeads().size();
        menu.addButton(20, new MenuButton(HeadTexture.MHF_CAKE)
                .name("&f&lMarc's Head Format")
                .addLore("&7Decorations for map makers by Marc Watson from 2014", "&7" + mhfCount + " heads")
                .addLore("", "&7➤ &f&nClick to view heads")
                .click(e -> {
                    Menu mhf = generateMenu(instance.getMhfHeads(), menu, "MHF Heads");
                    VexelCoreBukkitAPI.getMenuManager().open(mhf, player);
                })
        );

        // Add online players button
        int onlineAmount = Bukkit.getOnlinePlayers().size();
        menu.addButton(21, new MenuButton(new PlayerSkinData(player))
                .name("&f&lOnline Players")
                .addLore("&7Heads of players who are currently on the server", "&7" + onlineAmount + " head" + (onlineAmount == 1 ? "" : "s"))
                .addLore("", "&7➤ &f&nClick to view heads")
                .click(e -> {
                    Menu online = openOnline();
                    // Set previous menu before opening
                    VexelCoreBukkitAPI.getMenuManager().setPreviousMenu(online, menu);
                    VexelCoreBukkitAPI.getMenuManager().open(online, player);
                })
        );

        // Add get by username button
        menu.addButton(23, new MenuButton(HeadTexture.MAGNIFYING_GLASS)
                .name("&f&lGet By Username")
                .addLore("&7Obtain player head")
                .addLore("", "&7➤ &f&nClick to enter a username")
                .click(e -> openPlayerUsernameDialog(player, menu, null))
        );

        // Add search button
        menu.addButton(24, new MenuButton(HeadTexture.MAGNIFYING_GLASS)
                .name("&f&lSearch Skull Database")
                .addLore("&7Search through all heads")
                .addLore("", "&7➤ &f&nClick to search by term")
                .click(e -> openSkullSearchDialog(player, menu, null))
        );

        return menu;
    }

    /**
     * Fetches a player's skull by username and adds it to the target player's inventory
     */
    public void givePlayerHeadByUsername(@NotNull Player target, @NotNull String input, @Nullable Menu fallbackMenu) {
        if (!CacheManagerV2.isValidUsernameFormat(input)) {
            if (fallbackMenu != null) {
                Bukkit.getScheduler().runTask(instance, () ->
                        openPlayerUsernameDialog(target, fallbackMenu, "&cInvalid username format: " + input));
            } else {
                new BukkitMsgBuilder("&cInvalid username format: " + input).send(target);
            }
            return;
        }

        CompletableFuture<CachedPlayerV2> cpf = VexelCoreBukkitAPI.getCacheManager().getCachedPlayer(input);
        cpf.thenAccept(p -> Bukkit.getScheduler().runTask(instance, () -> {
            if (!target.isOnline()) return;
            target.getInventory().addItem(new MenuButton(p).name("&d&l" + p.getUsername()).addLore("&7Player Head").getCleanItemStack());
            target.playSound(target.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1.0f, 0.0f);
            new BukkitMsgBuilder("&7Grabbed &d&l" + p.getUsername()).send(target);
        })).exceptionally(ex -> {
            Throwable cause = ex instanceof CompletionException ? ex.getCause() : ex;
            Bukkit.getScheduler().runTask(instance, () -> {
                if (!target.isOnline()) return;
                if (cause instanceof PlayerNotFoundException) {
                    new BukkitMsgBuilder("&cInvalid username: " + input).send(target);
                } else {
                    new BukkitMsgBuilder("&cFailed to retrieve player head: " + input).send(target);
                }
            });
            return null;
        });
    }

    /**
     * Opens a VexelInputDialog prompting the player for a valid Minecraft username
     */
    public void openPlayerUsernameDialog(@NotNull Player player, @Nullable Menu previousMenu, @Nullable String errorMessage) {
        VexelInputDialog dialog = new VexelInputDialog("Get By Username")
                .addMessage("&7Please enter a valid player username.")
                .setConfirmButton("&aGet Head")
                .setParent(target -> {
                    if (previousMenu != null) {
                        VexelCoreBukkitAPI.getMenuManager().open(previousMenu, target);
                    } else {
                        openMainMenu(target);
                    }
                });

        if (errorMessage != null && !errorMessage.isBlank()) {
            dialog.addMessage(errorMessage.startsWith("&") ? errorMessage : "&c" + errorMessage);
        }

        dialog.addInput(DialogInput.text("username", TextUtils.mm("Username"))
                .maxLength(16)
                .build());

        dialog.onConfirm(dialogCtx -> {
            Player target = dialogCtx.getPlayer();
            String rawInput = dialogCtx.getInputText("username");
            String input = rawInput != null ? rawInput.trim() : "";

            if (!CacheManagerV2.isValidUsernameFormat(input)) {
                openPlayerUsernameDialog(target, previousMenu, "&cInvalid username format: " + (input.isEmpty() ? "(empty)" : input));
                return;
            }

            givePlayerHeadByUsername(target, input, previousMenu);
        });

        dialog.open(player);
    }

    /**
     * Opens a VexelInputDialog prompting the player for a skull database search term
     */
    public void openSkullSearchDialog(@NotNull Player player, @Nullable Menu previousMenu, @Nullable String errorMessage) {
        VexelInputDialog dialog = new VexelInputDialog("Search Skull Database")
                .addMessage("&7Enter a search term to filter heads across the database.")
                .setConfirmButton("&aSearch")
                .setParent(target -> {
                    if (previousMenu != null) {
                        VexelCoreBukkitAPI.getMenuManager().open(previousMenu, target);
                    } else {
                        openMainMenu(target);
                    }
                });

        if (errorMessage != null && !errorMessage.isBlank()) {
            dialog.addMessage(errorMessage.startsWith("&") ? errorMessage : "&c" + errorMessage);
        }

        dialog.addInput(DialogInput.text("query", TextUtils.mm("Search Term"))
                .maxLength(64)
                .build());

        dialog.onConfirm(dialogCtx -> {
            Player target = dialogCtx.getPlayer();
            String rawInput = dialogCtx.getInputText("query");
            String input = rawInput != null ? rawInput.trim() : "";

            if (input.isEmpty()) {
                openSkullSearchDialog(target, previousMenu, "&cPlease enter a non-empty search term.");
                return;
            }

            Menu parentMenu = previousMenu;
            if (parentMenu == null) {
                parentMenu = createMainMenu(target);
                VexelCoreBukkitAPI.getMenuManager().register(parentMenu);
            }
            Menu searchMenu = skullSearch(input, parentMenu);
            VexelCoreBukkitAPI.getMenuManager().open(searchMenu, target);
        });

        dialog.open(player);
    }

    private ArrayList<LibreHead> getAllHeads() {
        // Create new list with all database heads
        ArrayList<LibreHead> allHeads = new ArrayList<>(instance.getHeads());
        // Add online players
        Bukkit.getOnlinePlayers().forEach(p -> {
            PlayerSkinData skinData = new PlayerSkinData(p);
            if (skinData.getTexture() != null) {
                allHeads.add(new LibreHead("&f&l" + p.getName(), skinData.getTexture(), skinData.getSignature()).database(LibreHead.DATABASE.ONLINE_PLAYER));
            }
        });
        return allHeads;
    }

    /**
     * Creates a menu showing search results
     * @param query Search query
     * @param previous Previous menu to return to
     * @return Search results menu
     */
    public Menu skullSearch(@NotNull String query, @NotNull Menu previous) {
        PaginatedMenu search = new PaginatedMenu(MenuRowSize.SIX, "Search Results: " + query);
        String lowerQuery = query.toLowerCase(Locale.ROOT);
        ArrayList<LibreHead> heads = getAllHeads();
        Set<String> onlineNamesLower = Bukkit.getOnlinePlayers().stream()
                .map(p -> p.getName().toLowerCase(Locale.ROOT))
                .collect(Collectors.toSet());

        // Only seek specific player if query could be a username and isn't already an MHF head
        if (CacheManagerV2.isValidUsernameFormat(query) && !lowerQuery.startsWith("mhf_")) {
            VexelCoreBukkitAPI.getCacheManager().getCachedPlayer(query)
                    .thenApplyAsync(p -> {
                        try {
                            return VexelCoreBukkitAPI.getCacheManager().getAllPlayers().stream()
                                    .filter(p2 -> p2.getUsername() != null)
                                    .filter(p2 -> p2.getPlayerSkinData() != null && p2.getPlayerSkinData().getTexture() != null)
                                    .filter(p2 -> !p2.getUsername().toLowerCase(Locale.ROOT).startsWith("mhf_"))
                                    .filter(p2 -> !onlineNamesLower.contains(p2.getUsername().toLowerCase(Locale.ROOT)))
                                    .filter(p2 -> p2.getUsername().toLowerCase(Locale.ROOT).contains(lowerQuery))
                                    .toList();
                        } catch (DataRequestFailure ignore) {
                            return List.<CachedPlayerV2>of();
                        }
                    })
                    .thenAccept(playerResults -> Bukkit.getScheduler().runTask(instance, () -> {
                        playerResults.forEach(h -> addHeadsToMenu(new LibreHead(h).database(LibreHead.DATABASE.PLAYER), search));
                    }))
                    .exceptionally(ex -> null);
        }

        // Filter heads based on search query (matching name or tags)
        ArrayList<LibreHead> headResults = new ArrayList<>(heads.stream()
                .filter(head -> matchesQuery(head, lowerQuery))
                .toList());
        // Generate the menu with search results
        addHeadsToMenu(headResults, search);
        VexelCoreBukkitAPI.getMenuManager().setPreviousMenu(search, previous);

        return search;
    }

    private static boolean matchesQuery(@NotNull LibreHead head, @NotNull String lowerQuery) {
        if (head.getName() != null && head.getName().toLowerCase(Locale.ROOT).contains(lowerQuery)) {
            return true;
        }
        if (head.getTags() != null) {
            for (String tag : head.getTags()) {
                if (tag.toLowerCase(Locale.ROOT).contains(lowerQuery)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Creates a button that gives the player the skull when clicked
     */
    public static MenuButton getGetterSkull(MenuButton b, String skullName) {
        return b.click(e -> {
            Player player = (Player) e.getWhoClicked();
            player.getInventory().addItem(b.getCleanItemStack(new ArrayList<>()));
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, (float) 1, 0);
            new BukkitMsgBuilder("&7Grabbed &f&l" + skullName).send(player);
        });
    }

    /**
     * Creates a menu showing online players
     */
    private PaginatedMenu openOnline() {
        PaginatedMenu online = new PaginatedMenu(MenuRowSize.SIX, "Online Players");
        Bukkit.getOnlinePlayers().forEach(op -> {
            MenuButton b = new MenuButton(new PlayerSkinData(op))
                    .name("&f&l" + op.getName())
                    .addLore("&7Player", "&7Online Player");
            online.addButton(getGetterSkull(b, "&f&l" + op.getName()));
        });
        return online;
    }

    /**
     * Adds a category button to the main menu
     */
    private void addCategory(Menu menu, HeadTexture skullTexture, LibreHead.CATEGORY category, int size, int slot, Player player) {
        MenuButton b = new MenuButton(skullTexture)
                .name("&f&l" + category.get().getName())
                .addLore("&7" + category.get().getDescription(), "&7" + instance.getDF().format(size) + " heads")
                .addLore("", "&7➤ &f&nClick to view heads");

        // Create click handler for each category
        b.click(e -> {
            // Create the category menu
            Menu categoryMenu = generateMenu(instance.getCategoryHeads(category), menu, category.get().getName());

            // Set previous menu and open
            VexelCoreBukkitAPI.getMenuManager().open(categoryMenu, player);
        });

        menu.addButton(slot, b);
    }

    /**
     * Add many heads to a menu
     */
    public static void addHeadsToMenu(List<LibreHead> heads, Menu menu) {
        heads.forEach(head -> {
            addHeadsToMenu(head, menu);
        });
    }

    /**
     * Add a single head to a menu
     */
    public static void addHeadsToMenu(LibreHead head, Menu menu) {
        String name = "&r&f&l" + head.getName();
        SkullBuilderUtil sbu = new SkullBuilderUtil(head.getValue());
        if (head.hasSignature()) {
            sbu.setSignature(head.getSignature());
        }
        MenuButton b = new MenuButton(sbu.get()).name(name);
        if (head.getCategory() != null) {
            b.addLore("&7" + head.getCategory().toString());
        }
        if (head.getDatabase() != null) {
            b.addLore("&7" + head.getDatabase().toString());
        }
        b.addLore("", "&7➤ &f&nClick to add head to your inventory");
        menu.addButton(getGetterSkull(b, name));
    }

    /**
     * Generates a menu for a list of heads
     */
    public Menu generateMenu(List<LibreHead> heads, Menu previous, String name) {
        PaginatedMenu menu = new PaginatedMenu(MenuRowSize.SIX, name + " (" + instance.getDF().format(heads.size()) + ")");
        addHeadsToMenu(heads, menu);
        if (previous != null) {
            VexelCoreBukkitAPI.getMenuManager().setPreviousMenu(menu, previous);
        }
        return menu;
    }

    static class MenuScmd extends VexelPlayerSubCommand<LibreHeadsContext> {

        private final SkullCmd parent;

        MenuScmd(@NotNull LibreHeadsContext context, @NotNull SkullCmd parent) {
            super(context, "menu", "Browse decorative and player head categories", "libreheads.skull.get");
            this.parent = parent;
        }

        @Override
        public @NotNull List<String> getAliases() {
            return List.of("browse", "gui");
        }

        @Override
        public @NotNull List<VexelArg<?>> getArguments() {
            return Collections.emptyList();
        }

        @Override
        public void execute(@NotNull CommandContext<CommandSourceStack> ctx, @NotNull Player player) {
            parent.openMainMenu(player);
        }
    }

    static class SearchScmd extends VexelPlayerSubCommand<LibreHeadsContext> {

        private final SkullCmd parent;
        private final VexelArg<String> QUERY_ARG;

        SearchScmd(@NotNull LibreHeadsContext context, @NotNull SkullCmd parent) {
            super(context, "search", "Search through all decorative and player heads", "libreheads.skull.get");
            this.parent = parent;
            this.QUERY_ARG = VexelArg.builder("query", StringArgumentType.greedyString(), String.class, "Head name or player username to search for")
                    .optional()
                    .suggestsContextual(ctx -> {
                        ArrayList<String> names = new ArrayList<>(context.getLibreHeads().getHeads().stream().map(LibreHead::getName).toList());
                        names.addAll(BukkitUtils.getOnlinePlayerNames());
                        return names;
                    })
                    .build();
        }

        @Override
        public @NotNull List<String> getAliases() {
            return List.of("find");
        }

        @Override
        public @NotNull List<VexelArg<?>> getArguments() {
            return List.of(QUERY_ARG);
        }

        @Override
        public void execute(@NotNull CommandContext<CommandSourceStack> ctx, @NotNull Player player) {
            String query = optionalArg(ctx, QUERY_ARG);
            if (query == null || query.isBlank()) {
                parent.openSkullSearchDialog(player, null, null);
                return;
            }

            Menu mainMenu = parent.createMainMenu(player);
            VexelCoreBukkitAPI.getMenuManager().register(mainMenu);
            Menu searchMenu = parent.skullSearch(query.trim(), mainMenu);
            VexelCoreBukkitAPI.getMenuManager().open(searchMenu, player);
        }
    }

    static class PlayerScmd extends VexelPlayerSubCommand<LibreHeadsContext> {

        private final SkullCmd parent;
        private final VexelArg<String> USERNAME_ARG;

        PlayerScmd(@NotNull LibreHeadsContext context, @NotNull SkullCmd parent) {
            super(context, "player", "Get a player's head by username", "libreheads.skull.get");
            this.parent = parent;
            this.USERNAME_ARG = VexelArg.builder("username", StringArgumentType.word(), String.class, "Minecraft player username")
                    .optional()
                    .suggestsContextual(ctx -> BukkitUtils.getOnlinePlayerNames())
                    .build();
        }

        @Override
        public @NotNull List<String> getAliases() {
            return List.of("user", "username");
        }

        @Override
        public @NotNull List<VexelArg<?>> getArguments() {
            return List.of(USERNAME_ARG);
        }

        @Override
        public void execute(@NotNull CommandContext<CommandSourceStack> ctx, @NotNull Player player) {
            String username = optionalArg(ctx, USERNAME_ARG);
            if (username == null || username.isBlank()) {
                parent.openPlayerUsernameDialog(player, null, null);
                return;
            }

            parent.givePlayerHeadByUsername(player, username.trim(), null);
        }
    }
}
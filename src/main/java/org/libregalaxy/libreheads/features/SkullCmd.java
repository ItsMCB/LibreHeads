package org.libregalaxy.libreheads.features;

import me.itsmcb.vexelcore.bukkit.VexelCoreBukkitAPI;
import me.itsmcb.vexelcore.bukkit.api.cache.CacheManagerV2;
import me.itsmcb.vexelcore.bukkit.api.cache.CachedPlayerV2;
import me.itsmcb.vexelcore.bukkit.api.cache.PlayerSkinData;
import me.itsmcb.vexelcore.bukkit.api.cache.exceptions.DataRequestFailure;
import me.itsmcb.vexelcore.bukkit.api.command.CustomCommand;
import me.itsmcb.vexelcore.bukkit.api.conversation.InputPrefix;
import me.itsmcb.vexelcore.bukkit.api.menu.Menu;
import me.itsmcb.vexelcore.bukkit.api.menu.MenuButton;
import me.itsmcb.vexelcore.bukkit.api.menu.MenuRowSize;
import me.itsmcb.vexelcore.bukkit.api.menu.PaginatedMenu;
import me.itsmcb.vexelcore.bukkit.api.menuv2.SkullBuilder;
import me.itsmcb.vexelcore.bukkit.api.text.BukkitMsgBuilder;
import me.itsmcb.vexelcore.bukkit.api.utils.BukkitUtils;
import me.itsmcb.vexelcore.bukkit.api.utils.SkullBuilderUtil;
import me.itsmcb.vexelcore.common.api.command.CMDHelper;
import me.itsmcb.vexelcore.common.api.utils.MojangUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.libregalaxy.libreheads.LibreHead;
import org.libregalaxy.libreheads.LibreHeads;
import org.libregalaxy.libreheads.StaticMenuHeadTextures;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class SkullCmd extends CustomCommand {

    private LibreHeads instance;
    public SkullCmd(LibreHeads instance, String cmdName) {
        super(cmdName, "Get a decorative or player head", "libreheads.skull.get");
        this.instance = instance;
    }

    @Override
    public void executeAsPlayer(Player player, String[] args) {
        Menu mainMenu = createMainMenu(player);
        VexelCoreBukkitAPI.getMenuManager().register(mainMenu);

        // Handle search
        if (args.length > 0) {
            CMDHelper cmdHelper = new CMDHelper(args);
            String searchQuery = cmdHelper.getStringOfArgsAfterIndex(-1);
            Menu searchMenu = skullSearch(searchQuery, mainMenu);
            VexelCoreBukkitAPI.getMenuManager().open(searchMenu, player);
            return;
        }

        // Main menu
        VexelCoreBukkitAPI.getMenuManager().open(mainMenu, player);
    }

    /**
     * Creates the main menu with all categories and functions (ex. search)
     */
    private Menu createMainMenu(Player player) {
        Menu menu = new Menu(MenuRowSize.THREE, "Head Categories ("+instance.getDF().format(instance.getHeads().size())+")");

        // Add all category buttons
        addCategory(menu, StaticMenuHeadTextures.OakWoodA.getTexture(), LibreHead.CATEGORY.ALPHABET, instance.getAlphabetHeads().size(), 2, player);
        addCategory(menu, StaticMenuHeadTextures.Cow.getTexture(), LibreHead.CATEGORY.ANIMALS, instance.getAnimalHeads().size(), 3, player);
        addCategory(menu, StaticMenuHeadTextures.DiamondOre.getTexture(), LibreHead.CATEGORY.BLOCKS, instance.getBlockHeads().size(), 4, player);
        addCategory(menu, StaticMenuHeadTextures.Chest.getTexture(), LibreHead.CATEGORY.DECORATION, instance.getDecorationHeads().size(), 5, player);
        addCategory(menu, StaticMenuHeadTextures.Bread.getTexture(), LibreHead.CATEGORY.FOOD_DRINKS, instance.getFoodDrinkHeads().size(), 6, player);
        addCategory(menu, StaticMenuHeadTextures.Person.getTexture(), LibreHead.CATEGORY.HUMANS, instance.getHumanHeads().size(), 11, player);
        addCategory(menu, StaticMenuHeadTextures.Villager.getTexture(), LibreHead.CATEGORY.HUMANOID, instance.getHumanoidHeads().size(), 12, player);
        addCategory(menu, StaticMenuHeadTextures.Emoji.getTexture(), LibreHead.CATEGORY.MISCELLANEOUS, instance.getMiscellaneousHeads().size(), 13, player);
        addCategory(menu, StaticMenuHeadTextures.Zombie.getTexture(), LibreHead.CATEGORY.MONSTERS, instance.getMonsterHeads().size(), 14, player);
        addCategory(menu, StaticMenuHeadTextures.Plant.getTexture(), LibreHead.CATEGORY.PLANTS, instance.getPlantHeads().size(), 15, player);

        // Add MHF button
        VexelCoreBukkitAPI.getCacheManager().getCachedPlayer("MHF_Cake").thenAccept(p -> {
            menu.addButton(20, new MenuButton(p.getPlayerSkinData().getTexture())
                    .name("&d&lMarc's Head Format")
                    .addLore("&aDecorations for map makers by Marc Watson from 2014", "&7" + MojangUtils.MHFHeads().size() + " heads")
                    .click(e -> {
                        Menu mhf = openMHF();
                        // Set previous menu before opening
                        VexelCoreBukkitAPI.getMenuManager().setPreviousMenu(mhf, menu);
                        VexelCoreBukkitAPI.getMenuManager().open(mhf, player);
                    })
            );
        });

        // Add online players button
        menu.addButton(21, new MenuButton(new PlayerSkinData(player))
                .name("&d&lOnline Players")
                .addLore("&aHeads of players who are currently on the server", "&7" + Bukkit.getOnlinePlayers().size() + " heads")
                .click(e -> {
                    Menu online = openOnline();
                    // Set previous menu before opening
                    VexelCoreBukkitAPI.getMenuManager().setPreviousMenu(online, menu);
                    VexelCoreBukkitAPI.getMenuManager().open(online, player);
                })
        );

        // Add get by username button
        menu.addButton(23, new MenuButton("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjIyODcyMzQyZDJjZjIwNzU0YjllMWJhZTljMDkwMjkxMmRjYWUxMmU2M2I1MjBiNmZlOGJkOTExYjkxMDE4YiJ9fX0=")
                .name("&d&lGet By Username")
                .addLore("&7Obtain player head", "&7Click this then type in chat a Minecraft username")
                .click(e -> {
                    ConversationFactory conversationFactory = new ConversationFactory(instance);
                    Conversation c = conversationFactory
                            .withFirstPrompt(new ValidPlayerSearchPrompt())
                            .withEscapeSequence("exit")
                            .withTimeout(60)
                            .withPrefix(new InputPrefix())
                            .withLocalEcho(false)
                            .buildConversation(player);
                    c.getContext().setSessionData("lh", instance);
                    // Store reference to main menu in conversation
                    c.getContext().setSessionData("pm", menu);
                    c.begin();
                })
        );

        // Add search button
        menu.addButton(24, new MenuButton("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjIyODcyMzQyZDJjZjIwNzU0YjllMWJhZTljMDkwMjkxMmRjYWUxMmU2M2I1MjBiNmZlOGJkOTExYjkxMDE4YiJ9fX0=")
                .name("&d&lSearch Skull Database")
                .addLore("&7Click this then type a skull name in chat")
                .click(e -> {
                    ConversationFactory conversationFactory = new ConversationFactory(instance);
                    Conversation c = conversationFactory
                            .withFirstPrompt(new SkullSearchPrompt())
                            .withEscapeSequence("exit")
                            .withTimeout(60)
                            .withPrefix(new InputPrefix())
                            .withLocalEcho(false)
                            .buildConversation(player);
                    c.getContext().setSessionData("lh", instance);
                    c.getContext().setSessionData("pm", menu);
                    c.begin();
                })
        );

        return menu;
    }

    private ArrayList<LibreHead> getAllHeads() {
        // Create new list with all database heads
        ArrayList<LibreHead> allHeads = new ArrayList<>(instance.getHeads());
        // Add online players
        Bukkit.getOnlinePlayers().forEach(p -> {
            allHeads.add(new LibreHead("&d&l"+p.getName(), new SkullBuilder(p).getTexture()).database(LibreHead.DATABASE.ONLINE_PLAYER));
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
        ArrayList<LibreHead> heads = getAllHeads();
        // Only seek specific player if query could be a username
        if (CacheManagerV2.isValidUsername(query)) {
            CompletableFuture<CachedPlayerV2> cpf = VexelCoreBukkitAPI.getCacheManager().getCachedPlayer(query);
            cpf.thenAccept(p -> {
                try {
                    // List should include newly cached player
                    List<CachedPlayerV2> playerResults = VexelCoreBukkitAPI.getCacheManager().getAllPlayers().stream().filter(p2 -> p2.getUsername().contains(query)).toList();
                    playerResults.forEach(h -> addHeadsToMenu(new LibreHead(h).database(LibreHead.DATABASE.PLAYER),search));
                } catch (DataRequestFailure ignore) {}
            });
        }
        // Filter heads based on search query
        ArrayList<LibreHead> headResults = new ArrayList<>(heads.stream()
                .filter(head -> head.getName().toLowerCase().contains(query.toLowerCase()))
                .toList());
        // Generate the menu with search results
        addHeadsToMenu(headResults, search);
        VexelCoreBukkitAPI.getMenuManager().setPreviousMenu(search, previous);

        return search;
    }

    /**
     * Creates a button that gives the player the skull when clicked
     */
    public static MenuButton getGetterSkull(MenuButton b, String skullName) {
        return b.click(e -> {
            Player player = (Player) e.getWhoClicked();
            player.getInventory().addItem(b.getCleanItemStack());
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, (float) 1, 0);
            new BukkitMsgBuilder("&7Grabbed &d&l" + skullName).send(player);
        });
    }

    /**
     * Creates a menu showing online players
     */
    private PaginatedMenu openOnline() {
        PaginatedMenu online = new PaginatedMenu(MenuRowSize.SIX, "Online Players");
        Bukkit.getOnlinePlayers().forEach(op -> {
            MenuButton b = new MenuButton(new PlayerSkinData(op))
                    .name("&d&l" + op.getName())
                    .addLore("&aPlayer", "&7Online Player");
            online.addButton(getGetterSkull(b, "&d&l" + op.getName()));
        });
        return online;
    }

    /**
     * Creates a menu showing MHF heads
     */
    private Menu openMHF() {
        Menu mhfHeads = new PaginatedMenu(MenuRowSize.SIX, "MHF Heads");
        MojangUtils.MHFHeads().forEach(un -> {
            CompletableFuture<CachedPlayerV2> cpf = VexelCoreBukkitAPI.getCacheManager().getCachedPlayer(un);
            cpf.thenAccept(p -> {
                MenuButton b = new MenuButton(p.getPlayerSkinData())
                        .name("&d&l" + un)
                        .addLore("&aMarc's Head Format", "&7MHF Player");
                mhfHeads.addButton(getGetterSkull(b, "&d&l" + un));
            });
        });
        return mhfHeads;
    }

    /**
     * Adds a category button to the main menu
     */
    private void addCategory(Menu menu, String skullTexture, LibreHead.CATEGORY category, int size, int slot, Player player) {
        MenuButton b = new MenuButton(skullTexture)
                .name("&d&l" + category.get().getName())
                .addLore("&a" + category.get().getDescription(), "&7" + instance.getDF().format(size) + " heads");

        // Create click handler for each category
        b.click(e -> {
            // Create the category menu
            Menu categoryMenu = generateMenu(getCategoryHeads(category), menu, category.name());

            // Set previous menu and open
            VexelCoreBukkitAPI.getMenuManager().open(categoryMenu, player);
        });

        menu.addButton(slot, b);
    }

    /**
     * Gets heads for a specific category
     */
    private List<LibreHead> getCategoryHeads(LibreHead.CATEGORY category) {
        switch (category) {
            case ALPHABET: return instance.getAlphabetHeads();
            case ANIMALS: return instance.getAnimalHeads();
            case BLOCKS: return instance.getBlockHeads();
            case DECORATION: return instance.getDecorationHeads();
            case FOOD_DRINKS: return instance.getFoodDrinkHeads();
            case HUMANS: return instance.getHumanHeads();
            case HUMANOID: return instance.getHumanoidHeads();
            case MISCELLANEOUS: return instance.getMiscellaneousHeads();
            case MONSTERS: return instance.getMonsterHeads();
            case PLANTS: return instance.getPlantHeads();
            default: return new ArrayList<>();
        }
    }

    /**
     * Add many heads to a menu
     */
    public static void addHeadsToMenu(List<LibreHead> heads, Menu menu) {
        heads.forEach(head -> {
            addHeadsToMenu(head,menu);
        });
    }

    /**
     * Add a single head to a menu
     */
    public static void addHeadsToMenu(LibreHead head, Menu menu) {
        String name = "&r&d&l" + head.getName();
        SkullBuilderUtil sbu = new SkullBuilderUtil(head.getValue());
        if (head.hasSignature()) {
            sbu.setSignature(head.getSignature());
        }
        MenuButton b = new MenuButton(sbu.get()).name(name);
        if (head.getCategory() != null) {
            b.addLore("&a" + head.getCategory().toString());
        }
        if (head.getDatabase() != null) {
            b.addLore("&7" + head.getDatabase().toString());
        }
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

    @Override
    public List<String> getAdditionalCompletions(CommandSender sender) {
        ArrayList<String> names = new ArrayList<>(instance.getHeads().stream().map(LibreHead::getName).toList());
        names.addAll(BukkitUtils.getOnlinePlayerNames());
        return names;
    }
}
package org.libregalaxy.libreheads.features;

import me.itsmcb.vexelcore.bukkit.api.command.CustomCommand;
import me.itsmcb.vexelcore.bukkit.api.conversation.InputPrefix;
import me.itsmcb.vexelcore.bukkit.api.menuv2.MenuV2;
import me.itsmcb.vexelcore.bukkit.api.menuv2.MenuV2Item;
import me.itsmcb.vexelcore.bukkit.api.menuv2.PaginatedMenu;
import me.itsmcb.vexelcore.bukkit.api.menuv2.SkullBuilder;
import me.itsmcb.vexelcore.bukkit.api.text.BukkitMsgBuilder;
import me.itsmcb.vexelcore.bukkit.plugin.CachedPlayer;
import me.itsmcb.vexelcore.common.api.command.CMDHelper;
import me.itsmcb.vexelcore.common.api.utils.MojangUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.libregalaxy.libreheads.LibreHead;
import org.libregalaxy.libreheads.LibreHeads;
import org.libregalaxy.libreheads.StaticMenuHeadTextures;

import java.util.List;

public class SkullCmd extends CustomCommand {

    private LibreHeads instance;
    public SkullCmd(LibreHeads instance, String cmdName) {
        super(cmdName, "Get a skull", "libreheads.skull.get");
        this.instance = instance;
    }

    @Override
    public void executeAsPlayer(Player player, String[] args) {

        if (args.length > 0) {
            CMDHelper cmdHelper = new CMDHelper(args);
            String search = cmdHelper.getStringOfArgsAfterIndex(-1);
            List<LibreHead> headResults = instance.getHeads().stream().filter(head -> head.getName().toLowerCase().contains(search.toLowerCase())).toList();
            MenuV2 resultsMenu = generateMenu(headResults, null,"Search Results: "+search, player);
            instance.getMenuManager().open(resultsMenu, player);
            return;
        }

        MenuV2 mainMenu = new MenuV2("Head Categories ("+instance.getDF().format(instance.getHeads().size())+")", InventoryType.CHEST,27).clickCloseMenu(true);
        addCategory(mainMenu, StaticMenuHeadTextures.OakWoodA.getTexture(), LibreHead.CATEGORY.ALPHABET, instance.getAlphabetHeads().size(), 2, player);
        addCategory(mainMenu, StaticMenuHeadTextures.Cow.getTexture(), LibreHead.CATEGORY.ANIMALS, instance.getAnimalHeads().size(), 3, player);
        addCategory(mainMenu, StaticMenuHeadTextures.DiamondOre.getTexture(), LibreHead.CATEGORY.BLOCKS, instance.getBlockHeads().size(), 4, player);
        addCategory(mainMenu, StaticMenuHeadTextures.Chest.getTexture(), LibreHead.CATEGORY.DECORATION, instance.getDecorationHeads().size(), 5, player);
        addCategory(mainMenu, StaticMenuHeadTextures.Bread.getTexture(), LibreHead.CATEGORY.FOOD_DRINKS, instance.getFoodDrinkHeads().size(), 6, player);
        addCategory(mainMenu, StaticMenuHeadTextures.Person.getTexture(), LibreHead.CATEGORY.HUMANS, instance.getHumanHeads().size(), 11, player);
        addCategory(mainMenu, StaticMenuHeadTextures.Villager.getTexture(), LibreHead.CATEGORY.HUMANOID, instance.getHumanoidHeads().size(), 12, player);
        addCategory(mainMenu, StaticMenuHeadTextures.Emoji.getTexture(), LibreHead.CATEGORY.MISCELLANEOUS, instance.getMiscellaneousHeads().size(), 13, player);
        addCategory(mainMenu, StaticMenuHeadTextures.Zombie.getTexture(), LibreHead.CATEGORY.MONSTERS, instance.getMonsterHeads().size(), 14, player);
        addCategory(mainMenu, StaticMenuHeadTextures.Plant.getTexture(), LibreHead.CATEGORY.PLANTS, instance.getPlantHeads().size(), 15, player);

        mainMenu.addItem(new SkullBuilder(Bukkit.getOfflinePlayer("MHF_Cake")).slot(21).name("&d&lMHF").clickAction(e -> instance.getMenuManager().open(openMHF(player),player,mainMenu)));
        mainMenu.addItem(new SkullBuilder(player).slot(22).name("&d&lOnline Players").clickAction(e -> instance.getMenuManager().open(openOnline(player),player,mainMenu)));
        mainMenu.addItem(new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjIyODcyMzQyZDJjZjIwNzU0YjllMWJhZTljMDkwMjkxMmRjYWUxMmU2M2I1MjBiNmZlOGJkOTExYjkxMDE4YiJ9fX0=").slot(23).name("&d&lGet By Username").addLore("&7Click this then type in chat a Minecraft username").clickAction(e -> {
            ConversationFactory conversationFactory = new ConversationFactory(instance);
            Conversation c = conversationFactory
                    .withFirstPrompt(new SkullSearchPrompt())
                    .withEscapeSequence("exit")
                    .withTimeout(60)
                    .withPrefix(new InputPrefix())
                    .withLocalEcho(false)
                    .buildConversation(player);
            c.getContext().setSessionData("lh",instance);
            c.begin();
        }));

        instance.getMenuManager().open(mainMenu, player);

    }

    public MenuV2Item getGetterSkull(MenuV2Item i, String skullName, Player player) {
        i.clickAction(e -> {
            player.getInventory().addItem(i.getCleanItemStack());
            new BukkitMsgBuilder("&7Grabbed &d&l"+skullName).send(player);
        });
        return i;
    }

    private MenuV2 openOnline(Player player) {
        MenuV2 online = new PaginatedMenu("Online Players",54,player);
        Bukkit.getOnlinePlayers().forEach(op -> {
            MenuV2Item i = new SkullBuilder(op).name("&d&l"+op.getName()).addLore("&aPlayer","&7Online Player");
            online.addItem(getGetterSkull(i,"&d&l"+op.getName(),player));
        });
        return online;
    }

    private MenuV2 openMHF(Player player) {
        MenuV2 mhfHeads = new PaginatedMenu("MHF Heads",36,player);
        MojangUtils.MHFHeads().forEach(un -> {
            CachedPlayer cachedPlayer = instance.getCacheManager().get(un);
            MenuV2Item i = new SkullBuilder(cachedPlayer).name("&d&l"+un).addLore("&aMarc's Head Format","&7MHF Player");
            mhfHeads.addItem(getGetterSkull(i,"&d&l"+un,player));
        });
        return mhfHeads;
    }

    private void addCategory(MenuV2 menu, String skullTexture,LibreHead.CATEGORY category, int size, int slot, Player player) {
        MenuV2Item i = new SkullBuilder(skullTexture)
                .name("&d&l"+category.get().getName())
                .addLore("&a"+category.get().getDescription(),"&7"+instance.getDF().format(size)+" heads").slot(slot);
        switch (category) {
            case ALPHABET -> i.clickAction(e -> instance.getMenuManager().open(generateMenu(instance.getAlphabetHeads(), menu, LibreHead.CATEGORY.ALPHABET.name(), player),player));
            case ANIMALS -> i.clickAction(e -> instance.getMenuManager().open(generateMenu(instance.getAnimalHeads(), menu, LibreHead.CATEGORY.ANIMALS.name(), player),player));
            case BLOCKS -> i.clickAction(e -> instance.getMenuManager().open(generateMenu(instance.getBlockHeads(), menu, LibreHead.CATEGORY.BLOCKS.name(), player),player));
            case DECORATION -> i.clickAction(e -> instance.getMenuManager().open(generateMenu(instance.getDecorationHeads(), menu, LibreHead.CATEGORY.DECORATION.name(), player),player));
            case FOOD_DRINKS -> i.clickAction(e -> instance.getMenuManager().open(generateMenu(instance.getFoodDrinkHeads(), menu, LibreHead.CATEGORY.FOOD_DRINKS.name(), player),player));
            case HUMANS -> i.clickAction(e -> instance.getMenuManager().open(generateMenu(instance.getHumanHeads(), menu, LibreHead.CATEGORY.HUMANS.name(), player),player));
            case HUMANOID -> i.clickAction(e -> instance.getMenuManager().open(generateMenu(instance.getHumanoidHeads(), menu, LibreHead.CATEGORY.HUMANOID.name(), player),player));
            case MISCELLANEOUS -> i.clickAction(e -> instance.getMenuManager().open(generateMenu(instance.getMiscellaneousHeads(), menu, LibreHead.CATEGORY.MISCELLANEOUS.name(), player),player));
            case MONSTERS -> i.clickAction(e -> instance.getMenuManager().open(generateMenu(instance.getMonsterHeads(), menu, LibreHead.CATEGORY.MONSTERS.name(), player),player));
            case PLANTS -> i.clickAction(e -> instance.getMenuManager().open(generateMenu(instance.getPlantHeads(), menu, LibreHead.CATEGORY.PLANTS.name(), player),player));
        }
        menu.addItem(i);
    }

    private void addHeadsToMenu(List<LibreHead> heads, MenuV2 menu, Player player) {
        heads.forEach(head -> {
            String name = "&r&d&l"+head.getName();
            MenuV2Item i = new SkullBuilder(head.getValue()).name(name);
            if (head.getCategory() != null) {
                i.addLore(new BukkitMsgBuilder("&a"+head.getCategory().toString()).get());
            }
            if (head.getDatabase() != null) {
                i.addLore(new BukkitMsgBuilder("&7"+head.getDatabase().toString()).get());
            }
            menu.addItem(getGetterSkull(i,name,player));
        });
    }

    public MenuV2 generateMenu(List<LibreHead> heads, MenuV2 previous, String name, Player player) {
        MenuV2 menu = new PaginatedMenu(name + " ("+instance.getDF().format(heads.size())+")",54, player);
        addHeadsToMenu(heads, menu, player);
        menu.setManager(instance.getMenuManager());
        menu.setPreviousMenu(previous);
        return menu;
    }


    @Override
    public List<String> getAdditionalCompletions(CommandSender sender) {
        return instance.getHeads().stream().map(LibreHead::getName).toList();
    }

}

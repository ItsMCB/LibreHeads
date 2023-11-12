package org.libregalaxy.libreheads.features;

import me.itsmcb.vexelcore.bukkit.api.command.CustomCommand;
import me.itsmcb.vexelcore.bukkit.api.menuv2.MenuV2;
import me.itsmcb.vexelcore.bukkit.api.menuv2.MenuV2Item;
import me.itsmcb.vexelcore.bukkit.api.menuv2.PaginatedMenu;
import me.itsmcb.vexelcore.bukkit.api.menuv2.SkullBuilder;
import me.itsmcb.vexelcore.bukkit.api.text.BukkitMsgBuilder;
import me.itsmcb.vexelcore.common.api.command.CMDHelper;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.libregalaxy.libreheads.LibreHead;
import org.libregalaxy.libreheads.LibreHeads;
import org.libregalaxy.libreheads.StaticMenuHeadTextures;

import java.util.List;

public class SkullCmd extends CustomCommand {

    private LibreHeads instance;
    public SkullCmd(LibreHeads instance) {
        super("skull", "Get a skull", "libreheads.skull.get");
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

        MenuV2 mainMenu = new MenuV2("Head Categories ("+instance.getDF().format(instance.getHeads().size())+")", InventoryType.CHEST,18);
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

        instance.getMenuManager().open(mainMenu, player);

    }

    private void addCategory(MenuV2 menu, String skullTexture,LibreHead.CATEGORY category, int size, int slot, Player player) {
        MenuV2Item i = new SkullBuilder(skullTexture)
                .name("&d&l"+category.get().getName())
                .addLore("&a"+category.get().getDescription(),"&7"+instance.getDF().format(size)+" heads").slot(slot);
        switch (category) {
            case ALPHABET -> i.leftClickAction(e -> instance.getMenuManager().open(generateMenu(instance.getAlphabetHeads(), menu, LibreHead.CATEGORY.ALPHABET.name(), player),player));
            case ANIMALS -> i.leftClickAction(e -> instance.getMenuManager().open(generateMenu(instance.getAnimalHeads(), menu, LibreHead.CATEGORY.ANIMALS.name(), player),player));
            case BLOCKS -> i.leftClickAction(e -> instance.getMenuManager().open(generateMenu(instance.getBlockHeads(), menu, LibreHead.CATEGORY.BLOCKS.name(), player),player));
            case DECORATION -> i.leftClickAction(e -> instance.getMenuManager().open(generateMenu(instance.getDecorationHeads(), menu, LibreHead.CATEGORY.DECORATION.name(), player),player));
            case FOOD_DRINKS -> i.leftClickAction(e -> instance.getMenuManager().open(generateMenu(instance.getFoodDrinkHeads(), menu, LibreHead.CATEGORY.FOOD_DRINKS.name(), player),player));
            case HUMANS -> i.leftClickAction(e -> instance.getMenuManager().open(generateMenu(instance.getHumanHeads(), menu, LibreHead.CATEGORY.HUMANS.name(), player),player));
            case HUMANOID -> i.leftClickAction(e -> instance.getMenuManager().open(generateMenu(instance.getHumanoidHeads(), menu, LibreHead.CATEGORY.HUMANOID.name(), player),player));
            case MISCELLANEOUS -> i.leftClickAction(e -> instance.getMenuManager().open(generateMenu(instance.getMiscellaneousHeads(), menu, LibreHead.CATEGORY.MISCELLANEOUS.name(), player),player));
            case MONSTERS -> i.leftClickAction(e -> instance.getMenuManager().open(generateMenu(instance.getMonsterHeads(), menu, LibreHead.CATEGORY.MONSTERS.name(), player),player));
            case PLANTS -> i.leftClickAction(e -> instance.getMenuManager().open(generateMenu(instance.getPlantHeads(), menu, LibreHead.CATEGORY.PLANTS.name(), player),player));
        }
        menu.addItem(i);
    }

    private void addHeadsToMenu(List<LibreHead> heads, MenuV2 menu, Player player) {
        heads.forEach(head -> {
            MenuV2Item i = new SkullBuilder(head.getValue()).name("&r&d&l"+head.getName());
            i.leftClickAction(e -> {
                player.getInventory().addItem(i.getCleanItemStack());
                new BukkitMsgBuilder("&7Grabbed &d&l"+head.getName()).send(player);
            });
            if (head.getCategory() != null) {
                i.addLore(new BukkitMsgBuilder("&a"+head.getCategory().toString()).get());
            }
            if (head.getDatabase() != null) {
                i.addLore(new BukkitMsgBuilder("&7"+head.getDatabase().toString()).get());
            }
            menu.addItem(i);
        });
    }

    public MenuV2 generateMenu(List<LibreHead> heads, MenuV2 previous, String name, Player player) {
        MenuV2 menu = new PaginatedMenu(name + " ("+instance.getDF().format(heads.size())+")",36, player);
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

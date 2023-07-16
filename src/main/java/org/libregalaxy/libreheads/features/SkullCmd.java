package org.libregalaxy.libreheads.features;

import me.itsmcb.vexelcore.bukkit.api.command.CustomCommand;
import me.itsmcb.vexelcore.bukkit.api.menuv2.MenuV2;
import me.itsmcb.vexelcore.bukkit.api.menuv2.PaginatedMenu;
import me.itsmcb.vexelcore.bukkit.api.menuv2.SkullBuilder;
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
            MenuV2 resultsMenu = generateMenu(headResults, "Search Results: "+search, player);
            instance.getMenuManager().open(resultsMenu, player);
            return;
        }

        MenuV2 alphabet = generateMenu(instance.getAlphabetHeads(), LibreHead.CATEGORY.ALPHABET.name(), player);

        MenuV2 animals = generateMenu(instance.getAnimalHeads(), LibreHead.CATEGORY.ANIMALS.name(), player);

        MenuV2 blocks = generateMenu(instance.getBlockHeads(), LibreHead.CATEGORY.BLOCKS.name(), player);

        MenuV2 decoration = generateMenu(instance.getDecorationHeads(), LibreHead.CATEGORY.DECORATION.name(), player);

        MenuV2 foodDrink = generateMenu(instance.getFoodDrinkHeads(), LibreHead.CATEGORY.FOOD_DRINKS.name(), player);

        MenuV2 humans = generateMenu(instance.getHumanHeads(), LibreHead.CATEGORY.HUMANS.name(), player);

        MenuV2 humanoids = generateMenu(instance.getHumanoidHeads(), LibreHead.CATEGORY.HUMANOID.name(), player);

        MenuV2 miscellaneous = generateMenu(instance.getMiscellaneousHeads(), LibreHead.CATEGORY.MISCELLANEOUS.name(), player);

        MenuV2 monsters = generateMenu(instance.getMonsterHeads(), LibreHead.CATEGORY.MONSTERS.name(), player);

        MenuV2 plants = generateMenu(instance.getPlantHeads(), LibreHead.CATEGORY.PLANTS.name(), player);

        MenuV2 mainMenu = new MenuV2("Head Categories ("+instance.getDF().format(instance.getHeads().size())+")", InventoryType.CHEST,18);
        addCategory(mainMenu, alphabet, StaticMenuHeadTextures.OakWoodA.getTexture(), LibreHead.CATEGORY.ALPHABET, instance.getAlphabetHeads().size(), 2, player);
        addCategory(mainMenu, animals, StaticMenuHeadTextures.Cow.getTexture(), LibreHead.CATEGORY.ANIMALS, instance.getAnimalHeads().size(), 3, player);
        addCategory(mainMenu, blocks, StaticMenuHeadTextures.DiamondOre.getTexture(), LibreHead.CATEGORY.BLOCKS, instance.getBlockHeads().size(), 4, player);
        addCategory(mainMenu, decoration, StaticMenuHeadTextures.Chest.getTexture(), LibreHead.CATEGORY.DECORATION, instance.getDecorationHeads().size(), 5, player);
        addCategory(mainMenu, foodDrink, StaticMenuHeadTextures.Bread.getTexture(), LibreHead.CATEGORY.FOOD_DRINKS, instance.getFoodDrinkHeads().size(), 6, player);
        addCategory(mainMenu, humans, StaticMenuHeadTextures.Person.getTexture(), LibreHead.CATEGORY.HUMANS, instance.getHumanHeads().size(), 11, player);
        addCategory(mainMenu, humanoids, StaticMenuHeadTextures.Villager.getTexture(), LibreHead.CATEGORY.HUMANOID, instance.getHumanoidHeads().size(), 12, player);
        addCategory(mainMenu, miscellaneous, StaticMenuHeadTextures.Emoji.getTexture(), LibreHead.CATEGORY.MISCELLANEOUS, instance.getMiscellaneousHeads().size(), 13, player);
        addCategory(mainMenu, monsters, StaticMenuHeadTextures.Zombie.getTexture(), LibreHead.CATEGORY.MONSTERS, instance.getMonsterHeads().size(), 14, player);
        addCategory(mainMenu, plants, StaticMenuHeadTextures.Plant.getTexture(), LibreHead.CATEGORY.PLANTS, instance.getPlantHeads().size(), 15, player);

        instance.getMenuManager().open(mainMenu, player);

    }

    private void addCategory(MenuV2 menu, MenuV2 openMenu, String skullTexture,LibreHead.CATEGORY category, int size, int slot, Player player) {
        menu.addItem(new SkullBuilder(skullTexture)
                .name("&d&l"+category.get().getName())
                .addLore("&a"+category.get().getDescription(),"&7"+instance.getDF().format(size)+" heads").slot(slot).leftClickAction(e -> {
                    instance.getMenuManager().open(openMenu,player,menu);
                }));
    }

    private void addHeadsToMenu(List<LibreHead> heads, MenuV2 menu, Player player) {
        heads.forEach(head -> {
            menu.addItem(head.getItem().leftClickAction(event -> {
                player.getInventory().addItem(head.getItem().getCleanItemStack());
            }));
        });
    }

    public MenuV2 generateMenu(List<LibreHead> heads, String name, Player player) {
        MenuV2 menu = new PaginatedMenu(name + " ("+instance.getDF().format(heads.size())+")",36, player);
        addHeadsToMenu(heads, menu, player);
        return menu;
    }


    @Override
    public List<String> getAdditionalCompletions(CommandSender sender) {
        return instance.getHeads().stream().map(LibreHead::getName).toList();
    }

}

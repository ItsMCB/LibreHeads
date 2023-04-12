package org.libregalaxy.libreheads.features;

import me.itsmcb.vexelcore.bukkit.api.command.CustomCommand;
import me.itsmcb.vexelcore.bukkit.api.menuv2.*;
import me.itsmcb.vexelcore.bukkit.api.text.BukkitMsgBuilder;
import me.itsmcb.vexelcore.common.api.command.CMDHelper;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.libregalaxy.libreheads.LibreHead;
import org.libregalaxy.libreheads.LibreHeads;

import java.text.DecimalFormat;
import java.util.List;

public class SkullCmd extends CustomCommand {

    private LibreHeads instance;
    public SkullCmd(LibreHeads instance) {
        super("skull", "Get a skull", "libreheads.skull.get");
        this.instance = instance;
    }

    private DecimalFormat df = new DecimalFormat("###,###");


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
        // Main Menu
        String oakWoodA = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTY3ZDgxM2FlN2ZmZTViZTk1MWE0ZjQxZjJhYTYxOWE1ZTM4OTRlODVlYTVkNDk4NmY4NDk0OWM2M2Q3NjcyZSJ9fX0";
        String cow = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGQ0ZWY3OTM4ZGZkYzg3Yzk4OWNhNWQ5MjMxZTIzZDFlMzY4ZDIxZjgxMDBmODI3MzE2ZDcwZTU0MWY0ZDMxNSJ9fX0";
        String diamondOre = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGRmMjE4NTI1YzE5YTkyMmRmMmQwMDAxNzc4YTFkOTQ1Yzc1ZDJhOTI0YzBlYWEyMGU0YjI3MTlmODQ4NTQyZSJ9fX0";
        String chest = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjg5Yjk4ZjA0YzMyMjdkMzdkMzE5YmJjZmZjNTFmNTJlNzhkOTZhMDViMTI4NTJkMWI0NjRiYjc0MDhhNzgxMCJ9fX0";
        String bread = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTlkY2YxNGVhNzI0OWZlYTZlNzIwMmVlNTg4MjU0YTZjMzJlMzM0NWYxZjNkYTNhYTI2NDlhZmFhNTgyIn19fQ";
        String person = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjE1ZTE0YWExMjM2YjZmZDA0ODI5MGRlMjkxZGZjNDM1YmYxNzJkZjQzNmI4NDk3YzFkZDYxMmVkZDMxYTViZSJ9fX0";
        String villager = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjRmOGI1ZDJkZGQxYjRmNDkzNmI4MWJlMjZhMzZhNGJjZjA1OWE0ZmE5MzBlNzBlYjBjY2U5MGU1ZmIyMGRhZiJ9fX0";
        String emoji = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGU0ZTk5NGVhY2Y5MGI2MGVlODdiMTBjNTBhY2I4MGRkMWRhZjllZTZmMmM2M2E3OWIwMTE1NGIxNmRjZjBjZiJ9fX0";
        String zombie = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjZmMTViOWNkYWE4MzQyZWY0ZDlkNjRkNzc2NzlhNTkyYTIwYzVlMTNlMTg3NTVhN2E0M2EzNTI2NzdmZDA3MSJ9fX0";
        String plant = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjVmYjQyM2EwZjg5Nzc3YTVhNTNjYTJkZDJhOTZmYzMwMjMxMzgwNzA0M2ZkMjliMGUwODQ1YTM3NzA1Yzg5OCJ9fX0";
        int totalHeads = instance.getAlphabetHeads().size()+instance.getAnimalHeads().size()+instance.getBlockHeads().size()+instance.getDecorationHeads().size()+instance.getDemoHeads().size()+instance.getFoodDrinkHeads().size()+instance.getHumanHeads().size()+instance.getHumanoidHeads().size()+instance.getMiscellaneousHeads().size()+instance.getMonsterHeads().size()+instance.getPlantHeads().size();

        MenuV2 mainMenu = new MenuV2("Head Categories ("+df.format(totalHeads)+")", InventoryType.CHEST,18);
        addCategory(mainMenu, alphabet, oakWoodA, LibreHead.CATEGORY.ALPHABET, instance.getAlphabetHeads().size(), 2, player);
        addCategory(mainMenu, animals, cow, LibreHead.CATEGORY.ANIMALS, instance.getAnimalHeads().size(), 3, player);
        addCategory(mainMenu, blocks, diamondOre, LibreHead.CATEGORY.BLOCKS, instance.getBlockHeads().size(), 4, player);
        addCategory(mainMenu, decoration, chest, LibreHead.CATEGORY.DECORATION, instance.getDecorationHeads().size(), 5, player);
        addCategory(mainMenu, foodDrink, bread, LibreHead.CATEGORY.FOOD_DRINKS, instance.getFoodDrinkHeads().size(), 6, player);
        addCategory(mainMenu, humans, person, LibreHead.CATEGORY.HUMANS, instance.getHumanHeads().size(), 11, player);
        addCategory(mainMenu, humanoids, villager, LibreHead.CATEGORY.HUMANOID, instance.getHumanoidHeads().size(), 12, player);
        addCategory(mainMenu, miscellaneous, emoji, LibreHead.CATEGORY.MISCELLANEOUS, instance.getMiscellaneousHeads().size(), 13, player);
        addCategory(mainMenu, monsters, zombie, LibreHead.CATEGORY.MONSTERS, instance.getMonsterHeads().size(), 14, player);
        addCategory(mainMenu, plants, plant, LibreHead.CATEGORY.PLANTS, instance.getPlantHeads().size(), 15, player);
        instance.getMenuManager().open(mainMenu, player);

    }

    private void addHeadsToMenu(List<LibreHead> heads, MenuV2 menu, Player player) {
        heads.forEach(head -> {
            ItemBuilder headBuilder = new SkullBuilder(head.getValue()).name("&r&d&l"+head.getName()).lore(new BukkitMsgBuilder("&a"+head.getCategory().toString()).get(), new BukkitMsgBuilder("&7"+head.getDatabase().toString()).get());

            menu.addItem(new MenuV2Item(headBuilder).leftClickAction(event -> {
                player.getInventory().addItem(headBuilder.getCleanItemStack());
            }));
        });
    }

    public MenuV2 generateMenu(List<LibreHead> heads, String name, Player player) {
        MenuV2 menu = new PaginatedMenu(name + " ("+df.format(heads.size())+")",36, player);
        addHeadsToMenu(heads, menu, player);
        return menu;
    }

    public void addCategory(MenuV2 menu, MenuV2 openMenu, String skullTexture,LibreHead.CATEGORY category, int size, int slot, Player player) {
        menu.addItem(new MenuV2Item(new SkullBuilder(skullTexture).name("&d&l"+category.get().getName())
                .lore("&a"+category.get().getDescription(),"&7"+df.format(size)+" heads")).slot(slot).leftClickAction(action -> {
            instance.getMenuManager().open(openMenu, player);
        }));
    }

    @Override
    public List<String> getAdditionalCompletions(CommandSender sender) {
        return instance.getHeads().stream().map(LibreHead::getName).toList();
    }

}

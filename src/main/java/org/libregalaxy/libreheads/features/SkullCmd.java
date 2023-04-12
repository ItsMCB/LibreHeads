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

            MenuV2 resultsMenu = new PaginatedMenu("Search Results: "+search + " ("+headResults.size()+")",36, player);
            addHeadsToMenu(headResults, resultsMenu, player);

            instance.getMenuManager().open(resultsMenu, player);
            return;
        }

        MenuV2 alphabet = new PaginatedMenu("Alphabet", 36, player);
        List<LibreHead> alphabetHeads = instance.getHeads().stream().filter(head -> head.getCategory().equals(LibreHead.CATEGORY.ALPHABET)).toList();
        addHeadsToMenu(alphabetHeads,alphabet,player);

        MenuV2 animals = new PaginatedMenu("Animals", 36, player);
        List<LibreHead> animalHeads = instance.getHeads().stream().filter(head -> head.getCategory().equals(LibreHead.CATEGORY.ANIMALS)).toList();
        addHeadsToMenu(animalHeads,animals,player);

        MenuV2 blocks = new PaginatedMenu("Blocks", 36, player);
        List<LibreHead> blockHeads = instance.getHeads().stream().filter(head -> head.getCategory().equals(LibreHead.CATEGORY.BLOCKS)).toList();
        addHeadsToMenu(blockHeads,blocks,player);

        MenuV2 decoration = new PaginatedMenu("Decoration", 36, player);
        List<LibreHead> decorationHeads = instance.getHeads().stream().filter(head -> head.getCategory().equals(LibreHead.CATEGORY.DECORATION)).toList();
        addHeadsToMenu(decorationHeads,decoration,player);

        MenuV2 foodDrink = new PaginatedMenu("Food & Drink", 36, player);
        List<LibreHead> foodDrinkHeads = instance.getHeads().stream().filter(head -> head.getCategory().equals(LibreHead.CATEGORY.FOOD_DRINKS)).toList();
        addHeadsToMenu(foodDrinkHeads,foodDrink,player);

        MenuV2 humans = new PaginatedMenu("Humans", 36, player);
        List<LibreHead> humanHeads = instance.getHeads().stream().filter(head -> head.getCategory().equals(LibreHead.CATEGORY.HUMANS)).toList();
        addHeadsToMenu(humanHeads,humans,player);

        MenuV2 humanoids = new PaginatedMenu("Humanoids", 36, player);
        List<LibreHead> humanoidHeads = instance.getHeads().stream().filter(head -> head.getCategory().equals(LibreHead.CATEGORY.HUMANOID)).toList();
        addHeadsToMenu(humanoidHeads,humanoids,player);

        MenuV2 misc = new PaginatedMenu("Miscellaneous", 36, player);
        List<LibreHead> miscHeads = instance.getHeads().stream().filter(head -> head.getCategory().equals(LibreHead.CATEGORY.MISCELLANEOUS)).toList();
        addHeadsToMenu(miscHeads,misc,player);

        MenuV2 monsters = new PaginatedMenu("Monsters", 36, player);
        List<LibreHead> monsterHeads = instance.getHeads().stream().filter(head -> head.getCategory().equals(LibreHead.CATEGORY.MONSTERS)).toList();
        addHeadsToMenu(monsterHeads,monsters,player);

        MenuV2 plants = new PaginatedMenu("Plants", 36, player);
        List<LibreHead> plantHeads = instance.getHeads().stream().filter(head -> head.getCategory().equals(LibreHead.CATEGORY.PLANTS)).toList();
        addHeadsToMenu(plantHeads,plants,player);


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
        int totalHeads = alphabetHeads.size()+animalHeads.size()+blockHeads.size()+decorationHeads.size()+foodDrinkHeads.size()+humanHeads.size()+humanoidHeads.size()+miscHeads.size()+monsterHeads.size()+plantHeads.size();

        MenuV2 mainMenu = new MenuV2("Head Categories ("+totalHeads+")", InventoryType.CHEST,18)
                .addItem(new MenuV2Item(new SkullBuilder(oakWoodA).name("&a&lAlphabet").lore("&e&l"+alphabetHeads.size()+" heads")).slot(0).leftClickAction(action -> {
                    instance.getMenuManager().open(alphabet, player);
                }))
                .addItem(new MenuV2Item(new SkullBuilder(cow).name("&a&lAnimals").lore("&e&l"+animalHeads.size()+" heads")).slot(1).leftClickAction(action -> {
                    instance.getMenuManager().open(animals, player);
                }))
                .addItem(new MenuV2Item(new SkullBuilder(diamondOre).name("&a&lBlocks").lore("&e&l"+blockHeads.size()+" heads")).slot(2).leftClickAction(action -> {
                    instance.getMenuManager().open(blocks, player);
                }))
                .addItem(new MenuV2Item(new SkullBuilder(chest).name("&a&lDecoration").lore("&e&l"+decorationHeads.size()+" heads")).slot(3).leftClickAction(action -> {
                    instance.getMenuManager().open(decoration, player);
                }))
                .addItem(new MenuV2Item(new SkullBuilder(bread).name("&a&lFood & Drink").lore("&e&l"+foodDrinkHeads.size()+" heads")).slot(4).leftClickAction(action -> {
                    instance.getMenuManager().open(foodDrink, player);
                }))
                .addItem(new MenuV2Item(new SkullBuilder(person).name("&a&lHumans").lore("&e&l"+humanHeads.size()+" heads")).slot(5).leftClickAction(action -> {
                    instance.getMenuManager().open(humans, player);
                }))
                .addItem(new MenuV2Item(new SkullBuilder(villager).name("&a&lHumanoid").lore("&e&l"+humanoidHeads.size()+" heads")).slot(6).leftClickAction(action -> {
                    instance.getMenuManager().open(humanoids, player);
                }))
                .addItem(new MenuV2Item(new SkullBuilder(emoji).name("&a&lMiscellaneous").lore("&e&l"+miscHeads.size()+" heads")).slot(7).leftClickAction(action -> {
                    instance.getMenuManager().open(misc, player);
                }))
                .addItem(new MenuV2Item(new SkullBuilder(zombie).name("&a&lMonsters").lore("&e&l"+monsterHeads.size()+" heads")).slot(8).leftClickAction(action -> {
                    instance.getMenuManager().open(monsters, player);
                }))
                .addItem(new MenuV2Item(new SkullBuilder(plant).name("&a&lPlants").lore("&e&l"+plantHeads.size()+" heads")).slot(9).leftClickAction(action -> {
                    instance.getMenuManager().open(plants, player);
                }))
                ;
        instance.getMenuManager().open(mainMenu, player);

    }

    private void addHeadsToMenu(List<LibreHead> heads, MenuV2 menu, Player player) {
        heads.forEach(head -> {
            ItemBuilder headBuilder = new SkullBuilder(head.getValue()).name("&r&d&l"+head.getName()).lore(new BukkitMsgBuilder(head.getCategory().toString()).get());

            menu.addItem(new MenuV2Item(headBuilder).leftClickAction(event -> {
                player.getInventory().addItem(headBuilder.getCleanItemStack());
            }));
        });
    }

    @Override
    public List<String> getAdditionalCompletions(CommandSender sender) {
        return instance.getHeads().stream().map(LibreHead::getName).toList();
    }

}

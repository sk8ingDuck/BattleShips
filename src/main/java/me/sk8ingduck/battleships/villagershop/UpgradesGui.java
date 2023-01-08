package me.sk8ingduck.battleships.villagershop;

import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.game.Team;
import me.sk8ingduck.battleships.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.ipvp.canvas.mask.Mask;
import org.ipvp.canvas.mask.RecipeMask;
import org.ipvp.canvas.slot.Slot;
import org.ipvp.canvas.type.ChestMenu;

public class UpgradesGui extends Gui {

    public UpgradesGui() {
        gui = ChestMenu.builder(3).title("Upgrades").build();

        Mask border = RecipeMask.builder(gui)
                .item('b', new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(" ").build())
                .item('r', new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE).setDisplayName(" ").build())
                .pattern("bbbbbbbbb")
                .pattern("b0r0r0r0b")
                .pattern("bbbbbbbbb").build();

        border.apply(gui);

        Slot upgrade1 = gui.getSlot(10);
        Slot upgrade2 = gui.getSlot(12);
        Slot upgrade3 = gui.getSlot(14);
        Slot upgrade4 = gui.getSlot(16);

        upgrade1.setItem(new ItemBuilder(Material.ENDER_CHEST).setDisplayName("TNT-Kanone aktivieren").setLores("", "§6Kosten§7: §a50 Emeralds").build());
        upgrade2.setItem(new ItemBuilder(Material.ENDER_CHEST).setDisplayName("TNT-Kanone Upgrade 1").setLores("", "§6Kosten§7: §a150 Emeralds").build());
        upgrade3.setItem(new ItemBuilder(Material.ENDER_CHEST).setDisplayName("TNT-Kanone Upgrade 2").setLores("", "§6Kosten§7: §a250 Emeralds").build());
        upgrade4.setItem(new ItemBuilder(Material.ENDER_CHEST).setDisplayName("TNT-Kanone Upgrade 3").setLores("", "§6Kosten§7: §a500 Emeralds").build());

        upgrade1.setClickHandler((player, clickInformation) -> upgrade(player, 1, 50));
        upgrade2.setClickHandler((player, clickInformation) -> upgrade(player, 2, 150));
        upgrade3.setClickHandler((player, clickInformation) -> upgrade(player, 3, 250));
        upgrade4.setClickHandler((player, clickInformation) -> upgrade(player, 4, 500));
    }

    private void upgrade(Player player, int tntGunLevel, int cost) {
        Team team = BattleShips.getInstance().getGame().getTeam(player);
        if (team == null) return;

        if (team.getTntGunLevel() != tntGunLevel - 1) {
            player.sendMessage("§cDu musst erst Upgrade §6" + (tntGunLevel + 1) + " §ckaufen.");
            return;
        }
        if (!player.getInventory().containsAtLeast(new ItemStack(Material.EMERALD), cost)) {
            player.sendMessage("§cDu hast nicht genug Emeralds.");
            return;
        }

        player.getInventory().removeItem(new ItemStack(Material.EMERALD, cost));
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.7F, 1.0F);
        team.sendMessage("§aTNT-Kanone wurde auf §6Stufe " + tntGunLevel + " §ageupgradet!");
        player.closeInventory();
        team.increaseTntGunLevel();
    }
}

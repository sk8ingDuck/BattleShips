package me.sk8ingduck.battleships.listener;

import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.config.MessagesConfig;
import me.sk8ingduck.battleships.config.SettingsConfig;
import me.sk8ingduck.battleships.game.GameSession;
import me.sk8ingduck.battleships.game.GameState;
import me.sk8ingduck.battleships.game.Team;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.ipvp.canvas.type.ChestMenu;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        GameSession game = BattleShips.getGame();
        Player player = event.getPlayer();

        if (game.getCurrentGameState() == null || game.getCurrentGameState() == GameState.LOBBY) {
            if (player.getInventory().getItemInMainHand().getType() != Material.AIR
                    && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
                    && player.getInventory().getItemInMainHand().getItemMeta().getDisplayName()
                    .equals(BattleShips.getSettingsConfig().getTeamChooserItem().getItemMeta().getDisplayName())) {

                chooseTeamGui().open(player);
                return;
            }
        }


        //prevent farm trampling
        if (event.getAction() == Action.PHYSICAL
                && event.getClickedBlock() != null
                && (event.getClickedBlock().getType() == Material.FARMLAND
                || event.getClickedBlock().getType() == Material.SOUL_SOIL)) {
            event.setCancelled(true);
        }

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            if (block != null && block.getType().equals(Material.CHEST)) {
                for (Team chestTeam : game.getPlayingTeams()) {
                    if (!chestTeam.getChestLocation().getBlock().equals(block)) continue;

                    event.setCancelled(true);

                    Team playerTeam = game.getTeam(player);
                    if (playerTeam == null) return; //spectator

                    if (chestTeam.equals(playerTeam)) { //own chest
                        Team bannerOnHead = game.getStolenBanner(player);
                        if (bannerOnHead == null) { //open own captured banners
                            chestTeam.getCapturedBannersChest().open(player);
                            return;
                        }

                        playerTeam.addCapturedBanner(bannerOnHead);
                        game.removeStolenBanner(player);
                        player.getInventory().setHelmet(null);
                        if (chestTeam.equals(bannerOnHead)) //returned own banner
                            chestTeam.resetBanner();
                        Bukkit.broadcastMessage(BattleShips.getMessagesConfig().get("game.bannerCaptured")
                                .replaceAll("%TEAM%", bannerOnHead.toString())
                                .replaceAll("%CAPTURED_TEAM%", playerTeam.toString()));
                        playerTeam.checkWin();
                        return;
                    }

                    chestTeam.getCapturedBannersChest().open(player);
                }
            }
        }
    }

    private ChestMenu chooseTeamGui() {
        GameSession game = BattleShips.getGame();
        ChestMenu gui = ChestMenu.builder(1).title("Team auswählen").build();

        for (int i = 0; i < BattleShips.getSettingsConfig().getTeamCount(); i++) {
            Team team = Team.getActiveTeams()[i];

            gui.getSlot(i).setItemTemplate(team::getTeamChooseItem);
            gui.getSlot(i).setClickHandler((player, clickInformation) -> {
                Team playerTeam = game.getTeam(player);

                if (team.equals(playerTeam)) {
                    game.removeTeam(player);
                    gui.close();
                    return;
                }
                if (team.getSize() == BattleShips.getSettingsConfig().getTeamSize()) {

                    player.sendMessage(BattleShips.getMessagesConfig().get("error.teamFull").replaceAll("%TEAM%", team.toString()));
                    return;
                }

                game.removeTeam(player);

                game.setTeam(player, team);
                gui.close(player);
            });

        }

        return gui;
    }
}

package me.sk8ingduck.battleships.listener;

import me.sk8ingduck.battleships.BattleShips;
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
import org.bukkit.inventory.ItemStack;
import org.ipvp.canvas.type.ChestMenu;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        GameSession game = BattleShips.getInstance().getGame();
        SettingsConfig config = BattleShips.getInstance().getSettingsConfig();
        Player player = event.getPlayer();

        if (game.getCurrentGameState() == null || game.getCurrentGameState() == GameState.LOBBY) {
            if (player.getInventory().getItemInMainHand().getType() != Material.AIR
                    && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
                    && player.getInventory().getItemInMainHand().getItemMeta().getDisplayName()
                    .equals(config.getTeamChooserItem().getItemMeta().getDisplayName())) {

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
                for (Team team : Team.getActiveTeams()) {
                    if (team.getChestLocation().getBlock().equals(block)) {
                        event.setCancelled(true);

                        Team playerTeam = game.getTeam(player);
                        if (playerTeam == null) return; //spectator

                        if (playerTeam.equals(team)) { //own chest
                            Team bannerOnHead = game.getBanner(player);
                            if (bannerOnHead == null) { //open own captured banners
                                team.openCapturedBannersChest(player);
                                return;
                            }

                            playerTeam.addCapturedBanner(bannerOnHead);
                            game.removeStolenBanner(player);
                            player.getInventory().setHelmet(null);
                            if (team.equals(bannerOnHead)) {
                                team.resetBanner();
                            }
                            Bukkit.broadcastMessage("§eDer Banner von Team " + bannerOnHead + " §ewurde in die Base von Team " + playerTeam + " §egebracht!");

                        } else { //other team's chest
                            if (game.getBanner(player) != null) { //player has already a banner on head
                                player.sendMessage("§cDu hast bereits einen Banner. Bringe diesen erst in deine Base zurück.");
                                return;
                            }

                            if (team.hasCapturedBanner(playerTeam)) { //other team has player's team banner
                                team.removeCapturedBanner(playerTeam);
                                Bukkit.broadcastMessage("§eDer Banner von Team " + playerTeam + " §ewurde von " + playerTeam.getColor() + player.getName() + " §egenommen!");
                                game.setBannerStolen(playerTeam, player);
                                player.getInventory().setHelmet(new ItemStack(playerTeam.getBanner()));
                            } else {
                                team.openCapturedBannersChest(player);
                            }
                        }
                    }
                }
            }
        }
    }

    private ChestMenu chooseTeamGui() {
        GameSession game = BattleShips.getInstance().getGame();
        SettingsConfig config = BattleShips.getInstance().getSettingsConfig();

        ChestMenu gui = ChestMenu.builder(1).title("Team auswählen").build();

        for (int i = 0; i < config.getTeamCount(); i++) {
            Team team = Team.getActiveTeams()[i];

            gui.getSlot(i).setItemTemplate(team::getTeamChooseItem);
            gui.getSlot(i).setClickHandler((player, clickInformation) -> {
                if (game.getTeam(player) != null) {
                    if (team.equals(game.getTeam(player))) {
                        game.removeTeam(player);
                        return;
                    }
                }
                if (team.getSize() == config.getTeamSize()) {
                    player.sendMessage("§cTeam " + team + " §cist bereits voll!");
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

package me.sk8ingduck.battleships.listener;

import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.config.TeamConfig;
import me.sk8ingduck.battleships.game.GameSession;
import me.sk8ingduck.battleships.game.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.ipvp.canvas.type.ChestMenu;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        GameSession game = BattleShips.getGame();
        Player player = event.getPlayer();

        if (!game.isIngame()) {
            if (player.getInventory().getItemInMainHand().getType() != Material.AIR
                    && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
                    && player.getInventory().getItemInMainHand().getItemMeta().getDisplayName()
                    .equals(BattleShips.getSettingsConfig().getTeamChooserItem().getItemMeta().getDisplayName())) {

                chooseTeamGui().open(player);
                return;
            }
        }

        if (game.isIngame()
                && player.getInventory().getItemInMainHand().getType() != Material.AIR
                && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
                && game.getTeam(player) == null
                && player.getInventory().getItemInMainHand().getItemMeta().getDisplayName()
                .equals(BattleShips.getSettingsConfig().getTeleportItem().getItemMeta().getDisplayName())) {
            compassGui().open(player);
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
                    if (!chestTeam.getChest().getLocation().getBlock().equals(block)) continue;

                    event.setCancelled(true);

                    Team playerTeam = game.getTeam(player);
                    if (playerTeam == null) return; //spectator

                    if (chestTeam.equals(playerTeam)) { //own chest
                        Team bannerOnHead = game.getStolenBanner(player);
                        if (bannerOnHead == null) { //open own captured banners
                            chestTeam.getCapturedBannersChest().open(player);
                            return;
                        }

                        game.getStats(player.getUniqueId()).addCapturedBanner();
                        playerTeam.addCapturedBanner(bannerOnHead);
                        game.removeStolenBanner(player);
                        player.getInventory().setHelmet(null);
                        if (chestTeam.equals(bannerOnHead)) { //returned own banner
                            chestTeam.getBanner().setBlock();
                            chestTeam.addCapturedBanner(chestTeam);
                        }

                        game.updateBoards();
                        Bukkit.broadcastMessage(BattleShips.getMessagesConfig().get("game.bannerCaptured")
                                .replaceAll("%TEAM%", bannerOnHead.toString())
                                .replaceAll("%CAPTURE_TEAM%", playerTeam.toString()));
                        game.checkWin(playerTeam);
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
        TeamConfig teamConfig = BattleShips.getTeamConfig();
        for (int i = 0; i < teamConfig.getTeamCount(); i++) {
            Team team = teamConfig.getActiveTeams().get(i);

            gui.getSlot(i).setItemTemplate(team::getTeamChooseItem);
            gui.getSlot(i).setClickHandler((player, clickInformation) -> {
                Team playerTeam = game.getTeam(player);

                if (team.equals(playerTeam)) {
                    game.removeTeam(player);
                    gui.close();
                    return;
                }
                if (team.getSize() == teamConfig.getTeamSize()) {

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

    private ChestMenu compassGui() {
        GameSession game = BattleShips.getGame();

        ChestMenu gui = ChestMenu.builder(3).title("§5Teleporter").build();
        int slot = 0;
        for (Team team : game.getPlayingTeams()) { //get all teams
            for (Player member : team.getMembers()) { //get all members of each team
                gui.getSlot(slot).setItem(getHead(member.getName(), team.getColor())); //add player head of team member
                gui.getSlot(slot++).setClickHandler((player, clickInformation) ->
                        player.teleport(member.getLocation())); //teleport spectator to player on click
            }
        }

        return gui;
    }
    private ItemStack getHead(String name, ChatColor color) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        headMeta.setDisplayName(color + name);
        headMeta.setOwner(name);
        head.setItemMeta(headMeta);
        return head;
    }
}

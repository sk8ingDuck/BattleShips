package me.sk8ingduck.battleships.listener;

import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.config.MessagesConfig;
import me.sk8ingduck.battleships.game.GameSession;
import me.sk8ingduck.battleships.game.Team;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;


public class PlayerDeathListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.getDrops().removeIf(item -> !item.getType().equals(Material.EMERALD));

        Player player = event.getEntity();
        Team playerTeam = BattleShips.getGame().getTeam(player);
        MessagesConfig msgs = BattleShips.getMessagesConfig();
        if (playerTeam == null) return;

        GameSession game = BattleShips.getGame();
        game.getStats(player.getUniqueId()).addDeath();

        if (event.getEntity().getKiller() != null) {
            Player killer = event.getEntity().getKiller();
            Team killerTeam = BattleShips.getGame().getTeam(killer);
            if (killerTeam == null) return;


            event.setDeathMessage(msgs.get("player.deathMessage")
                    .replaceAll("%PLAYER%", playerTeam.getColor() + player.getName())
                    .replaceAll("%KILLER%", killerTeam.getColor() + killer.getName()));
            game.getStats(killer.getUniqueId()).addKill();
        } else {
            event.setDeathMessage(msgs.get("player.otherDeathMessage")
                    .replaceAll("%PLAYER%", playerTeam.getColor() + player.getName()));
        }


        Team bannerOnHead = game.getStolenBanner(player);
        if (bannerOnHead != null) {
            bannerOnHead.resetBanner();
            bannerOnHead.addCapturedBanner(bannerOnHead);
            game.checkWin(bannerOnHead);
            game.removeStolenBanner(player);
            Bukkit.broadcastMessage(msgs.get("game.bannerReturned").replaceAll("%TEAM%", bannerOnHead.toString()));
        }
    }
}

package me.sk8ingduck.battleships.listener;

import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.game.GameSession;
import me.sk8ingduck.battleships.game.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Team playerTeam = BattleShips.getInstance().getGame().getTeam(player);
        if (playerTeam == null) return;

        if (event.getEntity().getKiller() != null) {
            Player killer = event.getEntity().getKiller();
            Team killerTeam = BattleShips.getInstance().getGame().getTeam(killer);
            if (killerTeam == null) return;
            event.setDeathMessage(playerTeam.getColor() + player.getName() + " §7wurde von " + killerTeam.getColor() + killer.getName() + " §7getötet.");
        } else {
            event.setDeathMessage(playerTeam.getColor() + player.getName() + " §7ist gestorben.");
        }

        GameSession game = BattleShips.getInstance().getGame();

        Team stolenBannerTeam = game.getBanner(player);
        if (stolenBannerTeam != null) {
            stolenBannerTeam.resetBanner();
            game.setBannerStolen(null, player);
            Bukkit.broadcastMessage("§eDer Banner von Team " + stolenBannerTeam + " §ewurde zurückgebracht!");
        }
    }
}

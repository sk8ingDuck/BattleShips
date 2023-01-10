package me.sk8ingduck.battleships.listener;

import de.nandi.chillsuchtapi.api.ChillsuchtAPI;
import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerLoginListener implements Listener {


    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(PlayerLoginEvent event) {
        if (BattleShips.getGame().getCurrentGameState() != null &&
                BattleShips.getGame().getCurrentGameState() != GameState.LOBBY) {
            event.getPlayer().sendMessage(ChillsuchtAPI.PREFIX + "§cDas Spiel hat bereits angefangen.");
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "");
            return;
        }
        if (event.getResult() != PlayerLoginEvent.Result.KICK_FULL)
            return;
        if (event.getPlayer().hasPermission("chillsucht.join.server")) {
            List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
            Collections.shuffle(players);
            for (Player player : players) {
                if (!player.hasPermission("chillsucht.join.server")) {
                    if (players.size() == Bukkit.getMaxPlayers()) {
                        player.sendMessage("§cDu wurdest von einem Spieler mit mehr Rechten gekickt. " +
                                "Hole dir §6Premium§c um so etwas zu umgehen.");
                        player.kickPlayer("");
                    }
                    event.allow();
                    return;
                }
            }
            event.getPlayer().sendMessage(ChillsuchtAPI.PREFIX + "§cDie Runde ist schon voll mit Premiums.");
            event.setKickMessage("");
        } else {
            event.getPlayer().sendMessage(ChillsuchtAPI.PREFIX +
                    "§cDie Runde ist voll. Hole dir §6Premium§c um vollen Runden beizutreten.");
            event.setKickMessage("");
        }
    }
}

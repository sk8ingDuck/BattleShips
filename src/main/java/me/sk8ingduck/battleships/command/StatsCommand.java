package me.sk8ingduck.battleships.command;

import de.nandi.chillsuchtapi.api.ChillsuchtAPI;
import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.config.MessagesConfig;
import me.sk8ingduck.battleships.game.GameSession;
import me.sk8ingduck.battleships.mysql.PlayerStats;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class StatsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        GameSession game = BattleShips.getGame();
        MessagesConfig msgs = BattleShips.getMessagesConfig();

        if (args.length == 0) {
            if (sender instanceof Player player) {
                PlayerStats stats = game.getStats(player.getUniqueId());
                player.sendMessage(msgs.get("player.stats")
                        .replaceAll("%PLAYER%", player.getName())
                        .replaceAll("%KILLS%", String.valueOf(stats.getKills()))
                        .replaceAll("%DEATHS%", String.valueOf(stats.getDeaths()))
                        .replaceAll("%KILLS_DEATHS_RATIO%", stats.getKD())
                        .replaceAll("%GAMES_PLAYED%", String.valueOf(stats.getGamesPlayed()))
                        .replaceAll("%GAMES_WON%", String.valueOf(stats.getGamesWon()))
                        .replaceAll("%WIN_LOSS_RATIO%", stats.getWL())
                        .replaceAll("%FARMED_EMERALDS%", String.valueOf(stats.getFarmedEmeralds()))
                        .replaceAll("%CAPTURED_BANNERS%", String.valueOf(stats.getCapturedBanners())));
            } else {
                sender.sendMessage("Not a player.");
            }
        } else {
            String playerName = args[0];
            UUID playerUUID = ChillsuchtAPI.getGeneralAPI().getUUIDFromName(playerName);
            PlayerStats stats = game.getStats(playerUUID);
            if (stats == null) {
                stats = BattleShips.getMySQL().getPlayerStats(playerUUID);
            }
            if (stats == null) {
                sender.sendMessage("§cPlayer not found.");
                return true;
            }
            sender.sendMessage(msgs.get("player.stats")
                    .replaceAll("%PLAYER%", playerName)
                    .replaceAll("%KILLS%", String.valueOf(stats.getKills()))
                    .replaceAll("%DEATHS%", String.valueOf(stats.getDeaths()))
                    .replaceAll("%KILLS_DEATHS_RATIO%", stats.getKD())
                    .replaceAll("%GAMES_PLAYED%", String.valueOf(stats.getGamesPlayed()))
                    .replaceAll("%GAMES_WON%", String.valueOf(stats.getGamesWon()))
                    .replaceAll("%WIN_LOSS_RATIO%", stats.getWL())
                    .replaceAll("%FARMED_EMERALDS%", String.valueOf(stats.getFarmedEmeralds()))
                    .replaceAll("%CAPTURED_BANNERS%", String.valueOf(stats.getCapturedBanners())));
        }
        return true;
    }

}

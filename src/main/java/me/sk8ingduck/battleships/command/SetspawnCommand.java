package me.sk8ingduck.battleships.command;

import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.game.Team;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetspawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Not a player.");
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage("Syntax: /setspawn <Team>");
            return true;
        }

        Player player = (Player) sender;
        String teamName = args[0];

        Team team = Team.valueOf(teamName.toUpperCase());
        BattleShips.getInstance().getTeamConfig().setSpawnLocation(team, player.getLocation());
        sender.sendMessage("Spawn Location von Team " + team + " gesetzt.");
        return true;
    }
}

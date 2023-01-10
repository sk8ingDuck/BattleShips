package me.sk8ingduck.battleships.command;

import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.game.Team;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

public class SetspawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Not a player.");
            return true;
        }
        if (!(player.hasPermission("chillsucht.command.setup"))) {
            BattleShips.getTranslateAPI().sendTranslation(player,"command.perms");
            return false;
        }
        if (args.length != 1 && args.length != 2) {
            sender.sendMessage("Mögliche Befehle:");
            sender.sendMessage("/setspawn <Team>");
            sender.sendMessage("/setspawn <Team> <banner | bannerChest>");
            sender.sendMessage("/setspawn <lobby | spectatorspawn | shop>");
            return true;
        }

        if (args.length == 2) {
            String teamName = args[0];
            Team team = Team.valueOf(teamName.toUpperCase());
            if (args[1].equalsIgnoreCase("banner")) {
                BattleShips.getInstance().getTeamConfig().setBannerLocation(team, player.getLocation());
                sender.sendMessage("Banner Location von Team " + team + " gesetzt.");
                return true;
            } else if (args[1].equalsIgnoreCase("bannerchest")) {
                BattleShips.getInstance().getTeamConfig().setChestLocation(team, player.getLocation());
                sender.sendMessage("Chest Location von Team " + team + " gesetzt.");
                return true;
            }
        }

        if (args[0].equalsIgnoreCase("lobby")) {
            BattleShips.getInstance().getSettingsConfig().setLobbySpawn(player.getLocation());
            sender.sendMessage("Lobbyspawn gesetzt.");
            return true;
        }

        if (args[0].equalsIgnoreCase("shop")) {
            Villager v = (Villager) player.getWorld().spawnEntity(player.getLocation(), EntityType.VILLAGER);
            v.setCustomName("Shop");
            v.setAI(false);
            v.setInvulnerable(true);
            v.setCanPickupItems(false);
            v.setRemoveWhenFarAway(false);
            return true;
        }


        String teamName = args[0];
        Team team = Team.valueOf(teamName.toUpperCase());

        BattleShips.getInstance().getTeamConfig().setSpawnLocation(team, player.getLocation());
        sender.sendMessage("Spawn Location von Team " + team + " gesetzt.");
        return true;
    }


}

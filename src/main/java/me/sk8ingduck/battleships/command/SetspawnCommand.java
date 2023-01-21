package me.sk8ingduck.battleships.command;

import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.game.Team;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import java.util.HashMap;

public class SetspawnCommand implements CommandExecutor {

    public static final HashMap<Player, Team> banner = new HashMap<>();
    public static final HashMap<Player, Team> chest = new HashMap<>();

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
            sender.sendMessage("/setspawn <Team> <banner | chest | tntgun>");
            sender.sendMessage("/setspawn <lobby | spectator | shop>");
            return true;
        }

        if (args.length == 2) {
            String teamName = args[0];
            Team team = BattleShips.getTeamConfig().getTeamByName(teamName);
            if (args[1].equalsIgnoreCase("banner")) {
                sender.sendMessage("§ePlatziere nun den Banner von Team " + team);
                banner.put(player, team);
                return true;
            } else if (args[1].equalsIgnoreCase("chest")) {
                sender.sendMessage("§ePlatziere nun die Kiste von Team " + team);
                chest.put(player, team);
                return true;
            } else if (args[1].equalsIgnoreCase("tntgun")) {
                BattleShips.getTeamConfig().setTntGunLocation(team, player.getLocation()
                        .getBlock().getRelative(BlockFace.DOWN).getLocation());
                sender.sendMessage("Tnt-Gun von Team " + team + " gesetzt.");
                return true;
            }
        }

        if (args[0].equalsIgnoreCase("lobby")) {
            BattleShips.getSettingsConfig().setLobbySpawn(player.getLocation());
            sender.sendMessage("Lobbyspawn gesetzt.");
            return true;
        }

        if (args[0].equalsIgnoreCase("spectator")) {
            BattleShips.getSettingsConfig().setSpectatorSpawn(player.getLocation());
            sender.sendMessage("Spectatorspawn gesetzt.");
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
        Team team = BattleShips.getTeamConfig().getTeamByName(teamName);

        BattleShips.getTeamConfig().setSpawnLocation(team, player.getLocation());
        sender.sendMessage("Spawn Location von Team " + team + " gesetzt.");
        return true;
    }

}

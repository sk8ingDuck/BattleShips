package me.sk8ingduck.battleships.config;

import me.sk8ingduck.battleships.game.Team;
import me.sk8ingduck.battleships.game.TeamItem;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TeamConfig extends Config {

    private final int teamCount;
    private final int teamSize;
    private final ArrayList<Team> teams;

    public TeamConfig(String name, File path) {
        super(name, path);

        this.teamCount = (int) getPathOrSet("teamCount", 8);
        this.teamSize = (int) getPathOrSet("teamSize", 1);

        ArrayList<Team> defaultTeams = new ArrayList<>();
        defaultTeams.add(new Team("Rot", ChatColor.RED, new ItemStack(Material.RED_WOOL)));
        defaultTeams.add(new Team("Grün", ChatColor.GREEN, new ItemStack(Material.GREEN_WOOL)));
        defaultTeams.add(new Team("Blau", ChatColor.BLUE, new ItemStack(Material.BLUE_WOOL)));
        defaultTeams.add(new Team("Gelb", ChatColor.YELLOW, new ItemStack(Material.YELLOW_WOOL)));
        defaultTeams.add(new Team("Orange", ChatColor.GOLD, new ItemStack(Material.ORANGE_WOOL)));
        defaultTeams.add(new Team("Lila", ChatColor.LIGHT_PURPLE, new ItemStack(Material.PURPLE_WOOL)));
        defaultTeams.add(new Team("Grau", ChatColor.GRAY, new ItemStack(Material.GRAY_WOOL)));
        defaultTeams.add(new Team("Weiss", ChatColor.WHITE, new ItemStack(Material.WHITE_WOOL)));

        teams = (ArrayList<Team>) getPathOrSet("teams", defaultTeams);
    }

    public int getTeamCount() {
        return teamCount;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public void setSpawnLocation(Team team, Location location) {
        getFileConfiguration().set("team." + team.getName() + ".spawnLocation", location);
        team.setSpawnLocation(location);
        save();
    }

    public void setTntGunLocation(Team team, Location location) {
        getFileConfiguration().set("team." + team.getName() + ".tntGunLocation", location);
        team.setTntGunLocation(location);
        save();
    }

    public void setBanner(Team team, Block block) {
        TeamItem banner = new TeamItem(block);
        getFileConfiguration().set("team." + team.getName() + ".banner", banner);
        team.setBanner(banner);
        save();
    }

    public void setChest(Team team, Block block) {
        TeamItem chest = new TeamItem(block);
        getFileConfiguration().set("team." + team.getName() + ".chest", chest);
        team.setChest(chest);
        save();
    }

    public List<Team> getActiveTeams() {
        return teams.subList(0, teamCount);
    }

    public Team getTeamByName(String name) {
        return teams.stream().filter(team -> team.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
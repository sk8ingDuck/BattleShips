package me.sk8ingduck.battleships.config;

import me.sk8ingduck.battleships.util.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;

public class SettingsConfig extends Config {

    private final int teamCount;
    private final int teamSize;
    private final int maxPlayers;
    private final int neededPlayersToStart;
    private final ItemStack teamChooserItem;
    private Location lobbySpawn;
    private ArrayList<Location> villagerSpawns;

    public SettingsConfig(String name, File path) {
        super(name, path);

        this.teamCount = (int) getPathOrSet("teamCount", 8);
        this.teamSize = (int) getPathOrSet("teamSize", 1);
        this.maxPlayers = (int) getPathOrSet("maxPlayers", 12);
        this.neededPlayersToStart = (int) getPathOrSet("neededPlayersToStart", 1);
        this.teamChooserItem = (ItemStack) getPathOrSet("teamChooserItem", new ItemBuilder(Material.CHEST).setDisplayName("Team auswählen").build());
        this.lobbySpawn = (Location) getPathOrSet("lobbySpawn", null);
        this.villagerSpawns = (ArrayList<Location>) getPathOrSet("villagerSpawns", new ArrayList<Location>());
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getNeededPlayersToStart() {
        return neededPlayersToStart;
    }

    public int getTeamCount() {
        return teamCount;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public ItemStack getTeamChooserItem() {
        return teamChooserItem;
    }

    public Location getLobbySpawn() {
        return lobbySpawn;
    }

    public void setLobbySpawn(Location lobbySpawn) {
        this.lobbySpawn = lobbySpawn;
        getFileConfiguration().set("lobbySpawn", lobbySpawn);
        save();
    }

    public ArrayList<Location> getVillagerSpawns() {
        return villagerSpawns;
    }

    public void addVillagerSpawn(Location location) {
        villagerSpawns.add(location);
        save();
    }

    public boolean removeVillagerSpawn(Location location) {
        if (villagerSpawns.removeIf(villagerSpawn ->
                villagerSpawn.getWorld().equals(location.getWorld())
                        && villagerSpawn.getBlockX() == location.getBlockX()
                        && villagerSpawn.getBlockY() == location.getBlockY()
                        && villagerSpawn.getBlockZ() == location.getBlockZ())) {
            save();
            return true;
        }
        return false;
    }
}

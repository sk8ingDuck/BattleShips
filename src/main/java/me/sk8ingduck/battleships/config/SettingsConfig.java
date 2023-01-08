package me.sk8ingduck.battleships.config;

import me.sk8ingduck.battleships.util.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class SettingsConfig extends Config {

    private final int teamCount;
    private final int teamSize;
    private final int neededPlayersToStart;
    private final ItemStack teamChooserItem;
    private Location lobbySpawn;

    public SettingsConfig(String name, File path) {
        super(name, path);

        this.teamCount = (int) getPathOrSet("teamCount", 8);
        this.teamSize = (int) getPathOrSet("teamSize", 1);
        this.neededPlayersToStart = (int) getPathOrSet("neededPlayersToStart", 4);
        this.teamChooserItem = (ItemStack) getPathOrSet("teamChooserItem", new ItemBuilder(Material.CHEST).setDisplayName("Team auswählen").build());
        this.lobbySpawn = (Location) getPathOrSet("lobbySpawn", null);
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

}

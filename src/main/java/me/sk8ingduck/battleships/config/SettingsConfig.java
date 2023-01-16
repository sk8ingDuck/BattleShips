package me.sk8ingduck.battleships.config;

import me.sk8ingduck.battleships.util.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class SettingsConfig extends Config {

    private final String mapName;
    private final int teamCount;
    private final int teamSize;
    private final int neededPlayersToStart;
    private final ItemStack teamChooserItem;
    private final ItemStack tntGunItem;
    private Location lobbySpawn;

    public SettingsConfig(String name, File path) {
        super(name, path);
        this.mapName = (String) getPathOrSet("mapName", "Penis");
        this.teamCount = (int) getPathOrSet("teamCount", 8);
        this.teamSize = (int) getPathOrSet("teamSize", 1);
        this.neededPlayersToStart = (int) getPathOrSet("neededPlayersToStart", 4);
        this.teamChooserItem = (ItemStack) getPathOrSet("teamChooserItem", new ItemBuilder(Material.CHEST).setDisplayName("Team auswählen").build());
        this.tntGunItem = (ItemStack) getPathOrSet("tntGunItem", new ItemBuilder(Material.CROSSBOW).setGlowing()
                .setDisplayName("§0§kO §cTNT-Gun §0§kO").setDamage(464).setCrossBowCharged().build());
        this.lobbySpawn = (Location) getPathOrSet("lobbySpawn", null);
    }

    public String getMapName() {
        return mapName;
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

    public ItemStack getTntGunItem() {
        return tntGunItem;
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
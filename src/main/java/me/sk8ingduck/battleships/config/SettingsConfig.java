package me.sk8ingduck.battleships.config;

import me.sk8ingduck.battleships.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class SettingsConfig extends Config {


    private final int teamCount;
    private final int teamSize;
    private final int maxPlayers;
    private final int neededPlayersToStart;
    private final ItemStack teamChooserItem;

    public SettingsConfig(String name, File path) {
        super(name, path);

        this.teamCount = (int) getPathOrSet("teamCount", 8);
        this.teamSize = (int) getPathOrSet("teamSize", 1);
        this.maxPlayers = (int) getPathOrSet("maxPlayers", 12);
        this.neededPlayersToStart = (int) getPathOrSet("neededPlayersToStart", 1);
        this.teamChooserItem =(ItemStack) getPathOrSet("teamChooserItem", ItemUtil.createItem(Material.CHEST, "Team auswählen"));
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
}

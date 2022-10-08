package me.sk8ingduck.battleships.game;

import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.config.SettingsConfig;
import me.sk8ingduck.battleships.config.TeamConfig;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public enum Team {

    RED("Rot", "&c", null, null, new ItemStack(Material.RED_BANNER), new ItemStack(Material.RED_WOOL)),
    GREEN("Grün", "&a", null, null, new ItemStack(Material.GREEN_BANNER), new ItemStack(Material.GREEN_WOOL)),
    BLUE("Blau", "&9", null, null, new ItemStack(Material.BLUE_BANNER), new ItemStack(Material.BLUE_WOOL)),
    YELLOW("Gelb", "&e", null, null, new ItemStack(Material.YELLOW_BANNER), new ItemStack(Material.YELLOW_WOOL)),
    WHITE("Weiss", "&f", null, null, new ItemStack(Material.WHITE_BANNER), new ItemStack(Material.WHITE_WOOL)),
    ORANGE("Orange", "&6", null, null, new ItemStack(Material.ORANGE_BANNER), new ItemStack(Material.ORANGE_WOOL)),
    PURPLE("Lila", "&d", null, null, new ItemStack(Material.PURPLE_BANNER), new ItemStack(Material.PURPLE_WOOL)),
    GRAY("Grau", "&8", null, null, new ItemStack(Material.GRAY_BANNER), new ItemStack(Material.GRAY_WOOL));

    private final String name;
    private final String color;
    private final Location spawnLocation;
    private final Location bannerLocation;
    private final ItemStack banner;
    private final ItemStack teamChooseItem;
    private final ArrayList<Player> members;

    private final TeamConfig teamConfig = BattleShips.getInstance().getTeamConfig();
    private final SettingsConfig settingsConfig = BattleShips.getInstance().getSettingsConfig();


    Team(String defaultName, String defaultColor, Location defaultSpawnLocation, Location defaultBannerLocation, ItemStack defaultBannerItem, ItemStack defaultTeamChooseItem) {
        this.name = (String) teamConfig.getPathOrSet("team." + name() + ".name", defaultName);
        this.color = (String) teamConfig.getPathOrSet("team." + name() + ".color", defaultColor);
        this.spawnLocation = (Location) teamConfig.getPathOrSet("team." + name() + ".spawnLocation", defaultSpawnLocation);
        this.bannerLocation = (Location) teamConfig.getPathOrSet("team." + name() + ".bannerLocation", defaultBannerLocation);
        this.banner = (ItemStack) teamConfig.getPathOrSet("team." + name()+ ".bannerItem", defaultBannerItem);
        this.teamChooseItem = (ItemStack) teamConfig.getPathOrSet("team." + name() + ".teamChooseItem", defaultTeamChooseItem);

        this.members = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public Location getBannerLocation() {
        return bannerLocation;
    }

    public ItemStack getBanner() {
        return banner;
    }

    public ItemStack getTeamChooseItem() {
        ItemMeta itemMeta = teamChooseItem.getItemMeta();
        String prefix = members.size() == settingsConfig.getTeamSize() ? "§4" : "§a";
        itemMeta.displayName(Component.text(this + " " + prefix + members.size() + "/" + settingsConfig.getTeamSize()));
        ArrayList<Component> lore = new ArrayList<>();
        members.forEach(member -> lore.add(Component.text(member.getName())));
        itemMeta.lore(lore);
        teamChooseItem.setItemMeta(itemMeta);

        return teamChooseItem;
    }

    public void setSpawnLocation(Location location) {
        teamConfig.setSpawnLocation(this, location);
    }

    public void addMember(Player player) {
        members.add(player);
    }

    public void removeMember(Player player) {
        members.remove(player);
    }

    public void sendMessage(String message) {
        members.forEach(member -> member.sendMessage(message));
    }

    @Override
    public String toString() {
        return color + name + "§r";
    }
}
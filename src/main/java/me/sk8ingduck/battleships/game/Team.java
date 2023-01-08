package me.sk8ingduck.battleships.game;

import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.config.TeamConfig;
import me.sk8ingduck.battleships.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.ipvp.canvas.type.ChestMenu;

import java.util.ArrayList;

public enum Team {

    RED("Rot", "RED", new ItemStack(Material.RED_BANNER), new ItemStack(Material.RED_WOOL)),
    GREEN("Grün", "GREEN", new ItemStack(Material.GREEN_BANNER), new ItemStack(Material.GREEN_WOOL)),
    BLUE("Blau", "BLUE", new ItemStack(Material.BLUE_BANNER), new ItemStack(Material.BLUE_WOOL)),
    YELLOW("Gelb", "YELLOW", new ItemStack(Material.YELLOW_BANNER), new ItemStack(Material.YELLOW_WOOL)),
    ORANGE("Orange", "GOLD", new ItemStack(Material.ORANGE_BANNER), new ItemStack(Material.ORANGE_WOOL)),
    PURPLE("Lila", "LIGHT_PURPLE", new ItemStack(Material.PURPLE_BANNER), new ItemStack(Material.PURPLE_WOOL)),
    GRAY("Grau", "GRAY", new ItemStack(Material.GRAY_BANNER), new ItemStack(Material.GRAY_WOOL)),
    WHITE("Weiss", "WHITE", new ItemStack(Material.WHITE_BANNER), new ItemStack(Material.WHITE_WOOL));

    private org.bukkit.scoreboard.Team scoreboardTeam;
    private final String name;
    private final ChatColor color;
    private Location spawnLocation;
    private Location bannerLocation;
    private Location chestLocation;
    private final ItemStack banner;
    private final ItemStack teamChooseItem;
    private final ArrayList<Player> members;
    private int tntGunLevel;
    private final ArrayList<Team> capturedBanners;

    Team(String defaultName, String defaultColor, ItemStack defaultBannerItem, ItemStack defaultTeamChooseItem) {

        TeamConfig teamConfig = BattleShips.getInstance().getTeamConfig();
        this.name = (String) teamConfig.getPathOrSet("team." + name() + ".name", defaultName);
        this.color = ChatColor.valueOf((String) teamConfig.getPathOrSet("team." + name() + ".color", defaultColor));
        this.spawnLocation = (Location) teamConfig.getPathOrSet("team." + name() + ".spawnLocation", null);
        this.bannerLocation = (Location) teamConfig.getPathOrSet("team." + name() + ".bannerLocation", null);
        this.chestLocation = (Location) teamConfig.getPathOrSet("team." + name() + ".chestLocation", null);
        this.banner = (ItemStack) teamConfig.getPathOrSet("team." + name() + ".bannerItem", defaultBannerItem);
        this.teamChooseItem = (ItemStack) teamConfig.getPathOrSet("team." + name() + ".teamChooseItem", defaultTeamChooseItem);
        this.members = new ArrayList<>();
        this.tntGunLevel = 0;
        capturedBanners = new ArrayList<>();
        capturedBanners.add(this);

        scoreboardTeam = BattleShips.getInstance().getScoreboard().getTeam(name());
        if (scoreboardTeam == null)
            scoreboardTeam = BattleShips.getInstance().getScoreboard().registerNewTeam(name());

        scoreboardTeam.setColor(color);
        resetBanner();
    }

    public String getName() {
        return name;
    }

    public ChatColor getColor() {
        return color;
    }

    public void setSpawnLocation(Location location) {
        spawnLocation = location;
    }

    public void teleportPlayers() {
        members.forEach(member -> member.teleport(spawnLocation));
    }

    public void teleportPlayer(Player player) {
        player.teleport(spawnLocation);
    }

    public void setBannerLocation(Location bannerLocation) {
        if (this.bannerLocation != null) {
            this.bannerLocation.getBlock().setType(Material.AIR);
        }

        this.bannerLocation = bannerLocation;

        resetBanner();
    }

    public void resetBanner() {
        if (bannerLocation != null)
            bannerLocation.getBlock().setType(banner.getType());
    }

    public Location getBannerLocation() {
        return bannerLocation;
    }


    public ItemStack getBanner() {
        return banner;
    }

    public Location getChestLocation() {
        return chestLocation;
    }

    public void setChestLocation(Location chestLocation) {
        if (this.chestLocation != null) {
            this.chestLocation.getBlock().setType(Material.AIR);
        }

        this.chestLocation = chestLocation;
        this.chestLocation.getBlock().setType(Material.CHEST);
    }


    public ItemStack getTeamChooseItem(Player player) {
        int maxTeamSize = BattleShips.getInstance().getSettingsConfig().getTeamSize();
        String prefix = maxTeamSize == members.size() ? "§4" : "§a";
        String[] lores = new String[members.size()];
        for (int i = 0; i < members.size(); i++) {
            lores[i] = "§7- " + members.get(i).getName();
        }
        ItemBuilder item = new ItemBuilder(teamChooseItem)
                .setDisplayName("§rTeam " + this + prefix + " (" + members.size() + "/" + maxTeamSize + ")")
                .setLores(lores);
        if (members.contains(player)) {
            item.setGlowing();
        }

        return item.build();
    }

    public void addMember(Player player) {
        player.sendMessage("§eDu bist Team " + this + " §ebeigetreten.");
        members.add(player);
        scoreboardTeam.addPlayer(player);
    }

    public void removeMember(Player player) {
        player.sendMessage("§eDu hast Team " + this + " §everlassen.");
        members.remove(player);
        scoreboardTeam.removePlayer(player);
    }

    public int getSize() {
        return members.size();
    }

    public int getTntGunLevel() {
        return tntGunLevel;
    }

    public void increaseTntGunLevel() {
        tntGunLevel++;
    }

    public boolean hasCapturedBanner(Team team) {
        return capturedBanners.contains(team);
    }

    public void addCapturedBanner(Team team) {
        if (!capturedBanners.contains(team))
            capturedBanners.add(team);
    }

    public void removeCapturedBanner(Team team) {
        capturedBanners.remove(team);
    }

    public void openCapturedBannersChest(Player player) {
        ChestMenu gui = ChestMenu.builder(1).title("Eroberte Banner von " + this).build();
        for (int i = 0; i < capturedBanners.size(); i++)
            gui.getSlot(i).setItem(capturedBanners.get(i).getBanner());

        gui.open(player);
    }

    public void sendMessage(String message) {
        members.forEach(member -> member.sendMessage(message));
    }


    @Override
    public String toString() {
        return color + name + "§r";
    }

    public static Team[] getActiveTeams() {
        int teamCount = BattleShips.getInstance().getSettingsConfig().getTeamCount();
        Team[] activeTeams = new Team[teamCount];
        for (int i = 0; i < teamCount; i++) {
            activeTeams[i] = values()[i];
        }

        return activeTeams;
    }
}
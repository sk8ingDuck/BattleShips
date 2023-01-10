package me.sk8ingduck.battleships.game;

import de.nandi.chillsuchtapi.api.ChillsuchtAPI;
import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.config.TeamConfig;
import me.sk8ingduck.battleships.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.ipvp.canvas.slot.Slot;
import org.ipvp.canvas.type.ChestMenu;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

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

        scoreboardTeam = BattleShips.getScoreboard().getTeam("z" + name());
        if (scoreboardTeam == null)
            scoreboardTeam = BattleShips.getScoreboard().registerNewTeam("z" + name());

        scoreboardTeam.setColor(color);
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
        members.forEach(member -> {
            member.getInventory().clear();
            member.setFireTicks(0);
            member.setHealth(20);
            member.setFoodLevel(20);
            member.getActivePotionEffects().forEach(effect -> member.removePotionEffect(effect.getType()));
            member.teleport(spawnLocation);
        });
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

    public void removeBannerAndChest() {
        if (bannerLocation != null)
            bannerLocation.getBlock().setType(Material.AIR);
        if (chestLocation != null)
            chestLocation.getBlock().setType(Material.AIR);
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
        int maxTeamSize = BattleShips.getSettingsConfig().getTeamSize();
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
        player.sendMessage(BattleShips.getMessagesConfig().get("player.joinTeam").replaceAll("%TEAM%", toString()));
        members.add(player);
        //ChillsuchtAPI.getPermissionAPI().removeRank(player, BattleShips.getScoreboard());
        scoreboardTeam.addEntry(player.getName());
    }

    public void removeMember(Player player) {
        player.sendMessage(BattleShips.getMessagesConfig().get("player.leaveTeam").replaceAll("%TEAM%", toString()));
        members.remove(player);
        scoreboardTeam.removeEntry(player.getName());
        //ChillsuchtAPI.getPermissionAPI().setRank(player, BattleShips.getScoreboard());
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

    public void addCapturedBanner(Team team) {
        if (!capturedBanners.contains(team))
            capturedBanners.add(team);
    }

    public void removeCapturedBanner(Team team) {
        capturedBanners.remove(team);
    }

    public ChestMenu getCapturedBannersChest() {
        ChestMenu gui = ChestMenu.builder(1).title("Eroberte Banner von " + this).build();
        GameSession game = BattleShips.getGame();
        AtomicInteger slot = new AtomicInteger();
        capturedBanners.forEach(bannerTeam -> {
            Slot guiSlot = gui.getSlot(slot.getAndIncrement());
            guiSlot.setItem(new ItemBuilder(bannerTeam.getBanner()).setDisplayName("§fBanner von Team " + bannerTeam).build());
            guiSlot.setClickHandler((player, clickInformation) -> {
                if (!bannerTeam.equals(this) //player cant capture banner of team's chest
                        && !game.getTeam(player).equals(this)  //player cant capture banners of own chest
                        && game.captureBanner(player, bannerTeam)) { //cant capture banner for other reason
                    this.removeCapturedBanner(bannerTeam);
                    gui.close();
                }
            });
        });

        return gui;
    }

    public int getCapturedBanners() {
        return capturedBanners.size();
    }

    public void sendMessage(String message) {
        members.forEach(member -> member.sendMessage(message));
    }

    public void addWin() {
        members.forEach(member -> BattleShips.getGame().getStats(member.getUniqueId()).addGamesWon());
    }
    public static Team[] getActiveTeams() {
        int teamCount = BattleShips.getSettingsConfig().getTeamCount();
        Team[] activeTeams = new Team[teamCount];
        System.arraycopy(values(), 0, activeTeams, 0, teamCount);
        return activeTeams;
    }

    public String getSideBoardText(int playingTeams) {
        return (capturedBanners.contains(this) ? "§a✓" : "§4✗") + " " + this + " §7(" + capturedBanners.size() + "/" + playingTeams + ")";
    }//
    @Override
    public String toString() {
        return color + name + "§r";
    }
}
package me.sk8ingduck.battleships.game;

import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.ipvp.canvas.slot.Slot;
import org.ipvp.canvas.type.ChestMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Team implements ConfigurationSerializable {

	private final String name;
	private final ChatColor color;
	private final ItemStack teamChooseItem;
	private org.bukkit.scoreboard.Team scoreboardTeam;
	private Location spawnLocation;
	private Location tntGunLocation;
	private TeamItem banner;
	private TeamItem chest;
	private ArrayList<Player> members;
	private ArrayList<Team> capturedBanners;
	private int tntGunLevel;
	private int tntGunCooldown;

	private BukkitTask cooldown;

	public Team(String defaultName, ChatColor defaultColor, ItemStack defaultTeamChooseItem) {
		name = defaultName;
		color = defaultColor;
		teamChooseItem = defaultTeamChooseItem;
		initTeam();
	}

	public Team(Map<String, Object> saveMap) {
		name = (String) saveMap.get("name");
		color = ChatColor.valueOf((String) saveMap.get("color"));
		teamChooseItem = (ItemStack) saveMap.get("teamChooseItem");
		spawnLocation = (Location) saveMap.get("spawnLocation");
		tntGunLocation = (Location) saveMap.get("tntGunLocation");
		banner = (TeamItem) saveMap.get("banner");
		chest = (TeamItem) saveMap.get("chest");
		initTeam();
	}

	@Override
	public Map<String, Object> serialize() {
		HashMap<String, Object> saveMap = new HashMap<>();
		saveMap.put("name", name);
		saveMap.put("color", color.name());
		saveMap.put("teamChooseItem", teamChooseItem);
		saveMap.put("spawnLocation", spawnLocation);
		saveMap.put("tntGunLocation", tntGunLocation);
		saveMap.put("banner", banner);
		saveMap.put("chest", chest);
		return saveMap;
	}

	private void initTeam() {
		if (banner != null)
			banner.setBlock();
		if (chest != null)
			chest.setBlock();

		members = new ArrayList<>();
		capturedBanners = new ArrayList<>();
		capturedBanners.add(this);
		tntGunLevel = 0;

		scoreboardTeam = BattleShips.getScoreboard().getTeam("z" + name);
		if (scoreboardTeam == null)
			scoreboardTeam = BattleShips.getScoreboard().registerNewTeam("z" + name);

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

	public Location getTntGunLocation() {
		return tntGunLocation;
	}

	public void setTntGunLocation(Location tntGunLocation) {
		this.tntGunLocation = tntGunLocation;
	}

	public int getTntGunLevel() {
		return tntGunLevel;
	}

	public void increaseTntGunLevel() {
		tntGunLevel++;
	}

	public void setTntGunCooldown(int tntGunCooldown) {
		this.tntGunCooldown = tntGunCooldown;

		GameSession game = BattleShips.getGame();
		cooldown = Bukkit.getScheduler().runTaskTimer(BattleShips.getInstance(), () -> {
			this.tntGunCooldown--;
			game.updateBoards(this);
			if (this.tntGunCooldown == 0) cooldown.cancel();
		}, 0, 20);
	}

	public boolean isCooldownActive() {
		return tntGunCooldown != 0;
	}

	public TeamItem getBanner() {
		return banner;
	}

	public void setBanner(TeamItem banner) {
		this.banner = banner;
	}

	public TeamItem getChest() {
		return chest;
	}

	public void setChest(TeamItem chest) {
		this.chest = chest;
	}

	public ItemStack getTeamChooseItem(Player player) {
		int maxTeamSize = BattleShips.getTeamConfig().getTeamSize();
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
		members.add(player);
		player.setPlayerListName(color + name + " §7» " + color + player.getName());
		//ChillsuchtAPI.getPermissionAPI().removeRank(player, BattleShips.getScoreboard());
		scoreboardTeam.addEntry(player.getName());
	}

	public void removeMember(Player player) {
		members.remove(player);
		scoreboardTeam.removeEntry(player.getName());
		//ChillsuchtAPI.getPermissionAPI().setRank(player, BattleShips.getScoreboard());
	}

	public int getSize() {
		return members.size();
	}

	public ArrayList<Player> getMembers() {
		return members;
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
			guiSlot.setItem(new ItemBuilder(bannerTeam.getBanner().getItem()).setDisplayName("§fBanner von Team " + bannerTeam).build());
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

	public String getSideBoardText(int playingTeams) {
		return (capturedBanners.contains(this) ? "§a✓" : "§4✗") + " " + this + " §7(" + capturedBanners.size() + "/" + playingTeams + ")";
	}

	public String[] getTNTGunText() {
		return new String[]{"", "§bTNT-Gun §7(Level: " + tntGunLevel + ")",
				((tntGunLevel == 0) ? "§cnicht bereit" : (tntGunCooldown == 0) ? "§abereit" : "§c" + tntGunCooldown)};
	}

	@Override
	public String toString() {
		return color + name + "§r";
	}


}

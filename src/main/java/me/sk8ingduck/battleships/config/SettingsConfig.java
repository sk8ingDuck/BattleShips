package me.sk8ingduck.battleships.config;

import de.nandi.chillsuchtapi.api.ChillsuchtAPI;
import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.mysql.PlayerStats;
import me.sk8ingduck.battleships.util.ItemBuilder;
import me.sk8ingduck.battleships.util.LeaderboardSign;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class SettingsConfig extends Config {

	private final String mapName;
	private final int neededPlayersToStart;
	private final ItemStack teamChooserItem;
	private final ItemStack tntGunItem;
	private final ItemStack teleportItem;
	private Location lobbySpawn;
	private Location spectatorSpawn;
	private ArrayList<String> signLayout;
	private ArrayList<LeaderboardSign> leaderboardSigns;

	public SettingsConfig(String name, File path) {
		super(name, path);
		this.mapName = (String) getPathOrSet("mapName", "Penis");
		this.neededPlayersToStart = (int) getPathOrSet("neededPlayersToStart", 4);
		this.teamChooserItem = (ItemStack) getPathOrSet("teamChooserItem", new ItemBuilder(Material.CHEST).setDisplayName("Team auswählen").build());
		this.tntGunItem = (ItemStack) getPathOrSet("tntGunItem", new ItemBuilder(Material.CROSSBOW).setGlowing()
				.setDisplayName("§0§kO §cTNT-Gun §0§kO").setDamage(464).setCrossBowCharged().build());
		this.teleportItem = (ItemStack) getPathOrSet("teleportItem", new ItemBuilder(Material.COMPASS).setDisplayName("§5Teleporter").build());
		this.lobbySpawn = (Location) getPathOrSet("lobbySpawn", null);
		this.lobbySpawn = (Location) getPathOrSet("spectatorSpawn", null);

		ArrayList<String> defaultSignLayout = new ArrayList<>(Arrays.asList("Platz #%RANK%", "%PLAYER%", "Gespielt: %GAMES_PLAYED%", "Gewonnen: %GAMES_WON%"));
		this.signLayout = (ArrayList<String>) getPathOrSet("signLayout", defaultSignLayout);
		this.leaderboardSigns = (ArrayList<LeaderboardSign>) getPathOrSet("leaderboardSigns", new ArrayList<LeaderboardSign>());

		leaderboardSigns.forEach(this::updateLeaderboardSign);
	}

	public String getMapName() {
		return mapName;
	}

	public int getNeededPlayersToStart() {
		return neededPlayersToStart;
	}

	public ItemStack getTeamChooserItem() {
		return teamChooserItem;
	}

	public ItemStack getTntGunItem() {
		return tntGunItem;
	}

	public ItemStack getTeleportItem() {
		return teleportItem;
	}

	public Location getLobbySpawn() {
		return lobbySpawn;
	}

	public void setLobbySpawn(Location lobbySpawn) {
		this.lobbySpawn = lobbySpawn;
		getFileConfiguration().set("lobbySpawn", lobbySpawn);
		save();
	}

	public Location getSpectatorSpawn() {
		return spectatorSpawn;
	}

	public void setSpectatorSpawn(Location spectatorSpawn) {
		this.spectatorSpawn = spectatorSpawn;
		getFileConfiguration().set("spectatorSpawn", spectatorSpawn);
		save();
	}

	public void addLeaderboardSign(LeaderboardSign leaderboardSign) {
		leaderboardSigns.add(leaderboardSign);
		getFileConfiguration().set("leaderboardSigns", leaderboardSigns);
		save();

		updateLeaderboardSign(leaderboardSign);
	}

	private void updateLeaderboardSign(LeaderboardSign leaderboardSign) {
		Block signBlock = leaderboardSign.getSignLocation().getBlock();
		Block skullBlock = leaderboardSign.getHeadLocation().getBlock();

		PlayerStats playerStats = BattleShips.getMySQL().getPlayerStats(leaderboardSign.getRank());
		if (!(signBlock.getState() instanceof Sign sign)
				|| !(skullBlock.getState() instanceof Skull skull)
				|| playerStats == null) {
			leaderboardSigns.remove(leaderboardSign);
			return;
		}

		String name = ChillsuchtAPI.getGeneralAPI().getNameFromUUID(playerStats.getUuid());

		skull.setOwningPlayer(Bukkit.getOfflinePlayer(playerStats.getUuid()));
		skull.update();

		for (int i = 0; i < 4; i++)
			sign.setLine(i, playerStats.replace(signLayout.get(i), name));

		sign.update();
	}


}
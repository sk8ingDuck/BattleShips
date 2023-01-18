package me.sk8ingduck.battleships.util;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class LeaderboardSign implements ConfigurationSerializable {

	private final int rank;
	private final Location signLocation;
	private Location headLocation;
	public LeaderboardSign(int rank, Location signLocation) {
		this.rank = rank;
		this.signLocation = signLocation;
	}

	public LeaderboardSign(Map<String, Object> saveMap) {
		this.rank = (int) saveMap.get("rank");
		this.signLocation = (Location) saveMap.get("signLocation");
		this.headLocation = (Location) saveMap.get("headLocation");
	}

	public int getRank() {
		return rank;
	}

	public Location getSignLocation() {
		return signLocation;
	}

	public Location getHeadLocation() {
		return headLocation;
	}

	public void setHeadLocation(Location headLocation) {
		this.headLocation = headLocation;
	}

	@Override
	public Map<String, Object> serialize() {
		HashMap<String, Object> saveMap = new HashMap<>();
		saveMap.put("rank", rank);
		saveMap.put("signLocation", signLocation);
		saveMap.put("headLocation", headLocation);
		return saveMap;
	}
}

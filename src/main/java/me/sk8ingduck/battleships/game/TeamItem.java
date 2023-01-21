package me.sk8ingduck.battleships.game;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Rotatable;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import java.util.HashMap;
import java.util.Map;

public class TeamItem implements ConfigurationSerializable {

	private final Location location;
	private final Material item;
	private final BlockFace blockFace;


	public TeamItem(Block block) {
		this.location = block.getLocation();
		this.item = block.getType();
		if (block.getBlockData() instanceof Directional) {
			this.blockFace = ((Directional) block.getBlockData()).getFacing();
		} else {
			this.blockFace = ((Rotatable) block.getBlockData()).getRotation();
		}
	}

	public TeamItem(Map<String, Object> saveMap) {
		this.location = (Location) saveMap.get("location");
		this.item = Material.valueOf((String) saveMap.get("item"));
		this.blockFace = BlockFace.valueOf((String) saveMap.get("blockFace"));
	}

	public Location getLocation() {
		return location;
	}

	public Material getItem() {
		return item;
	}

	public void setBlock() {
		location.getBlock().setType(item);
		if (location.getBlock().getBlockData() instanceof Directional blockData) {
			blockData.setFacing(blockFace);
			location.getBlock().setBlockData(blockData);
		} else if (location.getBlock().getBlockData() instanceof Rotatable blockData) {
			blockData.setRotation(blockFace);
			location.getBlock().setBlockData(blockData);
		}
	}

	public void removeBlock() {
		location.getBlock().setType(Material.AIR);
	}

	@Override
	public Map<String, Object> serialize() {
		HashMap<String, Object> saveMap = new HashMap<>();
		saveMap.put("location", location);
		saveMap.put("item", item.name());
		saveMap.put("blockFace", blockFace.name());
		return saveMap;
	}
}

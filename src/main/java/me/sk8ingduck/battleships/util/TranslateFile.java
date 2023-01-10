package me.sk8ingduck.battleships.util;

import de.nandi.chillsuchtapi.api.ChillsuchtAPI;
import de.nandi.chillsuchtapi.translation.Language;
import me.sk8ingduck.battleships.BattleShips;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class TranslateFile {

	private File translateFile;
	private FileConfiguration translateConfig;
	private final BattleShips plugin;

	public TranslateFile(BattleShips plugin) throws IOException {
		this.plugin = plugin;
		createTranslateFile();
	}

	final int version = 1;

	private void createTranslateFile() throws IOException {
		translateFile = new File(plugin.getDataFolder(), "translate.yml");
		if (translateFile.createNewFile()) {
			translateConfig = YamlConfiguration.loadConfiguration(translateFile);
			translateConfig.set("Version", version);
			FileConfiguration tc = translateConfig;
			String pf = "";
			String label1 = "label1";
			String label2 = "label2";
			String label3 = "label3";
			tc.set(Language.GERMAN + ".command.perms", pf + ChillsuchtAPI.NO_PERMISSION_NO_PREFIX);


			tc.set(Language.ENGLISH + ".command.perms", pf + "§cYou don´t have the permission to do that.");


			translateConfig.save(translateFile);
			return;
		}

		translateConfig = YamlConfiguration.loadConfiguration(translateFile);
		if (translateConfig.getInt("Version", 0) != version) {
			if (translateFile.delete())
				createTranslateFile();
		}
	}


	public File getTranslateFile() {
		return translateFile;
	}

	public FileConfiguration getTranslateConfig() {
		return translateConfig;
	}
}

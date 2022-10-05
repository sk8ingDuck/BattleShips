package me.sk8ingduck.battleships;

import com.google.common.reflect.ClassPath;
import me.sk8ingduck.battleships.command.SetspawnCommand;
import me.sk8ingduck.battleships.config.DBConfig;
import me.sk8ingduck.battleships.config.SettingsConfig;
import me.sk8ingduck.battleships.config.TeamConfig;
import me.sk8ingduck.battleships.game.GameSession;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.IOException;

public final class BattleShips extends JavaPlugin {

    private static BattleShips instance;

    private GameSession game;

    private DBConfig dbConfig;

    private TeamConfig teamConfig;
    private SettingsConfig settingsConfig;
    @Override
    public void onEnable() {
        instance = this;

        dbConfig = new DBConfig("database.yml", getDataFolder());
        teamConfig = new TeamConfig("teams.yml", getDataFolder());
        settingsConfig = new SettingsConfig("settings.yml", getDataFolder());

        game = new GameSession();

        PluginManager pluginManager = Bukkit.getPluginManager();
        try {
            for (ClassPath.ClassInfo classInfo : ClassPath.from(getClassLoader())
                    .getTopLevelClasses("com.sk8ingduck.battleships.listener")) {
                Class listener = Class.forName(classInfo.getName());

                if (Listener.class.isAssignableFrom(listener)) {
                    pluginManager.registerEvents((Listener) listener.newInstance(), this);
                }
            }
        } catch (IOException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        this.getCommand("setspawn").setExecutor(new SetspawnCommand());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static BattleShips getInstance() {
        return instance;
    }

    public GameSession getGame() {
        return game;
    }

    public DBConfig getDbConfig() {
        return dbConfig;
    }

    public TeamConfig getTeamConfig() {
        return teamConfig;
    }
}

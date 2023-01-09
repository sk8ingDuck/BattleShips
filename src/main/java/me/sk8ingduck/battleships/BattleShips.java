package me.sk8ingduck.battleships;

import com.google.common.reflect.ClassPath;
import me.sk8ingduck.battleships.command.SetspawnCommand;
import me.sk8ingduck.battleships.config.DBConfig;
import me.sk8ingduck.battleships.config.MessagesConfig;
import me.sk8ingduck.battleships.config.SettingsConfig;
import me.sk8ingduck.battleships.config.TeamConfig;
import me.sk8ingduck.battleships.game.GameSession;
import me.sk8ingduck.battleships.mysql.MySQL;
import me.sk8ingduck.battleships.villagershop.GuiManager;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.ipvp.canvas.MenuFunctionListener;

import java.io.IOException;

public final class BattleShips extends JavaPlugin {

    private static BattleShips instance;
    private static GameSession game;
    private static TeamConfig teamConfig;
    private static SettingsConfig settingsConfig;
    private static MessagesConfig messagesConfig;
    private static Scoreboard scoreboard;

    private MySQL mySQL;

    @Override
    public void onEnable() {
        instance = this;

        scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        scoreboard.getTeams().forEach(team -> team.getEntries().forEach(team::removeEntry));

        PluginManager pluginManager = Bukkit.getPluginManager();
        try {
            for (ClassPath.ClassInfo classInfo : ClassPath.from(getClassLoader())
                    .getTopLevelClasses("me.sk8ingduck.battleships.listener")) {
                Class listener = Class.forName(classInfo.getName());

                if (Listener.class.isAssignableFrom(listener)) {
                    pluginManager.registerEvents((Listener) listener.newInstance(), this);
                }
            }
        } catch (IOException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        pluginManager.registerEvents(new MenuFunctionListener(), this);
        this.getCommand("setspawn").setExecutor(new SetspawnCommand());

        DBConfig dbConfig = new DBConfig("database.yml", getDataFolder());
        teamConfig = new TeamConfig("teams.yml", getDataFolder());
        settingsConfig = new SettingsConfig("settings.yml", getDataFolder());
        messagesConfig = new MessagesConfig("messages.yml", getDataFolder());

        //mySQL = new MySQL(dbConfig.getHost(), dbConfig.getPort(), dbConfig.getUsername(), dbConfig.getPassword(), dbConfig.getDatabase());
        game = new GameSession();

        GuiManager.init();

        Bukkit.getWorlds().forEach(world -> world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false));

    }

    public static BattleShips getInstance() {
        return instance;
    }

    public static GameSession getGame() {
        return game;
    }

    public static TeamConfig getTeamConfig() {
        return teamConfig;
    }

    public static SettingsConfig getSettingsConfig() {
        return settingsConfig;
    }

    public static MessagesConfig getMessagesConfig() {
        return messagesConfig;
    }

    public static Scoreboard getScoreboard() {
        return scoreboard;
    }
}

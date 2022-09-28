package me.sk8ingduck.battleships;

import com.google.common.reflect.ClassPath;
import me.sk8ingduck.battleships.game.GameSession;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class BattleShips extends JavaPlugin {

    private static BattleShips instance;

    private GameSession game;

    @Override
    public void onEnable() {
        instance = this;
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
}

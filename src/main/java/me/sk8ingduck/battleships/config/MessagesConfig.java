package me.sk8ingduck.battleships.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MessagesConfig extends Config {

    private final HashMap<String, String> msgs;
    private final ArrayList<String> sideboardLines;

    public MessagesConfig(String name, File path) {
        super(name, path);

        msgs = new HashMap<>();
        msgs.put("player.joinMessage", "&a%PLAYER% &ehat das Spiel betreten.");
        msgs.put("player.leaveMessage", "&c%PLAYER% &ehat das Spiel verlassen.");
        msgs.put("player.deathMessage", "%PLAYER% &7wurde von %KILLER% &7getötet.");
        msgs.put("player.otherDeathMessage", "%PLAYER% &7ist gestorben.");
        msgs.put("player.joinTeam", "&eDu bist Team %TEAM% &ebeigetreten.");
        msgs.put("player.leaveTeam", "&eDu hast Team %TEAM% &everlassen.");
        msgs.put("player.stats", """
                &e---------- &7Stats von %PLAYER% &e----------
                &7> Rank: &e#%RANK%
                &7> Kills: &e%KILLS%
                &7> Deaths: &e%DEATHS%
                &7> K/D: &e%KILLS_DEATHS_RATIO%
                &7> Gespielte Spiele: &e%GAMES_PLAYED%
                &7> Gewonnene Spiele: &e%GAMES_WON%
                &7> Win/Loss-Rate: &e%WIN_LOSS_RATIO%%
                &7> Abgebaute Emeralds: &e%FARMED_EMERALDS%
                &7> Eroberte Banner: &e%CAPTURED_BANNERS%
                &e---------- &7Stats von %PLAYER% &e----------""");

        msgs.put("game.start", "&6Die Aufwärmphase ist nun vorbei!");
        msgs.put("game.bannerCaptured", "&eDer Banner von Team %TEAM% &ewurde in die Base von Team %CAPTURE_TEAM% &egebracht.");
        msgs.put("game.bannerStolen", "&eDer Banner von Team %TEAM% &ewurde von %PLAYER% &egenommen");
        msgs.put("game.bannerReturned", "&eDer Banner von Team %TEAM% &ewurde zurückgebracht.");
        msgs.put("game.teamWin", "&aTeam %TEAM% &ahat das Spiel gewonnen!");
        msgs.put("game.nobodyWon", "&cDas Spiel ist vorbei! Niemand hat gewonnen.");

        msgs.put("countdown.lobby", "&aDas Spiel startet in &6%SECONDS% &aSekunden.");
        msgs.put("countdown.warmup", "&aDie Runde startet in &6%SECONDS% &aSekunden.");
        msgs.put("countdown.restarting", "&cDer Server startet in &6%SECONDS% &cSekunden neu.");

        msgs.put("error.gameAlreadyStarted", "&cDas Spiel hat bereits angenfangen.");
        msgs.put("error.serverFull", "&cDer Server ist voll.");
        msgs.put("error.teamFull", "&cTeam %TEAM% &cist bereits voll.");
        msgs.put("error.alreadyHasBanner", "&cDu hast bereits einen Banner. Bringe diesen erst in deine Base zurück.");
        msgs.put("error.notEnoughEmeralds", "&cDu hast nicht genug Emeralds.");
        msgs.put("error.playerNotFound", "&cSpieler wurde nicht gefunden.");

        msgs.put("sideboard.title", " §7» &3BattleShips &7« ");

        msgs.forEach((msgPath, message) -> msgs.put(msgPath, (String) getPathOrSet(msgPath, message)));

        ArrayList<String> defaultSideboardLines = new ArrayList<>(Arrays.asList(
                " ",
                "&eMap:",
                "&6%MAP%",
                "",
                "&eModus:",
                "&6%TEAM_COUNT%&7x&6%TEAM_SIZE%",
                " "));

        sideboardLines = (ArrayList<String>) getPathOrSet("sideboard.lines", defaultSideboardLines);
    }

    public String get(String path) {
        return msgs.get(path);
    }
    public ArrayList<String> getSideboardLines() {
        return sideboardLines;
    }
}
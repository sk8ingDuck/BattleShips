package me.sk8ingduck.battleships.config;

import java.io.File;
import java.util.HashMap;

public class MessagesConfig extends Config {

    private final HashMap<String, String> msgs;

    public MessagesConfig(String name, File path) {
        super(name, path);

        msgs = new HashMap<>();
        msgs.put("player.joinMessage", "&a%PLAYER% &ehat das Spiel betreten.");
        msgs.put("player.leaveMessage", "&c%PLAYER% &ehat das Spiel verlassen.");
        msgs.put("player.deathMessage", "%PLAYER% &7wurde von %KILLER% &7getötet.");
        msgs.put("player.otherDeathMessage", "%PLAYER% &7ist gestorben.");
        msgs.put("player.joinTeam", "&eDu bist Team %TEAM% &ebeigetreten.");
        msgs.put("player.leaveTeam", "&eDu hast Team %TEAM% &everlassen.");

        msgs.put("game.bannerCaptured", "&eDer Banner von Team %TEAM% &ewurde in die Base von Team %CAPTURE_TEAM% &egebracht.");
        msgs.put("game.bannerStolen", "&eDer Banner von Team %TEAM% &ewurde von %PLAYER% &egenommen");
        msgs.put("game.bannerReturned", "&eDer Banner von Team %TEAM% &ewurde zurückgebracht.");
        msgs.put("game.teamWin", "&aTeam %TEAM% &ahat das Spiel gewonnen!");

        msgs.put("countdown.lobby", "&aDas Spiel startet in &6%SECONDS% &aSekunden.");
        msgs.put("countdown.warmup", "&aDie Runde startet in &6%SECONDS% &aSekunden.");
        msgs.put("countdown.restarting", "&cDer Server startet in &6%SECONDS% &cSekunden neu.");

        msgs.put("error.gameAlreadyStarted", "&cDas Spiel hat bereits angenfangen.");
        msgs.put("error.serverFull", "&cDer Server ist voll.");
        msgs.put("error.teamFull", "&cTeam %TEAM% &cist bereits voll.");
        msgs.put("error.alreadyHasBanner", "&cDu hast bereits einen Banner. Bringe diesen erst in deine Base zurück.");
        msgs.put("error.notEnoughEmeralds", "&cDu hast nicht genug Emeralds.");

        msgs.forEach((msgPath, message) -> msgs.put(msgPath, (String) getPathOrSet(msgPath, message)));
    }

    public String get(String path) {
        return msgs.get(path);
    }

}
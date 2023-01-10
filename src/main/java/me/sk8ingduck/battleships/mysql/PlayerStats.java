package me.sk8ingduck.battleships.mysql;

import java.text.DecimalFormat;

public class PlayerStats {

    private int kills;
    private int deaths;
    private int gamesPlayed;
    private int gamesWon;
    private int farmedEmeralds;
    private int capturedBanners;

    public PlayerStats(int kills, int deaths, int gamesPlayed, int gamesWon, int farmedEmeralds, int capturedBanners) {
        this.kills = kills;
        this.deaths = deaths;
        this.gamesPlayed = gamesPlayed;
        this.gamesWon = gamesWon;
        this.farmedEmeralds = farmedEmeralds;
        this.capturedBanners = capturedBanners;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public int getFarmedEmeralds() {
        return farmedEmeralds;
    }

    public int getCapturedBanners() {
        return capturedBanners;
    }

    public String getKD() {
        if (deaths == 0) return "NaN";
        return new DecimalFormat("0.00").format((double) kills /  (double) deaths);
    }

    public String getWL() {
        if (gamesPlayed == 0) return "0";
        return new DecimalFormat("0.00").format(((double) gamesWon / (double) gamesPlayed) * 100);
    }

    public void addKill() {
        kills++;
    }

    public void addDeath() {
        deaths++;
    }

    public void addGamePlayed() {
        gamesPlayed++;
    }

    public void addGamesWon() {
        gamesWon++;
    }

    public void addFarmedEmerald() {
        farmedEmeralds++;
    }

    public void addCapturedBanner() {
        capturedBanners++;
    }

}

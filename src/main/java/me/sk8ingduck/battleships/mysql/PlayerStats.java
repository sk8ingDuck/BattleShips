package me.sk8ingduck.battleships.mysql;

import java.text.DecimalFormat;
import java.util.UUID;

public class PlayerStats {

	private final int rank;
	private UUID uuid;
	private int kills;
	private int deaths;
	private int gamesPlayed;
	private int gamesWon;
	private int farmedEmeralds;
	private int capturedBanners;

	public PlayerStats(UUID uuid, int kills, int deaths, int gamesPlayed, int gamesWon, int farmedEmeralds, int capturedBanners, int rank) {
		this.uuid = uuid;
		this.kills = kills;
		this.deaths = deaths;
		this.gamesPlayed = gamesPlayed;
		this.gamesWon = gamesWon;
		this.farmedEmeralds = farmedEmeralds;
		this.capturedBanners = capturedBanners;
		this.rank = rank;
	}

	public UUID getUuid() {
		return uuid;
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

    private String getKD() {
        if (deaths == 0) return "NaN";
        return new DecimalFormat("0.00").format((double) kills / (double) deaths);
    }

    private String getWL() {
        if (gamesPlayed == 0) return "0";
        return new DecimalFormat("0.00").format(((double) gamesWon / (double) gamesPlayed) * 100);
    }
    public String replace(String replacement, String name) {
		return replacement
                .replaceAll("%PLAYER%", name)
                .replaceAll("%RANK%", String.valueOf(rank))
				.replaceAll("%KILLS%", String.valueOf(kills))
				.replaceAll("%DEATHS", String.valueOf(deaths))
                .replaceAll("%KILLS_DEATHS_RATIO%", getKD())
				.replaceAll("%GAMES_PLAYED%", String.valueOf(gamesPlayed))
				.replaceAll("%GAMES_WON%", String.valueOf(gamesWon))
				.replaceAll("%WIN_LOSS_RATIO%", getWL())
				.replaceAll("%FARMED_EMERALDS%", String.valueOf(farmedEmeralds))
				.replaceAll("%CAPTURED_BANNERS%", String.valueOf(capturedBanners));
	}

}

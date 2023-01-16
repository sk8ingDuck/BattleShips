package me.sk8ingduck.battleships.mysql;

import de.nandi.chillsuchtapi.api.ChillsuchtAPI;

import javax.sql.DataSource;
import java.sql.*;
import java.util.UUID;

public class MySQL {

	private final DataSource dataSource;


	public MySQL() {
		dataSource = ChillsuchtAPI.getDatabaseAPI().openDataSource();

		try (Connection con = dataSource.getConnection(); PreparedStatement stmt = con.prepareStatement(
				"CREATE TABLE IF NOT EXISTS BattleShips(" +
						"uuid VARCHAR(36) NOT NULL PRIMARY KEY," +
						"kills INT DEFAULT 0," +
						"deaths INT DEFAULT 0," +
						"gamesPlayed INT DEFAULT 0," +
						"gamesWon INT DEFAULT 0," +
						"farmedEmeralds INT DEFAULT 0," +
						"capturedBanners INT DEFAULT 0)")) {
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public PlayerStats getPlayerStats(UUID uuid) {
		try (Connection con = dataSource.getConnection(); PreparedStatement stmt = con.prepareStatement(
				"SELECT * FROM (SELECT *, RANK() over (ORDER BY gamesWon DESC) rank FROM BattleShips) t where uuid=?")) {
			stmt.setString(1, uuid.toString());
			ResultSet resultSet = stmt.executeQuery();

			if (resultSet.next()) {
				return new PlayerStats(uuid, resultSet.getInt("kills"),
						resultSet.getInt("deaths"), resultSet.getInt("gamesPlayed"),
						resultSet.getInt("gamesWon"), resultSet.getInt("farmedEmeralds"),
						resultSet.getInt("capturedBanners"), resultSet.getInt("rank"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	public PlayerStats getPlayerStats(int rank) {
		try (Connection con = dataSource.getConnection(); PreparedStatement stmt = con.prepareStatement(
				"SELECT * FROM (SELECT *, RANK() over (ORDER BY gamesWon DESC) rank FROM BattleShips) t where rank=?")) {
			stmt.setInt(1, rank);
			ResultSet resultSet = stmt.executeQuery();

			if (resultSet.next()) {
				return new PlayerStats(UUID.fromString(resultSet.getString("uuid")), resultSet.getInt("kills"),
						resultSet.getInt("deaths"), resultSet.getInt("gamesPlayed"),
						resultSet.getInt("gamesWon"), resultSet.getInt("farmedEmeralds"),
						resultSet.getInt("capturedBanners"), resultSet.getInt("rank"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	public void savePlayerStats(UUID uuid, PlayerStats playerStats) {
		try (Connection con = dataSource.getConnection(); PreparedStatement stmt = con.prepareStatement(
				"INSERT INTO `BattleShips`VALUES (?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE " +
						"kills=?, deaths=?, gamesPlayed=?, gamesWon=?, farmedEmeralds=?, capturedBanners=?")) {
			stmt.setString(1, uuid.toString());
			stmt.setInt(2, playerStats.getKills());
			stmt.setInt(3, playerStats.getDeaths());
			stmt.setInt(4, playerStats.getGamesPlayed());
			stmt.setInt(5, playerStats.getGamesWon());
			stmt.setInt(6, playerStats.getFarmedEmeralds());
			stmt.setInt(7, playerStats.getCapturedBanners());

			stmt.setInt(8, playerStats.getKills());
			stmt.setInt(9, playerStats.getDeaths());
			stmt.setInt(10, playerStats.getGamesPlayed());
			stmt.setInt(11, playerStats.getGamesWon());
			stmt.setInt(12, playerStats.getFarmedEmeralds());
			stmt.setInt(13, playerStats.getCapturedBanners());
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}

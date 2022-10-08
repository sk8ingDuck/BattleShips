package me.sk8ingduck.battleships.listener;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.config.SettingsConfig;
import me.sk8ingduck.battleships.game.GameSession;
import me.sk8ingduck.battleships.game.GameState;
import me.sk8ingduck.battleships.game.Team;
import me.sk8ingduck.battleships.game.TeamManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        GameSession game = BattleShips.getInstance().getGame();
        SettingsConfig config = BattleShips.getInstance().getSettingsConfig();
        if (game.getCurrentGameState() == null || game.getCurrentGameState() == GameState.LOBBY) {
            Player player = event.getPlayer();
            if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName()
                    .equals(config.getTeamChooserItem().getItemMeta().getDisplayName())) {

                if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    openChooseTeamGui(player);
                }
            }
        }
    }

    private void openChooseTeamGui(Player player) {
        TeamManager teamManager = BattleShips.getInstance().getTeamManager();
        SettingsConfig config = BattleShips.getInstance().getSettingsConfig();

        Gui chooseTeamGui = Gui.gui()
                .title(Component.text("Team auswählen"))
                .create();

        for (int i = 0; i < config.getTeamCount(); i++) {
            Team team = Team.values()[i];

            GuiItem chooseTeamItem = ItemBuilder.from(team.getTeamChooseItem()).asGuiItem();
            chooseTeamGui.addItem(chooseTeamItem);

            chooseTeamItem.setAction(onTeamClick -> {
                Team currentTeam = teamManager.getTeam(player);
                if (currentTeam != null) {
                    if (team.equals(currentTeam)) {
                        player.sendMessage("Du bist bereits in Team " + team);
                        chooseTeamGui.close(player);
                        return;
                    }
                    teamManager.removeTeam(player);
                }
                teamManager.setTeam(player, team);
                player.sendMessage("Du bist Team " + team + " beigetreten.");
                chooseTeamGui.close(player);
            });
        }
        chooseTeamGui.open(player);
    }
}

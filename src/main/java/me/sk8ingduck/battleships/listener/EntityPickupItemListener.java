package me.sk8ingduck.battleships.listener;

import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.game.GameSession;
import me.sk8ingduck.battleships.game.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class EntityPickupItemListener implements Listener {

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        GameSession game = BattleShips.getInstance().getGame();

        if (game.getCurrentGameState() == null || game.getCurrentGameState() == GameState.LOBBY) {
            event.setCancelled(true);
        }
    }
}

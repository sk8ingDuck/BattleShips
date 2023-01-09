package me.sk8ingduck.battleships.listener;

import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.game.GameSession;
import me.sk8ingduck.battleships.game.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        GameSession game = BattleShips.getGame();

        if (game.getCurrentGameState() == null || game.getCurrentGameState() == GameState.LOBBY) {
            if (!event.getPlayer().isOp()) {
                event.setCancelled(true);
            }
        }
    }
}

package me.sk8ingduck.battleships.command;

import me.sk8ingduck.battleships.BattleShips;
import me.sk8ingduck.battleships.game.GameSession;
import me.sk8ingduck.battleships.game.GameState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StartCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        GameSession game = BattleShips.getGame();
        if (game.getCurrentGameState() == null || game.getCurrentGameState() == GameState.LOBBY) {
            game.changeGameState(GameState.LOBBY);
            game.getCurrentGameState().setSeconds(3);
        }
        return true;
    }
}

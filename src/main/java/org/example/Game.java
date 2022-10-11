package org.example;

import org.example.exception.GameException;
import org.example.service.GameService;

/**
 * Tennis Game Program
 */
public class Game {
	public static void main(final String[] args) throws GameException {
		final var service = new GameService();
		service.checkPlayers(args);
		
		final String scenario = args[0];
		final var players = service.initGame(scenario);
		service.playGame(players, scenario);
	}
}

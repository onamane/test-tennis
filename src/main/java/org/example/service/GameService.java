package org.example.service;

import java.util.List;
import java.util.stream.Collectors;

import org.example.exception.GameException;
import org.example.model.Player;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Slf4j
public class GameService {
	
	public static final int POINTS_10 = 10;
	public static final int POINTS_15 = 15;
	public static final int POINTS_30 = 30;
	public static final int POINTS_40 = 40;
	public static final int MAX_COUNT_PLAYERS = 2;
	public static final int MAX_WIN_PLAYERS = 4;
	
	public static final int INITIAL_POINTS = 0;
	public static final boolean INITIAL_ADVANTAGE = false;
	
	/**
	 * Initialize the game : define two players and initialize it data
	 *
	 * @param scenario order of victories
	 * @return players ready to play
	 */
	public List<Player> initGame(final String scenario) {
		final var names = scenario.chars().distinct().mapToObj(c -> String.valueOf((char) c)).collect(Collectors.toList());
		final var playerOne = Player.builder().name(names.get(0)).points(INITIAL_POINTS).advantage(INITIAL_ADVANTAGE).build();
		final var playerTwo = Player.builder().name(names.get(1)).points(INITIAL_POINTS).advantage(INITIAL_ADVANTAGE).build();
		log.info("Welcome to the player {} and player {} !", playerOne.getName(), playerTwo.getName());
		
		return List.of(playerOne, playerTwo);
	}
	
	/**
	 * Play rounds in order of scenario
	 *
	 * @param players  players from initGame function
	 * @param scenario order of victories
	 */
	public void playGame(final List<Player> players, final String scenario) {
		final var playerOne = players.get(0);
		final var playerTwo = players.get(1);
		
		scenario.chars().mapToObj(c -> String.valueOf((char) c)).forEach(winnerName -> {
			if (playerOne.getName().equals(winnerName)) {
				if (isEndGame(playerOne, playerTwo)) {
					return;
				}
				playSet(playerOne, playerTwo);
			} else {
				if (isEndGame(playerTwo, playerOne)) {
					return;
				}
				playSet(playerTwo, playerOne);
			}
			
			log.info("Player {} : {} / Player {} : {}", playerOne.getName(), playerOne.getPoints(), playerTwo.getName(), playerTwo.getPoints());
		});
	}
	
	/**
	 * Play a set : the looser loses the advantage and the winner has it and additional points
	 *
	 * @param winner the player who wins the ball
	 * @param looser the player who loses the ball
	 */
	public void playSet(final Player winner, final Player looser) {
		looser.setAdvantage(false);
		winBall(winner);
	}
	
	/**
	 * Check if context of game is valid
	 *
	 * @param args input program parameter
	 * @throws GameException game exception
	 */
	public void checkPlayers(final String[] args) throws GameException {
		if (args.length != 1) {
			throw new GameException("The program needs just one input with two players names");
		}
		final String scenario = args[0];
		
		if (scenario.chars().distinct().count() > MAX_COUNT_PLAYERS) {
			throw new GameException("The game needs just two players");
		}
		if (scenario.chars().count() < MAX_WIN_PLAYERS) {
			throw new GameException("the match must have at least 4 steps");
		}
	}
	
	/**
	 * Check if it is the end of the game
	 *
	 * @param winner the player who wins the ball
	 * @param looser the player who loses the ball
	 * @return true if it is the end , false otherwise
	 */
	protected boolean isEndGame(final Player winner, final Player looser) {
		if (winner.getPoints().equals(POINTS_40) && (!looser.getPoints().equals(POINTS_40) || Boolean.TRUE.equals(winner.getAdvantage()))) {
			log.info("Player {} wins the game !", winner.getName());
			return true;
		}
		return false;
	}
	
	/**
	 * The player wins the ball : add points and the winner has the advantage
	 *
	 * @param winner the player who wins the ball
	 */
	protected void winBall(final Player winner) {
		final var initialPoints = winner.getPoints();
		if (initialPoints.equals(POINTS_30)) {
			winner.setPoints(initialPoints + POINTS_10);
		} else {
			winner.setPoints(initialPoints + POINTS_15);
		}
		winner.setAdvantage(true);
	}
}

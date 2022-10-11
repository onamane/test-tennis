package org.example.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.example.exception.GameException;
import org.example.model.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {
	
	public static final int POINTS_40 = 40;
	public static final int POINTS_15 = 15;
	public static final int POINTS_30 = 30;
	
	@InjectMocks
	private GameService service;
	
	@Test
	void should_initialize_game() {
		final var scenario = "ABABABA";
		final var playerOne = Player.builder().name("A").points(0).advantage(false).build();
		final var playerTwo = Player.builder().name("B").points(0).advantage(false).build();
		
		final var players = service.initGame(scenario);
		assertFalse(players.isEmpty());
		assertThat(players).containsExactly(playerOne, playerTwo);
	}
	
	@Test
	void should_play_a_game() {
		final var scenario = "ABABAA";
		
		final var playerOne = Player.builder().name("A").points(0).advantage(false).build();
		final var playerTwo = Player.builder().name("B").points(0).advantage(false).build();
		
		service.playGame(List.of(playerOne, playerTwo), scenario);
		
		assertTrue(playerOne.getAdvantage());
		assertFalse(playerTwo.getAdvantage());
		assertEquals(POINTS_40, playerOne.getPoints());
		assertEquals(POINTS_30, playerTwo.getPoints());
	}
	
	@Test
	void should_play_a_set() {
		final var winner = Player.builder().name("A").points(0).advantage(false).build();
		final var looser = Player.builder().name("B").points(0).advantage(false).build();
		
		service.playSet(winner, looser);
		
		assertTrue(winner.getAdvantage());
		assertFalse(looser.getAdvantage());
		assertEquals(POINTS_15, winner.getPoints());
	}
	
	@Test
	void should_check_players_valid() {
		final var arguments = new String[] { "ABABAB" };
		assertDoesNotThrow(() -> service.checkPlayers(arguments));
	}
	
	@Test
	void should_check_players_invalid_arguments() {
		final var arguments = new String[] { "ABABAB", "CDCDCD" };
		final var exception = assertThrows(GameException.class, () -> service.checkPlayers(arguments));
		
		final String expectedMessage = "The program needs just one input with two players names";
		final String actualMessage = exception.getMessage();
		
		assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	void should_check_players_invalid_max_count_players() {
		final var arguments = new String[] { "ABABABCDCDCD" };
		final var exception = assertThrows(GameException.class, () -> service.checkPlayers(arguments));
		
		final String expectedMessage = "The game needs just two players";
		final String actualMessage = exception.getMessage();
		
		assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	void should_check_players_invalid_max_win_players() {
		final var arguments = new String[] { "AB" };
		final var exception = assertThrows(GameException.class, () -> service.checkPlayers(arguments));
		
		final String expectedMessage = "the match must have at least 4 steps";
		final String actualMessage = exception.getMessage();
		
		assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	void should_be_end_game_winner_has_advantage() {
		final var winner = Player.builder().name("A").points(POINTS_40).advantage(true).build();
		final var looser = Player.builder().name("B").points(POINTS_30).advantage(false).build();
		
		assertTrue(service.isEndGame(winner, looser));
	}
	
	@Test
	void should_not_be_end_game_deuce_looser_has_advantage() {
		final var winner = Player.builder().name("A").points(POINTS_40).advantage(false).build();
		final var looser = Player.builder().name("B").points(POINTS_40).advantage(true).build();
		
		assertFalse(service.isEndGame(winner, looser));
	}
	
	@Test
	void should_be_end_game_deuce_winner_has_advantage() {
		final var winner = Player.builder().name("A").points(POINTS_40).advantage(true).build();
		final var looser = Player.builder().name("B").points(POINTS_40).advantage(false).build();
		
		assertTrue(service.isEndGame(winner, looser));
	}
	
	@Test
	void should_win_ball_15_points() {
		final var winner = Player.builder().name("A").points(0).advantage(false).build();
		
		service.winBall(winner);
		
		assertTrue(winner.getAdvantage());
		assertEquals(POINTS_15, winner.getPoints());
	}
	
	@Test
	void should_win_ball_10_points() {
		final var winner = Player.builder().name("A").points(POINTS_30).advantage(false).build();
		
		service.winBall(winner);
		
		assertTrue(winner.getAdvantage());
		assertEquals(POINTS_40, winner.getPoints());
	}
}
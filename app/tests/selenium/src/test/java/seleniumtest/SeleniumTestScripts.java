package seleniumtest;

import org.openqa.selenium.logging.LogEntry;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.truth.Truth.assertThat;
import static seleniumtest.TicTacToePage.Square.*;
import static seleniumtest.TicTacToePage.Square.rightSide;
import static seleniumtest.Util.*;

class SeleniumTestScripts {
  static void shouldLoadTheGame(TicTacToePage ticTacToe) {
      // assert
      assertThat(ticTacToe.heading()).isEqualTo("Tic Tac Toe");
  }

  static void shouldWinForX(TicTacToePage ticTacToe) {
    /*
      'x', 'o', 'x',
      'o', 'x', 'o',
      'x', ' ', ' '
    */

      // act
      ticTacToe.play(topLeft); // x
      ticTacToe.play(topSide); // o
      ticTacToe.play(topRight); // x
      ticTacToe.play(rightSide); // o
      ticTacToe.play(center); // x
      ticTacToe.play(leftSide); // o
      ticTacToe.play(bottomLeft); // x

      // assert
      ticTacToe.shouldBeWinner(center);
  }

  static void shouldWinForO(TicTacToePage ticTacToe) {
    /*
      'x', 'o', 'x',
      ' ', 'o', 'x',
      ' ', 'o', ' '
    */

      // act
      ticTacToe.play(topLeft); // x
      ticTacToe.play(topSide); // o
      ticTacToe.play(topRight); // x
      ticTacToe.play(center); // o
      ticTacToe.play(rightSide); // x
      ticTacToe.play(bottomSide); // o

      // assert
      ticTacToe.shouldBeWinner(center);
  }

  static void shouldTie(TicTacToePage ticTacToe) {
    /*
      'x', 'o', 'x',
      'o', 'o', 'x',
      'x', 'x', 'o'
    */

      // act
      ticTacToe.play(topLeft); // x
      ticTacToe.play(topSide); // o
      ticTacToe.play(topRight); // x
      ticTacToe.play(leftSide); // o
      ticTacToe.play(bottomLeft); // x
      ticTacToe.play(center); // o
      ticTacToe.play(bottomSide); // x
      ticTacToe.play(bottomRight); // o
      ticTacToe.play(rightSide); // x

      // assert
      ticTacToe.shouldNotBeWinner(center);
  }

  static void shouldShowCorrectLeaderboardScore(TicTacToePage ticTacToe) {
      // act
      ticTacToe.clearScore();

      for (int i = 0; i < 6; i++) {
        // play six x winning games
        ticTacToe.winForX();
        ticTacToe.shouldBeWinner(center);
        ticTacToe.newGame();
      }

      for (int i = 0; i < 2; i++) {
        // play two tie games
        ticTacToe.playTieGame();
        ticTacToe.shouldNotBeWinner(center);
        ticTacToe.newGame();
      }

      // assert
      assertThat(ticTacToe.leaderBoardScoreX()).isEqualTo(6);
      assertThat(ticTacToe.leaderBoardScoreTie()).isEqualTo(2);
      assertThat(ticTacToe.leaderBoardScoreO()).isEqualTo(0);
  }

  static void shouldIncrementScoreOnWin(TicTacToePage ticTacToe) {
      // act
      List<LogEntry> posts = getRequestsByMethod("POST");
      assertThat(posts.size()).isEqualTo(0);
      ticTacToe.winForX();
      ticTacToe.shouldBeWinner(center);

      posts = getRequestsByMethod("POST");
      assertThat(posts.size()).isEqualTo(1);

      String message = posts.get(0).getMessage();
      String url = getStringFromJson(message, new String[]{"message", "params", "request", "url"});
      assertThat(url.endsWith("/api/score")).isTrue();

      String postData = getStringFromJson(message, new String[]{"message", "params", "request", "postData"});
      String player = getStringFromJson(postData, new String[]{"player"});
      assertThat(player).isEqualTo("x");

      // assert
  }

  static void shouldNotChangeScoreOnNewGameClick(TicTacToePage ticTacToe) {
      // act
      List<LogEntry> posts = getRequestsByMethod("POST");
      assertThat(posts.size()).isEqualTo(0);

      ticTacToe.play(topLeft); // x
      ticTacToe.play(topSide); // o
      ticTacToe.play(topRight); // x
      ticTacToe.play(rightSide); // o
      ticTacToe.newGame();

      posts = getRequestsByMethod("POST");
      assertThat(posts.size()).isEqualTo(0);
  }

  static void shouldReturnScoresFromApi(TicTacToePage ticTacToe) {
      // act

      Set<String> expectedKeys = new HashSet<>(List.of(new String[]{"x", "tie", "o"}));
      Set<String> actualKeys = objectKeysFromApiCall("/api/score");
      assertThat(actualKeys).isEqualTo(expectedKeys);
  }
}

package seleniumtest;

import org.junit.jupiter.api.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.lifecycle.TestDescription;

import java.util.*;

import static seleniumtest.SeleniumTestScripts.*;

public class SeleniumTest {
  public static BrowserWebDriverContainer<?> chrome;
  public static final int PORT = 8081;
  public static final String URL = "http://host.testcontainers.internal:" + PORT;
  String testName;
  Optional<Throwable> cause = Optional.empty();

  @BeforeEach
  void setUp() {
    ChromeOptions chromeOptions = new ChromeOptions()
      .addArguments("--disable-dev-shm-usage")
      .addArguments("--window-size=1200,1000");

    chromeOptions.setCapability("goog:loggingPrefs", Collections.singletonMap("performance", "ALL"));

    chrome = ConfigureWebDriver.configure(PORT, chromeOptions);
  }

  @AfterEach
  void tearDown() {
    try {
      chrome.afterTest(new TestDescription() {
        @Override
        public String getTestId() {
          return testName;
        }

        @Override
        public String getFilesystemFriendlyName() {
          return testName;
        }
      }, cause);
    } finally {
      chrome.stop();
    }
  }

  @Test
  void shouldVisitTheCorrectPage() {
    try {
      testName = "should-visit-the-correct-page";
      TicTacToePage ticTacToe = new TicTacToePage(URL);

      // assert
      ticTacToe.shouldHaveUrl(URL + "/");
    } catch (Throwable t) {
      cause = Optional.of(t);
      throw t;
    }
  }

  @Test
  void loadTheGame() {
    try {
      testName = "should-load-the-game";
      shouldLoadTheGame(new TicTacToePage(URL));
    } catch (Throwable t) {
      cause = Optional.of(t);
      throw t;
    }
  }

  @Test
  void winForX() {
    try {
      testName = "should-win-for-x";
      shouldWinForX(new TicTacToePage(URL));
    } catch (Throwable t) {
      cause = Optional.of(t);
      throw t;
    }
  }

  @Test
  void winForO() {
    try {
      testName = "should-win-for-o";
      shouldWinForO(new TicTacToePage(URL));
    } catch (Throwable t) {
      cause = Optional.of(t);
      throw t;
    }
  }

  @Test
  void tie() {
    try {
      testName = "should-tie";
      shouldTie(new TicTacToePage(URL));
    } catch (Throwable t) {
      cause = Optional.of(t);
      throw t;
    }
  }

  @Test
  void showCorrectLeaderboardScore() {
    try {
      testName = "should-show-correct-leaderboard-score";
      shouldShowCorrectLeaderboardScore(new TicTacToePage(URL));
    } catch (Throwable t) {
      cause = Optional.of(t);
      throw t;
    }
  }

  @Test
  void incrementScoreOnWin() {
    try {
      testName = "should-increment-score-on-win";
      shouldIncrementScoreOnWin(new TicTacToePage(URL));
    } catch (Throwable t) {
      cause = Optional.of(t);
      throw t;
    }
  }

  @Test
  void notChangeScoreOnNewGameClick() {
    try {
      testName = "should-not-change-score-on-new-game-click";
      shouldNotChangeScoreOnNewGameClick(new TicTacToePage(URL));
    } catch (Throwable t) {
      cause = Optional.of(t);
      throw t;
    }
  }

  @Test
  void returnScoresFromApi() {
    try {
      testName = "should-return-scores-from-api";
      shouldReturnScoresFromApi(new TicTacToePage(URL));
    } catch (Throwable t) {
      cause = Optional.of(t);
      throw t;
    }
  }
}

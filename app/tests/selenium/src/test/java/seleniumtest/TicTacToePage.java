package seleniumtest;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverConditions;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static seleniumtest.TicTacToePage.Square.*;

public class TicTacToePage {
  public enum Square {
    topLeft, topSide, topRight, leftSide, center, rightSide, bottomLeft, bottomSide, bottomRight
  }

  private SelenideElement square(Square square) {
    switch (square) {
      case topLeft:
        return $("[id='0']");
      case topSide:
        return $("[id='1']");
      case topRight:
        return $("[id='2']");
      case leftSide:
        return $("[id='3']");
      case center:
        return $("[id='4']");
      case rightSide:
        return $("[id='5']");
      case bottomLeft:
        return $("[id='6']");
      case bottomSide:
        return $("[id='7']");
      case bottomRight:
        return $("[id='8']");
    }

    throw new IllegalArgumentException("Invalid square: " + square);
  }

  private SelenideElement clearScoreButton() {
    return $$("button").find(text("Clear Scores"));
  }

  private SelenideElement newGameButton() {
    return $("[data-cy=newGame]");
  }

  private SelenideElement xScore() {
    return $("[data-cy=\"x-score\"]");
  }

  private SelenideElement tieScore() {
    return $("[data-cy=\"tie-score\"]");
  }

  private SelenideElement oScore() {
    return $("[data-cy=\"o-score\"]");
  }

  public TicTacToePage(String url) {
    open(url);
  }

  public String heading() {
    return $("h1").text();
  }

  public void shouldHaveUrl(String url) {
    webdriver().shouldHave(WebDriverConditions.url(url));
  }

  public void play(Square square) {
    square(square).click();
  }

  public void winForX() {
    // ASSUME: page is loaded and nothing is played yet
    play(topLeft); // x
    play(topSide); // o
    play(topRight); // x
    play(rightSide); // o
    play(center); // x
    play(leftSide); // o
    play(bottomLeft); // x
  }

  public void playTieGame() {
    play(center); // x
    play(topLeft); // o
    play(topSide); // x
    play(bottomSide); // o
    play(topRight); // x
    play(bottomLeft); // o
    play(leftSide); // x
    play(rightSide); // o
    play(bottomRight); // x
  }

  public void shouldBeWinner(Square square) {
    square(square).$(".win").should(exist);
  }
  public void shouldNotBeWinner(Square square) {
    square(square).$(".win").shouldNot(exist);
  }

  public void clearScore() {
    clearScoreButton().click();
  }

  public void newGame() {
    newGameButton().click();
  }

  public int leaderBoardScoreX() {
    return Integer.parseInt(xScore().text());
  }

  public int leaderBoardScoreTie() {
    return Integer.parseInt(tieScore().text());
  }

  public int leaderBoardScoreO() {
    return Integer.parseInt(oScore().text());
  }
}

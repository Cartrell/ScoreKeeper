package com.example.cartrell.spaceinvaders;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Button;

import com.example.cartrell.spaceinvaders.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

  //======================================================
  // const variables
  //======================================================

  //Base points awarded for each alien destroyed. Higher waves (levels) will award more
  // points based on these values.
  private final int ALIEN_M_BASE_POINTS = 2;
  private final int ALIEN_S_BASE_POINTS = 3;
  private final int ALIEN_X_BASE_POINTS = 7;

  //Max number of waves (levels) in the game. Game is completed when the final wave is cleared
  // of aliens.
  private final int MAX_WAVES = 3;

  //number of lives both players start with
  private final int PLAYER_START_LIVES = 3;

  //how many aliens start the first wave, and how many new aliens are added per wave
  private final int NUM_BASE_ALIENS = 8;
  private final int NUM_NEW_ALIENS_PER_WAVE = 4;

  //the multiplier given when a player's bonus is active
  private final int BONUS_MULTIPLIER = 2;

  //======================================================
  // member variables
  //======================================================

  //Arrays that contain various types of views. The index of each view corresponds to
  // the player index. For example, all of player 1's views are at index 0, and player 2's are
  // at index 1.
  private Button[] m_alienSButtons;
  private Button[] m_alienMButtons;
  private Button[] m_alienXButtons;
  private Button[] m_bonusButtons;
  private Button[] m_lostButtons;
  private AppCompatTextView[] m_scoreTextViews;
  private AppCompatTextView[] m_livesTextViews;
  private AppCompatTextView[] m_resultTextViews;

  //Players data array. The index of each player data object corresponds to the player index.
  private PlayerData[] m_players;

  //used to data binding
  private ActivityMainBinding m_binding;

  //current wave
  private int m_wave;

  //number of aliens left to clear the current wave
  private int m_numAliensLeft;

  //======================================================
  // public
  //======================================================

  //------------------------------------------------------
  // onAlienMClick
  //------------------------------------------------------
  /**
   * Called when a player's Alien-M button is pressed.
   * @param view The object that triggered the event.
   */
  public void onAlienMClick(View view) {
    int playerIndex = arrayIndexOf(m_alienMButtons, view);
    if (playerIndex == -1) {
      return;
    }

    addPlayerScore(playerIndex, calcAlienMPoints(playerIndex));
    destroyAlien();
  }

  //------------------------------------------------------
  // onAlienSClick
  //------------------------------------------------------
  /**
   * Called when a player's Alien-S button is pressed.
   * @param view The object that triggered the event.
   */
  public void onAlienSClick(View view) {
    int playerIndex = arrayIndexOf(m_alienSButtons, view);
    if (playerIndex == -1) {
      return;
    }

    addPlayerScore(playerIndex, calcAlienSPoints(playerIndex));
    destroyAlien();
  }

  //------------------------------------------------------
  // onAlienXClick
  //------------------------------------------------------
  /**
   * Called when a player's Alien-X button is pressed.
   * @param view The object that triggered the event.
   */
  public void onAlienXClick(View view) {
    int playerIndex = arrayIndexOf(m_alienXButtons, view);
    if (playerIndex == -1) {
      return;
    }

    addPlayerScore(playerIndex, calcAlienXPoints(playerIndex));
    destroyAlien();
  }

  //------------------------------------------------------
  // onBonusClick
  //------------------------------------------------------
  /**
   * Called when a player's Bonus button is pressed.
   * @param view The object that triggered the event.
   */
  public void onBonusClick(View view) {
    int playerIndex = arrayIndexOf(m_bonusButtons, view);
    if (playerIndex > -1) {
      togglePlayerBonus(playerIndex);
    }
  }

  //------------------------------------------------------
  // onLostClick
  //------------------------------------------------------
  /**
   * Called when a player's Lost button is pressed.
   * @param view The object that triggered the event.
   */
  public void onLostClick(View view) {
    int playerIndex = arrayIndexOf(m_lostButtons, view);
    if (playerIndex > -1) {
      playerLost(playerIndex);
    }
  }

  //------------------------------------------------------
  // onResetClick
  //------------------------------------------------------
  /**
   * Called when the Reset button is pressed.
   * @param view The object that triggered the event.
   */
  public void onResetClick(View view) {
    reset();
  }

  //======================================================
  // protected
  //======================================================

  //------------------------------------------------------
  // onCreate
  //------------------------------------------------------
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    m_binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    initPlayersUiMaps();
    initPlayers();
    reset();
  }

  //======================================================
  // private
  //======================================================

  //------------------------------------------------------
  // addPlayerScore
  //------------------------------------------------------
  /**
   * Adds points value to the player's current score.
   * @param playerIndex int The index of the affecting player.
   * @param pointsToAdd int The number of points to add to the score.
   */
  private void addPlayerScore(int playerIndex, int pointsToAdd) {
    PlayerData playerData = m_players[playerIndex];
    int score = playerData.getScore();
    setPlayerScore(playerIndex, score + pointsToAdd);
  }

  //------------------------------------------------------
  // arrayIndexOf
  //------------------------------------------------------
  /**
   * Searches the array of buttons and returns the index position of a button if it exists within
   * the array.
   * @param buttons The array of buttons to search.
   * @param targetButton The button to search for.
   * @return int The index of the button within the array if found, or -1 if it doesn't exist.
   */
  private int arrayIndexOf(Button[] buttons, View targetButton) {
    for (int index = 0; index < buttons.length; index++) {
      if (buttons[index] == targetButton) {
        return(index);
      }
    }

    return(-1);
  }

  //------------------------------------------------------
  // calcAlienMPoints
  //------------------------------------------------------
  /**
   * Calculates the points for destroying an M-Alien.
   * @param playerIndex int The index of player who destroyed the alien.
   * @return int The points awarded. The currnt wave and player's bonus status are taken into
   * account.
   */
  private int calcAlienMPoints(int playerIndex) {
    int points = ALIEN_M_BASE_POINTS * (m_wave + 1);
    if (isPlayerBonusOn(playerIndex)) {
      points *= BONUS_MULTIPLIER;
    }
    return(points);
  }

  //------------------------------------------------------
  // calcAlienSPoints
  //------------------------------------------------------
  /**
   * Calculates the points for destroying an S-Alien.
   * @param playerIndex int The index of player who destroyed the alien.
   * @return int The points awarded. The currnt wave and player's bonus status are taken into
   * account.
   */
  private int calcAlienSPoints(int playerIndex) {
    int points = ALIEN_S_BASE_POINTS * (m_wave + 1);
    if (isPlayerBonusOn(playerIndex)) {
      points *= BONUS_MULTIPLIER;
    }
    return(points);
  }

  //------------------------------------------------------
  // calcAlienXPoints
  //------------------------------------------------------
  /**
   * Calculates the points for destroying an X-Alien.
   * @param playerIndex int The index of player who destroyed the alien.
   * @return int The points awarded. The currnt wave and player's bonus status are taken into
   * account.
   */
  private int calcAlienXPoints(int playerIndex) {
    int points = ALIEN_X_BASE_POINTS * (m_wave + 1);
    if (isPlayerBonusOn(playerIndex)) {
      points *= BONUS_MULTIPLIER;
    }
    return(points);
  }

  //------------------------------------------------------
  // comparePlayers
  //------------------------------------------------------
  /**
   * Compares the performance of both players. The result texts indicate the winner or a tie.
   */
  private void comparePlayers() {
    //first, compare both players' scores
    int winningPlayerIndex = comparePlayerValues(m_players[0].getScore(), m_players[1].getScore());
    if (winningPlayerIndex == -1) {
      //if tied, compare both players' lives
      winningPlayerIndex = comparePlayerValues(m_players[0].getLives(), m_players[1].getLives());
    }

    presentWinningPlayer(winningPlayerIndex);
  }

  //------------------------------------------------------
  // comparePlayerValues
  //------------------------------------------------------
  /**
   * Compares two values representing player 1 and player 2, respectively, and returns the
   * player index of the higehr value
   * @param player0Value int The value representing player 1.
   * @param player1Value int The value representing player 2.
   * @return int Returns 0 if player 1 has the higher value. Returns 1 if player 2 has the
   * higher value. If both values are equal, -1 is returned.
   */
  private int comparePlayerValues(int player0Value, int player1Value) {
    if (player0Value > player1Value) {
      return(0);
    }

    if (player1Value > player0Value) {
      return(1);
    }

    return(-1);
  }

  //------------------------------------------------------
  // destroyAlien
  //------------------------------------------------------
  /**
   * Called each time a player destroys an alien. Sets the next wave if this was the final alien
   * in the wave, and updates the wave status.
   */
  private void destroyAlien() {
    if (--m_numAliensLeft <= 0) {
      setNextWave();
    } else {
      updateWaveStatus();
    }
  }

  //------------------------------------------------------
  // gameCleared
  //------------------------------------------------------
  /**
   * Called when the game is cleared by attempting to advance past the final wave.
   */
  private void gameCleared() {
    setPlayersButtonsEnabled(false);
    comparePlayers();
  }

  //------------------------------------------------------
  // initPlayersUiMaps
  //------------------------------------------------------
  /**
   * Setup arrays for each type of button.
   */
  private void initPlayersUiMaps() {
    m_alienSButtons = new Button[]{ m_binding.btnAlienS1, m_binding.btnAlienS2 };
    m_alienMButtons = new Button[]{ m_binding.btnAlienM1, m_binding.btnAlienM2 };
    m_alienXButtons = new Button[]{ m_binding.btnAlienX1, m_binding.btnAlienX2 };
    m_bonusButtons = new Button[]{ m_binding.btnBonus1, m_binding.btnBonus2 };
    m_lostButtons = new Button[]{ m_binding.btnLost1, m_binding.btnLost2 };
    m_scoreTextViews = new AppCompatTextView[]{ m_binding.txtScore1, m_binding.txtScore2};
    m_livesTextViews = new AppCompatTextView[]{ m_binding.txtLives1, m_binding.txtLives2};
    m_resultTextViews = new AppCompatTextView[]{ m_binding.txtResult1, m_binding.txtResult2};
  }

  //------------------------------------------------------
  // initPlayers
  //------------------------------------------------------
  /**
   * Setup an array that contains the players' data.
   */
  private void initPlayers() {
    m_players = new PlayerData[]{ new PlayerData(), new PlayerData() };
  }

  //------------------------------------------------------
  // isPlayerBonusOn
  //------------------------------------------------------
  /**
   * Determines if a player's bonus is on (active).
   * @param playerIndex int The index of the player to check.
   * @return boolean True if the player's bonus is active.
   */
  private boolean isPlayerBonusOn(int playerIndex) {
    PlayerData playerData = m_players[playerIndex];
    return(playerData.getIsBonusActive());
  }

  //------------------------------------------------------
  // playerLost
  //------------------------------------------------------
  /**
   * Called when a player has lost a life. If a player has lost their last life, game ends, and
   * the other player automatically wins.
   * @param playerIndex The index of the affecting player.
   */
  private void playerLost(int playerIndex) {
    PlayerData playerData = m_players[playerIndex];
    int lives = playerData.getLives() - 1;
    setPlayerLives(playerIndex, lives);

    if (lives == 0) {
      playerWon(playerIndex == 0 ? 1 : 0);
    }
  }

  //------------------------------------------------------
  // playerWon
  //------------------------------------------------------
  /**
   * Called when the game is over, and a player has won.
   * @param playerIndex int The index of the player who won.
   */
  private void playerWon(int playerIndex) {
    setPlayersButtonsEnabled(false);
    presentWinningPlayer(playerIndex);
  }

  //------------------------------------------------------
  // presentWinningPlayer
  //------------------------------------------------------
  /**
   * Displays the winning player of te game, or if the game ended in a tie
   * @param playerIndex int The index of the player who won, or -1 if nobody won.
   */
  private void presentWinningPlayer(int playerIndex) {
    if (playerIndex == 0) {
      m_resultTextViews[0].setText(R.string.winner);
      m_resultTextViews[1].setText("");
    } else if (playerIndex == 1) {
      m_resultTextViews[1].setText(R.string.winner);
      m_resultTextViews[0].setText("");
    } else {
      m_resultTextViews[0].setText(R.string.tied);
      m_resultTextViews[1].setText(R.string.tied);
    }
  }

  //------------------------------------------------------
  // reset
  //------------------------------------------------------
  /**
   * Resets the game state
   */
  private void reset() {
    resetPlayer(0);
    resetPlayer(1);
    setWave(0);
    updatePlayersButtons();
  }

  //------------------------------------------------------
  // resetPlayer
  //------------------------------------------------------
  /**
   * Resets the state of an individual player
   * @param index int The array index of the player to reset
   */
  private void resetPlayer(int index) {
    setPlayerScore(index, 0);
    setPlayerLives(index, PLAYER_START_LIVES);
    setPlayerBonus(index, false);
    m_resultTextViews[index].setText("");
    setPlayerButtonsEnabled(index, true);
  }

  //------------------------------------------------------
  // setNextWave
  //------------------------------------------------------
  /**
   * Advances to the next wave in the game. This is called when all the aliens in the current
   * wave have been destroyed. When attempting to advance past the last wave, the game is
   * cleared.
   */
  private void setNextWave() {
    int nextWave = m_wave + 1;
    if (nextWave >= MAX_WAVES) {
      gameCleared();
    } else {
      setWave(nextWave);
      updatePlayersButtons();
    }
  }

  //------------------------------------------------------
  // setPlayerBonus
  //------------------------------------------------------
  /**
   * Sets the activation an individual player's points bonus multiplier
   * @param index int The array index of the player
   * @param isActive boolean Specifies if the bonus is active or not
   */
  private void setPlayerBonus(int index, boolean isActive) {
    PlayerData playerData = m_players[index];
    playerData.setIsBonusActive(isActive);

    Button button = m_bonusButtons[index];
    String text = getResources().getText(isActive ? R.string.bonusOn : R.string.bonusOff).toString();
    button.setText(text);
  }

  //------------------------------------------------------
  // setPlayerButtonsEnabled
  //------------------------------------------------------
  /**
   * Enables or disables all the buttons of an individual player.
   * @param playerIndex int The index of the affecting player.
   * @param isEnabled boolean Specified if the buttons will be enabled
   */
  private void setPlayerButtonsEnabled(int playerIndex, boolean isEnabled) {
    m_alienSButtons[playerIndex].setEnabled(isEnabled);
    m_alienMButtons[playerIndex].setEnabled(isEnabled);
    m_alienXButtons[playerIndex].setEnabled(isEnabled);
    m_bonusButtons[playerIndex].setEnabled(isEnabled);
    m_lostButtons[playerIndex].setEnabled(isEnabled);
  }

  //------------------------------------------------------
  // setPlayerLives
  //------------------------------------------------------
  /**
   * Sets the lives of an individual player
   * @param index int The array index of the player
   * @param lives int The lives value
   */
  private void setPlayerLives(int index, int lives) {
    //update the player's score
    PlayerData playerData = m_players[index];
    playerData.setLives(lives);

    //update the textview with the player's score
    AppCompatTextView textView = m_livesTextViews[index];
    textView.setText(String.valueOf(lives));
  }

  //------------------------------------------------------
  // setPlayersButtonsEnabled
  //------------------------------------------------------
  /**
   * Enables or disables all of both players' buttons.
   */
  private void setPlayersButtonsEnabled(boolean isEnabled) {
    setPlayerButtonsEnabled(0, isEnabled);
    setPlayerButtonsEnabled(1, isEnabled);
  }

  //------------------------------------------------------
  // setPlayerScore
  //------------------------------------------------------
  /**
   * Sets the score of an individual player
   * @param index int The array index of the player
   * @param score int The score value
   */
  private void setPlayerScore(int index, int score) {
    //update the player's score
    PlayerData playerData = m_players[index];
    playerData.setScore(score);

    //update the textview with the player's score
    AppCompatTextView textView = m_scoreTextViews[index];
    textView.setText(String.valueOf(score));
  }

  //------------------------------------------------------
  // setWave
  //------------------------------------------------------
  /**
   * Sets the current wave of the game. Also resets the number of aliens remaining.
   * @param wave
   */
  private void setWave(int wave) {
    m_wave = wave;
    m_numAliensLeft = NUM_BASE_ALIENS + wave * NUM_NEW_ALIENS_PER_WAVE;

    //update the text field that displays both wabe and aliens remaining
    updateWaveStatus();
  }

  //------------------------------------------------------
  // togglePlayerBonus
  //------------------------------------------------------
  /**
   * Toggle the on/off state of a player's bonus button.
   * @param playerIndex int The index of the player.
   */
  private void togglePlayerBonus(int playerIndex) {
    PlayerData playerData = m_players[playerIndex];
    boolean isBonusOn = playerData.getIsBonusActive();
    setPlayerBonus(playerIndex, !isBonusOn);
    updatePlayerButtons(playerIndex);
  }

  //------------------------------------------------------
  // updatePlayerButton
  //------------------------------------------------------
  /**
   * Updates the text of a player's individual action button
   * @param playerIndex int The array index of the player
   * @param basePoints int Base points awarded for the alien being represented
   * @param stringResourceId int String resource ID of the format string used to build the button text
   */
  private void updatePlayerButton(int playerIndex, Button[] buttons, int basePoints,
  int stringResourceId) {
    PlayerData playerData = m_players[playerIndex];
    Button button = buttons[playerIndex];
    String format = getResources().getText(stringResourceId).toString();

    int pointsAwarded;
    if (basePoints == ALIEN_M_BASE_POINTS) {
      pointsAwarded = calcAlienMPoints(playerIndex);
    } else if (basePoints == ALIEN_S_BASE_POINTS) {
      pointsAwarded = calcAlienSPoints(playerIndex);
    } else {
      pointsAwarded = calcAlienXPoints(playerIndex);
    }

    button.setText(String.format(format, pointsAwarded));
  }

  //------------------------------------------------------
  // updatePlayerButtons
  //------------------------------------------------------
  /**
   * Updates the texts of a player's alien buttons
   * @param playerIndex int The array index of the player
   */
  private void updatePlayerButtons(int playerIndex) {
    updatePlayerButton(playerIndex, m_alienSButtons, ALIEN_S_BASE_POINTS, R.string.alienS);
    updatePlayerButton(playerIndex, m_alienMButtons, ALIEN_M_BASE_POINTS, R.string.alienM);
    updatePlayerButton(playerIndex, m_alienXButtons, ALIEN_X_BASE_POINTS, R.string.alienX);
  }

  //------------------------------------------------------
  // updatePlayersButtons
  //------------------------------------------------------
  /**
   * Updates the texts of both players' alien buttons.
   */
  private void updatePlayersButtons() {
    updatePlayerButtons(0);
    updatePlayerButtons(1);
  }

  //------------------------------------------------------
  // updateWaveStatus
  //------------------------------------------------------
  /**
   * Updates the wave status text view with the current wave and number of aliens remaining
   */
  private void updateWaveStatus() {
    String format = getResources().getText(R.string.waveStatus).toString();

    //Since wave is a zero-based value, we want to add one for presentation (first level should
    // display 1, not 0)
    String text = String.format(format, m_wave + 1, m_numAliensLeft);
    m_binding.txtWaveStatus.setText(text);
  }
}

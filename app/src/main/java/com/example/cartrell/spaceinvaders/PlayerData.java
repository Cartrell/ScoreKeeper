package com.example.cartrell.spaceinvaders;

class PlayerData {
  //======================================================
  // member variables
  //======================================================
  private int m_score;
  private int m_lives;
  private boolean m_isBonusActive;

  //======================================================
  // package-private
  //======================================================

  //------------------------------------------------------
  // (ctor)
  //------------------------------------------------------
  PlayerData() {
    reset();
  }

  //------------------------------------------------------
  // getLives
  //------------------------------------------------------
  /**
   * Returns the number of lives the player has remaining
   * @return int
   */
  public int getLives() {
    return(m_lives);
  }

  /**
   * Returns true if the bonus is active, false if it's not
   * @return boolean
   */
  //------------------------------------------------------
  // getIsBonusActive
  //------------------------------------------------------
  public boolean getIsBonusActive() {
    return(m_isBonusActive);
  }

  //------------------------------------------------------
  // getScore
  //------------------------------------------------------
  /**
   * Returns the player's score
   * @return int
   */
  public int getScore() {
    return(m_score);
  }

  //------------------------------------------------------
  // reset
  //------------------------------------------------------
  /**
   * Resets the state of the player's data
   */
  void reset() {
    m_score = 0;
    m_lives = 0;
    m_isBonusActive = false;
  }

  //------------------------------------------------------
  // setLives
  //------------------------------------------------------

  /**
   * Sets the lives of the player
   * @param value int
   */
  public void setLives(int value) {
    m_lives = value;
  }

  //------------------------------------------------------
  // setIsBonusActive
  //------------------------------------------------------
  /**
   * Enables of disables the status of the player's bonus
   * @param value boolean
   */
  public void setIsBonusActive(boolean value) {
    m_isBonusActive = value;
  }

  //------------------------------------------------------
  // setScore
  //------------------------------------------------------
  /**
   * Sets the player's current score
   * @param value int
   */
  public void setScore(int value) {
    m_score = value;
  }
}

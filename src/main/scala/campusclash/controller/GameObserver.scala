package campusclash.controller

import campusclash.model.GameState

trait GameObserver:

  def onGameStateChanged(state: GameState): Unit

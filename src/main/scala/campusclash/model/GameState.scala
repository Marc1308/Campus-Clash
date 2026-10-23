package campusclash.model

case class GameState(
                      player: Player,
                      currentLocation: Location
                    ):
  def moveTo(location: Location): GameState = copy(currentLocation = location)
  def damagePlayer (amount: Int): GameState = copy(player = player.takeDamage(amount))
  def healPlayer(amount: Int): GameState = copy(player=player.heal(amount))
  def isGameOver: Boolean = !player.isAlive
package campusclash.model

case class Player(
                   name: String,
                   health: Int,
                   maxHealth: Int,
                   level: Int
                 ):
  def isAlive : Boolean = health > 0
  def takeDamage(amount: Int): Player = copy(health = math.max(0, health-amount))
  def heal(amount: Int): Player = copy(health = math.min(maxHealth, health + amount))
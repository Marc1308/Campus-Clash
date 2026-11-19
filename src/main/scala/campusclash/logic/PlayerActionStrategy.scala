package campusclash.logic

import campusclash.model.Player

trait PlayerActionStrategy:

  def execute(player: Player, amount: Int): Player


object DamageStrategy extends PlayerActionStrategy:

  override def execute(player: Player, amount: Int): Player =
    player.takeDamage(amount)


object HealingStrategy extends PlayerActionStrategy:

  override def execute(player: Player, amount: Int): Player =
    player.heal(amount)

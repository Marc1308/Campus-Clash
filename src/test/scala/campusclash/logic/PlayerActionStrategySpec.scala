package campusclash.logic

import campusclash.model.Player
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class PlayerActionStrategySpec extends AnyWordSpec with Matchers:

  "DamageStrategy" should {

    "reduce the player's health" in {
      val player = Player("Alex", 80, 100, 2)

      val result = DamageStrategy.execute(player, 30)

      result.health shouldBe 50
    }
  }

  "HealingStrategy" should {

    "increase the player's health" in {
      val player = Player("Alex", 50, 100, 2)

      val result = HealingStrategy.execute(player, 20)

      result.health shouldBe 70
    }
  }

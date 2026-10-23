package campusclash.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class PlayerSpec extends AnyWordSpec with Matchers:

  "A Player" should {

    "store the player's name" in {
      Player("Max", 100, 100, 1).name shouldBe "Max"
    }

    "store the player's health" in {
      Player("Max", 80, 100, 1).health shouldBe 80
    }

    "store the player's maximum health" in {
      Player("Max", 80, 100, 1).maxHealth shouldBe 100
    }

    "store the player's level" in {
      Player("Max", 80, 100, 3).level shouldBe 3
    }

    "be alive when health is above 0" in {
      Player("Max", 10, 100, 1).isAlive shouldBe true
    }

    "not be alive when health is 0" in {
      Player("Max", 0, 100, 1).isAlive shouldBe false
    }

    "lose health when taking damage" in {
      Player("Max", 100, 100, 1).takeDamage(30).health shouldBe 70
    }

    "not go below 0 health" in {
      Player("Max", 10, 100, 1).takeDamage(50).health shouldBe 0
    }

    "not be alive after taking fatal damage" in {
      Player("Max", 10, 100, 1).takeDamage(50).isAlive shouldBe false
    }

    "not change the original player when taking damage" in {
      val player = Player("Max", 100, 100, 1)
      player.takeDamage(30)
      player.health shouldBe 100
    }

    "regain health when healed" in {
      Player("Max", 50, 100, 1).heal(20).health shouldBe 70
    }

    "heal up to maximum health" in {
      Player("Max", 90, 100, 1).heal(50).health shouldBe 100
    }
  }
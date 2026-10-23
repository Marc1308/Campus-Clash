package campusclash.model

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class PlayerSpec extends AnyFlatSpec with Matchers:

  "A Player" should "store the player's name" in {
    val player = Player("Max", 100, 100, 1)
    player.name shouldBe "Max"
  }

  it should "store the player's health" in {
    val player = Player("Max", 80, 100, 1)
    player.health shouldBe 80
  }

  it should "store the player's maximum health" in {
    val player = Player("Max", 80, 100, 1)
    player.maxHealth shouldBe 100
  }

  it should "store the player's level" in {
    val player = Player("Max", 80, 100, 3)
    player.level shouldBe 3
  }

  it should "be alive when health is above 0" in {
    Player("Max", 10, 100, 1).isAlive shouldBe true
  }

  it should "not be alive when health is 0" in {
    Player("Max", 0, 100, 1).isAlive shouldBe false
  }

  it should "lose health when taking damage" in {
    Player("Max", 100, 100, 1).takeDamage(30).health shouldBe 70
  }

  it should "not go below 0 health" in {
    Player("Max", 10, 100, 1).takeDamage(50).health shouldBe 0
  }

  it should "not be alive after taking fatal damage" in {
    Player("Max", 10, 100, 1).takeDamage(50).isAlive shouldBe false
  }

  it should "not change the original player when taking damage" in {
    val player = Player("Max", 100, 100, 1)
    player.takeDamage(30)
    player.health shouldBe 100
  }

  it should "regain health when healed" in {
    Player("Max", 50, 100, 1).heal(20).health shouldBe 70
  }

  it should "heal up to maximum health" in {
    Player("Max", 90, 100, 1).heal(50).health shouldBe 100
  }
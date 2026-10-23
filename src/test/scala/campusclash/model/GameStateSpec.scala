package campusclash.model

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class GameStateSpec extends AnyFlatSpec with Matchers:

  "A GameState" should "store the player and current location" in {
    val player = Player("Alex", 80, 100, 4)
    val state = GameState(player, Location.Campus)

    state.player shouldBe player
    state.currentLocation shouldBe Location.Campus
  }

  it should "allow changing the current location" in {
    val player = Player("Alex", 80, 100, 2)
    val state = GameState(player, Location.Campus)

    val updatedState = state.copy(currentLocation = Location.Library)

    updatedState.currentLocation shouldBe Location.Library
  }

  it should "move to a new location" in {
    val player = Player("Alex", 80, 100, 2)
    val state = GameState(player, Location.Campus)

    state.moveTo(Location.BuildingA).currentLocation shouldBe Location.BuildingA
  }

  it should "not change the original state when moving" in {
    val player = Player("Alex", 80, 100, 2)
    val state = GameState(player, Location.Campus)

    state.moveTo(Location.Library)

    state.currentLocation shouldBe Location.Campus
  }

  it should "keep the player when moving" in {
    val player = Player("Alex", 80, 100, 2)
    val state = GameState(player, Location.Campus)

    state.moveTo(Location.Library).player shouldBe player
  }

  it should "damage the player" in {
    val player = Player("Alex", 80, 100, 2)
    val state = GameState(player, Location.Campus)

    state.damagePlayer(30).player.health shouldBe 50
  }

  it should "keep the location when the player takes damage" in {
    val player = Player("Alex", 80, 100, 2)
    val state = GameState(player, Location.Library)

    state.damagePlayer(30).currentLocation shouldBe Location.Library
  }

  it should "heal the player" in {
    val player = Player("Alex", 50, 100, 2)
    val state = GameState(player, Location.Campus)

    state.healPlayer(30).player.health shouldBe 80
  }

  it should "not heal the player above maximum health" in {
    val player = Player("Alex", 90, 100, 2)
    val state = GameState(player, Location.Campus)

    state.healPlayer(50).player.health shouldBe 100
  }

  it should "not be game over while the player is alive" in {
    val player = Player("Alex", 80, 100, 2)
    val state = GameState(player, Location.Campus)

    state.isGameOver shouldBe false
  }

  it should "be game over when the player has no health left" in {
    val player = Player("Alex", 80, 100, 2)
    val state = GameState(player, Location.Campus)

    state.damagePlayer(80).isGameOver shouldBe true
  }
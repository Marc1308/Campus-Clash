package campusclash.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class GameStateSpec extends AnyWordSpec with Matchers:

  private val player = Player("Alex", 80, 100, 2)

  "A GameState" should {

    "store the player and current location" in {
      val state = GameState(player, Location.Campus)

      state.player shouldBe player
      state.currentLocation shouldBe Location.Campus
    }

    "allow changing the current location" in {
      val state = GameState(player, Location.Campus)
      val updatedState = state.copy(currentLocation = Location.Library)

      updatedState.currentLocation shouldBe Location.Library
    }

    "move to a new location" in {
      val state = GameState(player, Location.Campus)

      state.moveTo(Location.BuildingA).currentLocation shouldBe Location.BuildingA
    }

    "not change the original state when moving" in {
      val state = GameState(player, Location.Campus)
      state.moveTo(Location.Library)

      state.currentLocation shouldBe Location.Campus
    }

    "keep the player when moving" in {
      val state = GameState(player, Location.Campus)

      state.moveTo(Location.Library).player shouldBe player
    }

    "damage the player" in {
      val state = GameState(player, Location.Campus)

      state.damagePlayer(30).player.health shouldBe 50
    }

    "keep the location when the player takes damage" in {
      val state = GameState(player, Location.Library)

      state.damagePlayer(30).currentLocation shouldBe Location.Library
    }

    "heal the player" in {
      val state = GameState(Player("Alex", 50, 100, 2), Location.Campus)

      state.healPlayer(30).player.health shouldBe 80
    }

    "not heal the player above maximum health" in {
      val state = GameState(Player("Alex", 90, 100, 2), Location.Campus)

      state.healPlayer(50).player.health shouldBe 100
    }

    "not be game over while the player is alive" in {
      GameState(player, Location.Campus).isGameOver shouldBe false
    }

    "be game over when the player has no health left" in {
      val state = GameState(player, Location.Campus).damagePlayer(80)

      state.isGameOver shouldBe true
    }
  }
package campusclash.controller

import campusclash.model.{GameState, Player}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class UndoManagerSpec extends AnyWordSpec with Matchers:

  private val state1 =
    GameState(Player("Student", 100, 100, 1), None, None, Set.empty)

  private val state2 =
    GameState(Player("Student", 90, 100, 1), None, None, Set.empty)

  private val state3 =
    GameState(Player("Student", 80, 100, 1), None, None, Set.empty)

  private val command = NavigationCommand.LeaveBuilding

  "UndoManager" should {

    "return None when undo stack is empty" in {
      val manager = new UndoManager

      manager.undo() shouldBe None
      manager.canUndo shouldBe false
    }

    "return None when redo stack is empty" in {
      val manager = new UndoManager

      manager.redo() shouldBe None
      manager.canRedo shouldBe false
    }

    "store an executed command" in {
      val manager = new UndoManager

      manager.execute(command, state1, state2)

      manager.canUndo shouldBe true
      manager.canRedo shouldBe false
    }

    "undo an executed command" in {
      val manager = new UndoManager

      manager.execute(command, state1, state2)

      manager.undo() shouldBe Some(state1)
      manager.canUndo shouldBe false
      manager.canRedo shouldBe true
    }

    "redo an undone command" in {
      val manager = new UndoManager

      manager.execute(command, state1, state2)
      manager.undo()

      manager.redo() shouldBe Some(state2)
      manager.canUndo shouldBe true
      manager.canRedo shouldBe false
    }

    "undo multiple commands in reverse order" in {
      val manager = new UndoManager

      manager.execute(command, state1, state2)
      manager.execute(command, state2, state3)

      manager.undo() shouldBe Some(state2)
      manager.undo() shouldBe Some(state1)
    }

    "clear redo history after a new command" in {
      val manager = new UndoManager

      manager.execute(command, state1, state2)
      manager.undo()

      manager.canRedo shouldBe true

      manager.execute(command, state1, state3)

      manager.canRedo shouldBe false
    }
  }
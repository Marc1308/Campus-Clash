package campusclash.controller

import campusclash.model.GameState

final class UndoManager:

  private case class HistoryEntry(
                                   command: NavigationCommand,
                                   previousState: GameState,
                                   nextState: GameState
                                 )

  private var undoStack: List[HistoryEntry] = Nil
  private var redoStack: List[HistoryEntry] = Nil

  def execute(
               command: NavigationCommand,
               currentState: GameState,
               nextState: GameState
             ): Unit =
    val entry = HistoryEntry(command, currentState, nextState)

    undoStack = entry :: undoStack
    redoStack = Nil

  def undo(): Option[GameState] =
    undoStack match
      case Nil =>
        None

      case head :: tail =>
        undoStack = tail
        redoStack = head :: redoStack
        Some(head.command.undoStep(head.previousState))

  def redo(): Option[GameState] =
    redoStack match
      case Nil =>
        None

      case head :: tail =>
        redoStack = tail
        undoStack = head :: undoStack
        Some(head.command.redoStep(head.nextState))

  def canUndo: Boolean =
    undoStack.nonEmpty

  def canRedo: Boolean =
    redoStack.nonEmpty
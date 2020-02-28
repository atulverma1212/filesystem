package org.averma.commands
import org.averma.filesystem.State

object DefaultCommand extends Command {
  override def apply(state: State): State =
    state.setMessage("Command not found! ")
}

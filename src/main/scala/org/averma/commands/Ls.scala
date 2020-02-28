package org.averma.commands
import org.averma.filesystem.State

class Ls extends Command {
  override def apply(state: State): State = {
    val output = state.wd.contents.map(e => e.name + ": " + e.getType + "\n").mkString
    state.setMessage(output)
  }
}

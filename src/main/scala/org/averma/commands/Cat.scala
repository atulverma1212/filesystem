package org.averma.commands
import org.averma.filesystem.State

class Cat(filename : String) extends Command {
  override def apply(state: State): State = {
    val dirEntry = state.wd.findEntry(filename)
    if (dirEntry == null || !dirEntry.isFile)
      state.setMessage(s"$filename : no such file")
    else
      state.setMessage(dirEntry.asFile.contents)
  }
}

package org.averma.commands
import org.averma.files.{DirEntry, File}
import org.averma.filesystem.State

class Touch(name: String) extends CreateEntry(name) {

  override def doCreateEntry(state: State): DirEntry =
    File.empty(state.wd.path, name)
}

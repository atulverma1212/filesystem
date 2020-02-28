package org.averma.commands
import org.averma.files.{DirEntry, Directory}
import org.averma.filesystem.State

class MkDir(name: String) extends CreateEntry(name) {
  override def doCreateEntry(state: State): DirEntry =
    Directory.empty(state.wd.path, name)
}

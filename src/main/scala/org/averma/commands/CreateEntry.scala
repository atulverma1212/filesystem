package org.averma.commands
import org.averma.files.{DirEntry, Directory}
import org.averma.filesystem.State

abstract class CreateEntry(entryName: String) extends Command {
  def doCreateEntry(state: State): DirEntry

  override def apply(state: State): State = {
    val wd = state.wd
    if (wd.hasEntry(entryName)) state.setMessage(s"Directory with entryName: $entryName already exists")
    else if(entryName.contains(Directory.SEPARATOR) || !isValidentryName(entryName)) state.setMessage("Invalid entryName: " + entryName)
    else execute(state, entryName)
  }

  private def isValidentryName(entryName: String): Boolean = {
    !entryName.contains(".")
  }

  private def execute(state: State, entryName: String): State = {
    def updateStructure(currentDir: Directory, path: List[String], newDir: DirEntry): Directory = {
      if (path.isEmpty) currentDir.addEntry(newDir)
      else {
        val oldEntry = currentDir.findEntry(path.head).asDirectory
        currentDir.replaceEntry(oldEntry.name, updateStructure(oldEntry, path.tail, newDir ))
      }
    }

    val wd = state.wd
    // all dirs in full path/working directory
    val allDirsInPath = wd.getAllFoldersInPath
    // create a new entry in wd
    val newEntry: DirEntry = doCreateEntry(state)
    // updae whoile directory structure starting from root
    val newRoot = updateStructure(state.root, allDirsInPath, newEntry)
    // find new wd INSTANCE given wd's full path in the new directory structure
    val newWd = newRoot.findDescendant(allDirsInPath)
    State(newRoot, newWd)
  }
}

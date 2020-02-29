package org.averma.commands
import org.averma.files.{Directory, File}
import org.averma.filesystem.State

class Echo(args: Array[String]) extends Command {

  val APPEND_CONTENT : String = ">>"
  val SET_CONTENT : String = ">"

  // Top Inclusive
  private def createContent(args: Array[String], top: Int) : String =
    args.toList.slice(0, top).mkString(" ")

  override def apply(state: State): State = {
    if (args.isEmpty) state
    else if (args.length == 1) state.setMessage(args(0))
    else {
      val fileName = args.last
      val operator = args(args.length - 2)
      val contents = createContent(args, args.length - 2)

      operator match {
        case APPEND_CONTENT => execute(state, contents, fileName, append = true)
        case SET_CONTENT => execute(state, contents, fileName)
        case _ => state.setMessage(createContent(args, args.length))
      }
    }
  }

  private def execute(state: State, contents: String, fileName: String, append : Boolean = false) : State = {
    if (fileName.contains(Directory.SEPARATOR))
      state.setMessage("fileName must not contain Separators")
    else {
      val newRoot : Directory = getNewRootAfterEcho(state.wd, state.wd.getAllFoldersInPath :+ fileName, contents, append)
      if (newRoot == state.root)
        state.setMessage(fileName + ": no such file")
      else
        State(newRoot, newRoot.findDescendant(state.wd.getAllFoldersInPath))
    }
  }

  private def getNewRootAfterEcho(currentDir : Directory, path : List[String], contents: String, append : Boolean) : Directory = {
      if (path.isEmpty) currentDir
      else if(path.tail.isEmpty) {
        val dirEntry = currentDir.findEntry(path.head)
        if (dirEntry == null)
          currentDir.addEntry(new File(currentDir.path, path.head, contents))
        else if (!dirEntry.isFile) currentDir
        else
          if (append) currentDir.replaceEntry(path.head, dirEntry.asFile.appendContents(contents))
          else currentDir.replaceEntry(path.head, dirEntry.asFile.setContents(contents))
      } else {
        val nextDir = currentDir.findEntry(path.head)
        if (!nextDir.isDirectory) currentDir
        else {
          val nextDir = currentDir.findEntry(path.head).asDirectory
          val newNextDir = getNewRootAfterEcho(nextDir, path.tail, contents, append)
          if(newNextDir == nextDir) currentDir
          else currentDir.replaceEntry(path.head, newNextDir)
        }
      }
  }
}

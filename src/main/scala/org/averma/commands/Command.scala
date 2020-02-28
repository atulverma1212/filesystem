package org.averma.commands

import java.nio.file.DirectoryIteratorException

import org.averma.commands.Command.incompleteCommand
import org.averma.files.{DirEntry, Directory}
import org.averma.filesystem.State

trait Command {
  def apply(state: State): State

}

object Command {

  val MKDIR = "mkdir"
  val LS = "ls"
  val PWD = "pwd"
  val TOUCH = "touch"
  val CD = "cd"
  val RM = "rm"

  def emptyCommand: Command = (state: State) => state
  def incompleteCommand(name: String): Command = (state: State) => state.setMessage(s"Incomplete command: $name")

  def from(cmd: String): Command = {
    val tokens : Array[String] = cmd.split(" ")

    if (tokens.isEmpty) emptyCommand
    else tokens(0) match {

      case MKDIR =>
        if (tokens.length<2) incompleteCommand(tokens(0))
        else new MkDir(tokens(1))

      case LS => new Ls

      case PWD => new Pwd

      case TOUCH =>
        if (tokens.length<2) incompleteCommand(tokens(0))
        else new Touch(tokens(1))

      case CD =>
        if (tokens.length<2) incompleteCommand(tokens(0))
        else new Cd(tokens(1))

      case RM =>
        if (tokens.length<2) incompleteCommand(tokens(0))
        else new Rm(tokens(1))

      case _ =>  DefaultCommand
    }
  }
}

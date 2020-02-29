package org.averma.commands

import java.nio.file.DirectoryIteratorException

import org.averma.commands.Command.incompleteCommand
import org.averma.files.{DirEntry, Directory}
import org.averma.filesystem.State

trait Command extends (State => State) {
}

object Command {

  private val MKDIR = "mkdir"
  private val LS = "ls"
  private val PWD = "pwd"
  private val TOUCH = "touch"
  private val CD = "cd"
  private val RM = "rm"
  private val ECHO = "echo"
  private val CAT = "cat"

  def emptyCommand: Command = (state: State) => state
  def incompleteCommand(name: String): Command = (state: State) => state.setMessage(s"Incomplete command: $name")

  def from(cmd: String): Command = {
    val tokens : Array[String] = cmd.split(" ")

    if (tokens.isEmpty) emptyCommand
    else tokens(0) match {

      case MKDIR =>
        if (tokens.length<2) incompleteCommand(tokens(0))
        else new MkDir(tokens(1))

      case LS =>
        if (tokens.length<2) new Ls
        else new Ls(tokens(1).substring(1))


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

      case ECHO =>
        if (tokens.length<2) incompleteCommand(tokens(0))
        else new Echo(tokens.tail)

      case CAT =>
        if (tokens.length<2) incompleteCommand(tokens(0))
        else new Cat(tokens(1))

      case _ =>  DefaultCommand
    }
  }
}

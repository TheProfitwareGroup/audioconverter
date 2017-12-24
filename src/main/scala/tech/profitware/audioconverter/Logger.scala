package tech.profitware.audioconverter

trait Logger {
  def log(s: String): Unit
}

trait SimpleLogger extends Logger {
  def log(s: String) = println(s)
}

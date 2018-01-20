package snake

import java.util.Random

import org.scalajs.dom
import org.scalajs.dom.html

import scala.scalajs.js.annotation.JSExport

case class Point(x: Double, y: Double){
  def +(p: Point) = Point(x + p.x, y + p.y)
  def -(p: Point) = Point(x - p.x, y - p.y)
  def /(d: Double) = Point(x / d, y / d)
  def *(d: Double) = Point(x * d, y * d)
  def length = Math.sqrt(x * x + y * y)
}


@JSExport
object SnakeApp {

  case class Apple(isFound: Boolean, currLoc: Point, previousLoc: Option[Point]){
    def reSpawnApple: Apple = Apple(false, drawAPoint, Some(currLoc))
    def foundApple: Apple = copy(isFound = true)
  }

  def drawAPoint: Point = {
    val randGen = new Random()
    Point(randGen.nextInt(canvas.width + 1), randGen.nextInt(canvas.height + 1))
  }

  var direction = Point(0, -5)

  val canvas =
    dom.document
       .getElementById("canvas")
       .asInstanceOf[html.Canvas]

  val ctx =
    canvas.getContext("2d")
          .asInstanceOf[dom.CanvasRenderingContext2D]

  canvas.height = dom.innerHeight
  canvas.width = dom.innerWidth

  var apple = Apple(false, drawAPoint ,None)

  var count = 0
  var snake = Seq(Point(dom.innerWidth / 2, dom.innerHeight / 2),Point((dom.innerWidth / 2) + 5, dom.innerHeight / 2),Point((dom.innerWidth / 2) + 10, dom.innerHeight / 2) )
  val corners = Seq(Point(255, 255), Point(0, 255), Point(128, 0))

  def run(): Unit = {

    if (keysDown(38)) direction = Point(0, -5) //Up
    if (keysDown(37)) direction = Point(-5, 0) // Left
    if (keysDown(39)) direction = Point(5, 0) // Right
    if (keysDown(40)) direction = Point(0, 5) // Down

    val newSnakeHead = snake.head + direction

    val newSnakeTail = {
      if (Math.abs(apple.currLoc.x - newSnakeHead.x) <= 10 && Math.abs(apple.currLoc.y - newSnakeHead.y) <= 10) {
        val previousApple = apple.currLoc
        apple = apple.reSpawnApple
        snake ++ Seq(previousApple)
      } else {
        snake
      }
    }

    snake = newSnakeHead +: {newSnakeTail.init}

  }

  def draw(): Unit = {
    ctx.fillStyle = "black"
    ctx.fillRect(0, 0, canvas.width, canvas.height)

    ctx.fillStyle = "white"

    for (snakePart <- snake){
      ctx.fillRect(snakePart.x - 5, snakePart.y - 5, 10, 10)
    }

    ctx.fillRect(apple.currLoc.x - 5, apple.currLoc.y, 10,10)

    apple.previousLoc.foreach {priviousLoc=>
      ctx.fillStyle = "black"
      ctx.fillRect(priviousLoc.x - 5, priviousLoc.y - 5, 10, 10)
    }


  }

  val keysDown = collection.mutable.Set.empty[Int]
  @JSExport
  def main(): Unit = {

    dom.console.log("main")

    dom.onkeydown = {(e: dom.KeyboardEvent) =>
      keysDown.add(e.keyCode)
    }
    dom.onkeyup = {(e: dom.KeyboardEvent) =>
      keysDown.remove(e.keyCode)
    }
    dom.setInterval(() => {run(); draw()}, 40)
  }
}

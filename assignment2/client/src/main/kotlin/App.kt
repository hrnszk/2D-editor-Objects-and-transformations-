import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.events.*
import org.w3c.dom.Window
import vision.gears.webglmath.Vec3
import vision.gears.webglmath.Vec2

class App(val canvas : HTMLCanvasElement, val overlay : HTMLDivElement) {

  val keysPressed = HashSet<String>()
  var selectedIndex = 0
  var mouseDown = false


  val gl = (canvas.getContext("webgl2", object{val alpha = false}) ?: throw Error("Browser does not support WebGL2")) as WebGL2RenderingContext //#alpha# never make canvas transparent ˙HUN˙ ne legyen áttetsző a vászon  

  val scene = Scene(gl)//#scene# this object is responsible for resource allocation and drawing ˙HUN˙ ez az objektum felel az erőforrások kezeléséért és kirajzolásáért
  init {
    resize()//## we adjust render resolution in a separate method, as we will also need it when the canvas is resized ˙HUN˙ rajzolási felbontás beállítása külön metódusban, mert ablakátméretezéskor is kell majd ugyanez
  }

  fun resize() {
    canvas.width = canvas.clientWidth//#canvas.width# rendering resolution ˙HUN˙ rajzolási felbontás #canvas.clientWidth# canvas size ˙HUN˙ a vászon mérete 
    canvas.height = canvas.clientHeight
    scene.resize(canvas)
  }

  @Suppress("UNUSED_PARAMETER")
  fun registerEventHandlers() {
    document.onkeydown =  { //#{# locally defined function
      event : KeyboardEvent ->

      if(event.key  == "z"){
        scene.zoomIn = true
      }
      if(event.key  == "x"){
        scene.zoomOut = true
      }
      keysPressed.add( event.key )
    }

    document.onkeyup = { 
      event : KeyboardEvent ->

      if(event.key == " "){
        selectedIndex++
      }
      if(event.key == "Backspace"){
        scene.delete = true
      }
      if(event.key == "Enter"){
        if(scene.isRotate){
        scene.isRotate = false}
        else scene.isRotate = true
      }
      if(event.key  == "z"){
        scene.zoomIn = false
      }
      if(event.key  == "x"){
        scene.zoomOut = false
      }

      keysPressed.remove( event.key )
    }

    canvas.onmousedown = { 
      event : MouseEvent ->
      mouseDown = true
      scene.oldMousePos.x = 2 * event.x.toFloat()
      scene.oldMousePos.y = (2 * event.y.toFloat() * (-1)).toFloat()
      event
    }

    canvas.onmousemove = { 
      event : MouseEvent ->
      val mX :  Float = event.asDynamic().movementX
      val mY  : Float = event.asDynamic().movementY

      if(mouseDown){
        scene.mousePosition.x = 2 * mX/canvas.width
        scene.mousePosition.y = (2 * mY/canvas.height) * (-1)

        scene.newMousePos.x = 2 * event.x.toFloat()/canvas.width - 1f
        scene.newMousePos.y = -(2 * event.y.toFloat()/canvas.height - 1f)
      }
      event.stopPropagation()
    }

    canvas.onmouseup = { 
      event : MouseEvent ->
      mouseDown = false
      scene.oldMousePos = Vec3(0f,0f,0f)
      scene.newMousePos = Vec3(0f,0f,0f)
      event // This line is a placeholder for event handling code. It has no effect, but avoids the "unused parameter" warning.
    }

    canvas.onmouseout = { 
      event : Event ->
      event // This line is a placeholder for event handling code. It has no effect, but avoids the "unused parameter" warning.
    }

    window.onresize = { //update the ratio of the display screen whenever  the window ratiois changed
      event: Event ->
      resize()
    }

    window.requestAnimationFrame {//#requestAnimationFrame# trigger rendering
      update()//#update# this method is responsible; for drawing a frame
    }
  }  

  fun update() {
    scene.update(keysPressed,selectedIndex)    
    window.requestAnimationFrame { update() }
  }
}

fun main() {
  val canvas = document.getElementById("canvas") as HTMLCanvasElement
  val overlay = document.getElementById("overlay") as HTMLDivElement
  overlay.innerHTML = """<font color="red">WebGL</font>"""

  try{
    val app = App(canvas, overlay)//#app# from this point on,; this object is responsible; for handling everything
    app.registerEventHandlers()//#registerEventHandlers# we implement this; to make sure the app; knows when there is; something to do
  } catch(e : Error) {
    console.error(e.message)
  }
}
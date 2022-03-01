import org.w3c.dom.HTMLCanvasElement
import org.khronos.webgl.WebGLRenderingContext as GL //# GL# we need this for the constants declared ˙HUN˙ a constansok miatt kell
import kotlin.js.Date
import kotlin.math.atan2
import vision.gears.webglmath.UniformProvider
import vision.gears.webglmath.Vec1
import vision.gears.webglmath.Vec2
import vision.gears.webglmath.Vec3
import vision.gears.webglmath.Mat4
import vision.gears.webglmath.Vec4
import kotlin.math.pow

class Scene (
  val gl : WebGL2RenderingContext)
   {
    var delete = false
    val mousePosition =  Vec3(0f,0f,0f)
    var oldMousePos = Vec3(0f,0f,0f)
    var newMousePos = Vec3(0f,0f,0f)
    var delta =0.0f
    var isRotate = false
    var zoomIn = false
    var zoomOut = false

  val vsTrafo = Shader(gl, GL.VERTEX_SHADER, "trafo-vs.glsl")
  val fsSolid = Shader(gl, GL.FRAGMENT_SHADER, "solid-fs.glsl")
  val solidProgram = Program(gl, vsTrafo, fsSolid)
  
  val  material = Material(solidProgram)
  val yellowMaterial = Material(solidProgram)
  init{
    yellowMaterial["solidColor"]?.set(1.0f,1.0f,0.0f)
  } 

  val triangleGeometry = TriangleGeometry(gl)

  val yellowTriangleMesh = Mesh(yellowMaterial, triangleGeometry)

  
  val camera = OrthoCamera(*Program.all).apply{
    position.set(0f,0f) //focus on triange at (1,1)
    updateViewProjMatrix()
  }

  val gameObjects = ArrayList<GameObject>()


  init{
    gameObjects += GameObject(yellowTriangleMesh).apply{
      position.set(0.5f, 0.5f)
    }
    gameObjects += GameObject(yellowTriangleMesh).apply{
      position.set(-0.5f, 0.5f)
    }
    gameObjects += GameObject(yellowTriangleMesh).apply{
      position.set(0.5f, -0.5f)
    }
    gameObjects += GameObject(yellowTriangleMesh).apply{
      position.set(-0.5f, -0.5f)
    }
    gameObjects += GameObject(yellowTriangleMesh).apply{
      position.set(0.0f, 0.0f)
    }
  }


  fun resize(canvas : HTMLCanvasElement) {
    gl.viewport(0, 0, canvas.width, canvas.height)//#viewport# tell the rasterizer which part of the canvas to draw to ˙HUN˙ a raszterizáló ide rajzoljon
    camera.setAspectRatio(canvas.width.toFloat()/canvas.height)
  }

  val timeAtFirstFrame = Date().getTime()
  var timeAtLastFrame =  timeAtFirstFrame


  @Suppress("UNUSED_PARAMETER")
  fun update(keysPressed : Set<String>, selectedIndex : Int) {

    val timeAtThisFrame = Date().getTime() 
    val dt = (timeAtThisFrame - timeAtLastFrame).toFloat() / 1000.0f
    val t = (timeAtThisFrame - timeAtFirstFrame).toFloat() / 1000.0f
    timeAtLastFrame = timeAtThisFrame


    gl.clearColor(0.3f, 0.0f, 0.3f, 1.0f)//## red, green, blue, alpha in [0, 1]
    gl.clearDepth(1.0f)//## will be useful in 3D ˙HUN˙ 3D-ben lesz hasznos
    gl.clear(GL.COLOR_BUFFER_BIT or GL.DEPTH_BUFFER_BIT)//#or# bitwise OR of flags

    var sizeOfObjects = gameObjects.size

    if(delete){
      gameObjects.removeAt(selectedIndex%sizeOfObjects)
      delete = false
    }
    sizeOfObjects = gameObjects.size


    val selectedGameObject = gameObjects[selectedIndex%sizeOfObjects]
    val pivot = selectedGameObject.position.xy

    var pivotWorldPosition = Vec4(pivot.x, pivot.y, 0f,1f) * camera.viewProjMatrixInverse
    var mouseWorldPosition = Vec4(mousePosition.x, mousePosition.y, 0f, 1f) * camera.viewProjMatrixInverse
    var newMouseWorldPosition = Vec4(newMousePos.x, newMousePos.y, 0f, 1f) * camera.viewProjMatrixInverse
    var oldMouseWorldPosition = Vec4(oldMousePos.x, oldMousePos.y, 0f, 1f) * camera.viewProjMatrixInverse


    if(!isRotate){
    selectedGameObject.position+=Vec3(mouseWorldPosition.x, mouseWorldPosition.y, 0.0f)
    mousePosition.x = 0.0f
    mousePosition.y = 0.0f
    mouseWorldPosition.x = 0.0f
    mouseWorldPosition.y = 0.0f
    }

    if(isRotate){
    delta = atan2(newMouseWorldPosition.y-pivotWorldPosition.y, newMouseWorldPosition.x-pivotWorldPosition.x) - atan2(oldMouseWorldPosition.y-pivotWorldPosition.y, oldMouseWorldPosition.x-pivotWorldPosition.x)
    selectedGameObject.roll += delta
    }


    if(zoomIn){
      camera.windowSize *= (0.5f).pow(dt)
      camera.updateViewProjMatrix()
    }

    if(zoomOut){
      camera.windowSize *= (1.5f).pow(dt)
      camera.updateViewProjMatrix()
    }



    gameObjects.forEach{
      it.move(dt, t, keysPressed, gameObjects)
    }

    gameObjects.forEach{
      it.update()
    }

    gameObjects.forEach{
      it.draw()
    }

    
    oldMousePos.set(newMousePos)

    }
}

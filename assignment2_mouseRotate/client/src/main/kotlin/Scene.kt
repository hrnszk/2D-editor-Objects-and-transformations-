import org.w3c.dom.HTMLCanvasElement
import org.khronos.webgl.WebGLRenderingContext as GL //# GL# we need this for the constants declared ˙HUN˙ a constansok miatt kell
import kotlin.js.Date
import kotlin.math.atan2
import vision.gears.webglmath.UniformProvider
import vision.gears.webglmath.Vec1
import vision.gears.webglmath.Vec2
import vision.gears.webglmath.Vec3
import vision.gears.webglmath.Mat4

class Scene (
  val gl : WebGL2RenderingContext)
   {
    var oldMousePos = Vec3(0f,0f,0f)
    var newMousePos = Vec3(0f,0f,0f)

    var delta =0.0f

  val vsTrafo = Shader(gl, GL.VERTEX_SHADER, "trafo-vs.glsl")
  val fsSolid = Shader(gl, GL.FRAGMENT_SHADER, "solid-fs.glsl")
  val solidProgram = Program(gl, vsTrafo, fsSolid)
  
  val  material = Material(solidProgram)
  val yellowMaterial = Material(solidProgram)
  init{
    yellowMaterial["solidColor"]?.set(1.0f,1.0f,0.0f)
  } 
  val redMaterial = Material(solidProgram)
  init{
    redMaterial["solidColor"]?.set(1.0f,0.0f,0.0f)
  }

  val triangleGeometry = TriangleGeometry(gl)

  val yellowTriangleMesh = Mesh(yellowMaterial, triangleGeometry)
  val redTriangleMesh = Mesh(redMaterial, triangleGeometry)

  
  val camera = OrthoCamera(*Program.all).apply{
    position.set(1f,1f) //focus on triange at (1,1)
    updateViewProjMatrix()
  }

  val gameObjects = ArrayList<GameObject>()
    
  val avatar = object : GameObject(redTriangleMesh){ 
        override fun move(
      dt: Float,
      t :Float,
      keysPressed : Set<String>,
      gameObjects : List<GameObject>
    ): Boolean{
      roll += dt
      return true
    }
  }
      

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


  @Suppress("UNUSED_PARAMETER")
  fun update(keysPressed : Set<String>, selectedIndex : Int) {

    
    gl.clearColor(0.3f, 0.0f, 0.3f, 1.0f)//## red, green, blue, alpha in [0, 1]
    gl.clearDepth(1.0f)//## will be useful in 3D ˙HUN˙ 3D-ben lesz hasznos
    gl.clear(GL.COLOR_BUFFER_BIT or GL.DEPTH_BUFFER_BIT)//#or# bitwise OR of flags

    val selectedGameObject = gameObjects[selectedIndex%5]
    val pivot = selectedGameObject.position.xy

    delta = atan2(newMousePos.y-pivot.y, newMousePos.x-pivot.x) - atan2(oldMousePos.y-pivot.y, oldMousePos.x-pivot.x)
    selectedGameObject.roll += delta
    
    selectedGameObject.update()
    selectedGameObject.draw()

    oldMousePos.set(newMousePos)
    }
}

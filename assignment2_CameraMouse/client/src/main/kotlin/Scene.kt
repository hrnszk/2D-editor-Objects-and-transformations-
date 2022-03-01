import org.w3c.dom.HTMLCanvasElement
import org.khronos.webgl.WebGLRenderingContext as GL //# GL# we need this for the constants declared ˙HUN˙ a constansok miatt kell
import kotlin.js.Date
import vision.gears.webglmath.UniformProvider
import vision.gears.webglmath.Vec1
import vision.gears.webglmath.Vec2
import vision.gears.webglmath.Vec3
import vision.gears.webglmath.Mat4
import vision.gears.webglmath.Vec4

class Scene (
  val gl : WebGL2RenderingContext)
  
   {
    val mousePosition =  Vec4(0f,0f,0f,1f)

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


      val camera = OrthoCamera(*Program.all).apply{
        position.set(0f,0f)
        updateViewProjMatrix()
      }


  fun resize(canvas : HTMLCanvasElement) {
    gl.viewport(0, 0, canvas.width, canvas.height)//#viewport# tell the rasterizer which part of the canvas to draw to ˙HUN˙ a raszterizáló ide rajzoljon
    camera.setAspectRatio(canvas.width.toFloat()/canvas.height)
  }


  @Suppress("UNUSED_PARAMETER")
  fun update(keysPressed : Set<String>, selectedIndex : Int) {
    
    camera.updateViewProjMatrix()
    var mouseWorldPosition = mousePosition * camera.viewProjMatrixInverse


    gl.clearColor(0.3f, 0.0f, 0.3f, 1.0f)//## red, green, blue, alpha in [0, 1]
    gl.clearDepth(1.0f)//## will be useful in 3D ˙HUN˙ 3D-ben lesz hasznos
    gl.clear(GL.COLOR_BUFFER_BIT or GL.DEPTH_BUFFER_BIT)//#or# bitwise OR of flags


    val selectedGameObject = gameObjects[selectedIndex%5]
    
    selectedGameObject.position+=Vec3(mouseWorldPosition.x, mouseWorldPosition.y, 0.0f)
    mouseWorldPosition = Vec4(0f,0f,0f,0f)
    selectedGameObject.update()
    selectedGameObject.draw()

    }
}
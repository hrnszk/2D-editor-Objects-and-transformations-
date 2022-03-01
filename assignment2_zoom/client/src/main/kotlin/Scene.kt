import org.w3c.dom.HTMLCanvasElement
import org.khronos.webgl.WebGLRenderingContext as GL //# GL# we need this for the constants declared ˙HUN˙ a constansok miatt kell
import kotlin.js.Date
import vision.gears.webglmath.UniformProvider
import vision.gears.webglmath.Vec1
import vision.gears.webglmath.Vec2
import vision.gears.webglmath.Vec3
import vision.gears.webglmath.Vec4
import vision.gears.webglmath.Mat4
import kotlin.math.pow

class Scene (
  val gl : WebGL2RenderingContext)
  //TODO: derive from UniformProvider
   {

    var zoomIn = false
    var zoomOut = false

  val vsTrafo = Shader(gl, GL.VERTEX_SHADER, "trafo-vs.glsl")
  val fsSolid = Shader(gl, GL.FRAGMENT_SHADER, "solid-fs.glsl")
  val solidProgram = Program(gl, vsTrafo, fsSolid)
  
  //TODO: create various materials with different solidColor settings
  val  material = Material(solidProgram)
  val yellowMaterial = Material(solidProgram)
  init{
    yellowMaterial["solidColor"]?.set(1.0f,1.0f,0.0f)
  } 

  val triangleGeometry = TriangleGeometry(gl)
  //TODO: create various meshes using combinations of materials and geometries
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
  fun update(keysPressed : Set<String>) {
    val timeAtThisFrame = Date().getTime() 
    val dt = (timeAtThisFrame - timeAtLastFrame).toFloat() / 1000.0f
    val t = (timeAtThisFrame - timeAtFirstFrame).toFloat() / 1000.0f
    timeAtLastFrame = timeAtThisFrame
    
    gl.clearColor(0.3f, 0.0f, 0.3f, 1.0f)//## red, green, blue, alpha in [0, 1]
    gl.clearDepth(1.0f)//## will be useful in 3D ˙HUN˙ 3D-ben lesz hasznos
    gl.clear(GL.COLOR_BUFFER_BIT or GL.DEPTH_BUFFER_BIT)//#or# bitwise OR of flags

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
      // gameObject ->
      // gameObject.draw() //replaces all the code below
    }

    gameObjects.forEach{ //for all the game Objects
      it.update() //the same thing as below
      // gameObject ->
      // gameObject.update()
    }

    gameObjects.forEach{
      it.draw(camera)
      // gameObject ->
      // gameObject.draw() //replaces all the code below
    }

    //avatar.roll += dt
    //avatar.draw()

    // gl.useProgram(solidProgram.glProgram) //1.set  program 2.set uniforms 3.draw, but 1 is missing bc  we included  the  information in Mesh
    // //yellowMaterial.draw()
    // Mat4().commit(gl, gl.getUniformLocation(
    //   solidProgram.glProgram, "gameObject.modelMatrix") //set the position for the game object?
    //   ?: throw Error("no modelMatrix"))
    // //triangleGeometry.draw()
    // yellowTriangleMesh.draw() //combine material and  geometry

    // //redMaterial.draw()
    // Mat4().translate(Vec2(0.5f,-0.3f)).commit(gl, gl.getUniformLocation(
    //   solidProgram.glProgram, "gameObject.modelMatrix")
    //   ?: throw Error("no modelMatrix"))
    // //triangleGeometry.draw()
    // redTriangleMesh.draw()//combine material and  geometry
  }
}

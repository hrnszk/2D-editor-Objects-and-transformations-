import vision.gears.webglmath.*
import kotlin.math.exp
import kotlin.math.PI
import kotlin.math.floor

open class GameObject(
  vararg val meshes : Mesh
   ) : UniformProvider("gameObject") { //provides gameObject.__

  val position = Vec3()
  var roll = 0.0f
  val scale = Vec3(1.0f, 1.0f, 1.0f)

  val modelMatrix by Mat4() //make sure kotlin has Matrix

  init { 
    addComponentsAndGatherUniforms(*meshes)
  }

  fun update() {
    modelMatrix.set(). //this["modelMatrix"]?.set()  is no longer necessary bc we have modelMatrix byMat4()
      scale(scale).
      rotate(roll). //rotation around z-axis
      translate(position)
  }

  open fun move(dt : Float = 0.016666f, t : Float = 0.0f, keysPressed : Set<String> = emptySet<String>(), gameObjects : List<GameObject> = emptyList<GameObject>()) : Boolean {
    return true
  }


}


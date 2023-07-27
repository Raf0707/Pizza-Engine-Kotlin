package pize.io.joystick

import org.lwjgl.glfw.GLFW
import java.nio.ByteBuffer
import java.nio.FloatBuffer

class Joystick(private val id: Int) {
    val name: String?
    private val axes: FloatBuffer?
    private val buttons: ByteBuffer?
    private val hats: ByteBuffer?

    init {
        name = GLFW.glfwGetJoystickName(id)
        axes = GLFW.glfwGetJoystickAxes(id)
        buttons = GLFW.glfwGetJoystickButtons(id)
        hats = GLFW.glfwGetJoystickHats(id)

        // GLFWGamepadState state = new GLFWGamepadState();
        // state.set(buttons, axes);
        // glfwGetGamepadState(GLFW_JOYSTICK_3, state);
        // {
        //     if (state.buttons[GLFW_GAMEPAD_BUTTON_A])
        //     {
        //         input_jump();
        //     }
        //
        //     input_speed(state.axes[GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER]);
        // }
    }

    var userPointer: Long
        get() = GLFW.glfwGetJoystickUserPointer(id)
        set(pointer) {
            GLFW.glfwSetJoystickUserPointer(id, pointer)
        }
    val isGamepad: Boolean
        get() = GLFW.glfwJoystickIsGamepad(id)
    val iD: Long
        get() = id.toLong()
}

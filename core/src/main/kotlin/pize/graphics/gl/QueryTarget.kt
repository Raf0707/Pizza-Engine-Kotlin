package pize.graphics.gl

import org.lwjgl.opengl.*

enum class QueryTarget(val GL: Int) {
    SAMPLES_PASSED(GL15.GL_SAMPLES_PASSED),
    ANY_SAMPLES_PASSED(GL33.GL_ANY_SAMPLES_PASSED),
    ANY_SAMPLES_PASSED_CONSERVATIVE(GL43.GL_ANY_SAMPLES_PASSED_CONSERVATIVE),
    PRIMITIVES_GENERATED(GL30.GL_PRIMITIVES_GENERATED),
    TRANSFORM_FEEDBACK_PRIMITIVES_WRITTEN(GL30.GL_TRANSFORM_FEEDBACK_PRIMITIVES_WRITTEN),
    TIME_ELAPSED(GL33.GL_TIME_ELAPSED),
    SAMPLES_PASSED_ARB(ARBOcclusionQuery.GL_SAMPLES_PASSED_ARB)
}

package pize.devtests;

import pize.Pize;
import pize.app.AppAdapter;
import pize.graphics.camera.PerspectiveCamera;
import pize.graphics.camera.controller.Rotation3DController;
import pize.graphics.gl.Gl;
import pize.graphics.gl.Target;
import pize.graphics.gl.Type;
import pize.graphics.util.BaseShader;
import pize.graphics.util.SkyBox;
import pize.graphics.vertex.Mesh;
import pize.graphics.vertex.VertexAttr;
import pize.io.glfw.Key;
import pize.math.Maths;
import pize.math.vecmath.vector.Vec3f;
import pize.math.vecmath.vector.Vec3i;

public class QuadFromNormalTest extends AppAdapter{

    SkyBox skyBox;

    BaseShader shader;
    PerspectiveCamera camera;
    Rotation3DController rotationController;

    Mesh quadMesh;

    public void init(){
        // Camera
        camera = new PerspectiveCamera(0.5F, 500, 70);
        camera.position.y += 3;
        rotationController = new Rotation3DController();
        // Skybox
        skyBox = new SkyBox();
        // Shader
        shader = BaseShader.getPos3UColor();
        // Mesh

        quadMesh = new Mesh(new VertexAttr(3, Type.FLOAT));
        generateFromBlockFace(new Vec3i(0, 1, 0));
        quadMesh.setIndices(new int[]{
            0, 1, 2,
            2, 3, 0
        });

        // Disable cull face
        Gl.disable(Target.CULL_FACE);
    }

    public void update(){
        // Exit & Fullscreen
        if(Key.ESCAPE.isDown())
            Pize.exit();
        if(Key.F11.isDown())
            Pize.window().toggleFullscreen();

        // Camera control
        final Vec3f cameraMotion = new Vec3f();
        final float yawSin = Maths.sinDeg(camera.rotation.yaw);
        final float yawCos = Maths.cosDeg(camera.rotation.yaw);
        if(Key.W.isPressed())
            cameraMotion.add(yawCos, 0, yawSin);
        if(Key.S.isPressed())
            cameraMotion.add(-yawCos, 0, -yawSin);
        if(Key.A.isPressed())
            cameraMotion.add(-yawSin, 0, yawCos);
        if(Key.D.isPressed())
            cameraMotion.add(yawSin, 0, -yawCos);
        if(Key.SPACE.isPressed())
            cameraMotion.y++;
        if(Key.LEFT_SHIFT.isPressed())
            cameraMotion.y--;

        // Camera update
        camera.position.add(cameraMotion.mul(Pize.getDt() * 2));
        rotationController.update();
        camera.rotation.set(rotationController.rotation);
        camera.update();

    }

    public void render(){
        // Clear color
        Gl.clearColorBuffer();
        Gl.clearColor(0.2, 0.2, 0.22);

        // Render skybox
        skyBox.render(camera);

        // Render mesh & ray
        shader.bind();
        shader.setMatrices(camera);
        shader.setColor(1, 0, 0);
        quadMesh.render();
    }


    public void generateFromBlockFace(Vec3i normal){
        // Calculate positions (N - normal, p(1-4) - vertex position)

        //   p1 ------- p4
        //   |           |
        //   |     N     |
        //   |           |
        //   p2 ------- p3

        // Calc
        final Vec3f p4 = new Vec3f(1).sub(normal);  // calc p4
        final Vec3f p2 = p4.copy().mul(-1);         // inv p4
        final Vec3f p1 = new Vec3f(normal).crs(p4); // crs n*p4
        final Vec3f p3 = new Vec3f(normal).crs(p2); // crs n*p2

        System.out.println(new Vec3f(1, 0, 1).crs(new Vec3f(0, 1, 0)));
        System.out.println(p1);
        System.out.println(p2);
        System.out.println(p3);
        System.out.println(p4);

        // Scale, shift
        normal.mul(0.5).add(0.5);

        p1.mul(0.5).add(normal);
        p2.mul(0.5).add(normal);
        p3.mul(0.5).add(normal);
        p4.mul(0.5).add(normal);

        // Set vertices
        quadMesh.setVertices(new float[]{
            p1.x, p1.y, p1.z, // 0
            p2.x, p2.y, p2.z, // 1
            p3.x, p3.y, p3.z, // 2
            p4.x, p4.y, p4.z, // 3
        });
    }

}

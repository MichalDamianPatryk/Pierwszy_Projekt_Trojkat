package com.example.micha.trojkat;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by MIchał on 2017-10-17.
 */

public class Triangle{

    static final int VERTEX_POS_SIZE = 3;
    //COLOR_SIZE 3
    static final int COLOR_SIZE = 4;

    static final int VERTEX_ATTRIB_SIZE = VERTEX_POS_SIZE;
    static final int COLOR_ATTRIB_SIZE = COLOR_SIZE;

    private final int VERTEX_COUNT = triangleData.length / VERTEX_ATTRIB_SIZE;

    private FloatBuffer vertexDataBuffer;
    private FloatBuffer colorDataBuffer;

    static float triangleData[] = {   // in counterclockwise order:
            0.0f,  0.622008459f, 0.0f, // top
            -0.5f, -0.311004243f, 0.0f, // bottom left
            0.5f, -0.311004243f, 0.0f  // bottom right
    };

    static float colorData[] = {   // in counterclockwise order:
            1.0f, 0.0f, 0.0f, 1.0f, // Red
            0.0f, 1.0f, 0.0f, 1.0f, // Green
            0.0f, 0.0f, 1.0f, 1.0f// Blue
    };

    // Set color with red, green, blue and alpha (opacity) values
    float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };

    private final int mProgram;

    private final String vertexShaderCode =
            "attribute vec4 vPosition; \n" +
                    "attribute vec4 vColor; \n" +
                    "uniform mat4 uMVPMatrix;\n" +
                    "varying vec4 c; \n" +
                    "void main() { \n" +
                    "  c = vColor; \n" +
                    "  gl_Position = vPosition;\n" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;\n" +
                    "varying vec4 c;\n" +
                    "void main() {\n" +
                    "  gl_FragColor = c;\n" +
                    "}";



    private int positionHandle;
    private int colorHandle;

    public Triangle() {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bbv = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                triangleData.length * 4);
        // use the device hardware's native byte order
        bbv.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexDataBuffer = bbv.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexDataBuffer.put(triangleData);
        // set the buffer to read the first coordinate
        vertexDataBuffer.position(0);

        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bbc = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                colorData.length * 4);
        // use the device hardware's native byte order
        bbc.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        colorDataBuffer = bbc.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        colorDataBuffer.put(colorData);
        // set the buffer to read the first coordinate
        colorDataBuffer.position(0);

        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(mProgram, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(mProgram, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);

    }

    public void draw() {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);


        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(positionHandle, VERTEX_POS_SIZE,
                GLES20.GL_FLOAT, false,
                VERTEX_ATTRIB_SIZE * 4, vertexDataBuffer);

        colorHandle = GLES20.glGetAttribLocation(mProgram, "vColor");
        GLES20.glEnableVertexAttribArray(colorHandle);

        GLES20.glVertexAttribPointer(colorHandle, COLOR_SIZE,
                GLES20.GL_FLOAT, false,
                COLOR_ATTRIB_SIZE * 4, colorDataBuffer);

        // Pass the projection and view transformation to the shader
        GLES20.glUniform4fv(colorHandle, 1, colorData, 0);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, VERTEX_COUNT);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(colorHandle);

    }
}
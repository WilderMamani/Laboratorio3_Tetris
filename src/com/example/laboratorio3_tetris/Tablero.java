package com.example.laboratorio3_tetris;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Tablero {

	private float vertices[] = new float [] {
			0, 0,
			10, 0,
			10, 20,
			0, 20
	};
	
	FloatBuffer bufVertices;
	
	public Tablero() {

		ByteBuffer bufByte = ByteBuffer.allocateDirect(vertices.length * 4);
		bufByte.order(ByteOrder.nativeOrder());
		bufVertices = bufByte.asFloatBuffer();
		bufVertices.put(vertices);
		bufVertices.rewind(); 

	}
	public void dibuja(GL10 gl) {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, bufVertices);
		
		gl.glColor4f(0, 0, 1, 0);
		gl.glLineWidth(3);
		gl.glDrawArrays(GL10.GL_LINE_LOOP, 0, 4);
		gl.glScalef(1.04f, 1.02f, 0);
		gl.glTranslatef(-0.2f, -0.2f, 0);
		gl.glColor4f(0, 0, 1, 0);
		gl.glLineWidth(2);
		gl.glDrawArrays(GL10.GL_LINE_LOOP, 0, 4);
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
}

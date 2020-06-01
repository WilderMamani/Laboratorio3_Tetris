package com.example.laboratorio3_tetris;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;
public class Pieza {
	/*USANDO TRIANGLE FAN*/

	private float vertices[] = new float [] {
			 0, 0,		//0		
			 1, 0,		//1
			 1, 1,		//2
			 0, 1		//3
	};
	
	FloatBuffer bufVertices;
	
	public Pieza() {

		ByteBuffer bufByte = ByteBuffer.allocateDirect(vertices.length * 4);
		bufByte.order(ByteOrder.nativeOrder());
		bufVertices = bufByte.asFloatBuffer();
		bufVertices.put(vertices);
		bufVertices.rewind(); 

	}
	public void dibuja(GL10 gl) {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, bufVertices);

		gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);					/////////////////////////////////////////////////////////
		gl.glColor4f(0, 0, 0, 1);
		gl.glLineWidth(2);
		gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, 4);
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
}
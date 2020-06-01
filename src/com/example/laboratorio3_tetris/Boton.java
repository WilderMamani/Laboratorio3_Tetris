package com.example.laboratorio3_tetris;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Boton {

	private float vertices[] = new float [] {
			-1, -1,
			 1, -1,
			 0,	 1
	};
	
	FloatBuffer bufVertices;
	int tipo ;
	public Boton() {
		ByteBuffer bufByte = ByteBuffer.allocateDirect(vertices.length * 4);
		bufByte.order(ByteOrder.nativeOrder());
		bufVertices = bufByte.asFloatBuffer();
		bufVertices.put(vertices);
		bufVertices.rewind();
	}
	public void setColor(int tipo, GL10 gl){
		switch(tipo){
		case 0: gl.glColor4f(1, 1, 0, 0); break;
		case 1: gl.glColor4f(1, 0, 0, 0); break;
		case 2: gl.glColor4f(0, 1, 0, 0); break;
		case 3: gl.glColor4f(0, 0, 1, 0); break;
		case 4: gl.glColor4f(1, 0, 1, 0); break;
		}
	}
	public void dibuja(GL10 gl) {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, bufVertices);

//		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);gl.glColor4f(0, 0, 0, 1);
//		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, 4);
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
}

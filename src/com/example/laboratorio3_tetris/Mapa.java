package com.example.laboratorio3_tetris;

import javax.microedition.khronos.opengles.GL10;

public class Mapa {
	private Pieza pieza;
	private char ubicacion[][] = new char[24][10];
	
	private char[] fichas = {'I','J','L','O','S','T','Z'};
	private String[] colores = {"cyan","azul","naranja","amarrillo","verde","morado","rojo"};
	
	private GL10 gl;
	public Mapa(GL10 gl){
		this.gl = gl;
		pieza = new Pieza();
		for(int i=0; i<24; i++){
			for(int j=0; j<10; j++){
				ubicacion[i][j] = '_';
			}
		}
//		ubicacion[0][0] = 'I';
//		ubicacion[0][1] = 'I';
////		ubicacion[0][2] = 'I';
////		ubicacion[0][3] = 'I';
//		ubicacion[0][4] = 'I';
//		ubicacion[0][5] = 'I';
//		ubicacion[0][7] = 'I';
//		ubicacion[0][8] = 'I';
//		ubicacion[0][9] = 'I';
//		ubicacion[1][0] = 'I';
//		ubicacion[1][1] = 'I';
//		ubicacion[1][2] = 'I';
//		ubicacion[1][3] = 'I';
//		ubicacion[1][4] = 'I';
//		ubicacion[1][7] = 'I';
//		ubicacion[1][8] = 'I';
//		ubicacion[1][9] = 'I';
//		ubicacion[2][0] = 'I';
//		ubicacion[2][1] = 'I';
//		ubicacion[2][2] = 'I';
//		ubicacion[2][3] = 'I';
//		ubicacion[2][4] = 'I';
//		ubicacion[2][7] = 'I';
//		ubicacion[2][8] = 'I';
//		ubicacion[2][9] = 'I';
//		ubicacion[3][0] = 'I';
//		ubicacion[3][1] = 'I';
//		ubicacion[3][2] = 'I';
//		ubicacion[3][3] = 'I';
//		ubicacion[3][4] = 'I';
//		ubicacion[3][7] = 'I';
//		ubicacion[3][8] = 'I';
//		ubicacion[3][9] = 'I';
//		ubicacion[4][0] = 'I';
//		ubicacion[4][1] = 'I';
//		ubicacion[4][2] = 'I';
//		ubicacion[4][3] = 'I';
//		ubicacion[4][4] = 'I';
//		ubicacion[4][7] = 'I';
//		ubicacion[4][8] = 'I';
//		ubicacion[4][9] = 'I';
//		ubicacion[5][0] = 'I';
//		ubicacion[5][1] = 'I';
//		ubicacion[5][2] = 'I';
//		ubicacion[5][3] = 'I';
//		ubicacion[5][4] = 'I';
//		ubicacion[5][7] = 'I';
//		ubicacion[5][8] = 'I';
//		ubicacion[5][9] = 'I';
	}
	public void setColor(char c){
		switch(c){
			case 'i':
			case 'I': gl.glColor4f(0, 1, 1, 0);break;
			case 'j':
			case 'J': gl.glColor4f(0, 0, 1, 0);break;
			case 'l':
			case 'L': gl.glColor4f(1, 0.5f, 0, 0);break;
			case 'o':
			case 'O': gl.glColor4f(1, 1, 0, 0);break;
			case 's':
			case 'S': gl.glColor4f(0, 1, 0, 0);break;
			case 't':
			case 'T': gl.glColor4f(1, 0, 1, 0);break;
			case 'z':
			case 'Z': gl.glColor4f(1, 0, 0, 0);break;
			default: gl.glColor4f(0, 0, 0, 0);break;
		}
	}
	public void dibuja(){
		for(int i=0; i<20; i++){
			for(int j=0; j<10; j++){
				gl.glLoadIdentity();
				setColor(ubicacion[i][j]);
				gl.glTranslatef(j, i, 0);
				pieza.dibuja(gl);
			}
		}
	}
	public char[][] getUbicacion() {
		return ubicacion;
	}
	public void setUbicacion(char[][] ubicacion) {
		this.ubicacion = ubicacion;
	}
}
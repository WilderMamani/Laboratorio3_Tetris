package com.example.laboratorio3_tetris;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.util.Log;
import android.view.MotionEvent;

public class Renderiza extends GLSurfaceView implements Renderer {
	
	private Pieza pieza;
	private Boton boton;
	Context contexto;
	private Mapa mapa;
	private int alto, ancho;
	private boolean insertar;
	private boolean mover;
	private int x1, x2, x3, x4;
	private int y1, y2, y3, y4;
	private int ax1, ax2, ax3, ax4;
	private int ay1, ay2, ay3, ay4;
	
	private char[] fichas = {'i','j','l','o','s','t','z'};
	private char color, siguiente;
	
	private char[][] ubicacion;
	
	private long inicio, fin, duracion;
	private float tiempo_real;
	private float tiempoRotacion;
	private float PERIODO = 0.8f; 
	
	private boolean flag;
	int xRotado, yRotado;
	private Tablero tablero;
	public Renderiza(Context context) {
		super(context);
		boton = new Boton();
		this.contexto = context;
		this.setRenderer(this);
		this.requestFocus();
		this.setFocusableInTouchMode(true);
		this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig arg1) {
		pieza = new Pieza();
		mapa = new Mapa(gl);
		tablero = new Tablero();
		ubicacion = mapa.getUbicacion();
		insertar = true;
		flag = false;
		xRotado = 0; yRotado = 0;
		siguiente = fichas[(int)(Math.random()*7)];
		inicio = System.currentTimeMillis();
		tiempoRotacion = PERIODO;
		gl.glClearColor(0, 0, 0, 0);
	}
	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		mapa.dibuja();
		gl.glPushMatrix();
		gl.glLoadIdentity();
		tablero.dibuja(gl);
		gl.glPopMatrix();
		gl.glLoadIdentity();
		dibujarBotones(gl);
//		for(int i = 0; i<5; i++){
//			boton.setColor(i, gl);
//			boton.dibuja(gl);
//			gl.glTranslatef(2, 0, 0);
//		}
		gl.glLoadIdentity();
		if(insertar){
			insertar = false;
			mover = true;
			color  = siguiente;
			siguiente = fichas[(int)(Math.random()*7)];
			verificaEliminarLinea();
			insertarFicha();
			dibujarSiguiente(gl);
		}else{
			dibujarSiguiente(gl);
			bajarFicha();
//			actualizar();
		}
		
		//imprimir();
	}
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		ancho = width;
		alto = height;
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluOrtho2D(gl, -2, 12, -2, 22);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e){
		if(mover){
			float x = e.getX();
			float y = e.getY();
			float posx, posy;
			if (e.getAction() == MotionEvent.ACTION_DOWN) {
				//descenderFichas();
				posx = (x * 14/ (float) ancho)-2;
				posy = (24 - y * 24 / (float) alto)-2;
	//			Toast.makeText(contexto, "POSICION"+"X:"+posx+" Y:"+posy, Toast.LENGTH_SHORT).show();
				if (puntoEstaDentroDelRectangulo(posx, posy, 0, -2, 2, 2)){
					if(x1>0 && x2>0 && x3>0 && x4>0){
						moverIzq();
						tiempoRotacion = PERIODO;
						insertar = false;
					}
				}
				if (puntoEstaDentroDelRectangulo(posx, posy, 3, -2, 2, 2)){
					rotar();
					tiempoRotacion = PERIODO;
					insertar = false;
				}
				if (puntoEstaDentroDelRectangulo(posx, posy, 5, -2, 2, 2)){
					PERIODO = 0.05f;
				}
				if (puntoEstaDentroDelRectangulo(posx, posy, 8, -2, 2, 2)){
					if(x1<9 && x2<9 && x3<9 && x4<9){
						moverDer();
						tiempoRotacion = PERIODO;
						insertar = false;
					}
				}
				if (puntoEstaDentroDelRectangulo(posx, posy, -2, 18, 2, 2)){
					reiniciar();					
				}
			}
			if(e.getAction() == MotionEvent.ACTION_UP){
				PERIODO = 0.8f;
			}
		}
		return true;
	}
	public void dibujarBotones(GL10 gl){
		gl.glLoadIdentity();
		boton.setColor(0, gl);
		gl.glTranslatef(1, -1, 0);
		gl.glRotatef(90, 0, 0, 1);
		gl.glScalef(0.8f, 0.8f, 0);
		boton.dibuja(gl);
		
		gl.glLoadIdentity();
		boton.setColor(1, gl);
		gl.glTranslatef(4, -1, 0);
		gl.glScalef(0.8f, 0.8f, 0);
		boton.dibuja(gl);
		
		gl.glLoadIdentity();
		boton.setColor(2, gl);
		gl.glTranslatef(6, -1, 0);
		gl.glRotatef(180, 0, 0, 1);
		gl.glScalef(0.8f, 0.8f, 0);
		boton.dibuja(gl);
		
		gl.glLoadIdentity();
		boton.setColor(3, gl);
		gl.glTranslatef(9, -1, 0);
		gl.glRotatef(-90, 0, 0, 1);
		gl.glScalef(0.8f, 0.8f, 0);
		boton.dibuja(gl);
		
		gl.glLoadIdentity();
		boton.setColor(4, gl);
//		gl.glTranslatef(9.5f, -1, 0);
		gl.glTranslatef(-0.7f, 19, 0);
		gl.glRotatef(90, 0, 0, 1);
		gl.glScalef(1, 0.5f, 1);
		gl.glScalef(0.8f, 0.8f, 0);
		boton.dibuja(gl);
		gl.glTranslatef(0, 2, 0);
		boton.dibuja(gl);
		
	}
	public void dibujarSiguiente(GL10 gl){
		switch(siguiente){
		case 'i':
			mapa.setColor(siguiente);
			gl.glTranslatef(3, 20.2f, 0);
			gl.glScalef(0.8f, 0.8f, 0);
			pieza.dibuja(gl);
			mapa.setColor(siguiente);
			gl.glTranslatef(1, 0, 0);
			pieza.dibuja(gl);
			mapa.setColor(siguiente);
			gl.glTranslatef(1, 0, 0);
			pieza.dibuja(gl);
			mapa.setColor(siguiente);
			gl.glTranslatef(1, 0, 0);
			pieza.dibuja(gl);
			break;
		case 'j':
			mapa.setColor(siguiente);
			gl.glTranslatef(3, 21.2f, 0);
			gl.glScalef(0.8f, 0.8f, 0);
			pieza.dibuja(gl);
			mapa.setColor(siguiente);
			gl.glTranslatef(1, 0, 0);
			pieza.dibuja(gl);
			mapa.setColor(siguiente);
			gl.glTranslatef(1, 0, 0);
			pieza.dibuja(gl);
			mapa.setColor(siguiente);
			gl.glTranslatef(0, -1, 0);
			pieza.dibuja(gl);
			break;
		case 'l':
			mapa.setColor(siguiente);
			gl.glTranslatef(3, 21.2f, 0);
			gl.glScalef(0.8f, 0.8f, 0);
			pieza.dibuja(gl);
			mapa.setColor(siguiente);
			gl.glTranslatef(1, 0, 0);
			pieza.dibuja(gl);
			mapa.setColor(siguiente);
			gl.glTranslatef(1, 0, 0);
			pieza.dibuja(gl);
			mapa.setColor(siguiente);
			gl.glTranslatef(-2, -1, 0);
			pieza.dibuja(gl);
			break;
		case 'o':
			mapa.setColor(siguiente);
			gl.glTranslatef(4, 20.2f, 0);
			gl.glScalef(0.8f, 0.8f, 0);
			pieza.dibuja(gl);
			mapa.setColor(siguiente);
			gl.glTranslatef(1, 0, 0);
			pieza.dibuja(gl);
			mapa.setColor(siguiente);
			gl.glTranslatef(0, 1, 0);
			pieza.dibuja(gl);
			mapa.setColor(siguiente);
			gl.glTranslatef(-1, 0, 0);
			pieza.dibuja(gl);
			break;
		case 't':
			mapa.setColor(siguiente);
			gl.glTranslatef(3, 21.2f, 0);
			gl.glScalef(0.8f, 0.8f, 0);
			pieza.dibuja(gl);
			mapa.setColor(siguiente);
			gl.glTranslatef(1, 0, 0);
			pieza.dibuja(gl);
			mapa.setColor(siguiente);
			gl.glTranslatef(1, 0, 0);
			pieza.dibuja(gl);
			mapa.setColor(siguiente);
			gl.glTranslatef(-1, -1, 0);
			pieza.dibuja(gl);
			break;
		case 's':
			mapa.setColor(siguiente);
			gl.glTranslatef(3, 20.2f, 0);
			gl.glScalef(0.8f, 0.8f, 0);
			pieza.dibuja(gl);
			mapa.setColor(siguiente);
			gl.glTranslatef(1, 0, 0);
			pieza.dibuja(gl);
			mapa.setColor(siguiente);
			gl.glTranslatef(0, 1, 0);
			pieza.dibuja(gl);
			mapa.setColor(siguiente);
			gl.glTranslatef(1, 0, 0);
			pieza.dibuja(gl);
		case 'z':
			mapa.setColor(siguiente);
			gl.glTranslatef(3, 21.2f, 0);
			gl.glScalef(0.8f, 0.8f, 0);
			pieza.dibuja(gl);
			mapa.setColor(siguiente);
			gl.glTranslatef(1, 0, 0);
			pieza.dibuja(gl);
			mapa.setColor(siguiente);
			gl.glTranslatef(0, -1, 0);
			pieza.dibuja(gl);
			mapa.setColor(siguiente);
			gl.glTranslatef(1, 0, 0);
			pieza.dibuja(gl);
			break;
		}
	}
	public void actualizar(){
		ubicacion[ay1][ax1] = '_';
		ubicacion[ay2][ax2] = '_';
		ubicacion[ay3][ax3] = '_';
		ubicacion[ay4][ax4] = '_';
		if(!insertar){
			ubicacion[y1][x1] = color;
			ubicacion[y2][x2] = color;
			ubicacion[y3][x3] = color;
			ubicacion[y4][x4] = color;
			ay1 = y1;ay2 = y2;ay3 = y3;ay4 = y4;
			ax1 = x1;ax2 = x2;ax3 = x3;ax4 = x4;
		}else{
			char a = Character.toUpperCase(color);
			ubicacion[ay1][ax1] = a;
			ubicacion[ay2][ax2] = a;
			ubicacion[ay3][ax3] = a;
			ubicacion[ay4][ax4] = a;
			ay1 = y1;ay2 = y2;ay3 = y3;ay4 = y4;
			ax1 = x1;ax2 = x2;ax3 = x3;ax4 = x4;
		}
	}
	public void insertarFicha(){
		
		System.out.println(color);
		switch(color){
			case 'i': 
				x1 = 3; y1 = 19+2;
				x2 = 4; y2 = 19+2;
				x3 = 5; y3 = 19+2;
				x4 = 6; y4 = 19+2;
				break;
			case 'j': 
				x1 = 5; y1 = 18+2;
				x2 = 3; y2 = 19+2;
				x3 = 4; y3 = 19+2;
				x4 = 5; y4 = 19+2;
				break;
			case 'l': 
				x1 = 3; y1 = 18+2;
				x2 = 3; y2 = 19+2;
				x3 = 4; y3 = 19+2;
				x4 = 5; y4 = 19+2;
				break;
			case 'o': 
				x1 = 4; y1 = 18+2;
				x2 = 5; y2 = 18+2;
				x3 = 4; y3 = 19+2;
				x4 = 5; y4 = 19+2;
				break;
			case 's': 
				x1 = 3; y1 = 18+2;
				x2 = 4; y2 = 18+2;
				x3 = 4; y3 = 19+2;
				x4 = 5; y4 = 19+2;
				break;
			case 't': 
				x1 = 4; y1 = 18+2;
				x2 = 3; y2 = 19+2;
				x3 = 4; y3 = 19+2;
				x4 = 5; y4 = 19+2;
				break;
			case 'z': 
				x1 = 5; y1 = 18+2;
				x2 = 4; y2 = 18+2;
				x3 = 4; y3 = 19+2;
				x4 = 3; y4 = 19+2;
				break;
			default: Log.w("Error","Error en la determinacion de la ficha");break;
		}
		ay1 = y1; ay2 = y2; ay3 = y3; ay4 = y4;
		ax1 = x1; ax2 = x2; ax3 = x3; ax4 = x4;
		flag = false;
	}
	public void bajarFicha(){
		fin = System.currentTimeMillis();
		duracion = fin - inicio;
		tiempo_real = duracion / 1000f;
		inicio = fin;
		/* Incrementa y verifica el límite del tiempo */
		tiempoRotacion = tiempoRotacion - tiempo_real;
		if (tiempoRotacion < 0.001) {
			tiempoRotacion = PERIODO;
			verificaInsertar();
			if(!insertar)	
				y1--;y2--;y3--;y4--;
			actualizar();
		}
	}
	public void moverDer(){
		boolean sw = false;
		if((   ubicacion[y1][x1+1] == '_' || ubicacion[y1][x1+1] == color)
			&&(ubicacion[y2][x2+1] == '_' || ubicacion[y2][x2+1] == color)
			&&(ubicacion[y3][x3+1] == '_' || ubicacion[y3][x3+1] == color)
			&&(ubicacion[y4][x4+1] == '_' || ubicacion[y4][x4+1] == color)){
			
			x1++;x2++;x3++;x4++; sw = true;
		}
		verificaInsertar();
		actualizar();
		if(sw){
			ubicacion[y1][x1-1] = '_';
			ubicacion[y2][x2-1] = '_';
			ubicacion[y3][x3-1] = '_';
			ubicacion[y4][x4-1] = '_';
			ubicacion[y1][x1] = color;
			ubicacion[y2][x2] = color;
			ubicacion[y3][x3] = color;
			ubicacion[y4][x4] = color;
			sw = false;
		}
	}
	public void moverIzq(){
		boolean sw = false;
		if((   ubicacion[y1][x1-1]=='_' || ubicacion[y1][x1-1] == color)
			&&(ubicacion[y2][x2-1]=='_' || ubicacion[y2][x2-1] == color)
			&&(ubicacion[y3][x3-1]=='_' || ubicacion[y3][x3-1] == color)
			&&(ubicacion[y4][x4-1]=='_' || ubicacion[y4][x4-1] == color)){
			
			x1--;x2--;x3--;x4--;sw = true;
		}
		verificaInsertar();
		actualizar();
		if(sw){
			ubicacion[y1][x1+1] = '_';
			ubicacion[y2][x2+1] = '_';
			ubicacion[y3][x3+1] = '_';
			ubicacion[y4][x4+1] = '_';
			ubicacion[y1][x1] = color;
			ubicacion[y2][x2] = color;
			ubicacion[y3][x3] = color;
			ubicacion[y4][x4] = color;
			sw = false;
		}
	}
	public void verificaInsertar(){
		if(y1-1<0 || y2-1<0 || y3-1<0 || y4-1<0){
			insertar = true;
		}
		if(!insertar && ((ubicacion[y1-1][x1] != '_' && ubicacion[y1-1][x1] != color)
				||(ubicacion[y2-1][x2] != '_' && ubicacion[y2-1][x2] != color)
				||(ubicacion[y3-1][x3] != '_' && ubicacion[y3-1][x3] != color)
				||(ubicacion[y4-1][x4] != '_' && ubicacion[y4-1][x4] != color))){
				insertar = true;
		}else insertar = false;
		if(y1-1<0 || y2-1<0 || y3-1<0 || y4-1<0){
			insertar = true;
		}
	}
	public void girarMatriz(int pivoteX, int pivoteY, int x, int y){
		xRotado = (y-pivoteY) + pivoteX ; yRotado = (((x-pivoteX)*-1)+pivoteY) ;	//xRotado = y*1 ; yRotado = x*-1 ;'
		
	}
	public void rotar(){
		int xx1 = x1, yy1 = y1; 
		int xx2 = x2, yy2 = y2;
		int xx3 = x3, yy3 = y3;
		int xx4 = x4, yy4 = y4;
		boolean sw = false;
		switch(color){
			case 'i':
				girarMatriz(x2, y2, x1, y1);
				x1 = xRotado; y1 = yRotado;
				girarMatriz(x2, y2, x2, y2);
				x2 = xRotado; y2 = yRotado;
				girarMatriz(x2, y2, x3, y3);
				x3 = xRotado; y3 = yRotado;
				girarMatriz(x2, y2, x4, y4);
				x4 = xRotado; y4 = yRotado;
				break;
			case 'j': 
				girarMatriz(x3, y3, x1, y1);
				x1 = xRotado; y1 = yRotado;
				girarMatriz(x3, y3, x2, y2);
				x2 = xRotado; y2 = yRotado;
				girarMatriz(x3, y3, x4, y4);
				x4 = xRotado; y4 = yRotado;
				girarMatriz(x3, y3, x3, y3);
				x3 = xRotado; y3 = yRotado;
				break;
			case 'l': 
				girarMatriz(x3, y3, x1, y1);
				x1 = xRotado; y1 = yRotado;
				girarMatriz(x3, y3, x2, y2);
				x2 = xRotado; y2 = yRotado;
				girarMatriz(x3, y3, x4, y4);
				x4 = xRotado; y4 = yRotado;
				girarMatriz(x3, y3, x3, y3);
				x3 = xRotado; y3 = yRotado;
				break;
			case 'o': 
				break;
			case 's': 
				girarMatriz(x2, y2, x1, y1);
				x1 = xRotado; y1 = yRotado;
				girarMatriz(x2, y2, x2, y2);
				x2 = xRotado; y2 = yRotado;
				girarMatriz(x2, y2, x3, y3);
				x3 = xRotado; y3 = yRotado;
				girarMatriz(x2, y2, x4, y4);
				x4 = xRotado; y4 = yRotado;
				break;
			case 't': 
				girarMatriz(x1, y1, x1, y1);
				x1 = xRotado; y1 = yRotado;
				girarMatriz(x1, y1, x2, y2);
				x2 = xRotado; y2 = yRotado;
				girarMatriz(x1, y1, x3, y3);
				x3 = xRotado; y3 = yRotado;
				girarMatriz(x1, y1, x4, y4);
				x4 = xRotado; y4 = yRotado;
				break;
			case 'z': 
				girarMatriz(x2, y2, x1, y1);
				x1 = xRotado; y1 = yRotado;
				girarMatriz(x2, y2, x2, y2);
				x2 = xRotado; y2 = yRotado;
				girarMatriz(x2, y2, x4, y4);
				x4 = xRotado; y4 = yRotado;
				girarMatriz(x2, y2, x3, y3);
				x3 = xRotado; y3 = yRotado;
				break;	
			default: Log.w("Error","Error en la determinacion de la ficha");break;
		}
		while(y1<0||y2<0||y3<0||y4<0){
			y1++;y2++;y3++;y4++;
		}
		while(x1<0||x2<0||x3<0||x4<0){
			x1++;x2++;x3++;x4++;
		}
		while(x1>9||x2>9||x3>9||x4>9){
			x1--;x2--;x3--;x4--;
		}
		while((y1+2<24&&y2<24&&y3+2<24&&y4+2<24)&&((!Character.isUpperCase(ubicacion[y1+2][x1])&&Character.isUpperCase(ubicacion[y1][x1]))
				||(!Character.isUpperCase(ubicacion[y2+2][x2])&&Character.isUpperCase(ubicacion[y2][x2]))
				||(!Character.isUpperCase(ubicacion[y3+2][x3])&&Character.isUpperCase(ubicacion[y3][x3]))
				||(!Character.isUpperCase(ubicacion[y4+2][x4])&&Character.isUpperCase(ubicacion[y4][x4])))){
			y1++;y2++;y3++;y4++;
		}
		
		verificaInsertar();
		actualizar();
		ubicacion[yy1][xx1] = '_';
		ubicacion[yy2][xx2] = '_';
		ubicacion[yy3][xx3] = '_';
		ubicacion[yy4][xx4] = '_';
		if(Character.isUpperCase(ubicacion[y1][x1])
				||Character.isUpperCase(ubicacion[y2][x2])
				||Character.isUpperCase(ubicacion[y3][x3])
				||Character.isUpperCase(ubicacion[y4][x4])){
			ubicacion[yy1][xx1] = color;
			ubicacion[yy2][xx2] = color;
			ubicacion[yy3][xx3] = color;
			ubicacion[yy4][xx4] = color;
		}else{
			ubicacion[y1][x1] = color;
			ubicacion[y2][x2] = color;
			ubicacion[y3][x3] = color;
			ubicacion[y4][x4] = color;			
		}
		System.out.println("x1:"+x1+" y1:"+y1);
		System.out.println("x1:"+x2+" y1:"+y2);
		System.out.println("x1:"+x3+" y1:"+y3);
		System.out.println("x1:"+x4+" y1:"+y4);
//		while(x1<0||x2<0||x3<0||x4<0 && (ubicacion[y1][x1]!='_'&&ubicacion[y1][x1]!=color)
//				||(ubicacion[y2][x2]!='_'&&ubicacion[y2][x2]!=color)
//				||(ubicacion[y3][x3]!='_'&&ubicacion[y3][x3]!=color)
//				||(ubicacion[y4][x4]!='_'&&ubicacion[y4][x4]!=color)){  ///Revisar 2 veces
//			x1++;x2++;x3++;x4++;
//		}
//		while(x1>9||x2>9||x3>9||x4>9 && (ubicacion[y1][x1]!='_'&& ubicacion[y1][x1]!=color)
//				||(ubicacion[y2][x2]!='_'&&ubicacion[y2][x2]!=color)
//				||(ubicacion[y3][x3]!='_'&&ubicacion[y3][x3]!=color)
//				||(ubicacion[y4][x4]!='_'&&ubicacion[y4][x4]!=color)){
//			x1--;x2--;x3--;x4--;
//		}
//		if((ubicacion[y1][x1]!='_'&&ubicacion[y1][x1]!=color)
//				||(ubicacion[y2][x2]!='_'&&ubicacion[y2][x2]!=color)
//				||(ubicacion[y3][x3]!='_'&&ubicacion[y3][x3]!=color)
//				||(ubicacion[y4][x4]!='_'&&ubicacion[y4][x4]!=color)){
//			x1 = xx1; y1 = yy1;
//			x2 = xx2; y2 = yy2;
//			x3 = xx3; y3 = yy3;
//			x4 = xx4; y4 = yy4;
//		}
	}
	public void reiniciar(){
		pieza = new Pieza();
		pieza = new Pieza();
		for(int i=0; i<22; i++){
			for(int j=0; j<10; j++){
				ubicacion[i][j] = '_';
			}
		}
		ubicacion = mapa.getUbicacion();
		insertar = true;
		flag = false;
		siguiente = 'i';//fichas[(int)(Math.random()*6)];
		inicio = System.currentTimeMillis();
		tiempoRotacion = PERIODO;
	}
	public void verificaEliminarLinea(){
		boolean bajar = true;
		for(int i = 0; i < 19; i++){
			bajar = true;
			for(int j = 0; j<=9; j++){
				if(!Character.isUpperCase(ubicacion[i][j])||ubicacion[i][j]=='_'){
					bajar = false;
				}
			}
			if(bajar){
				descenderFichas(i);
				i = 0;
			}
		}
	}
	public void descenderFichas(int inicio){
		for(int i=inicio; i<19; i++){
			for(int j=0; j<=9; j++){
				ubicacion[i][j] = ubicacion[i+1][j];
			}
		}
		for(int j=0; j<=9; j++){
			ubicacion[19][j] = '_';
		}
		imprimir();
	}
	private boolean puntoEstaDentroDelRectangulo(float posx, float posy, 
			int x, int y, int ancho, int alto){
		
		return (x < posx && posx < x + ancho && 
				y < posy && posy < y + alto);
	}
	public void imprimir(){
		System.out.println("********************************************");
		System.out.println("********************************************");
		for(int i=19; i>=0; i--){
			for(int j=0; j<=9; j++){
				System.out.print("|"+ubicacion[i][j]);
			}	
			System.out.println();
		}
		System.out.println("********************************************");
	}
}
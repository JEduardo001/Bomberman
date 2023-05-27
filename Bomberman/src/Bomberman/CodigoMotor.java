package Bomberman;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;

public class CodigoMotor {

	private JFrame frame;
	private int tiempo=0;
	private int puntaje=0;
	//juego
	
	JPanel tablero = new JPanel();
	int limiteX=780, limiteY=500;
	static Random rand = new Random();
	
	boolean gameOver=false;
	Timer timer = new Timer();
	int xEspacio=50,x=20,yEspacio=50,y=20;
	public Mapa [][] mapa= new Mapa[10][15];


	//JUGADOR
	static Image imagenJugador= Toolkit.getDefaultToolkit().getImage("personaje1.png");
	public static int g=50;
	/* LO QUE REPRESENTA CADA DATO:
	 * int x,int y,int largo,int alto,int direccion,int velocidad,Image imagen
	 * 
	 *  	0 = ejeX, 1 = ejeY, 2= largo, 3 = alto, 4= direccion , 5= que tanto avanza velocidad, 6 = imagen del jugador, 7= cantidad de bombas que puede poner 
			8 = si el jugador puede patear una bomba
		DIRECCION:  0=arriba 1= abajo 2= derecha 3 = izquierda

	 */                      //     0  1   2  3  4  5    6          7   8
	Jugador jugador1 = new Jugador(120,370,40,40,0,50,imagenJugador,1,false);
	
	//BOMBA
	static Image imagenBomba= Toolkit.getDefaultToolkit().getImage("Bomba.png");
	Bomba bombas[] = new Bomba[10];
	
	//ENEMIGOS 
	Enemigos enemigos[]= new Enemigos[4];
	static Image imagenEnemigo= Toolkit.getDefaultToolkit().getImage("enemigo.png");
	boolean moverEnemigo=true;
	
	//BONUS
	Bonus bonus[]= new Bonus[15];

	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CodigoMotor window = new CodigoMotor();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	
	 TimerTask explotarBomba = new TimerTask() {
         public void run() {
        	 explotarBomba();
        	
         }
	};
	TimerTask sumartiempo = new TimerTask() {
        public void run() {
       	 sumartiempo();
       	
        }
	};
	 TimerTask moverEnemigos = new TimerTask() {
         public void run() {
        	 moverEnemigos();
        	
         }
	};

	TimerTask cambiarDireccionEnemiga = new TimerTask() {
        public void run() {
        	cambiarDireccionEnemiga();
       	
        }
	};
	Runnable activarBomba = new Runnable() {
        public void run() {
        	jugador1.cantBombas=1;
       	
        }
	};
	/*int cont=0;
	TimerTask moverJugador = new TimerTask() {
        public void run() {
        	
        	if(cont<=0) {
    			//cont=50;

    		}else {
    			if(moverJugador()) {
    				cont--;
            		switch(jugador1.direccion) {
            		case 0:
            			
            			jugador1.y-=jugador1.velocidad;
            		break;
            		case 1:
            			jugador1.y+=jugador1.velocidad;
                	break;
            		case 2:
            			jugador1.x+=jugador1.velocidad;
                	break;
            		case 3:
            			jugador1.x-=jugador1.velocidad;
                	break;
            		
            		}
					comprobarSiTomoBonus();

            		//System.out.println("quite  "+cont+ " x: "+jugador1.x+" y:: "+jugador1.y);
            	}else {
            		cont=0;
            	}
    			

    		}
    		
        	
        		
        		
        	
       	
        }
	};*/

	public CodigoMotor() {
		initialize();
	}

	public void crearBomba() {
		for(int i=0;i<bombas.length;i++) {
			
				try {
					if( bombas[i]== null || bombas[i].x==0) {
						bombas[i] = new Bomba(jugador1.x,jugador1.y,40,40,imagenBomba,1000);
						break;
					}
				}catch(Exception e) {
					System.out.println("ERROR NO SE PUEDE ACCEDER A AUN VALOR DE UN OBJETO QUE NO Existe... "+e);
				}
			
		}
	}
	
	
	public void genBonus(int i,int i2) {
		
	
			//esta varibale es para generar una probabilidad si es que va a haber un bonus o no
			//int genBonus= rand.nextInt(6);
			int genBonus=rand.nextInt(6);;

			if(genBonus==5) {
				for(int i4=0;i4<bonus.length;i4++) {
					//preguntamos si hay un lugar disponible en el arreglo de bonus
					if(bonus[i4]==null || bonus[i4].x==0) {
						//esta variable es para generar aleatoriamente que tipo de bonus sera
						int genTipoBonus = rand.nextInt(5);
						

						
						/*
						 * case 0= bonus de que puedes poner mas bombas
						 * case 1= malo osea el que no te deja poner bombas
						 * case 2= el de patear bomba
						 * case 3= aumentar velocidad del jugador
						 *  case 4= aumentar distancia  de explocion de las bombas
						 * 
						 */
						switch(genTipoBonus) {
						case 0:
							bonus[i4]= new Bonus(mapa[i][i2].x,mapa[i][i2].y,40,40,Toolkit.getDefaultToolkit().getImage("PonerMasBombas.png"),0);
						break;
						case 1:
							bonus[i4]= new Bonus(mapa[i][i2].x,mapa[i][i2].y,40,40,Toolkit.getDefaultToolkit().getImage("Malo.png"),1);

						break;
						case 2:
							bonus[i4]= new Bonus(mapa[i][i2].x,mapa[i][i2].y,40,40,Toolkit.getDefaultToolkit().getImage("PatearBomba.png"),2);
						break;
						case 3:
							bonus[i4]= new Bonus(mapa[i][i2].x,mapa[i][i2].y,40,40,Toolkit.getDefaultToolkit().getImage("Velocidad.png"),3);

						break;
						case 4:
							bonus[i4]= new Bonus(mapa[i][i2].x,mapa[i][i2].y,40,40,Toolkit.getDefaultToolkit().getImage("llama.png"),3);

						break;
						
						}
						
						break;
					}
				}
			
			}
		
	}
	public void sumartiempo() {
		tiempo++;
	}
	
	public void explotarBomba() {
		
		//for para las bombas
		for(int i3=0;i3<bombas.length;i3++) {
			try {
				if(bombas[i3]!=null || bombas[i3].x!=0) {
					
					if(bombas[i3].tiempo==0) {
						
						//for para las filas
						for(int i=0;i<10;i++) {
							
							
							
							//for para las columnas
							for(int i2=0;i2<15;i2++) {
								if(mapa[i][i2].tipoBloque!=0) {
									
									///se reinicia puntaje a 0 y se pregunta
									puntaje=0;
									for(int f5=0;f5<4;f5++) {
										//quitar enemigos de arriba y abajo por daño de la explocion 150 es el rango de explocion 
										if(bombas[i3].y-80<=enemigos[f5].y && bombas[i3].y+80>=enemigos[f5].y && bombas[i3].x>=enemigos[f5].x && bombas[i3].x<=enemigos[f5].x+enemigos[f5].largo) {
											enemigos[f5].x=-20;
											enemigos[f5].y=-20;
											enemigos[f5].vida=0;	
										}
										
										//quitar enemigos de derecha y izquierda por daño de la explocion 150 es el rango de explocion 
										if(bombas[i3].y>=enemigos[f5].y && bombas[i3].y<=enemigos[f5].y+enemigos[f5].alto && bombas[i3].x+80>=enemigos[f5].x && bombas[i3].x-80<=enemigos[f5].x ) {
											enemigos[f5].x=-20;
											enemigos[f5].y=-20;
											enemigos[f5].vida=0;
										}
										//se hace conteo de enemigos muertos
										if(enemigos[f5].vida==0) {
											puntaje+=100;
										}
									}
									
									//quitar bloques de arriba y abajo por daño de la explocion 150 es el rango de explocion 
									if(bombas[i3].y-80<=mapa[i][i2].y && bombas[i3].y+80>=mapa[i][i2].y && bombas[i3].x>=mapa[i][i2].x && bombas[i3].x<=mapa[i][i2].x+mapa[i][i2].largo) {
										genBonus(i,i2);
										mapa[i][i2].x=0;
										mapa[i][i2].y=0;
										mapa[i][i2].tipoBloque=0;
									}else {
										//System.out.println("xb:: "+bombas[i3].x+" yb:::  "+bombas[i3].y+"  xM:: "+mapa[i][i2].x+" yM :: "+mapa[i][i2].y);
									}
									
									//quitar bloques de derecha y izquierda por daño de la explocion 150 es el rango de explocion 
									if(bombas[i3].y>=mapa[i][i2].y && bombas[i3].y<=mapa[i][i2].y+mapa[i][i2].alto && bombas[i3].x+80>=mapa[i][i2].x && bombas[i3].x-80<=mapa[i][i2].x ) {
										genBonus(i,i2);

										mapa[i][i2].x=0;
										mapa[i][i2].y=0;
										mapa[i][i2].tipoBloque=0;
									}else {
										//System.out.println("xb:: "+bombas[i3].x+" yb:::  "+bombas[i3].y+"  xM:: "+mapa[i][i2].x+" yM :: "+mapa[i][i2].y);
									}
					
								}
								
							}
						}
						
						bombas[i3].x=0;
						bombas[i3].y=0;
						
					}else {
						
						bombas[i3].tiempo--;
					}
					
				
				}
				

			}catch(Exception e) {
				
			}
			
			
		}
		
		
	}
	public void generarMapa() {
	
		for(int i=0;i<10;i++) {
			for(int i2=0;i2<15;i2++) {
				//este if es para que en ciertas cordenadas no me pinte un bloque encima del jugador o lo deje encerrado en bloques
				if(i==7 && i2==3 || i==7 && i2==2 || i==6 && i2==2) {
					//EL ULTIMO PARAMETRO INDICA EL TIPO DE BLOQUE QUE SERA
					
					mapa[i][i2]= new Mapa(x,y,40,40,0);

				}else {
					mapa[i][i2]= new Mapa(x,y,40,40,rand.nextInt(3));

					
				}
				x+=xEspacio;
				
				
			}
			y+=yEspacio;
			x=20;
		}
		
	}
	
	
	//esta funcion valida si a la direccion a la que me voy a mover hay un muro o no
	public boolean moverJugador() {

		boolean poderMoverJugador=true;
		for(int i=0;i<10;i++) {
			int f=1;
			for(int i2=0;i2<15;i2++) {
				//DIRECCION:  0=arriba 1= abajo 2= derecha 3 = izquierda
				if(mapa[i][i2].tipoBloque!=0) {
					
					
					switch(jugador1.direccion) {
					//arriba
					case 0:
	
						
							if(jugador1.y-jugador1.velocidad<=mapa[i][i2].y+mapa[i][i2].alto && jugador1.y-jugador1.velocidad>=mapa[i][i2].y && jugador1.x>=mapa[i][i2].x && jugador1.x<=mapa[i][i2].x+mapa[i][i2].largo 
									|| jugador1.y-jugador1.velocidad<=mapa[i][i2].y+mapa[i][i2].alto && jugador1.y-jugador1.velocidad>=mapa[i][i2].y && jugador1.x+jugador1.largo>=mapa[i][i2].x && jugador1.x+jugador1.largo<=mapa[i][i2].x+mapa[i][i2].largo) {
								
								poderMoverJugador=false;
							}else {
								
								
								poderMoverJugador=true;
								
						    }
							
						
					
					
					break;
					//abajo
					case 1:
						
					
				
							
							if(jugador1.y+jugador1.alto+jugador1.velocidad>=mapa[i][i2].y && jugador1.y+jugador1.alto+jugador1.velocidad<=mapa[i][i2].y+mapa[i][i2].alto && jugador1.x>=mapa[i][i2].x && jugador1.x<=mapa[i][i2].x+mapa[i][i2].largo 
									 || jugador1.y+jugador1.alto+jugador1.velocidad>=mapa[i][i2].y && jugador1.y+jugador1.alto+jugador1.velocidad<=mapa[i][i2].y+mapa[i][i2].alto && jugador1.x+jugador1.largo>=mapa[i][i2].x && jugador1.x+jugador1.largo<=mapa[i][i2].x+mapa[i][i2].largo   ) {
										
										poderMoverJugador=false;
									}else {
										poderMoverJugador=true;
										
								    }
					
							
						break;
					//derecha
					case 2:
						
					
							if(jugador1.y>=mapa[i][i2].y && jugador1.y<=mapa[i][i2].y+mapa[i][i2].alto && jugador1.x+jugador1.largo+jugador1.velocidad>=mapa[i][i2].x && jugador1.x+jugador1.largo+jugador1.velocidad<=mapa[i][i2].x+mapa[i][i2].largo 
								 	|| jugador1.y+jugador1.alto>=mapa[i][i2].y && jugador1.y+jugador1.alto<=mapa[i][i2].y+mapa[i][i2].alto && jugador1.x+jugador1.largo+jugador1.velocidad>=mapa[i][i2].x && jugador1.x+jugador1.largo+jugador1.velocidad<=mapa[i][i2].x+mapa[i][i2].largo) {
									
									poderMoverJugador=false;
									
								}else {
									poderMoverJugador=true;
									
							    }
					
						
						
						

					break;
					//izquierda
					case 3:
						
						
							if(jugador1.y<=mapa[i][i2].y+mapa[i][i2].alto && jugador1.y>=mapa[i][i2].y && jugador1.x-jugador1.velocidad>=mapa[i][i2].x && jugador1.x-jugador1.velocidad<=mapa[i][i2].x+mapa[i][i2].largo 
									|| jugador1.y+jugador1.alto<=mapa[i][i2].y+mapa[i][i2].alto && jugador1.y+jugador1.largo>=mapa[i][i2].y && jugador1.x-jugador1.velocidad>=mapa[i][i2].x && jugador1.x-jugador1.velocidad<=mapa[i][i2].x+mapa[i][i2].largo) {
								
								//System.out.println("X: "+jugador1.x+" y:: "+jugador1.y+" X B:: "+mapa[i][i2].x+" Y B :: "+mapa[i][i2].y);
								poderMoverJugador=false;
							}else {
								poderMoverJugador=true;
								
						    }
						
						
						

					break;
					
					}
				}
				
				
				if(poderMoverJugador==false) {
					break;
				}
	
			}
			
			if(poderMoverJugador==false) {
				break;
			}		
			
		}
		
		
		//validamos si esta encima de una bomba para poder patearla
		
		if(hayBomba()) {
			poderMoverJugador=false;

		}
			if(patearBomba()==true) {
				poderMoverJugador=false;
			}
		

		
		
		return poderMoverJugador;
	
	}
	public boolean hayBomba() {
		boolean hayBomba=false;
		for(int i=0;i<bombas.length;i++) {
			try {
				if(bombas[i].x!=0) {
					switch(jugador1.direccion) {
					//arriba
					case 0:
						if(jugador1.y-50==bombas[i].y && jugador1.x==bombas[i].x){
							
							hayBomba=true;
						}else {
							hayBomba=false;
						}
					break;
					//abajo
					case 1:
						if(jugador1.y+50==bombas[i].y && jugador1.x==bombas[i].x) {
							hayBomba=true;
						}else {
							hayBomba=false;
							

						}
					break;
					//derecha
					case 2:
						if(jugador1.x+50==bombas[i].x && jugador1.y==bombas[i].y) {
							hayBomba=true;
						}else {
							hayBomba=false;
						}
					break;
					//izquierda
					case 3:
						
						if(jugador1.x-50==bombas[i].x && jugador1.y==bombas[i].y) {
							hayBomba=true;
						}else {
							hayBomba=false;
						}
					break;
					}
					
					if(hayBomba==true) {
						break;
					}
				}

			}catch(Exception e) {
				
			}
			
		}
		return hayBomba;
	}
	public boolean patearBomba() {
		boolean moverBomba=false,hayBomba=false;
		if(jugador1.patearBomba==true) {
			
			
			for(int i=0;i<bombas.length;i++) {
				try {
					if(bombas[i].x!=0) {
						switch(jugador1.direccion) {
						//arriba
						case 0:
							if(jugador1.y-50==bombas[i].y && jugador1.x==bombas[i].x){
								
								hayBomba=true;
							}else {
								hayBomba=false;
							}
						break;
						//abajo
						case 1:
							if(jugador1.y+50==bombas[i].y && jugador1.x==bombas[i].x) {
								hayBomba=true;
							}else {
								hayBomba=false;
							

							}
						break;
						//derecha
						case 2:
							if(jugador1.x+50==bombas[i].x && jugador1.y==bombas[i].y) {
								hayBomba=true;
							}else {
								hayBomba=false;
							}
						break;
						//izquierda
						case 3:
							
							if(jugador1.x-50==bombas[i].x && jugador1.y==bombas[i].y) {
								hayBomba=true;
							}else {
								hayBomba=false;
							}
						break;
						}
						
						
					}
					
					

					if(hayBomba==true) {
						
					
							
							for(int fila=0;fila<10;fila++) {
								for(int columna=0;columna<15;columna++) {
									if(mapa[fila][columna].tipoBloque!=0) {
										
										switch(jugador1.direccion) {
										case 0:
											if(bombas[i].y-50==mapa[fila][columna].y && bombas[i].x==mapa[fila][columna].x) {
												moverBomba=false;
											}else {
												moverBomba=true;
											}
										break;
										
										case 1:
											if(bombas[i].y+50==mapa[fila][columna].y && bombas[i].x==mapa[fila][columna].x) {
												moverBomba=false;
											}else {
												moverBomba=true;
											}
										break;
										case 2:
											if(bombas[i].x+50==mapa[fila][columna].x && bombas[i].y==mapa[fila][columna].y) {
												moverBomba=false;
											}else {
												moverBomba=true;
											}
										break;
										case 3:
											
											if(bombas[i].x-50==mapa[fila][columna].x && bombas[i].y==mapa[fila][columna].y) {
												moverBomba=false;
											}else {
												moverBomba=true;
											}
										break;
										}
										
									}else {
										moverBomba=true;
									}
									
									if(moverBomba==false) {
										break;
									}
								
								}
								if(moverBomba==false) {
									break;
								}
							}
							
							
							
							if(moverBomba==true) {
								switch(jugador1.direccion) {
								case 0:
									bombas[i].y-=50;
								break;
								case 1:
									bombas[i].y+=50;

								break;
								case 2:
									bombas[i].x+=50;

								break;
								case 3:
									bombas[i].x-=50;

								break;
								}
							}
						
						
					}
					
					
					
						
					
				}catch(Exception e) {
					
				}
				
			}
			
		
		}
		
		return moverBomba;
		
	}
	public void comprobarSiTomoBonus() {
		for(int i=0;i<bonus.length;i++) {
			try {
				if(jugador1.x>=bonus[i].x && jugador1.x<=bonus[i].x+bonus[i].largo && jugador1.y<=bonus[i].y+bonus[i].alto && jugador1.y>=bonus[i].y
					|| jugador1.x+jugador1.largo>=bonus[i].x && jugador1.x+jugador1.largo<=bonus[i].x+bonus[i].largo && jugador1.y<=bonus[i].y+bonus[i].alto && jugador1.y>=bonus[i].y
					|| jugador1.x>=bonus[i].x && jugador1.x<=bonus[i].x+bonus[i].largo && jugador1.y+jugador1.alto<=bonus[i].y+bonus[i].alto && jugador1.y+jugador1.alto>=bonus[i].y
					|| jugador1.x+jugador1.largo>=bonus[i].x && jugador1.x+jugador1.largo<=bonus[i].x+bonus[i].largo && jugador1.y+jugador1.alto<=bonus[i].y+bonus[i].alto && jugador1.y+jugador1.alto>=bonus[i].y) {
					
					//System.out.println("si :. xJ:: "+jugador1.x+"  :: yj:: "+jugador1.y +" :: xB : "+bonus[i].x+" : yB : "+bonus[i].y );
					//ponemos las coordenadas del bonus en 0 para que no lo vuelva  pintar, osea pa que lo desaparezca
					bonus[i].x=0;
					bonus[i].y=0;
					/*
					 * case 0= bonus de que puedes poner mas bombas
						 * case 1= malo osea el que no te deja poner bombas
						 * case 2= el de patear bomba
						 * case 3= aumentar velocidad del juagador
						 * 
					 */
					switch(bonus[i].tipoBonus) {
					case 0:
						jugador1.cantBombas++;
					break;
					case 1:
						jugador1.cantBombas=0;
						 ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
						executor.schedule(activarBomba, 5, TimeUnit.SECONDS);
						
						 executor.shutdown();
					break;
					case 2:
						jugador1.patearBomba=true;
					break;
					case 3:
						if(jugador1.velocidad+10<=100) {
								if(jugador1.velocidad+1<=3) {
									//jugador1.velocidad++;

								}

						}
					break;

					}
				}else {
					//System.out.println("noo :. xJ:: "+jugador1.x+"  :: yj:: "+jugador1.y +" :: xB : "+bonus[i].x+" : yB : "+bonus[i].y );

				}
					
			}catch(Exception e) {
				
			}
		}
	}
	
	
	
	//esta funcion se le llama en la funcion crear enemigos y solo es para saber la cantidad de espacios vacios que hay en una fila
	public int buscarCantidadEspaciosVacios(int fila) {
		int cantEspaciosVacios=0;
		for(int columna=0;columna<15;columna++) {
			if(mapa[fila][columna].tipoBloque==0) {
				cantEspaciosVacios++;
			}
		}
		return cantEspaciosVacios;
	}
	public void crearEnemigos() {
		//esta variable es pa saber si genero los 5 enemgios
		int enemigosGenerados=0;
		//espacio vacio se usa para saber en que bloque invisible osea en que espacio va a meter el enemigo
		//si en una fila hay 3 espacios vacios, esta variable generara un valor aleatorio y dira cual de los 3 espacios metera al enemigo
		int espacioVacio=0;
		

			//evaluamos toda la matriz de bloques para saber donde van a aparecer los enemigos
			while(enemigosGenerados!=4) {
						
				for(int i=0;i<enemigos.length;i++) {
					try {
						if(enemigos[i]==null) {
							int fila=rand.nextInt(10);
							
							espacioVacio=rand.nextInt(buscarCantidadEspaciosVacios(fila));
							
							for(int columnas=0;columnas<15;columnas++) {
								//si el tipo de bloque es 0 quiere decir que es el bloque que es invisible osea el espacio vacio tonses ahi creamos al enemigo
								if(mapa[fila][columnas].tipoBloque==0) {
									if(espacioVacio-1<=0) {
										enemigos[i]= new Enemigos(mapa[fila][columnas].x,mapa[fila][columnas].y,40,40,rand.nextInt(3),50,2,imagenEnemigo);
										enemigosGenerados++;
				 						break;		
									}else {
										espacioVacio--;
									}
									
			 					
			 							
								}
							}
						}

					}catch(Exception e) {
								
				   }
				}
						
			}
					
				
			
		
		enemigosGenerados=0;
		
	}
	
	public void cambiarDireccionEnemiga() {
		enemigos[rand.nextInt(enemigos.length)].direccion=rand.nextInt(4);
		
	}
	public void moverEnemigos() {
		
		for(int i=0;i<enemigos.length;i++) {
			//aqui preguntamos si el enemigo esta muerto o no
			if(enemigos[i].vida!=0) {
				
				//evaluamos todo el mapa para ver si el enemigo se podra mover hacia la direcciona  al que va o no.. esto pa saber si hay un muro en su camino
				for(int filas=0;filas<10;filas++) {
					for(int columnas=0;columnas<15;columnas++) {
						
							switch(enemigos[i].direccion) {
							//arriba
							case 0:
								if(enemigos[i].y-enemigos[i].velocidad>30) {
									
									if(enemigos[i].y-enemigos[i].velocidad<= mapa[filas][columnas].y+mapa[filas][columnas].alto && enemigos[i].y>mapa[filas][columnas].y && enemigos[i].x>=mapa[filas][columnas].x
											&& enemigos[i].x<=mapa[filas][columnas].x+mapa[filas][columnas].largo ) {
										

										if(mapa[filas][columnas].tipoBloque==0) {
											moverEnemigo=true;
											
										}else {
											moverEnemigo=false;
											filas=10;
											columnas=15;
										}
										
									}else {
										moverEnemigo=true;
										
									}
								}else {
									moverEnemigo=false;

								}
								
							break;
							//abajo
							case 1:
								if(enemigos[i].y+enemigos[i].velocidad<limiteY-20) {
									
									if(enemigos[i].y+enemigos[i].velocidad+enemigos[i].alto<= mapa[filas][columnas].y+mapa[filas][columnas].alto && enemigos[i].y+enemigos[i].alto+enemigos[i].velocidad>=mapa[filas][columnas].y && enemigos[i].x>=mapa[filas][columnas].x
											&& enemigos[i].x<=mapa[filas][columnas].x+mapa[filas][columnas].largo ) {
										

										if(mapa[filas][columnas].tipoBloque==0) {
											moverEnemigo=true;
											
										}else {
											moverEnemigo=false;
											filas=10;
											columnas=15;
										}
										
									}else {
										moverEnemigo=true;
										
									}
								}else {
									moverEnemigo=false;

								}
								
							break;
							//derecha
							case 2:
								if(enemigos[i].x+enemigos[i].velocidad<limiteX-40) {
									
									if(enemigos[i].x+enemigos[i].velocidad+enemigos[i].largo<= mapa[filas][columnas].x+mapa[filas][columnas].largo && enemigos[i].x+enemigos[i].largo+enemigos[i].velocidad>=mapa[filas][columnas].x && enemigos[i].y>=mapa[filas][columnas].y
											&& enemigos[i].y<=mapa[filas][columnas].y+mapa[filas][columnas].alto ) {
										

										if(mapa[filas][columnas].tipoBloque==0) {
											moverEnemigo=true;
											
										}else {
											moverEnemigo=false;
											filas=10;
											columnas=15;
										}
										
									}else {
										moverEnemigo=true;
										
									}
								}else {
									moverEnemigo=false;

								}
								
							break;
							//izquierda
							case 3:
								if(enemigos[i].x-enemigos[i].velocidad>20) {
									
									if(enemigos[i].x-enemigos[i].velocidad<= mapa[filas][columnas].x+mapa[filas][columnas].largo && enemigos[i].x-enemigos[i].velocidad>=mapa[filas][columnas].x && enemigos[i].y>=mapa[filas][columnas].y
											&& enemigos[i].y<=mapa[filas][columnas].y+mapa[filas][columnas].alto ) {
										

										if(mapa[filas][columnas].tipoBloque==0) {
											moverEnemigo=true;
											
										}else {
											moverEnemigo=false;
											filas=10;
											columnas=15;
										}
										
									}else {
										moverEnemigo=true;
										
									}
								}else {
									moverEnemigo=false;

								}
								
							break;
							
						  }
						
						
						
						
				 }
					
				
				}
				
				if(moverEnemigo==true) {
					switch(enemigos[i].direccion) {
					case 0:
						enemigos[i].y-=enemigos[i].velocidad;
					break;
					case 1:
						enemigos[i].y+=enemigos[i].velocidad;

					break;
					case 2:
						enemigos[i].x+=enemigos[i].velocidad;

					break;
					case 3:
						enemigos[i].x-=enemigos[i].velocidad;

					break;
					
				  }
				}else {
					enemigos[i].direccion=rand.nextInt(3);
				}
			}
		}
		
		/*for(int i=0;i<enemigos.length;i++) {
			System.out.println(i+" ::: "+enemigos[i].direccion);
		}*/
	}
	
	private void initialize() {
		
		generarMapa();
		crearEnemigos();
		
		JLabel tiempoo = new JLabel("00:00");
		
		
		
        /*tiempo = new Timer(1000, new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                seconds++;
               minutos = seconds / 60;
                segundos= seconds % 60;
                tiempoo.setText(String.format("%02d:%02d", minutos, segundos));
            }
        });*/
		
		//tiempos 1500
	  	timer.schedule(explotarBomba, 0, 1);
	  	timer.schedule(moverEnemigos, 0, 200);
	  	//cada 300 milisegundos va a cambiar la dieccion enemiga de algun enemigo
	  	timer.schedule(cambiarDireccionEnemiga, 0, 300);
	    timer.schedule(sumartiempo, 0, 1000);
	  	//timer.schedule(moverJugador, 0, g);



		frame = new JFrame();
		frame.setBounds(100, 100, 773, 561);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 128, 0));
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		
		tablero.setBackground(new Color(0, 0, 0));
		frame.getContentPane().add(tablero, BorderLayout.CENTER);
		
		tablero.add(new MyGraphics());
		
		
		
		frame.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				//System.out.println("dadas");
				if(gameOver!=true) {
					//cont=20;
					//arriba
					if(e.getKeyCode()==87) {
						jugador1.direccion=0;
						
							if(jugador1.y-jugador1.velocidad>0 && moverJugador()) {
								
								
								jugador1.y-=jugador1.velocidad;
								comprobarSiTomoBonus();
								
							}	
					}
					//abajo
					if(e.getKeyCode()==83) {
						jugador1.direccion=1;
					
							if(jugador1.y+40+jugador1.velocidad<limiteY+20 && moverJugador()) {
								
									jugador1.y+=jugador1.velocidad;
									comprobarSiTomoBonus();
	
							}	
							
						
					}
					//izquierda
					if(e.getKeyCode()==65) {
						jugador1.direccion=3;
						
						if(jugador1.x-jugador1.velocidad>0 && moverJugador()) {
							
								jugador1.x-=jugador1.velocidad;
								comprobarSiTomoBonus();
							
							
						}
						
						
					}
					//derecha
					if(e.getKeyCode()==68) {
						
						jugador1.direccion=2;
						
						if(jugador1.x+40+jugador1.velocidad<limiteX-5 && moverJugador()) {
							
								jugador1.x+=jugador1.velocidad;
								comprobarSiTomoBonus();

	
						}
						

		
					}
					if(e.getKeyCode()==10) {
						g-=10;
						//System.out.println(g);
						int bombasActivas=0;
						//al poner una bomba primero comprobamos si puede poner una 
						for(int i=0;i<bombas.length;i++) {
							try {
								if(bombas[i].x!=0) {
									bombasActivas++;
									
								}
							}catch(Exception e1) {
								
							}
							
						}
						
						if(bombasActivas<jugador1.cantBombas) {
							crearBomba();

						}
					}
					
					
				
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}});
		
		
	}
	
	
	public class MyGraphics extends JComponent {

        private static final long serialVersionUID = 1L;

        MyGraphics() {
            setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight()));
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            //pintar jugador
            
   			g.drawImage(jugador1.imagen, jugador1.x, jugador1.y, jugador1.largo, jugador1.alto, null);
            
   			//pintar bombas
   			for(int i=0;i<bombas.length;i++) {
   			
   					try {
   						//pregunto si es diferente de 0, ya que si es igual, esto indica que el objeto esta muerto o inactivo o desactivado
   						if(bombas[i].x!=0) {
   	   	   		   			g.drawImage(bombas[i].imagen, bombas[i].x, bombas[i].y, bombas[i].largo, bombas[i].alto, null);

   	   	   				}
   					}catch(Exception e) {
   						
   					}
   				
   					
   			}

            
   			//pintar escenario de juego
   			
   			for(int i=0;i<10;i++) {
   				
   				for(int i2=0;i2<15;i2++) {
   					
   					if(mapa[i][i2].tipoBloque==0) {
   					 
   	 
   						
   					}

   					if(mapa[i][i2].tipoBloque==1) {
      					 g.setColor(Color.red);
                        g.fillRect(mapa[i][i2].x, mapa[i][i2].y, mapa[i][i2].largo,mapa[i][i2].alto);
                        
      						
      				}
   					if(mapa[i][i2].tipoBloque==2) {
      					 g.setColor(Color.blue);
                        g.fillRect(mapa[i][i2].x, mapa[i][i2].y, mapa[i][i2].largo,mapa[i][i2].alto);
                        
      						
      				}
   	
   				}
   				
   			}
   			
   			//pintar enemigos
   			for(int i=0;i<enemigos.length;i++) {
   					try {
	   	   		   		g.drawImage(enemigos[i].imagen, enemigos[i].x, enemigos[i].y, enemigos[i].largo, enemigos[i].alto, null);

   					}catch(Exception e) {
   						
   					}
   				
   			}
   			
   			
   			//PINTAR BONUS
   			for(int i=0;i<bonus.length;i++) {
   				try {
   					if(bonus[i]!=null) {
   						if(bonus[i].x!=0) {
	   	   		   			g.drawImage(bonus[i].imagen, bonus[i].x, bonus[i].y, bonus[i].largo, bonus[i].alto, null);
	   	   		   			

   						}
	   	   		   		

   						
   					}
   				
   				}catch(Exception e) {
   					
   				}
   			}
            
            //BORDES DEL MAPA
            
            //horizontal arriba
            g.setColor(Color.blue);
            g.fillRect(0, 0, tablero.getWidth(),15);
            //horizontal abajo
            g.setColor(Color.blue);
            g.fillRect(0, limiteY, tablero.getWidth(),10);
            
            //verical izquierda
            g.setColor(Color.blue);
            g.fillRect(5, 0, 10,tablero.getHeight());
            //verical derecha
            g.setColor(Color.blue);
            g.fillRect(limiteX-25, 0, 10,tablero.getHeight());
            
            //Menu
            Font font = new Font("Comic Sans MS",Font.BOLD,14);
            g.setFont(font);
            g.setColor(Color.white);
            
            g.drawString("Tiempo:  "+tiempo, 20, 13);
            g.drawString("Vidas: ", 670, 13);
            g.drawString(puntaje+" pts", 350, 13);
            
            
        
            tablero.repaint();

       }
   
    }
	

}

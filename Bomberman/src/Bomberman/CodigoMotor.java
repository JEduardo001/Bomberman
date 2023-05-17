package Bomberman;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;

public class CodigoMotor {

	private JFrame frame;

	//juego
	JPanel tablero = new JPanel();
	int limiteX=780, limiteY=500;
	static Random rand = new Random();

	boolean gameOver=false;
	Timer timer = new Timer();
	int xEspacio=50,x=21,yEspacio=50,y=21;
	public Mapa [][] mapa= new Mapa[10][15];


	//JUGADOR
	static Image imagenJugador= Toolkit.getDefaultToolkit().getImage("personaje1.png");

	/* LO QUE REPRESENTA CADA DATO:
	 * int x,int y,int largo,int alto,int direccion,int velocidad,Image imagen
	 * 
	 *  	0 = ejeX, 1 = ejeY, 2= largo, 3 = alto, 4= direccion , 5= velocidad osea que tanto avanza, 6 = imagen del jugador

		DIRECCION:  0=arriba 1= abajo 2= derecha 3 = izquierda

	 */                      //     0  1   2  3  4  5    6
	Jugador jugador1 = new Jugador(120,370,30,30,0,10,imagenJugador);
	
	//BOMBA
	static Image imagenBomba= Toolkit.getDefaultToolkit().getImage("Bomba.png");
	Bomba bombas[] = new Bomba[10];
	
	
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
	
	 TimerTask quitarBomba = new TimerTask() {
         public void run() {
        	 explotarBomba();
        	
         }
  };

	public CodigoMotor() {
		initialize();
	}

	public void crearBomba() {
		for(int i=0;i<bombas.length;i++) {
			
				try {
					if( bombas[i]== null || bombas[i].x==0) {
						bombas[i] = new Bomba(jugador1.x+5,jugador1.y+5,20,20,imagenBomba);
						break;
					}
				}catch(Exception e) {
					System.out.println("ERROR NO SE PUEDE ACCEDER A AUN VALOR DE UN OBJETO QUE NO Existe... "+e);
				}
			
		}
	}
	
	
	
	public void explotarBomba() {
		
		//for para las bombas
		for(int i3=0;i3<bombas.length;i3++) {
			try {
				if(bombas[i3]!=null || bombas[i3].x!=0) {
					//for para las filas
					for(int i=0;i<10;i++) {
						

						//for para las columnas
						for(int i2=0;i2<15;i2++) {
							if(mapa[i][i2].tipoBloque!=0) {
								
								//quitar bloques de arriba y abajo por daño de la explocion 150 es el rango de explocion 
								if(bombas[i3].y-150<=mapa[i][i2].y && bombas[i3].y+150>=mapa[i][i2].y && bombas[i3].x>=mapa[i][i2].x && bombas[i3].x<=mapa[i][i2].x+mapa[i][i2].largo) {
									mapa[i][i2].x=0;
									mapa[i][i2].x=0;
									mapa[i][i2].tipoBloque=0;

									

								}else {
									System.out.println("xb:: "+bombas[i3].x+" yb:::  "+bombas[i3].y+"  xM:: "+mapa[i][i2].x+" yM :: "+mapa[i][i2].y);

								
								}
								
								
								//quitar bloques de derecha y izquierda por daño de la explocion 150 es el rango de explocion 
								if(bombas[i3].y>=mapa[i][i2].y && bombas[i3].y<=mapa[i][i2].y+mapa[i][i2].alto && bombas[i3].x+150>=mapa[i][i2].x && bombas[i3].x-150<=mapa[i][i2].x ) {
									mapa[i][i2].x=0;
									mapa[i][i2].x=0;
									mapa[i][i2].tipoBloque=0;

									

								}else {
									System.out.println("xb:: "+bombas[i3].x+" yb:::  "+bombas[i3].y+"  xM:: "+mapa[i][i2].x+" yM :: "+mapa[i][i2].y);

								
								}
								
								
								
							}
							
						}
					}
				}
				

				bombas[i3].x=0;
				bombas[i3].y=0;
				
				
				
				
				
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
			x=21;
		}
		
	}
	
	//esta funcion valida si a la direccion a la que me voy a mover hay un muro o no
	public boolean moverJugador() {

		boolean poderMoverJugador=true;
		for(int i=0;i<10;i++) {
			
			for(int i2=0;i2<15;i2++) {
				//DIRECCION:  0=arriba 1= abajo 2= derecha 3 = izquierda
				if(mapa[i][i2].tipoBloque!=0) {
					
					
					switch(jugador1.direccion) {
					//arriba
					case 0:
						if(jugador1.y-jugador1.velocidad<=mapa[i][i2].y+mapa[i][i2].alto && jugador1.y-jugador1.velocidad>=mapa[i][i2].y && jugador1.x+30>=mapa[i][i2].x && jugador1.x<=mapa[i][i2].x+mapa[i][i2].largo ) {
							
							poderMoverJugador=false;
						}else {
							poderMoverJugador=true;
							
					    }
					break;
					//abajo
					case 1:
						if(jugador1.y+jugador1.velocidad+jugador1.alto>=mapa[i][i2].y && jugador1.y+jugador1.velocidad+jugador1.alto<=mapa[i][i2].y+mapa[i][i2].alto && jugador1.x+30>=mapa[i][i2].x && jugador1.x<=mapa[i][i2].x+mapa[i][i2].largo ) {
							
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
		return poderMoverJugador;
	
	}
	
	private void initialize() {
		
		generarMapa();
		//tiempos
	  	timer.schedule(quitarBomba, 0, 1500);

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
					// TODO Auto-generated method stub
					//arriba
					if(e.getKeyCode()==87) {
						jugador1.direccion=0;
							if(jugador1.y-jugador1.velocidad>0 && moverJugador()) {
							
								jugador1.y-=jugador1.velocidad;

								
								
							}	
					}
					//abajo
					if(e.getKeyCode()==83) {
						jugador1.direccion=1;

							if(jugador1.y+40+jugador1.velocidad<limiteY+20 && moverJugador()) {
								
									jugador1.y+=jugador1.velocidad;
								
	
							}	
						
					}
					//izquierda
					if(e.getKeyCode()==65) {
						jugador1.direccion=3;

						if(jugador1.x-jugador1.velocidad>0 && moverJugador()) {
							
								jugador1.x-=jugador1.velocidad;

							
							
						}
						
						
					}
					//derecha
					if(e.getKeyCode()==68) {
						
						jugador1.direccion=2;

						if(jugador1.x+40+jugador1.velocidad<limiteX-5 && moverJugador()) {
							
								jugador1.x+=jugador1.velocidad;
							
	
						}
						

		
					}
					if(e.getKeyCode()==10) {
						
						crearBomba();
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

            
   			//pintar zona de juego
   			
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
   			
   				
            
            //BORDES DEL MAPA
            
            //horizontal arriba
            g.setColor(Color.blue);
            g.fillRect(0, 0, tablero.getWidth(),10);
            //horizontal abajo
            g.setColor(Color.blue);
            g.fillRect(0, limiteY, tablero.getWidth(),10);
            
          //verical izquierda
            g.setColor(Color.blue);
            g.fillRect(5, 0, 10,tablero.getHeight());
            //verical derecha
            g.setColor(Color.blue);
            g.fillRect(limiteX-30, 0, 10,tablero.getHeight());
            
        
            tablero.repaint();

          
            

       }

    }

}

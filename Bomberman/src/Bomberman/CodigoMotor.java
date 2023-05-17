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
	boolean gameOver=false;
	Timer timer = new Timer();


	

	
	//JUGADOR
	static Image imagenJugador= Toolkit.getDefaultToolkit().getImage("personaje1.png");

	/* LO QUE REPRESENTA CADA DATO:
	 * int x,int y,int largo,int alto,int direccion,int velocidad,Image imagen
	 * 
	 *  	0 = ejeX, 1 = ejeY, 2= largo, 3 = alto, 4= direccion , 5= velocidad osea que tanto avanza, 6 = imagen del jugador

		DIRECCION:  0=arriba 1= abajo 2= derecha 3 = izquierda

	 */                      //     0  1  2  3 4  5    6
	Jugador jugador1 = new Jugador(50,50,40,40,0,10,imagenJugador);
	
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
           
        	 quitarBomba();
         }
  };

	public CodigoMotor() {
		initialize();
	}

	public void crearBomba() {
		for(int i=0;i<bombas.length;i++) {
			
				try {
					if( bombas[i]== null || bombas[i].x==0) {
						bombas[i] = new Bomba(jugador1.x,jugador1.y,20,20,imagenBomba);
						break;
					}
				}catch(Exception e) {
					System.out.println("ERROR NO SE PUEDE ACCEDER A AUN VALOR DE UN OBJETO QUE NO Existe... "+e);
				}
			
		}
	}
	
	public void quitarBomba() {
		for(int i=0;i<bombas.length;i++) {
			try {
				//AL CAMBIAR EL VALOR DE CORDENADAS DE UN OBJETO A 0 X=0 O Y=0 ESTO INDICA QUE EL OBJETO ESTA MUERTO O INACTIVO O DESACTIVADO
				//POR LO QUE NO SE PINTARA Y NO HARA NADA NI SE MOSTRARA AL JUGADOR
				if(bombas[i].x!=0) {
					bombas[i].x=0;
					bombas[i].y=0;
					break;
				}
			}catch(Exception e) {
				
			}
		}
	}
	
	private void initialize() {
		
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
				
				if(gameOver!=true) {
					// TODO Auto-generated method stub
					//arriba
					if(e.getKeyCode()==87) {
						if(jugador1.y-jugador1.velocidad>0) {
							for(int i=0;i<4;i++) {
								jugador1.y-=jugador1.velocidad;

							}
							
						}
		
					}
					//abajo
					if(e.getKeyCode()==83) {
						
							if(jugador1.y+40+jugador1.velocidad<limiteY) {
								for(int i=0;i<4;i++) {
									jugador1.y+=jugador1.velocidad;
								}
	
							}	
						
					}
					//izquierda
					if(e.getKeyCode()==65) {
						if(jugador1.x-jugador1.velocidad>0) {
							for(int i=0;i<4;i++) {
								jugador1.x-=jugador1.velocidad;

							}
							
						}
						
						
					}
					//derecha
					if(e.getKeyCode()==68) {
						if(jugador1.x+40+jugador1.velocidad<limiteX-40) {
							for(int i=0;i<4;i++) {
								jugador1.x+=jugador1.velocidad;
							}
	
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

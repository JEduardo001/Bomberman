package Bomberman;

import java.awt.Image;

public class Jugador {
	int x,y,largo,alto,direccion,velocidad;
	Image imagen;
	
	
	public Jugador(int x,int y,int largo,int alto,int direccion,int velocidad,Image imagen) {
		this.x=x;
		this.y=y;
		this.largo=largo;
		this.alto=alto;
		this.direccion=direccion;
		this.velocidad=velocidad;
		this.imagen=imagen;
		
	}
}

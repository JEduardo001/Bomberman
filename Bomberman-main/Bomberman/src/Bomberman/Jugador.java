package Bomberman;

import java.awt.Image;

public class Jugador {
	int x,y,largo,alto,direccion,velocidad,cantBombas;
	Image imagen;
	boolean patearBomba;
	
	public Jugador(int x,int y,int largo,int alto,int direccion,int velocidad,Image imagen,int cantBombas,boolean patearBomba) {
		this.x=x;
		this.y=y;
		this.largo=largo;
		this.alto=alto;
		this.direccion=direccion;
		this.velocidad=velocidad;
		this.imagen=imagen;
		this.cantBombas=cantBombas;
		this.patearBomba=patearBomba;
		}
}

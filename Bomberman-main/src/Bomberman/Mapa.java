package Bomberman;

import java.awt.Image;

public class Mapa {
	int x,y,largo,alto,tipoBloque;
	Image imagen;
	
	
	public Mapa(int x,int y,int largo,int alto,int tipoBloque) {
		this.x=x;
		this.y=y;
		this.largo=largo;
		this.alto=alto;
		this.tipoBloque=tipoBloque;
		//this.imagen=imagen;
		
	}
}

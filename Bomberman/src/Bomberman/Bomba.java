package Bomberman;

import java.awt.Image;

public class Bomba {
	int x,y,largo,alto,tiempo;
	Image imagen;
	
	
	public Bomba(int x,int y,int largo,int alto,Image imagen,int tiempo) {
		this.x=x;
		this.y=y;
		this.largo=largo;
		this.alto=alto;
		this.imagen=imagen;
		this.tiempo=tiempo;
	}
}

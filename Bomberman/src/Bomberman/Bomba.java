package Bomberman;

import java.awt.Image;

public class Bomba {
	int x,y,largo,alto,tiempo,direccion;
	boolean moverBomba;
	Image imagen;
	
	
	public Bomba(int x,int y,int largo,int alto,Image imagen,int tiempo,int direccion,boolean moverBomba) {
		this.x=x;
		this.y=y;
		this.largo=largo;
		this.alto=alto;
		this.imagen=imagen;
		this.tiempo=tiempo;
		this.direccion=direccion;
		this.moverBomba=moverBomba;
	}
}

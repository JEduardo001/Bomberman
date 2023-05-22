package Bomberman;

import java.awt.Image;

public class Bonus {
	int x,y,largo,alto,tipoBonus;
	Image imagen;
	

	
	public Bonus(int x,int y,int largo,int alto,Image imagen,int tipoBonus) {
		this.x=x;
		this.y=y;
		this.largo=largo;
		this.alto=alto;
		this.imagen=imagen;
		this.tipoBonus=tipoBonus;
	
		
	}
	

}

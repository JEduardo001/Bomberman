package Bomberman;

import java.awt.Image;

public class Enemigos {
	int x,y,largo,alto,direccion,velocidad,vida;
	Image imagen;
	
	
	public Enemigos(int x,int y,int largo,int alto,int direccion,int velocidad,int vida,Image imagen) {
		this.x=x;
		this.y=y;
		this.largo=largo;
		this.alto=alto;
		this.direccion=direccion;
		this.velocidad=velocidad;
		this.vida=vida;
		this.imagen=imagen;
		
	}
}

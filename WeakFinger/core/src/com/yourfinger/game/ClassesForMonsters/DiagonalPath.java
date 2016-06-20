package com.yourfinger.game.ClassesForMonsters;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 *двигает по диагонали, ударяясь о стены
 *появляется в случайном месте
 *скорость в Height/мс. передается в mode
 *направление случайно 
 */
public class DiagonalPath implements Path {
	
	
	float x,y;
	float vx,vy;
	float radius;
	

	public DiagonalPath(float radius, int mode) {
		this.radius = radius;
		
		//скорость
		float speed = Gdx.graphics.getHeight()*1000/mode;
	
		//случайное направление
		Random rand = new Random();
		float angle = rand.nextInt(360);
		vx = (float) (speed*Math.cos(angle*Math.PI/180));
		vy = (float) (speed*Math.sin(angle*Math.PI/180));
		
		//случайный старт
		x = (Gdx.graphics.getWidth() - 2*radius)*rand.nextFloat() + radius;
		y = (Gdx.graphics.getHeight() - 2*radius)*rand.nextFloat() + radius;
	}


	@Override
	public void SetCoordinates(float delta, Sprite object) {	
		
		if (radius!=object.getWidth()/2)
			radius = object.getWidth()/2;
		
		x += + delta * vx;
		y += + delta * vy;
		
		if ((x > Gdx.graphics.getWidth() - radius)
				&& (vx > 0))
			vx *= -1;
		if ((x < radius) && (vx < 0))
			vx *= -1;
		if ((y > Gdx.graphics.getHeight() - radius)
				&& (vy > 0))
			vy *= -1;
		if ((y < radius) && (vy < 0))
			vy *= -1;
		
		object.setCenter(x, y);
	}

}

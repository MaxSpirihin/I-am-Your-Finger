package com.yourfinger.game.ExtraClasses;

/**
 * ваш палец, содержит только его координаты x и y. Сделан для передачи монстрам.
 */
public class Finger {
	
	public float x;
	public float y;
	
	public Finger(float x,float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Finger()
	{
		this.x = 0;
		this.y = 0;
	}

}

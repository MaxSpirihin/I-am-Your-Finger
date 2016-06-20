package com.yourfinger.game.ClassesForMonsters;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.yourfinger.game.Bosses.Alpha;
import com.yourfinger.game.ExtraClasses.Finger;
import com.yourfinger.game.ImportantClasses.Constants;

public class StateToFinger implements State {

	final int COUNT_OF_MOVES;
	Alpha alpha;
	Finger finger;
	int numOfMove;
	float vx, vy;
	float acceleration;
	float force;



	public StateToFinger(Alpha alpha,Finger finger, int acceleration, int force, int moves, float speedY) {
		this.alpha = alpha;
		this.finger = finger;
		this.COUNT_OF_MOVES = moves;
		this.numOfMove = 0;
		this.acceleration = acceleration * Gdx.graphics.getHeight()
				/ Constants.STANDART_HEIGHT;
		this.force = force * Gdx.graphics.getHeight() / Constants.STANDART_HEIGHT;
		
		
		alpha.setSize(Gdx.graphics.getWidth() * 0.5f,
				Gdx.graphics.getWidth() * 0.75f);
		alpha.setCenterPosition(new Random().nextInt(Gdx.graphics.getWidth()),
				Gdx.graphics.getHeight() + alpha.getHeight());
		
		
		

		vy = Gdx.graphics.getHeight() / speedY;
		vx = 0;
	}



	@Override
	public void act(float delta) {
		
		alpha.setY(alpha.getY() - delta*vy);

		// ускорение к пальцу
		float ax;
		if (finger.x == alpha.getCenterX())
			ax = 0;
		else if (finger.x > alpha.getCenterX())
			ax = acceleration;
		else
			ax = -acceleration;

		// учет силы трения
		if (vx > 0)
			ax -= force;
		else
			ax += force;

		// вычисляем новые координаты x по правилам из школьной физики
		vx += ax * delta;
		alpha.setX(ax * delta * delta / 2 + vx * delta + alpha.getX());
		

		
		if(alpha.getY() < - alpha.getHeight())
		{
			numOfMove++;
			alpha.setCenterPosition(new Random().nextInt(Gdx.graphics.getWidth()),
					Gdx.graphics.getHeight() + alpha.getHeight());
		}
	}



	@Override
	public void draw(Batch batch) {
	}



	@Override
	public boolean FingerIsDead(Finger finger) {
		return false;
	}



	@Override
	public boolean IsCurrent() {
		return numOfMove <COUNT_OF_MOVES;
	}

}

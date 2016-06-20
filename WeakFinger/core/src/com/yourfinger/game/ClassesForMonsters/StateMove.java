package com.yourfinger.game.ClassesForMonsters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.yourfinger.game.Bosses.Alpha;
import com.yourfinger.game.ExtraClasses.Finger;

public class StateMove implements State {

	Alpha alpha;
	int state;
	float vx, vy;
	boolean isEnd;
	float speed;
	float time;



	public StateMove(Alpha alpha, float speed) {
		this.alpha = alpha;
		this.speed = Gdx.graphics.getHeight() / speed;
		alpha.setSize(Gdx.graphics.getWidth() * 0.5f,
				Gdx.graphics.getWidth() * 0.75f);
		alpha.setCenterPosition(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight());
		vy = -Gdx.graphics.getHeight()/speed;
		vx = 0;
		state = 0;
	}



	@Override
	public void act(float delta) {
		alpha.setY(alpha.getY() + delta * vy);
		alpha.setX(alpha.getX() + delta * vx);

		switch (state) {
		case 0:
			if (alpha.getY() < -alpha.getHeight()) {
				state = 1;
				alpha.setCenterPosition(
						Gdx.graphics.getWidth() - alpha.getWidth() / 2,
						-alpha.getHeight() / 2);
				vy = speed;
				vx = 0;
			}
			break;
		case 1:
			if (alpha.getY() > Gdx.graphics.getHeight()) {
				state = 2;
				alpha.setPosition(0, Gdx.graphics.getHeight());
				vy = -speed;
				vx = 0;
			}
			break;
		case 2:
			if (alpha.getY() < -alpha.getHeight()) {
				state = 3;
				alpha.setCenterPosition(Gdx.graphics.getWidth() / 2,
						-alpha.getHeight() / 2);
				vy = speed;
				vx = 0;
			}
			break;
		case 3:
			if (alpha.getCenterY() > Gdx.graphics.getHeight() / 2) {
				state = 4;
				vy = 0;
				vx = 0;
				time = 0.3f;
			}
			break;
		case 4:
			time -= delta;
			if (time < 0) {
				state = 5;
				vy = 0;
				vx = -speed;
			}
			break;
		case 5:
			if (alpha.getX() < -alpha.getWidth()) {
				state = 6;
				alpha.setY(Gdx.graphics.getHeight() - alpha.getHeight());
				vx = speed;
				vy = 0;
			}
			break;
		case 6:
			if (alpha.getX() > Gdx.graphics.getWidth()) {
				state = 7;
				alpha.setY(Gdx.graphics.getHeight() / 2 - alpha.getHeight() / 2);
				vx = -speed;
				vy = 0;
			}
			break;
		case 7:
			if (alpha.getX() < -alpha.getWidth()) {
				state = 8;
				alpha.setY(0);
				vx = speed;
				vy = 0;
			}
			break;
		case 8:
			if (alpha.getX() > Gdx.graphics.getWidth()) {
				state = 9;
				alpha.setPosition(Gdx.graphics.getWidth(), -alpha.getHeight());
				float line = (float) Math.sqrt(Math
						.pow(Gdx.graphics.getWidth(), 2)
						+ Math.pow(Gdx.graphics.getWidth(), 2));
				vx = -speed * Gdx.graphics.getWidth() / line;
				vy = speed * Gdx.graphics.getHeight() / line;
			}
			break;
		case 9:
			if ((alpha.getX() < -alpha.getWidth())&&(alpha.getY()> Gdx.graphics.getHeight())) {
				state = 10;
				alpha.setPosition(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				float line = (float) Math.sqrt(Math
						.pow(Gdx.graphics.getWidth(), 2)
						+ Math.pow(Gdx.graphics.getWidth(), 2));
				vx = -speed * Gdx.graphics.getWidth() / line;
				vy = -speed * Gdx.graphics.getHeight() / line;
			}
			break;
		case 10:
			if ((alpha.getX() < -alpha.getWidth())&&(alpha.getY()< -alpha.getWidth())) {
				state = 11;
				alpha.setCenterPosition(Gdx.graphics.getWidth() / 2,
						-alpha.getHeight() / 2);
				vy = speed;
				vx = 0;
			}
			break;
		case 11:
			if (alpha.getCenterY() > Gdx.graphics.getHeight() / 2) {
				state = 12;
				vy = 0;
				vx = 0;
				time = 0.5f;
			}
			break;
		case 12:
			time-=delta;
			if (time < 0)
			{
				state = 13;
				vy = speed;
				vx = 0;
			}
		case 13:
			if (alpha.getY() > Gdx.graphics.getHeight())
			{
				isEnd = true;
			}
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
		return !isEnd;
	}

}

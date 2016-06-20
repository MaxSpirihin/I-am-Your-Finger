package com.yourfinger.game.ClassesForMonsters;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.yourfinger.game.Bosses.Alpha;
import com.yourfinger.game.ExtraClasses.Finger;
import com.yourfinger.game.ImportantClasses.Monster;
import com.yourfinger.game.Monsters.BreakingMonster;
import com.yourfinger.game.Monsters.LinearMonster;
import com.yourfinger.game.Monsters.LinearTurnMonster;

public class StateTank implements State {

	// основа
	private final float ALL_TIME = 10f;
	private final float ABS_SPEED;
	Alpha alpha;
	float time;
	float speed;
	Finger finger;
	TextureAtlas atlas;

	// вход
	static final float TIME_ENTER = 0.3f;
	static final float PART_ON_SCREEN = 0.7f;
	float speedEnter;

	// выход
	static final float TIME_EXIT = 0.7f;
	float backSpeed;

	// атаки
	List<Monster> rockets;
	final float MIN_TIME_OF_NEXT_BALL;
	final float MAX_TIME_OF_NEXT_BALL;
	float timeForBall;



	public StateTank(Finger finger, Alpha alpha, TextureAtlas atlas, float MIN_TIME, float MAX_TIME, float alphaSpeed) {
		this.alpha = alpha;
		this.finger = finger;
		this.atlas = atlas;
		MIN_TIME_OF_NEXT_BALL = MIN_TIME;
		MAX_TIME_OF_NEXT_BALL = MAX_TIME;
		ABS_SPEED = Gdx.graphics.getWidth()/alphaSpeed;
		time = ALL_TIME;
		speed = ABS_SPEED;

		alpha.setSize(Gdx.graphics.getWidth() * 0.5f,
				Gdx.graphics.getWidth() * 0.75f);

		// готовимся к възду
		alpha.setCenterPosition(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() + alpha.getHeight() / 2);
		speedEnter = alpha.getHeight() * PART_ON_SCREEN / TIME_ENTER;

		// это для выезда
		backSpeed = (Gdx.graphics.getHeight() + alpha.getHeight()
				* (1 - PART_ON_SCREEN))
				/ TIME_EXIT;

		// атака
		rockets = new LinkedList<Monster>();
		timeForBall = 0;

	}



	@Override
	public void act(float delta) {

		time -= delta;
		for (Monster m : rockets)
			m.act(delta);

		if (time > ALL_TIME - TIME_ENTER) {
			// идет вход
			alpha.setY(alpha.getY() - speedEnter * delta);
		} else if (time > TIME_EXIT) {
			// главная игра

			alpha.setX(alpha.getX() + delta * speed);
			if ((alpha.getCenterX() < 0) && (speed < 0))
				speed *= -1;
			if ((alpha.getCenterX() > Gdx.graphics.getWidth()) && (speed > 0))
				speed *= -1;

			timeForBall -= delta;
			if (timeForBall < 0) {

				int monNum = new Random().nextInt(11);
				if ((monNum == 4)||(monNum == 7)||(monNum == 9))
					monNum = 0;
				if ((monNum == 5)||(monNum == 8)||(monNum == 10))
					monNum = 1;
				if (monNum == 6)
					monNum = 3;

				
				
				
				Monster m = null;
				switch (monNum) {
				case 0:
					m = new LinearMonster(finger, atlas, "zero", 1f,
							new float[] { 0.04f, 0.06f }, new int[] { 0 }, 0, 0);
					m.setCenterPosition(alpha.getCenterX(), alpha.getY());
					break;
				case 1:
					m = new LinearMonster(finger, atlas, "one", 1f,
							new float[] { 0.04f, 0.06f }, new int[] { 0 }, 0, 0);
					m.setCenterPosition(alpha.getCenterX(), alpha.getY());
					break;
				case 2:
					int percent = new Random().nextInt(60);
					m = new BreakingMonster(finger, atlas, "grenade", 1.5f,
							new float[] { 0.1f, 0.05f },
							new int[] { -1,percent ,100 -  percent, -1,
									1, 3000 }, 0, 0);
					m.setCenterPosition(alpha.getCenterX(), alpha.getY());
					break;
				case 3:
					m = new LinearTurnMonster(finger, atlas, "text", 1f,
							new float[] { 0.21f, 0.03f }, new int[] { 0,1 }, 0, 0);
					m.setCenterPosition(alpha.getCenterX(), alpha.getY());
					break;
					
				}

				rockets.add(m);
				timeForBall = new Random().nextFloat()
						* (MAX_TIME_OF_NEXT_BALL - MIN_TIME_OF_NEXT_BALL)
						+ MIN_TIME_OF_NEXT_BALL;

			}

		} else {
			// выход
			alpha.setY(alpha.getY() - backSpeed * delta);
		}
	}



	@Override
	public void draw(Batch batch) {
		for (Monster m : rockets) {
			m.draw(batch, 0);
		}
	}



	@Override
	public boolean FingerIsDead(Finger finger) {
		for (Monster m : rockets) {
			if (m.FingerIsDead())
				return true;
		}
		return false;
	}



	@Override
	public boolean IsCurrent() {
		return time > 0;
	}

}

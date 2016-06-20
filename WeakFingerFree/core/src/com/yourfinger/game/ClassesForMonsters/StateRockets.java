package com.yourfinger.game.ClassesForMonsters;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.yourfinger.game.Bosses.Alpha;
import com.yourfinger.game.ExtraClasses.Finger;
import com.yourfinger.game.ExtraClasses.Utils;
import com.yourfinger.game.ImportantClasses.Monster;
import com.yourfinger.game.Monsters.SelfDirection;

public class StateRockets implements State {

	// основа
	private final float ALL_TIME = 8f;
	private final float WIDTH = Gdx.graphics.getWidth() * 0.5f;
	private final float HEIGHT = Gdx.graphics.getWidth() * 0.75f;

	Alpha alpha;
	Finger finger;
	TextureAtlas atlas;
	float time;

	// вход
	static final float TIME_ENTER = 0.5f;
	static final float PART_ON_SCREEN = 0.5f;
	float speedEnter;

	// выход
	static final float TIME_EXIT = 0.7f;
	float backSpeed;

	// пушки
	static final float GUN_SIZE = Gdx.graphics.getWidth() * 0.5f;
	static final float LEFT_TO_POSITION = GUN_SIZE * 0.2f;
	static final float RIGHT_TO_POSITION = Gdx.graphics.getWidth() - GUN_SIZE
			* 0.2f;
	Sprite gunLeft, gunRight;

	// атаки
	List<SelfDirection> rockets;
	final float MIN_TIME_OF_NEXT_BALL;
	final float MAX_TIME_OF_NEXT_BALL;
	final float START_SPEED = Gdx.graphics.getHeight() / 4f;
	final float TIME_OF_BALL;
	float timeForBall;



	public StateRockets(Alpha alpha, Finger finger, TextureAtlas atlas,
			float MIN_TIME, float MAX_TIME, float BALL_TIME) {
		this.alpha = alpha;
		this.finger = finger;
		this.atlas = atlas;
		alpha.setSize(0, 0);
		alpha.setCenterPosition(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() - HEIGHT / 2);
		
		time = ALL_TIME;

		gunLeft = new Sprite(atlas.findRegion("gun"));
		gunRight = new Sprite(atlas.findRegion("gun"));
		gunLeft.setSize(GUN_SIZE, GUN_SIZE);
		gunRight.setSize(GUN_SIZE, GUN_SIZE);
		gunLeft.setOrigin(GUN_SIZE/2, GUN_SIZE/2);
		gunRight.setOrigin(GUN_SIZE/2, GUN_SIZE/2);
		gunLeft.setCenter(-GUN_SIZE / 2, alpha.getY());
		gunRight.setCenter(Gdx.graphics.getWidth() + GUN_SIZE / 2, alpha.getY());

		MIN_TIME_OF_NEXT_BALL = MIN_TIME;
		MAX_TIME_OF_NEXT_BALL = MAX_TIME;
		TIME_OF_BALL = BALL_TIME;
		rockets = new LinkedList<SelfDirection>();
		timeForBall = 0;
	}



	@Override
	public void act(float delta) {
		time -= delta;

		for (Monster m : rockets)
			m.act(delta);

		if (time < 0)
			alpha.setSize(0, 0);
		else {

			if (time > ALL_TIME - TIME_ENTER) {
				// идет вход
				float x = alpha.getCenterX();
				float y = alpha.getCenterY();
				alpha.setSize(WIDTH * (ALL_TIME - time) / TIME_ENTER, HEIGHT
						* (ALL_TIME - time) / TIME_ENTER);
				alpha.setCenterPosition(x, y);

				gunLeft.setX(-GUN_SIZE + (LEFT_TO_POSITION + GUN_SIZE / 2)
						* (ALL_TIME - time) / TIME_ENTER);
				gunRight.setX(RIGHT_TO_POSITION
						+ (Gdx.graphics.getWidth() + GUN_SIZE / 2 - RIGHT_TO_POSITION)
						* (1 - (ALL_TIME - time) / TIME_ENTER) - GUN_SIZE / 2);

			} else if (time > TIME_EXIT) {
				// главная игра
				timeForBall -= delta;
				if (timeForBall < 0) {

					// левая
					SelfDirection s = new SelfDirection(finger, atlas,
							"rocket", TIME_OF_BALL, new float[] { 0.1f },
							new int[] { 0, 1000, 600 }, 0, 0);
					s.setCenterPosition(
							(float) (Math.sin(gunLeft.getRotation() * Math.PI
									/ 180)
									* (gunLeft.getWidth() / 2 + s.getHeight() / 2)
									+ gunLeft.getX() + gunLeft.getWidth() / 2),
							(float) (-Math.cos(gunLeft.getRotation() * Math.PI
									/ 180)
									* (gunLeft.getWidth() / 2 + s.getHeight() / 2)
									+ gunLeft.getY() + gunLeft.getWidth() / 2));
					s.vy = (float) (-START_SPEED * Math.cos(gunLeft
							.getRotation() * Math.PI / 180));
					s.vx = (float) (START_SPEED * Math.sin(gunLeft
							.getRotation() * Math.PI / 180));
					rockets.add(s);

					// правая
					s = new SelfDirection(finger, atlas, "rocket",
							TIME_OF_BALL, new float[] { 0.1f }, new int[] { 0,
									1000, 600 }, 0, 0);
					s.setCenterPosition(
							(float) (Math.sin(gunRight.getRotation() * Math.PI
									/ 180)
									* (gunRight.getWidth() / 2 + s.getHeight() / 2)
									+ gunRight.getX() + gunRight.getWidth() / 2),
							(float) (-Math.cos(gunRight.getRotation() * Math.PI
									/ 180)
									* (gunRight.getWidth() / 2 + s.getHeight() / 2)
									+ gunRight.getY() + gunRight.getWidth() / 2));
					s.vy = (float) (-START_SPEED * Math.cos(gunRight
							.getRotation() * Math.PI / 180));
					s.vx = (float) (START_SPEED * Math.sin(gunRight
							.getRotation() * Math.PI / 180));
					rockets.add(s);

					timeForBall = new Random().nextFloat()
							* (MAX_TIME_OF_NEXT_BALL - MIN_TIME_OF_NEXT_BALL)
							+ MIN_TIME_OF_NEXT_BALL;
				}

			} else {
				// выход
				float x = alpha.getCenterX();
				float y = alpha.getCenterY();
				alpha.setSize(WIDTH * time / TIME_EXIT, HEIGHT * time
						/ TIME_EXIT);
				alpha.setCenterPosition(x, y);

				gunLeft.setX(-GUN_SIZE + (LEFT_TO_POSITION + GUN_SIZE / 2)
						* time / TIME_EXIT);
				gunRight.setX(RIGHT_TO_POSITION
						+ (Gdx.graphics.getWidth() + GUN_SIZE / 2 - RIGHT_TO_POSITION)
						* (1 - time / TIME_EXIT) - GUN_SIZE / 2);

			}
		}
	}



	@Override
	public void draw(Batch batch) {

		gunLeft.setRotation(Utils.GetAngle(gunLeft.getX() + GUN_SIZE / 2,
				gunLeft.getY() + GUN_SIZE / 2, finger.x, finger.y));
		gunRight.setRotation(Utils.GetAngle(gunRight.getX() + GUN_SIZE / 2,
				gunRight.getY() + GUN_SIZE / 2, finger.x, finger.y));

		gunLeft.draw(batch);
		gunRight.draw(batch);

		for (Monster m : rockets)
			m.draw(batch, 0);
	}



	@Override
	public boolean FingerIsDead(Finger finger) {
		 for (Monster m : rockets)
		 if ((m.FingerIsDead())&&(m.IsCurrent()))
		 return true;
		if (Math.pow(finger.x - gunLeft.getX() - GUN_SIZE / 2, 2)
				+ Math.pow(finger.y - gunLeft.getY() - GUN_SIZE / 2, 2) < Math
					.pow(GUN_SIZE / 2, 2))
			return true;

		return (Math.pow(finger.x - gunRight.getX() - GUN_SIZE / 2, 2)
				+ Math.pow(finger.y - gunRight.getY() - GUN_SIZE / 2, 2) < Math
					.pow(GUN_SIZE / 2, 2));
	}



	@Override
	public boolean IsCurrent() {
		for (Monster m : rockets)
			if (m.IsCurrent())
				return true;
		return time > 0;
	}

}

package com.yourfinger.game.Monsters;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.yourfinger.game.ExtraClasses.Finger;
import com.yourfinger.game.ExtraClasses.Utils;
import com.yourfinger.game.ImportantClasses.Monster;

public class MachineGun extends Monster {

	float time;
	float allTime;
	float timeForNext;
	Finger finger;
	TextureAtlas atlas;
	float[] size;
	String backgroundName;

	TextureRegion txtrMachine;
	Sprite gun;
	TextureRegion txtrBullet;
	final float PART_OF_CENTER;
	final float PART_OF_CIRCLE;

	// вход
	static final float TIME_ENTER = 0.6f;
	float speedEnter;
	static final float PART_ON_SCREEN = 0.75f;

	// выход
	static final float TIME_EXIT = 0.6f;
	float backSpeed;

	// основа
	float gunSpeed;
	ArrayList<Sprite> rockets;
	float timeForBall;
	final float TIME_OF_BALL;
	final float MIN_TIME_OF_NEXT_BALL;
	final float MAX_TIME_OF_NEXT_BALL;



	public MachineGun(Finger finger, TextureAtlas atlas, String backgroundName,
			float time, float[] size, int mode[], float next) {
		// заполняем данные
		this.finger = finger;
		this.time = time;
		this.allTime = time;
		this.atlas = atlas;
		this.size = size;
		this.backgroundName = backgroundName;
		this.timeForNext = time - next;

		txtrMachine = atlas.findRegion(backgroundName);
		txtrBullet = atlas.findRegion(backgroundName + "_bullet");

		this.setSize(Gdx.graphics.getWidth() * size[0], Gdx.graphics.getWidth()
				* size[1]);

		// данные
		PART_OF_CENTER = size[2];
		PART_OF_CIRCLE = size[3];
		gunSpeed = 180 * 1000f / mode[1];
		TIME_OF_BALL = mode[1] * 0.001f;
		MIN_TIME_OF_NEXT_BALL = mode[2] * 0.001f;
		MAX_TIME_OF_NEXT_BALL = mode[3] * 0.001f;

		gun = new Sprite(atlas.findRegion(backgroundName + "_gun"));
		gun.setSize(getWidth() * PART_OF_CIRCLE, getWidth() * PART_OF_CIRCLE);
		gun.setOrigin(gun.getWidth() / 2, gun.getHeight() / 2);

		// готовимся к възду
		setCenterPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight()
				+ this.getHeight() / 2);
		speedEnter = this.getHeight() * PART_ON_SCREEN / TIME_ENTER;

		// это для выезда
		backSpeed = (Gdx.graphics.getHeight() + getHeight()
				* (1 - PART_ON_SCREEN))
				/ TIME_EXIT;

		// основа
		rockets = new ArrayList<Sprite>();

	}



	@Override
	public void draw(Batch batch, float parentAlpha) {
		// машина
		batch.draw(txtrMachine, getX(), getY(), getWidth(), getHeight());

		// пули
		for (Sprite s : rockets)
			s.draw(batch);

		// пулемет
		gun.setCenter(getCenterX(), getY() + getHeight() * PART_OF_CENTER);
		gun.draw(batch);

	}



	@Override
	public boolean IsCurrent() {
		return ((time > 0) || (!NextReady()));
	}



	@Override
	public boolean NextReady() {
		return (time < timeForNext);
	}



	@Override
	public boolean FingerIsDead() {

		for (Sprite rocket : rockets)
			if (Utils
					.PoiinsIsInRectangle(finger.x, finger.y, rocket.getX()
							+ rocket.getOriginX(),
							rocket.getY() + rocket.getOriginY(),
							rocket.getWidth(), rocket.getHeight(),
							rocket.getRotation()))
				return true;

		return ((finger.x >= this.getX())
				&& (finger.x <= this.getX() + this.getWidth())
				&& (finger.y >= this.getY()) && (finger.y <= this.getY()
				+ this.getHeight()));

	}



	@Override
	public void act(float delta) {
		time -= delta;

		if (time > allTime - TIME_ENTER) {
			// идет вход
			setY(getY() - speedEnter * delta);
		} else if (time > TIME_EXIT) {
			// главная игра

			// меняем угол
			gun.setRotation(gun.getRotation() + delta * gunSpeed);
			if ((gun.getRotation() > 95) && (gunSpeed > 0))
				gunSpeed *= -1;
			if ((gun.getRotation() < -95) && (gunSpeed < 0))
				gunSpeed *= -1;

			// двигаем ракеты
			for (Sprite r : rockets) {
				r.setX((float) (r.getX() + (Gdx.graphics.getHeight() / TIME_OF_BALL)
						* Math.sin(r.getRotation() * Math.PI / 180) * delta));
				r.setY((float) (r.getY() - (Gdx.graphics.getHeight() / TIME_OF_BALL)
						* Math.cos(r.getRotation() * Math.PI / 180) * delta));
			}

			timeForBall -= delta;
			if (timeForBall < 0) {

				// создаем 2 новые ракеты и добавляем к списку, если пора
				Sprite s = new Sprite(atlas.findRegion(backgroundName
						+ "_bullet"));
				s.setSize(Gdx.graphics.getWidth() * size[4],
						Gdx.graphics.getWidth() * size[5]);
				s.setCenter(
						(float) (Math.sin(gun.getRotation() * Math.PI / 180)
								* (gun.getWidth() / 2 + s.getHeight() / 2)
								+ gun.getX() + gun.getWidth() / 2),
						(float) (-Math.cos(gun.getRotation() * Math.PI / 180)
								* (gun.getWidth() / 2 + s.getHeight() / 2)
								+ gun.getY() + gun.getWidth() / 2));
				s.setOrigin(s.getWidth() / 2, s.getHeight() / 2);
				s.rotate(gun.getRotation());
				rockets.add(s);

				timeForBall = new Random().nextFloat()
						* (MAX_TIME_OF_NEXT_BALL - MIN_TIME_OF_NEXT_BALL)
						+ MIN_TIME_OF_NEXT_BALL;
			}

		} else {
			// выход

			// двигаем ракеты
			for (Sprite r : rockets) {
				r.setX((float) (r.getX() + (Gdx.graphics.getHeight() / TIME_OF_BALL)
						* Math.sin(r.getRotation() * Math.PI / 180) * delta));
				r.setY((float) (r.getY() - (Gdx.graphics.getHeight() / TIME_OF_BALL)
						* Math.cos(r.getRotation() * Math.PI / 180) * delta));
			}

			setY(getY() - backSpeed * delta);
		}
	}

}

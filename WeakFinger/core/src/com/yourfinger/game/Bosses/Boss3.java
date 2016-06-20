package com.yourfinger.game.Bosses;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.yourfinger.game.ExtraClasses.Finger;
import com.yourfinger.game.ImportantClasses.Boss;
import com.yourfinger.game.ImportantClasses.Monster;
import com.yourfinger.game.Monsters.LinearMonster;
import com.yourfinger.game.Monsters.SelfDirection;

public class Boss3 extends Boss {

	final float SIZE = 0.7f;

	// 1 состо€ние
	final static int STATE_EXPANSION = 0;
	final float TIME_EXPANSION = 0.6f;
	int numberOfExpansion;

	// 2 состо€ние
	final static int STATE_SHOOT_TO_FIRE = 1;
	final float TIME_EXP_TO_FIRE = 0.4f;
	float backSpeed;

	// 3 состо€ние
	final static int STATE_FIRE = 2;
	final float SIZE_HAND = 0.33f;
	final float TIME_FIRE = 8f;
	final int HAND_ACCELERATION = 900;
	final int HAND_FRICTION_FORCE = 400;
	SelfDirection rightHand;
	SelfDirection leftHand;

	// 4 состо€ние
	final static int STATE_FIRE_TO_ATTACK = 3;
	final float TIME_FIRE_TO_ATTACK = 0.2f;
	final float SAVE_ZONE_SIZE = 0.4f;
	Sprite saveZone;

	// 5 состо€ние
	final static int STATE_ATTACK = 4;
	final float TIME_ATTACK = 1.5f;

	// 6 состо€ние
	final static int STATE_FLASH = 5;
	final float TIME_REAL_FLASH = 0.2f;
	final float TIME_FLASH = 1.5f;

	// 7 состо€ние
	final static int STATE_SHOOT = 6;
	List<LinearMonster> balls;
	final float TIME_SHOOT = 7f;
	final float TIME_OF_BALL = 1.1f;
	final float MIN_TIME_OF_NEXT_BALL = 0.25f;
	final float MAX_TIME_OF_NEXT_BALL = 0.85f;
	float timeForBall;
	final float ABS_SPEED = Gdx.graphics.getWidth() / 1f;
	float speed;

	// ключевые объекты
	Finger finger;
	TextureAtlas atlas;

	// тестуры
	TextureRegion txtrBoss;

	// ключевые переменные
	float time;
	int state;
	boolean isEnd = false;



	public Boss3(Finger finger, TextureAtlas atlas) {
		this.finger = finger;
		this.atlas = atlas;
		lives = 3;
		txtrBoss = atlas.findRegion("boss");
		SetState(STATE_EXPANSION);

	}



	@Override
	public boolean FingerIsDead() {
		switch (state) {
		case STATE_FIRE:
			for (Monster m : balls) {
				if (m.FingerIsDead())
					return true;
			}
			return ((Math.pow(finger.x - this.getCenterX(), 2)
					+ Math.pow(finger.y - this.getCenterY(), 2) < Math.pow(
					this.getWidth() / 2, 2))
					|| (leftHand.FingerIsDead()) || (rightHand.FingerIsDead()));
		case STATE_SHOOT_TO_FIRE:
		case STATE_SHOOT:
			for (Monster m : balls) {
				if (m.FingerIsDead())
					return true;
			}
			return (Math.pow(finger.x - this.getCenterX(), 2)
					+ Math.pow(finger.y - this.getCenterY(), 2) < Math.pow(
					this.getWidth() / 2, 2));
		case STATE_ATTACK:
			return false;
		case STATE_FLASH:
			return false;
		default:
			return (Math.pow(finger.x - this.getCenterX(), 2)
					+ Math.pow(finger.y - this.getCenterY(), 2) < Math.pow(
					this.getWidth() / 2, 2));
		}
	}



	@Override
	public void draw(Batch batch, float parentAlpha) {

		switch (state) {
		case STATE_FIRE:
			batch.draw(txtrBoss, getX(), getY(), getWidth(), getHeight());
			for (Monster m : balls) {
				m.draw(batch, parentAlpha);
			}
			leftHand.draw(batch, parentAlpha);
			rightHand.draw(batch, parentAlpha);
			break;
		case STATE_FIRE_TO_ATTACK:
		case STATE_ATTACK:
			batch.draw(txtrBoss, getX(), getY(), getWidth(), getHeight());
			saveZone.draw(batch);
			break;
		case STATE_FLASH:
			if ((time > TIME_FLASH - TIME_REAL_FLASH) || (lives == 0))
				batch.draw(atlas.findRegion("daemon_save_bright"),
						-Gdx.graphics.getWidth(), -Gdx.graphics.getHeight(),
						Gdx.graphics.getWidth() * 5,
						Gdx.graphics.getWidth() * 5);
			break;
		case STATE_SHOOT_TO_FIRE:
		case STATE_SHOOT:
			batch.draw(txtrBoss, getX(), getY(), getWidth(), getHeight());
			for (Monster m : balls) {
				m.draw(batch, parentAlpha);
			}
			break;
		default:
			batch.draw(txtrBoss, getX(), getY(), getWidth(), getHeight());
			;
		}
	}



	public boolean IsDead() {
		return isEnd;
	}



	public boolean FingerCanBeLose() {
		return ((lives <= 0) && (state == STATE_FLASH));
	}



	private void SetState(int state) {
		this.state = state;
		switch (state) {
		case STATE_EXPANSION:
			numberOfExpansion = 0;
			time = TIME_EXPANSION;
			this.setSize(0, 0);
			setCenterPosition(Gdx.graphics.getWidth() / 2,
					Gdx.graphics.getHeight() - Gdx.graphics.getWidth() * SIZE
							* 0.2f);
			break;
		case STATE_SHOOT_TO_FIRE:
			time = TIME_EXP_TO_FIRE;
			backSpeed = (Gdx.graphics.getWidth() / 2 - this.getCenterX())
					/ TIME_EXP_TO_FIRE;
			break;
		case STATE_FIRE:
			time = TIME_FIRE;
			rightHand = new SelfDirection(finger, atlas, "boss_right",
					TIME_FIRE, new float[] { SIZE_HAND }, new int[] { 1,
							(int) (HAND_ACCELERATION * 1.5f),
							(int) (HAND_FRICTION_FORCE * 1.7f) }, 0, 0);
			leftHand = new SelfDirection(finger, atlas, "boss_left", TIME_FIRE,
					new float[] { SIZE_HAND }, new int[] { 100,
							HAND_ACCELERATION, HAND_FRICTION_FORCE }, 0, 0);
			break;
		case STATE_FIRE_TO_ATTACK:
			time = TIME_FIRE_TO_ATTACK;
			saveZone = new Sprite(atlas.findRegion("spike_light"));
			saveZone.setCenter(finger.x, finger.y);
			saveZone.setSize(0, 0);
			break;
		case STATE_ATTACK:
			time = TIME_ATTACK;
			break;
		case STATE_FLASH:
			time = TIME_FLASH;
			break;
		case STATE_SHOOT:
			balls = new LinkedList<LinearMonster>();
			time = TIME_SHOOT;
			setSize(Gdx.graphics.getWidth() * SIZE, Gdx.graphics.getWidth()
					* SIZE);
			setCenterPosition(Gdx.graphics.getWidth() / 2,
					Gdx.graphics.getHeight() - Gdx.graphics.getWidth() * SIZE
							* 0.2f);
			timeForBall = 0;
			speed = ABS_SPEED;
			break;
		default:
			break;
		}
	}



	@Override
	public boolean IsAttacked() {
		return state == STATE_ATTACK;
	}



	@Override
	public void act(float delta) {
		time -= delta;

		switch (state) {
		case STATE_EXPANSION:
			ExpansionAct();
			break;
		case STATE_SHOOT_TO_FIRE:
			ShootToFireAct(delta);
			break;
		case STATE_FIRE:
			FireAct(delta);
			break;
		case STATE_FIRE_TO_ATTACK:
			FireToAttackAct();
			break;
		case STATE_ATTACK:
			AttackAct(delta);
			break;
		case STATE_FLASH:
			if (time < 0) {
				if (lives > 0)
					SetState(STATE_EXPANSION);
				else
					isEnd = true;
			}
			break;
		case STATE_SHOOT:
			ShootAct(delta);
			break;
		}
	}



	private void ExpansionAct() {
		// будем расшир€тьс€ сохран€€ центр
		float centerX = getCenterX();
		float centerY = getCenterY();

		float percent = 1 - time / TIME_EXPANSION;

		this.setSize(Gdx.graphics.getWidth() * SIZE * percent,
				Gdx.graphics.getWidth() * SIZE * percent);
		this.setCenterPosition(centerX, centerY);

		if (time < 0)
			SetState(STATE_SHOOT);
	}



	private void ShootToFireAct(float delta) {
		for (Monster m : balls)
			m.act(delta);
		
		setX(getX() + delta * backSpeed);

		if (time < 0)
			SetState(STATE_FIRE);
	}



	private void FireAct(float delta) {
		for (Monster m : balls)
			m.act(delta);
		
		leftHand.act(delta);
		rightHand.act(delta);

		if (Math.pow(leftHand.getCenterX() - rightHand.getCenterX(), 2)
				+ Math.pow(leftHand.getCenterY() - rightHand.getCenterY(), 2) < Math
					.pow(leftHand.getWidth(), 2)) {
			// руки столкнулись
			if (leftHand.getCenterX() < rightHand.getCenterX()) {
				leftHand.setX(leftHand.getX() - 5);
				rightHand.setX(rightHand.getX() + 5);
			} else {
				leftHand.setX(leftHand.getX() + 5);
				rightHand.setX(rightHand.getX() - 5);
			}

			if (leftHand.getCenterY() < rightHand.getCenterY()) {
				leftHand.setY(leftHand.getY() - 5);
				rightHand.setY(rightHand.getY() + 5);
			} else {
				leftHand.setY(leftHand.getY() + 5);
				rightHand.setY(rightHand.getY() - 5);
			}
		}

		if (time < 0)
			SetState(STATE_FIRE_TO_ATTACK);
	}



	private void FireToAttackAct() {
		float percent = 1 - time / TIME_FIRE_TO_ATTACK;

		saveZone.setSize(Gdx.graphics.getWidth() * SAVE_ZONE_SIZE * percent,
				Gdx.graphics.getWidth() * SAVE_ZONE_SIZE * percent);
		saveZone.setCenter(finger.x, finger.y);

		if (time < 0)
			SetState(STATE_ATTACK);
	}



	private void AttackAct(float delta) {
		
		saveZone.setCenter(finger.x, finger.y);

		float centerX = getCenterX();
		float centerY = getCenterY();

		float percent = time / TIME_ATTACK;

		this.setSize(Gdx.graphics.getWidth() * SIZE * percent,
				Gdx.graphics.getWidth() * SIZE * percent);
		this.setCenterPosition(centerX, centerY);

		if (Math.pow(
				saveZone.getX() + saveZone.getWidth() / 2 - this.getCenterX(),
				2)
				+ Math.pow(
						saveZone.getY() + saveZone.getWidth() / 2
								- this.getCenterY(), 2) < Math.pow(
				this.getWidth() / 2 + saveZone.getWidth() / 2, 2)) {
			Gdx.input.vibrate(100);
			lives--;
			SetState(STATE_FLASH);
		} else if (time < 0)
			SetState(STATE_EXPANSION);// не успел атаковать

	}



	private void ShootAct(float delta) {

		// двигаем босса
		setX(getX() + delta * speed);
		if ((getCenterX() < 0) && (speed < 0))
			speed *= -1;
		if ((getCenterX() > Gdx.graphics.getWidth()) && (speed > 0))
			speed *= -1;

		timeForBall -= delta;
		for (Monster m : balls)
			m.act(delta);
		if (timeForBall < 0) {
			LinearMonster m = new LinearMonster(finger, atlas, "fire",
					TIME_OF_BALL, new float[] { 0.2f }, new int[] { 0, 1 }, 0,
					0);
			m.setCenterPosition(getCenterX(), Gdx.graphics.getHeight()
					- Gdx.graphics.getWidth() * SIZE * 0.5f);
			balls.add(m);
			timeForBall = new Random().nextFloat()
					* (MAX_TIME_OF_NEXT_BALL - MIN_TIME_OF_NEXT_BALL)
					+ MIN_TIME_OF_NEXT_BALL;
		}

		if (time < 0)
			SetState(STATE_SHOOT_TO_FIRE);
	}

}

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
import com.yourfinger.game.ExtraClasses.Utils;
import com.yourfinger.game.ImportantClasses.Boss;
import com.yourfinger.game.ImportantClasses.Monster;
import com.yourfinger.game.Monsters.LinearMonster;
import com.yourfinger.game.Monsters.SelfDirection;

public class Boss3 extends Boss {

	final float WIDTH = 0.5f;
	final float HEIGHT = 1.4f*WIDTH;

	// 1 состо€ние
	final static int STATE_EXPANSION = 0;
	final float TIME_EXPANSION = .6f;
	int numberOfExpansion;

	// 2 состо€ние
	final static int STATE_SHOOT_TO_FIRE = 1;
	final float TIME_EXP_TO_FIRE = 0.4f;
	float backSpeed;

	// 3 состо€ние
	final static int STATE_FIRE = 2;
	final float SIZE_HAND = 0.25f;
	final float TIME_FIRE = 5.5f;
	final int HAND_ACCELERATION = 700;
	final int HAND_FRICTION_FORCE = 350;
	SelfDirection rightHand;
	SelfDirection leftHand;

	// 4 состо€ние
	final static int STATE_FIRE_TO_ATTACK = 3;
	Sprite blaster;
	float vx;
	float vy;

	// 6 состо€ние
	final static int STATE_FLASH = 5;
	final float TIME_REAL_FLASH = 0.2f;
	final float TIME_FLASH = 1.5f;

	// 7 состо€ние
	final static int STATE_SHOOT = 6;
	List<LinearMonster> balls;
	final float TIME_SHOOT = 7f;
	final float TIME_OF_BALL = 1f;
	final float MIN_TIME_OF_NEXT_BALL = .6f;
	final float MAX_TIME_OF_NEXT_BALL = 1f;
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
			return ((Math.pow((finger.x - this.getCenterX())*2/getWidth(), 2)
					+ Math.pow((finger.y - this.getCenterY())*2/getHeight(), 2) < 1)
					|| (leftHand.FingerIsDead()) || (rightHand.FingerIsDead()));
		case STATE_SHOOT_TO_FIRE:
		case STATE_SHOOT:
			for (Monster m : balls) {
				if (m.FingerIsDead())
					return true;
			}
			return (Math.pow((finger.x - this.getCenterX())*2/getWidth(), 2)
					+ Math.pow((finger.y - this.getCenterY())*2/getHeight(), 2) < 1);
		case STATE_FLASH:
			return false;
		default:
			return (Math.pow((finger.x - this.getCenterX())*2/getWidth(), 2)
					+ Math.pow((finger.y - this.getCenterY())*2/getHeight(), 2) < 1);
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
			batch.draw(txtrBoss, getX(), getY(), getWidth(), getHeight());
			blaster.draw(batch, parentAlpha);
		case STATE_FLASH:
			if ((time > TIME_FLASH - TIME_REAL_FLASH) || (lives == 0))
				batch.draw(atlas.findRegion("daemon_save_bright"),
						getX(), getY(),
						getHeight(),
						getHeight());
			break;
		case STATE_SHOOT_TO_FIRE:
		case STATE_SHOOT:
			for (Monster m : balls) {
				m.draw(batch, parentAlpha);
			}
			batch.draw(txtrBoss, getX(), getY(), getWidth(), getHeight());
			
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
					Gdx.graphics.getHeight() - Gdx.graphics.getWidth() * HEIGHT
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
							(int) (HAND_ACCELERATION*1.4),
							(int) (HAND_FRICTION_FORCE*1.6) }, 0, 0);
			leftHand = new SelfDirection(finger, atlas, "boss_left", TIME_FIRE,
					new float[] { SIZE_HAND }, new int[] { 100,
							HAND_ACCELERATION, HAND_FRICTION_FORCE }, 0, 0);
			break;
		case STATE_FIRE_TO_ATTACK:
			blaster = new Sprite(atlas.findRegion("self_blaster"));
			blaster.setSize(Gdx.graphics.getWidth()*0.1f, Gdx.graphics.getHeight()*0.1f);
			blaster.setOrigin(blaster.getWidth()/2, blaster.getWidth()/2);
			blaster.setRotation(Utils.GetAngle(finger.x, finger.y, getCenterX(), getCenterY()));
			blaster.setCenter(finger.x, finger.y);
			vx = (getCenterX() - blaster.getX() - blaster.getOriginX())/0.5f;
			vy = (getCenterY() - blaster.getY() - blaster.getOriginY())/0.5f;
			break;

		case STATE_FLASH:
			time = TIME_FLASH;
			break;
		case STATE_SHOOT:
			balls = new LinkedList<LinearMonster>();
			time = TIME_SHOOT;
			setSize(Gdx.graphics.getWidth() * WIDTH, Gdx.graphics.getWidth()
					* HEIGHT);
			setCenterPosition(Gdx.graphics.getWidth() / 2,
					Gdx.graphics.getHeight() - Gdx.graphics.getWidth() * HEIGHT
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
		return false;
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
			FireToAttackAct(delta);
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

		this.setSize(Gdx.graphics.getWidth() * WIDTH * percent,
				Gdx.graphics.getWidth() * HEIGHT * percent);
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



	private void FireToAttackAct(float delta) {
		blaster.setY(blaster.getY() + delta * vy);
		blaster.setX(blaster.getX() + delta * vx);
		
		if (Math.pow((blaster.getX() + blaster.getWidth()/2 - this.getCenterX())*2/(getWidth()+blaster.getWidth()), 2)
				+ Math.pow((blaster.getY() + blaster.getHeight()/2 - this.getCenterY())*2/(getHeight()+blaster.getWidth()), 2) < 1)
		{
			lives--;
			SetState(STATE_FLASH);
		}
	}



//	private void AttackAct(float delta) {
//		
//		saveZone.setCenter(finger.x, finger.y);
//
//		float centerX = getCenterX();
//		float centerY = getCenterY();
//
//		float percent = time / TIME_ATTACK;
//
//		this.setSize(Gdx.graphics.getWidth() * WIDTH * percent,
//				Gdx.graphics.getWidth() * HEIGHT * percent);
//		this.setCenterPosition(centerX, centerY);
//
//		if (Math.pow(
//				saveZone.getX() + saveZone.getWidth() / 2 - this.getCenterX(),
//				2)
//				+ Math.pow(
//						saveZone.getY() + saveZone.getWidth() / 2
//								- this.getCenterY(), 2) < Math.pow(
//				this.getWidth() / 2 + saveZone.getWidth() / 2, 2)) {
//			Gdx.input.vibrate(100);
//			lives--;
//			SetState(STATE_FLASH);
//		} else if (time < 0)
//			SetState(STATE_EXPANSION);// не успел атаковать
//
//	}



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
					TIME_OF_BALL, new float[] { 0.1f }, new int[] { 0}, 0,
					0);
			m.setCenterPosition(getCenterX(), Gdx.graphics.getHeight()
					- Gdx.graphics.getWidth() * WIDTH * 0.5f);
			balls.add(m);
			timeForBall = new Random().nextFloat()
					* (MAX_TIME_OF_NEXT_BALL - MIN_TIME_OF_NEXT_BALL)
					+ MIN_TIME_OF_NEXT_BALL;
		}

		if (time < 0)
			SetState(STATE_SHOOT_TO_FIRE);
	}

}

package com.yourfinger.game.Bosses;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.yourfinger.game.ClassesForMonsters.GrenadeForGitler;
import com.yourfinger.game.ExtraClasses.Finger;
import com.yourfinger.game.ExtraClasses.Utils;
import com.yourfinger.game.ImportantClasses.Boss;

public class Boss4 extends Boss {

	// основа
	TextureRegion txtrBoss;
	int state;
	Finger finger;
	float time;
	TextureAtlas atlas;

	// 1 состояние - атака пулеметом
	private static final int STATE_GUN = 1;
	private static final float TIME_GUN = 4f;
	private static final float GUN_ANGULAR_SPEED = 180 / 1f;
	private static final float TIME_OF_ROCKET = 1.4f;
	private static final float MIN_TIME_OF_NEXT_ROCKET = 0.1f;
	private static final float MAX_TIME_OF_NEXT_ROCKET = 0.2f;
	private static final float TIME_FOR_LAST_ROCKET = 0.8f;
	float gunAngularSpeed;
	Sprite gun;
	ArrayList<Sprite> rockets;
	float timeForRocket;

	// 2 состояние - атака руками
	private static final int STATE_HAND = 2;
	private static final int COUNT_OF_HAND_ATTACK = 10;
	private static final float HAND_START_SIZE = Gdx.graphics.getWidth() * 0.15f;
	private static final float HAND_START_POSITION = Gdx.graphics.getHeight()
			- Gdx.graphics.getWidth() * 0.22f;
	private static final float HAND_SPEED = Gdx.graphics.getHeight() / 0.25f;
	private static final float HAND_EXPANSION_SPEED = (Gdx.graphics.getWidth() / 2 - HAND_START_SIZE / 2)
			* HAND_SPEED / (HAND_START_POSITION - HAND_START_SIZE / 2);
	private static final float SPRING_WIDTH = Gdx.graphics.getWidth() * 0.1f;
	TextureRegion txtrSpring;
	int currentHand;
	boolean isBack;
	Sprite rightHand;
	Sprite leftHand;

	// 3 состояние - гранаты
	private static final int STATE_GRENADE = 3;
	private static final float TIME_GRENADE = 4f;
	private static final float TIME_OF_GRENADE = 1.7f;
	private static final float MIN_TIME_OF_NEXT_GRENADE = 0.4f;
	private static final float MAX_TIME_OF_NEXT_GRENADE = 0.8f;
	ArrayList<GrenadeForGitler> grenades;
	float timeForGrenade;

	// 4 состояние - подение бомбы
	private static final int STATE_BOMB = 4;
	private static final float TIME_BOMB = 1.2f;
	float bombSpeed;
	Sprite bomb;

	// 5 состояние - взрыв
	private static final int STATE_BOOM = 5;
	private static final float TIME_BOOM = 0.15f;
	private static final float FINAL_TIME_BOOM = 2f;
	private static final float BOOM_MAX_SIZE = Gdx.graphics.getWidth()*1.5f;
	private static final float DEAD_MAX_SIZE = Gdx.graphics.getWidth();
	Sprite boom;
	Sprite dead;
	boolean isDead = false;



	public Boss4(Finger finger, TextureAtlas atlas) {

		this.finger = finger;
		this.atlas = atlas;
		lives = 3;

		// инициализируем босса
		txtrBoss = atlas.findRegion("boss");
		setSize(Gdx.graphics.getWidth(), Gdx.graphics.getWidth() * 0.8f);
		setPosition(0, Gdx.graphics.getHeight() - getHeight());

		// инициализируем пулемет
		gun = new Sprite(atlas.findRegion("boss_gun"));
		gun.setSize(Gdx.graphics.getWidth() * 0.6f,
				Gdx.graphics.getWidth() * 0.6f);
		gun.setOrigin(gun.getWidth() / 2, gun.getHeight() / 2);
		gun.setPosition(-getWidth() * 0.22f, getY() - getHeight() * 0.05f);

		// инициализируем перчатки (левый и правый - как мы видим экран)
		leftHand = new Sprite(atlas.findRegion("boss_hand_right"));
		rightHand = new Sprite(atlas.findRegion("boss_hand_left"));
		leftHand.setSize(HAND_START_SIZE, HAND_START_SIZE);
		rightHand.setSize(HAND_START_SIZE, HAND_START_SIZE);
		leftHand.setPosition(
				Gdx.graphics.getWidth() * 0.25f - leftHand.getWidth() / 2,
				HAND_START_POSITION);
		rightHand.setPosition(
				Gdx.graphics.getWidth() * 0.75f - rightHand.getWidth() / 2,
				HAND_START_POSITION);
		// пружина
		txtrSpring = atlas.findRegion("boss_spring");

		// списки
		rockets = new ArrayList<Sprite>();
		grenades = new ArrayList<GrenadeForGitler>();

		// бомба и взрыв
		bomb = new Sprite(atlas.findRegion("boss_bomb"));
		boom = new Sprite(atlas.findRegion("boss_boom"));
		dead = new Sprite(atlas.findRegion("boss_dead"));

		// ставим начальное состояние
		SetState(STATE_GUN);
	}



	@Override
	public boolean FingerIsDead() {

		// столкновение с ракетами
		for (Sprite r : rockets)
			if (Utils.PoiinsIsInRectangle(finger.x, finger.y,
					r.getX() + r.getOriginX(), r.getY() + r.getOriginY(),
					r.getWidth(), r.getHeight(), r.getRotation()))
				return true;

		// смерть от гранат
		for (GrenadeForGitler g : grenades)
			if (g.FingerIsDead())
				return true;

		// столкновение с перчатками
		if (Math.pow(finger.x - leftHand.getX() - leftHand.getWidth() / 2, 2)
				+ Math.pow(finger.y - leftHand.getY() - leftHand.getHeight()
						/ 2, 2) < Math.pow(leftHand.getWidth() / 2, 2))
			return true;
		if (Math.pow(finger.x - rightHand.getX() - rightHand.getWidth() / 2, 2)
				+ Math.pow(finger.y - rightHand.getY() - rightHand.getHeight()
						/ 2, 2) < Math.pow(rightHand.getWidth() / 2, 2))
			return true;

		// столкновение с самим боссом
		return (finger.y > getY());
	}



	@Override
	public void draw(Batch batch, float parentAlpha) {

		// рисуем самого босса
		batch.draw(txtrBoss, getX(), getY(), getWidth(), getHeight());

		// рисуем пушку
		gun.draw(batch);

		// рисуем пружинки
		batch.draw(txtrSpring, leftHand.getX() + leftHand.getWidth() / 2
				- SPRING_WIDTH / 2, leftHand.getY() + leftHand.getHeight() / 2,
				SPRING_WIDTH, HAND_START_POSITION + HAND_START_SIZE / 2
						- leftHand.getY() - leftHand.getHeight() / 2);
		batch.draw(txtrSpring, rightHand.getX() + rightHand.getWidth() / 2
				- SPRING_WIDTH / 2, rightHand.getY() + rightHand.getHeight()
				/ 2, SPRING_WIDTH, HAND_START_POSITION + HAND_START_SIZE / 2
				- rightHand.getY() - rightHand.getHeight() / 2);

		// рисуем печатки
		leftHand.draw(batch);
		rightHand.draw(batch);

		// рисуем ракеты
		for (Sprite r : rockets)
			r.draw(batch);

		// рисуем гранаты
		for (GrenadeForGitler g : grenades)
			g.draw(batch, parentAlpha);

		if (state == STATE_BOMB)
			bomb.draw(batch);

		if (state == STATE_BOOM)
			boom.draw(batch);

		if (lives <= 0)
			dead.draw(batch);

	}



	public boolean IsDead() {
		return isDead;
	}



	public boolean FingerCanBeLose() {
		return lives <= 0;
	}



	private void SetState(int state) {
		this.state = state;

		switch (state) {
		case STATE_GUN:
			time = TIME_GUN;
			gunAngularSpeed = GUN_ANGULAR_SPEED;
			rockets.clear();
			break;
		case STATE_HAND:
			currentHand = 0;
			isBack = false;
			break;
		case STATE_GRENADE:
			time = TIME_GRENADE;
			grenades.clear();
			timeForGrenade = 0;
			break;
		case STATE_BOMB:
			time = TIME_BOMB;
			bomb.setSize(Gdx.graphics.getWidth() * 0.4f,
					Gdx.graphics.getWidth() * 0.4f);
			bomb.setPosition(Gdx.graphics.getWidth() / 2 - bomb.getWidth() / 2,
					Gdx.graphics.getHeight());
			bombSpeed = (getHeight() / 2 + bomb.getHeight() / 2) / TIME_BOMB;
			break;
		case STATE_BOOM:
			if (lives > 0)
				time = TIME_BOOM;
			else
				time = FINAL_TIME_BOOM;
			boom.setSize(0, 0);
			dead.setSize(0, 0);
			boom.setPosition(bomb.getX() + bomb.getWidth() / 2, bomb.getY()
					+ bomb.getHeight() / 2);
			dead.setPosition(bomb.getX() + bomb.getWidth() / 2,
					bomb.getY() + bomb.getHeight() / 2);
			break;
		}

	}



	@Override
	public void act(float delta) {

		GeneralAct(delta);

		switch (state) {
		case STATE_GUN:
			ActStateGun(delta);
			break;
		case STATE_HAND:
			ActStateHand(delta);
			break;
		case STATE_GRENADE:
			ActStateGrenade(delta);
			break;
		case STATE_BOMB:
			time -= delta;
			bomb.setY(bomb.getY() - delta * bombSpeed);
			if (time < 0) {
				lives--;
				SetState(STATE_BOOM);
			}
			break;
		case STATE_BOOM:
			time -= delta;
			float moveTime = lives>0 ? TIME_BOOM : FINAL_TIME_BOOM;
			boom.setSize(boom.getWidth() + delta * BOOM_MAX_SIZE / moveTime,
					boom.getHeight() + delta * BOOM_MAX_SIZE / moveTime);
			boom.setPosition(boom.getX() - delta * BOOM_MAX_SIZE
					/ (2 * moveTime), boom.getY() - delta * BOOM_MAX_SIZE
					/ (2 * moveTime));
			
			dead.setSize(dead.getWidth() + delta * DEAD_MAX_SIZE / moveTime,
					dead.getHeight() + delta * DEAD_MAX_SIZE / moveTime);
			dead.setPosition(dead.getX() - delta * DEAD_MAX_SIZE
					/ (2 * moveTime), dead.getY() - delta * DEAD_MAX_SIZE
					/ (2 * moveTime));
			
			if (time < 0)
			{
				if (lives >0)
					SetState(STATE_GUN);
				else
					isDead = true;
			}
			break;
		}
	}



	private void GeneralAct(float delta) {
		// двигаем ракеты
		for (Sprite r : rockets) {
			r.setX((float) (r.getX() + (Gdx.graphics.getHeight() / TIME_OF_ROCKET)
					* Math.sin(r.getRotation() * Math.PI / 180) * delta));
			r.setY((float) (r.getY() - (Gdx.graphics.getHeight() / TIME_OF_ROCKET)
					* Math.cos(r.getRotation() * Math.PI / 180) * delta));
		}

		for (GrenadeForGitler g : grenades)
			g.act(delta);
	}



	private void ActStateGun(float delta) {
		time -= delta;
		gun.setRotation(gun.getRotation() + delta * gunAngularSpeed);
		if ((gun.getRotation() < -20) && (gunAngularSpeed < 0))
			gunAngularSpeed *= -1;
		if ((gun.getRotation() > 90) && (gunAngularSpeed > 0))
			gunAngularSpeed *= -1;

		timeForRocket -= delta;
		if (timeForRocket < 0) {

			// создаем ракету и добавляем к списку, если пора
			Sprite s = new Sprite(atlas.findRegion("boss_bullet"));
			s.setSize(Gdx.graphics.getWidth() * 0.07f,
					Gdx.graphics.getWidth() * 0.14f);
			s.setCenter(
					(float) (Math.sin(gun.getRotation() * Math.PI / 180)
							* (gun.getWidth() / 2 + s.getHeight() / 2)
							+ gun.getX() + gun.getWidth() / 2),
					(float) (-Math.cos(gun.getRotation() * Math.PI / 180)
							* (gun.getWidth() / 2 + s.getHeight() / 2)
							+ gun.getY() + gun.getWidth() / 2));
			s.setOrigin(s.getWidth() / 2, s.getHeight() / 2);
			s.rotate(gun.getRotation());
			
			if (time > TIME_FOR_LAST_ROCKET)
			rockets.add(s);

			timeForRocket = new Random().nextFloat()
					* (MAX_TIME_OF_NEXT_ROCKET - MIN_TIME_OF_NEXT_ROCKET)
					+ MIN_TIME_OF_NEXT_ROCKET;
		}

		if (time < 0)
			SetState(STATE_HAND);

	}



	private void ActStateHand(float delta) {
		if (currentHand % 2 == 0) {
			// левая рука
			if (isBack) {
				// возвращение левой
				leftHand.setY(leftHand.getY() + delta * HAND_SPEED);
				leftHand.setSize(leftHand.getWidth() - delta
						* HAND_EXPANSION_SPEED, leftHand.getWidth() - delta
						* HAND_EXPANSION_SPEED);
				leftHand.setX(leftHand.getX() + delta * HAND_EXPANSION_SPEED
						/ 2);

				// проверка на конец
				if (leftHand.getY() > HAND_START_POSITION) {
					leftHand.setY(HAND_START_POSITION);
					leftHand.setSize(HAND_START_SIZE, HAND_START_SIZE);
					leftHand.setPosition(Gdx.graphics.getWidth() * 0.25f
							- leftHand.getWidth() / 2, HAND_START_POSITION);
					currentHand++;
					isBack = false;
				}

			} else {
				leftHand.setY(leftHand.getY() - delta * HAND_SPEED);
				leftHand.setSize(leftHand.getWidth() + delta
						* HAND_EXPANSION_SPEED, leftHand.getWidth() + delta
						* HAND_EXPANSION_SPEED);
				leftHand.setX(leftHand.getX() - delta * HAND_EXPANSION_SPEED
						/ 2);

				// проверка на конец
				if (leftHand.getY() < 0)
					isBack = true;
			}
		} else {
			// правая рука
			if (isBack) {
				// возвращение правой
				rightHand.setY(rightHand.getY() + delta * HAND_SPEED);
				rightHand.setSize(rightHand.getWidth() - delta
						* HAND_EXPANSION_SPEED, rightHand.getWidth() - delta
						* HAND_EXPANSION_SPEED);
				rightHand.setX(rightHand.getX() + delta * HAND_EXPANSION_SPEED
						/ 2);

				// проверка на конец
				if (rightHand.getY() > HAND_START_POSITION) {
					rightHand.setY(HAND_START_POSITION);
					rightHand.setSize(HAND_START_SIZE, HAND_START_SIZE);
					rightHand.setPosition(Gdx.graphics.getWidth() * 0.75f
							- rightHand.getWidth() / 2, HAND_START_POSITION);
					currentHand++;
					isBack = false;
				}

			} else {
				rightHand.setY(rightHand.getY() - delta * HAND_SPEED);
				rightHand.setSize(rightHand.getWidth() + delta
						* HAND_EXPANSION_SPEED, rightHand.getWidth() + delta
						* HAND_EXPANSION_SPEED);
				rightHand.setX(rightHand.getX() - delta * HAND_EXPANSION_SPEED
						/ 2);

				// проверка на конец
				if (rightHand.getY() < 0)
					isBack = true;
			}
		}

		if (currentHand > COUNT_OF_HAND_ATTACK)
			SetState(STATE_GRENADE);
	}



	private void ActStateGrenade(float delta) {
		time -= delta;
		timeForGrenade -= delta;
		if (timeForGrenade < 0) {

			// создаем гранаты и добавляем к списку, если пора
			GrenadeForGitler g = new GrenadeForGitler(finger, atlas, "grenade",
					TIME_OF_GRENADE, new float[] { 0.14f, 0.055f },
					Gdx.graphics.getWidth() * 0.8f, getY() + getHeight()
							* 0.04f, Gdx.graphics.getWidth() / 2, getY()
							- getHeight() * 0.5f);
			grenades.add(g);

			timeForGrenade = new Random().nextFloat()
					* (MAX_TIME_OF_NEXT_GRENADE - MIN_TIME_OF_NEXT_GRENADE)
					+ MIN_TIME_OF_NEXT_GRENADE;
		}

		if (time < 0)
			SetState(STATE_BOMB);
	}

}

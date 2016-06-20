package com.yourfinger.game.Bosses;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.yourfinger.game.ClassesForMonsters.State;
import com.yourfinger.game.ClassesForMonsters.StateMove;
import com.yourfinger.game.ClassesForMonsters.StateRockets;
import com.yourfinger.game.ClassesForMonsters.StateTank;
import com.yourfinger.game.ClassesForMonsters.StateToFinger;
import com.yourfinger.game.ExtraClasses.Finger;
import com.yourfinger.game.Helpers.PrefBuilder;
import com.yourfinger.game.ImportantClasses.Boss;

public class Alpha extends Boss {

	// состояния и смены
	State state;
	int numOfState;
	final static int MAX_NUM_OF_STATE = 10;

	// ключевые объекты
	Finger finger;
	TextureAtlas atlas;

	// Альфа
	TextureRegion txtrAlpha;
	TextureRegion txtrAlphaAttacked;

	// пули и атакуемость Альфы
	private static final float TIME_FOR_ATTACKED_SPRITE = 0.05f;
	private static final float TIME_BETWEEN_BULLET = 0.1f;
	ArrayList<Bullet> bullets;
	float timeForAttackedSprite;
	float timeForBullet;

	// жизни и их вывод
	private static final int START_LIVES = 300;
	private static final int HEIGHT_OF_LIVES_TXTR_IN_PIXELS = 3;
	TextureRegion txtrLives;



	public Alpha(Finger finger, TextureAtlas atlas) {

		this.finger = finger;
		this.atlas = atlas;

		// Альфа
		txtrAlpha = atlas.findRegion("alpha");
		txtrAlphaAttacked = atlas.findRegion("alpha_attacked");

		// пули
		bullets = new ArrayList<Alpha.Bullet>();
		timeForBullet = TIME_BETWEEN_BULLET;

		// жизни
		txtrLives = atlas.findRegion("lives");
		lives = START_LIVES;

		// состояние
		numOfState = 0;

		SetState();

	}



	@Override
	public void draw(Batch batch, float parentAlpha) {

		// пули
		for (Sprite s : bullets)
			s.draw(batch);

		// состояние
		state.draw(batch);

		// Альфа
		batch.draw(txtrAlpha, getX(), getY(), getWidth(), getHeight());
		if (timeForAttackedSprite > 0)
			batch.draw(txtrAlphaAttacked, getX(), getY(), getWidth(),
					getHeight());

		// полоска жизней
		batch.draw(txtrLives, 0, Gdx.graphics.getHeight()
				- HEIGHT_OF_LIVES_TXTR_IN_PIXELS, Gdx.graphics.getWidth()
				* lives / START_LIVES, HEIGHT_OF_LIVES_TXTR_IN_PIXELS);

	}



	public boolean FingerIsDead() {

		if (state.FingerIsDead(finger))
			return true;

		// столкновение с Альфой
		return (Math.pow(2 * (finger.y - getCenterY()) / getHeight(), 2)
				+ Math.pow(2 * (finger.x - getCenterX()) / getWidth(), 2) < 1);
	}



	public boolean IsDead() {
		return lives <= 0;
	}



	public boolean FingerCanBeLose() {
		return false;
	}



	private void SetState() {

		switch (new Random().nextInt(4)) {
		case 0:
			if (numOfState >= MAX_NUM_OF_STATE)
				state = new StateTank(finger, this, atlas, 0.05f, 0.4f, 0.5f);
			else
				state = new StateTank(finger, this, atlas, 0.25f - 0.2f
						* numOfState / MAX_NUM_OF_STATE, 0.8f - 0.4f
						* numOfState / MAX_NUM_OF_STATE, 1f - 0.5f * numOfState
						/ MAX_NUM_OF_STATE);
			break;
		case 1:
			if (numOfState > 9)
				state = new StateMove(this, 0.5f);
			else
				state = new StateMove(this, 0.9f - 0.04f * numOfState);
			break;
		case 2:
			if (numOfState >= MAX_NUM_OF_STATE)
				state = new StateToFinger(this, finger, 10000, 5000, 4, 0.8f);
			else
				state = new StateToFinger(this, finger, 2000 + 8000
						* numOfState / MAX_NUM_OF_STATE, 1000 + 4000
						* numOfState / MAX_NUM_OF_STATE, 4, (float) (1.2f - 0.4
						* numOfState / MAX_NUM_OF_STATE));

			break;
		case 3:
			if (numOfState >= MAX_NUM_OF_STATE)
				state = new StateRockets(this, finger, atlas, 0.5f, 1.5f, 2.2f);
			else
				state = new StateRockets(this, finger, atlas, 0.7f - 0.2f
						* numOfState / MAX_NUM_OF_STATE, 2.5f - 1f * numOfState
						/ MAX_NUM_OF_STATE, 2.2f);
			break;
		}
		numOfState++;
	}



	@Override
	public void act(float delta) {

		// СОСТОЯНИЕ
		state.act(delta);
		if (!state.IsCurrent())
			SetState();

		// ПУЛИ

		// движение пуль
		for (Bullet b : bullets)
			b.act(delta);

		// появление новых пуль
		timeForBullet -= delta;
		if (timeForBullet < 0) {
			timeForBullet = TIME_BETWEEN_BULLET;
			bullets.add(new Bullet(finger.x, finger.y));
		}

		// удаление вышедших пуль
		int indexForRemove = -1;
		for (int i = 0; i < bullets.size(); i++)
			if (!bullets.get(i).isCurrent())
				indexForRemove = i;
		if (indexForRemove > -1)
			bullets.remove(indexForRemove);

		// АТАКА БОССА

		// проверка на попадание пуль
		for (Bullet b : bullets) {
			if (Math.pow(2 * (b.getY() + b.getHeight() / 2 - getCenterY())
					/ getHeight(), 2)
					+ Math.pow(2 * (b.getX() + b.getWidth() / 2 - getCenterX())
							/ getWidth(), 2) < 1) {
				// убираем жизнь, убираем пулю, вибрируем, ставим для вывода
				// красную текстуру
				lives--;
				b.destroy();
				if (PrefBuilder.VibroOn())
					Gdx.input.vibrate(10);
				timeForAttackedSprite = TIME_FOR_ATTACKED_SPRITE;
			}
		}

		// время красного спрайта
		if (timeForAttackedSprite > 0)
			timeForAttackedSprite -= delta;

	}




	/**
	 * Пуля из пальца
	 */
	private class Bullet extends Sprite {

		private final float SPEED = Gdx.graphics.getHeight() / 0.6f;
		private final float SIZE = Gdx.graphics.getWidth() * 0.03f;



		public Bullet(float x, float y) {
			setRegion(atlas.findRegion("bullet"));
			setSize(SIZE, SIZE);
			setCenter(x, y);
		}



		public void act(float delta) {
			setY(getY() + delta * SPEED);
		}



		public boolean isCurrent() {
			return getY() < Gdx.graphics.getHeight();
		}



		public void destroy() {
			setY(Gdx.graphics.getHeight() + 3 * getHeight());
		}

	}

}

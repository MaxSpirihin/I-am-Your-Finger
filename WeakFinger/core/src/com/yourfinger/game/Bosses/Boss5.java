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
import com.yourfinger.game.Helpers.PrefBuilder;
import com.yourfinger.game.ImportantClasses.Boss;

public class Boss5 extends Boss {

	TextureRegion txtrBoss;
	TextureRegion txtrBossAttacked;
	Finger finger;
	TextureAtlas atlas;
	final float TIME_FOR_ATTACKED_SPRITE = 0.3f;
	float timeForAttackedSprite;

	// атака
	List<Bullet> bullets;
	final float MIN_TIME_OF_NEXT_BULLET = 0.15f;
	final float MAX_TIME_OF_NEXT_BULLET = 0.45f;
	float timeForBullet;
	final float MIN_ABS_SPEED = 0.5f;
	final float MAX_ABS_SPEED = 1.5f;
	final float MIN_SIZE = 0.03f;
	final float MAX_SIZE = 0.1f;

	// отражалки
	List<NiceBullet> niceBullets;
	final float START_TIME_OF_NEXT_NICE = 1f;
	final float MIN_TIME_OF_NEXT_NICE = 2f;
	final float MAX_TIME_OF_NEXT_NICE = 6f;
	float timeForNice;
	final float NICE_SPEED = 0.6f;
	final float NICE_SIZE = 0.1f;



	public Boss5(Finger finger, TextureAtlas atlas) {
		lives = 7;
		this.finger = finger;
		this.atlas = atlas;

		txtrBoss = atlas.findRegion("boss");
		txtrBossAttacked = atlas.findRegion("boss_attacked");
		this.setSize(Gdx.graphics.getWidth() * 0.4f,
				Gdx.graphics.getWidth() * 1.2f);

		bullets = new LinkedList<Bullet>();
		timeForBullet = 0;
		niceBullets = new LinkedList<NiceBullet>();
		timeForNice = START_TIME_OF_NEXT_NICE;

	}



	@Override
	public boolean FingerIsDead() {

		for (Bullet b : bullets) {
			if (b.FingerIsDead(finger))
				return true;
		}

		for (Bullet b : niceBullets) {
			if (b.FingerIsDead(finger))
				return true;
		}

		return (finger.y > Gdx.graphics.getHeight() / 2);
	}



	@Override
	public void draw(Batch batch, float parentAlpha) {

		for (Sprite s : bullets) {
			s.draw(batch);
		}

		for (Sprite s : niceBullets) {
			s.draw(batch);
		}

		batch.draw(txtrBoss, finger.x - getWidth() / 2,
				Gdx.graphics.getHeight() - finger.y, getWidth(), getHeight());

		// атака босса
		for (NiceBullet n : niceBullets) {
			if ((n.getY() > Gdx.graphics.getHeight() - finger.y)&&(n.getY()<Gdx.graphics.getHeight())&&(n.getX()>finger.x - getWidth() / 2)&&(n.getX()<finger.x + getWidth() / 2) && (n.IsReturn()))
			{
				if (PrefBuilder.VibroOn())
					Gdx.input.vibrate(50);
				n.destroy();
				lives--;
				timeForAttackedSprite = TIME_FOR_ATTACKED_SPRITE;
			}
		}
		
		if (timeForAttackedSprite > 0)
		batch.draw(txtrBossAttacked, finger.x - getWidth() / 2,
				Gdx.graphics.getHeight() - finger.y, getWidth(), getHeight());
	}



	@Override
	public void act(float delta) {
		if (timeForAttackedSprite > 0)
			timeForAttackedSprite-=delta;

		for (Bullet m : bullets) {
			m.act(delta);
		}

		for (Bullet m : niceBullets) {
			m.act(delta);
		}

		// создание новых пуль
		timeForBullet -= delta;
		if (timeForBullet < 0) {
			float speed = new Random().nextFloat()
					* (MAX_ABS_SPEED - MIN_ABS_SPEED) + MIN_ABS_SPEED;
			float size = new Random().nextFloat() * (MAX_SIZE - MIN_SIZE)
					+ MIN_SIZE;
			Bullet b = new Bullet(finger.x,
					Gdx.graphics.getHeight() - finger.y, size, speed);
			bullets.add(b);
			timeForBullet = new Random().nextFloat()
					* (MAX_TIME_OF_NEXT_BULLET - MIN_TIME_OF_NEXT_BULLET)
					+ MIN_TIME_OF_NEXT_BULLET;

		}

		// удаление вышедших пуль
		int indexForRemove = -1;
		for (int i = 0; i < bullets.size(); i++)
			if (!bullets.get(i).isCurrent())
				indexForRemove = i;
		if (indexForRemove > -1)
			bullets.remove(indexForRemove);

		// создание новых отражалок
		timeForNice -= delta;
		if (timeForNice < 0) {
			NiceBullet b = new NiceBullet(finger.x, Gdx.graphics.getHeight()
					- finger.y, NICE_SIZE, NICE_SPEED);
			niceBullets.add(b);
			timeForNice = new Random().nextFloat()
					* (MAX_TIME_OF_NEXT_NICE - MIN_TIME_OF_NEXT_NICE)
					+ MIN_TIME_OF_NEXT_NICE;
		}

		// удаление вышедших отражалок
		indexForRemove = -1;
		for (int i = 0; i < niceBullets.size(); i++)
			if (!niceBullets.get(i).isCurrent())
				indexForRemove = i;
		if (indexForRemove > -1)
			niceBullets.remove(indexForRemove);

	}




	private class Bullet extends Sprite {

		float speed;



		public Bullet(float x, float y, float size, float speed) {
			setRegion(atlas.findRegion("boss_bullet"));

			setSize(Gdx.graphics.getWidth() * size, Gdx.graphics.getWidth()
					* size);
			setCenter(x, y);
			this.speed = Gdx.graphics.getHeight() / speed;
		}



		public void act(float delta) {
			setY(getY() - delta * speed);
		}



		public boolean FingerIsDead(Finger finger) {
			return (Math.pow(finger.x - getX() - getWidth() / 2, 2)
					+ Math.pow(finger.y - getY() - getHeight() / 2, 2) < Math
						.pow(getWidth() / 2, 2));
		}



		public boolean isCurrent() {
			return getY() > -getHeight();
		}

	}




	private class NiceBullet extends Bullet {

		boolean isDown;



		public NiceBullet(float x, float y, float size, float speed) {
			super(x, y, size, speed);
			setRegion(atlas.findRegion("boss_flash"));
			isDown = true;
		}



		@Override
		public void act(float delta) {
			if (isDown) {
				setY(getY() - delta * speed);
				if (getY() < 0)
					isDown = false;
			} else
				setY(getY() + delta * speed);
		}



		public boolean IsReturn() {
			return !isDown;
		}



		public void destroy() {
			setY(Gdx.graphics.getHeight() + 3 * getHeight());
		}



		@Override
		public boolean isCurrent() {
			return (getY() < Gdx.graphics.getHeight() + getHeight());
		}
	}

}

package com.yourfinger.game.Monsters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.yourfinger.game.ExtraClasses.Finger;
import com.yourfinger.game.ImportantClasses.Monster;

public class EvilCar extends Monster {

	private static float TIME_FOR_EXIT = 0.7f;
	private float backSpeed = 0;

	
	private static float START_SPEED = Gdx.graphics.getHeight()/1.7f;
	private static float END_SPEED = Gdx.graphics.getHeight()/0.85f;
	private float speed;

	private static final int RIGHT = 0;
	private static final int UP_RIGHT = 1;
	private static final int UP = 2;
	private static final int UP_LEFT = 3;
	private static final int LEFT = 4;
	private static final int DOWN_LEFT = 5;
	private static final int DOWN = 6;
	private static final int DOWN_RIGHT = 7;

	private static final int DIRECTION_RIGHT = 0;
	private static final int DIRECTION_UP = 1;
	private static final int DIRECTION_LEFT = 2;
	private static final int DIRECTION_DOWN = 3;

	int currentDirection;

	float time;
	float allTime;
	float timeForNext;
	Animation animation;
	Finger finger;
	float[] size;



	public EvilCar(Finger finger, TextureAtlas atlas, String backgroundName,
			float time, float[] size, int mode[], float next, float anim) {
		// заполняем данные
		this.finger = finger;
		this.time = time;
		this.allTime = time - TIME_FOR_EXIT;
		this.size = size;
		this.timeForNext = time - next;

		// добавим анимацию
		Array<TextureRegion> txtrMonster = new Array<TextureRegion>();
		if (atlas.findRegion(backgroundName, 1) != null) {
			// объект анимирован
			int i = 1;
			while (atlas.findRegion(backgroundName, i) != null) {
				txtrMonster.add(atlas.findRegion(backgroundName, i));
				i++;
			}
		} else {
			// для объекта одна текстурка
			txtrMonster.add(atlas.findRegion(backgroundName));
		}
		this.animation = new Animation(anim, txtrMonster);

		this.setSize(Gdx.graphics.getWidth() * size[0], Gdx.graphics.getWidth()
				* size[1]);

		this.setCenterPosition(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() + getHeight()/2);
		
		this.currentDirection = DIRECTION_DOWN;
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

		return ((finger.x >= this.getX())
				&& (finger.x <= this.getX() + this.getWidth())
				&& (finger.y >= this.getY()) && (finger.y <= this.getY()
				+ this.getHeight()));

	}



	@Override
	public void draw(Batch batch, float parentAlpha) {

		// с отрицательным временем будет вылет из-за анимации
		if (time > 0)
			batch.draw(animation.getKeyFrame(time, true), getX(), getY(),
					getWidth(), getHeight());

	}



	@Override
	public void act(float delta) {
		time -= delta;
		
		speed = (END_SPEED - START_SPEED)*(1 - time/allTime) + START_SPEED;

		if (time > TIME_FOR_EXIT) {
			System.out.println(Gdx.graphics.getHeight()/speed);

			int state;

			// определяем направление
			if ((finger.y >= this.getY())
					&& (finger.y <= this.getY() + this.getHeight())) {
				// по вертикали палец где машина
				if (finger.x < this.getX())
					state = LEFT;
				else
					state = RIGHT;

			} else if ((finger.y < this.getY())) {
				// по вертикали палец ниже
				if (finger.x < this.getX())
					state = DOWN_LEFT;
				else if (finger.x > this.getX() + this.getWidth())
					state = DOWN_RIGHT;
				else
					state = DOWN;
			} else {
				// по вертикали палец выше
				if (finger.x < this.getX())
					state = UP_LEFT;
				else if (finger.x > this.getX() + this.getWidth())
					state = UP_RIGHT;
				else
					state = UP;
			}

			if (state % 2 == 0) {
				switch (state) {
				case UP:
					currentDirection = DIRECTION_UP;
					break;
				case DOWN:
					currentDirection = DIRECTION_DOWN;
					break;
				case LEFT:
					currentDirection = DIRECTION_LEFT;
					break;
				case RIGHT:
					currentDirection = DIRECTION_RIGHT;
					break;
				}
			}

			// двигаем
			switch (currentDirection) {
			case DIRECTION_UP:
				setY(getY() + speed * delta);
				break;
			case DIRECTION_DOWN:
				setY(getY() - speed * delta);
				break;
			case DIRECTION_RIGHT:
				setX(getX() + speed * delta);
				break;
			case DIRECTION_LEFT:
				setX(getX() - speed * delta);
				break;
			}
		}
		else
		{
			
			//время валить
			if (backSpeed == 0)
				backSpeed = (getY() + getHeight())/TIME_FOR_EXIT;
			
			setY(getY() - backSpeed * delta);
		}

		
	}
}

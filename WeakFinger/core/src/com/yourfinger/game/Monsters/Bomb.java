package com.yourfinger.game.Monsters;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.yourfinger.game.ExtraClasses.Finger;
import com.yourfinger.game.ImportantClasses.Monster;

public class Bomb extends Monster {

	// значения массива режимов
	final int MODE_START_POSITION = 0;
	final int MODE_RELATIVE_BOOM_POSITION = 1;
	final int MODE_PERCENT_TIME_OF_BOOM = 2;
	final int MODE_ROTATION_DIRECTION = 3;
	final int MODE_TURN_SPEED = 4;

	// режимы старта
	final int CENTER_POSITION = 0;
	final int RANDOM_POSITION = -1;
	final int FINGER_POSITION = -2;

	// режимы поворота
	final int CLOCKWISE = 1;
	final int COUNTERCLOCKWISE = 2;

	final float AMINATION_TIME = 0.2f;
	final float DEFAULT_TURN_TIME = 1f;

	final float SIDE_TO_RADIUS = 0.5f;

	boolean isCurrent; // показывает не закончил ли монстр свое
						// выступление.

	// время
	float time;
	float timeForBoom;
	float fullTime;
	float timeForNext;

	float speed;
	float angularSpeed;
	Animation animation;

	Finger finger;
	float angle;
	float[] size;
	int[] mode;// режим работы, это для того, чтобы не писать разных
				// монстров с небольшими отличиями, а реализовать их в одном
				// классе с помощью режимов.

	boolean isBoom = false;



	public Bomb(Finger finger, TextureAtlas atlas, String backgroundName,
			float time, float[] size, int mode[], float next, float anim) {
		super();

		// заполняем данные
		this.finger = finger;
		this.time = time;
		this.fullTime = time;
		this.timeForBoom = time * mode[MODE_PERCENT_TIME_OF_BOOM] * 1f / 100;
		this.mode = mode;
		this.isCurrent = true;
		this.angle = 0;
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
		this.animation = new Animation((time - timeForBoom) / 3, txtrMonster);

		// ставим размер и скорость
		this.setSize(Gdx.graphics.getWidth() * size[0], Gdx.graphics.getWidth()
				* size[0]);

		int relativePosition = (mode[MODE_RELATIVE_BOOM_POSITION] == -1) ? (new Random())
				.nextInt(60) + 40 : mode[MODE_RELATIVE_BOOM_POSITION];

		this.speed = ((float) Gdx.graphics.getHeight() + 2 * this.getHeight())
				* relativePosition / ((this.time - this.timeForBoom) * 100);

		// утанавливаем положение на сцене
		switch (mode[MODE_START_POSITION]) {
		case CENTER_POSITION:
			this.setCenterPosition(Gdx.graphics.getWidth() / 2,
					Gdx.graphics.getHeight() + this.getHeight());
			break;
		case RANDOM_POSITION:
			Random rand = new Random();
			this.setCenterPosition(rand.nextInt(Gdx.graphics.getWidth()),
					Gdx.graphics.getHeight() + this.getHeight());
			break;
		case FINGER_POSITION:
			this.setCenterPosition(finger.x,
					Gdx.graphics.getHeight() + this.getHeight());
			break;
		default:
			this.setCenterPosition(
					mode[MODE_START_POSITION] * Gdx.graphics.getWidth() / 100,
					Gdx.graphics.getHeight() + this.getHeight());
		}

		if (mode.length != 3) {
			float timeForTurn = DEFAULT_TURN_TIME;

			if (mode.length != 4) {
				timeForTurn = mode[MODE_TURN_SPEED] * 1f / 1000;
			}

			switch (mode[MODE_ROTATION_DIRECTION]) {
			case 1:
				angularSpeed = -(360.0f / timeForTurn);
				break;
			case 2:
				angularSpeed = (360.0f / timeForTurn);
				break;
			default:
				angularSpeed = 0;
			}
		}
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
		// круг
		return (Math.pow(finger.x - this.getCenterX(), 2)
				+ Math.pow(finger.y - this.getCenterY(), 2) < Math.pow(
				this.getWidth() * SIDE_TO_RADIUS, 2));
	}



	@Override
	public void draw(Batch batch, float parentAlpha) {

		// с отрицательным временем будет вылет из-за анимации
		if (time > 0)
			if (!isBoom)
				batch.draw(animation.getKeyFrame(fullTime - time, true),
						getX(), getY(), this.getWidth() / 2,
						this.getHeight() / 2, getWidth(), getHeight(), 1, 1,
						angle);
			else
				batch.draw(animation.getKeyFrame(fullTime - timeForBoom, true),
						getX(), getY(), this.getWidth() / 2,
						this.getHeight() / 2, getWidth(), getHeight(), 1, 1,
						angle);

	}



	@Override
	public void act(float delta) {
		if ((time < timeForBoom) && (!isBoom)) {
			// мы перед взрывом
			isBoom = true;
			speed = this.size[1] * Gdx.graphics.getWidth() / timeForBoom;
			this.setX(this.getCenterX());
			this.setY(this.getCenterY());
			this.setSize(1, 1);

		}
		if (!isBoom) {
			// до взрыва
			time -= delta;
			angle += angularSpeed * delta;
			this.setY(this.getY() - delta * speed);
		} else {
			// расширяемся во время взрыва
			time -= delta;
			this.setSize(this.getWidth() + speed * delta, this.getWidth()
					+ speed * delta);
			this.setX(this.getX() - speed * delta / 2);
			this.setY(this.getY() - speed * delta / 2);
		}
	}

}

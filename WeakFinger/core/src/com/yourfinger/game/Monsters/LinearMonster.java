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

/**
 * простой монстр, летящий по прямой сверху вниз.
 */
public class LinearMonster extends Monster {

	// значения массива режимов
	final int MODE_START_POSITION = 0;
	final int MODE_ROTATION_DIRECTION = 1;
	final int MODE_TURN_SPEED = 2;

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
	boolean isCircle = false;// показывыает, считать ли при расчетах монстра
								// кругом
	float time;// время выступления монстра.
	float speed;
	float angularSpeed;
	Animation animation;
	Finger finger;
	float angle;
	float[] size;
	int[] mode;// режим работы, это для того, чтобы не писать разных
				// монстров с небольшими отличиями, а реализовать их в одном
				// классе с помощью режимов.
	float timeForNext;



	public LinearMonster(Finger finger, TextureAtlas atlas,
			String backgroundName, float time, float[] size, int mode[],
			float next, float anim) {
		super();

		// заполняем данные
		this.finger = finger;
		this.time = time;
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
		this.animation = new Animation(anim, txtrMonster);

		// ставим размер и скорость
		if (size.length == 1)
			this.setSize(Gdx.graphics.getWidth() * size[0],
					Gdx.graphics.getWidth() * size[0]);
		else
			this.setSize(Gdx.graphics.getWidth() * size[0],
					Gdx.graphics.getWidth() * size[1]);

		this.speed = ((float) Gdx.graphics.getHeight() + 2 * this.getHeight())
				/ this.time;

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

		if (mode.length != 1) {
			isCircle = true;
			float timeForTurn = DEFAULT_TURN_TIME;

			if (mode.length != 2) {
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
		if (!isCircle) {
			// квадрат
			if (size.length < 3)
				return ((finger.x >= this.getX())
						&& (finger.x <= this.getX() + this.getWidth())
						&& (finger.y >= this.getY()) && (finger.y <= this
						.getY() + this.getHeight()));
			else
			{
				if ((finger.x >= this.getX()+this.getWidth()*size[3])
						&& (finger.x <= this.getX() + this.getWidth()*(1-size[3]))
						&& (finger.y >= this.getY()) && (finger.y <= this
						.getY() + this.getHeight()*size[2]))
					return true;
				if ((finger.x >= this.getX()+this.getWidth()*size[4])
						&& (finger.x <= this.getX() + this.getWidth()*(1-size[4]))
						&& (finger.y >= this.getY()+ this.getHeight()*size[2]) && (finger.y <= this
						.getY() + this.getHeight()))
					return true;
				return false;
			}
		} else {
			// круг
			return (Math.pow(finger.x - this.getCenterX(), 2)
					+ Math.pow(finger.y - this.getCenterY(), 2) < Math.pow(
					this.getWidth() * SIDE_TO_RADIUS, 2));
		}
	}



	@Override
	public void draw(Batch batch, float parentAlpha) {

		// с отрицательным временем будет вылет из-за анимации
		if (time > 0)
			batch.draw(animation.getKeyFrame(time, true), getX(), getY(),
					this.getWidth() / 2, this.getHeight() / 2, getWidth(),
					getHeight(), 1, 1, angle);

	}



	@Override
	public void act(float delta) {
		time -= delta;
		angle += angularSpeed * delta;
		this.setY(this.getY() - delta * speed);
	}

}

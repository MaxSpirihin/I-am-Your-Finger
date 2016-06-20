package com.yourfinger.game.Monsters;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.yourfinger.game.ExtraClasses.Finger;
import com.yourfinger.game.ImportantClasses.Monster;

public class BreakingMonster extends Monster {

	// значения массива режимов
	final int MODE_START_POSITION = 0;
	final int MODE_RELATIVE_BOOM_POSITION = 1;
	final int MODE_PERCENT_TIME_OF_BOOM = 2;
	final int MODE_COUNT_OF_PEACES = 3;
	final int MODE_ROTATION_DIRECTION = 4;
	final int MODE_TURN_SPEED = 5;

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

	// скорости
	protected float speed;
	float speedOfPeace;
	float angularSpeed;

	// текстуры
	Animation animation;
	TextureRegion txtrPeace;

	Finger finger;
	float angle;
	float[] size;
	int[] mode;

	// куски
	ArrayList<Sprite> peaces;

	// произошел ли взрыв
	protected boolean isBoom = false;



	public BreakingMonster(Finger finger, TextureAtlas atlas,
			String backgroundName, float time, float[] size, int mode[],
			float next, float anim) {
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
		txtrPeace = atlas.findRegion(backgroundName + "_peace");
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
				.nextInt(70)+30 : mode[MODE_RELATIVE_BOOM_POSITION];
		
		this.speed = ((float) Gdx.graphics.getHeight() + 2 * this.getHeight())
				* relativePosition 
				/ ((this.time - this.timeForBoom) * 100);

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

		//крутиться или нет
		if (mode.length != 4) {
			float timeForTurn = DEFAULT_TURN_TIME;

			if (mode.length != 5) {
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

		if (!isBoom)
			//столкновение с гранатой
			return (Math.pow(finger.x - this.getCenterX(), 2)
					+ Math.pow(finger.y - this.getCenterY(), 2) < Math.pow(
					this.getWidth() * SIDE_TO_RADIUS, 2));
		else
			//столкновение с любым осколком
			for (Sprite peace : peaces)
				if ((finger.x >= peace.getX())
						&& (finger.x <= peace.getX() + peace.getWidth())
						&& (finger.y >= peace.getY())
						&& (finger.y <= peace.getY() + peace.getHeight()))
					return true;
		return false;

	}



	@Override
	public void draw(Batch batch, float parentAlpha) {

		// с отрицательным временем будет вылет из-за анимации
		if (time > 0)
			if (!isBoom)
				batch.draw(animation.getKeyFrame(time, true), getX(), getY(),
						this.getWidth() / 2, this.getHeight() / 2, getWidth(),
						getHeight(), 1, 1, angle);
			else {
				//рисуем каждый кусок
				for (Sprite peace : peaces)
					peace.draw(batch);
			}

	}



	@Override
	public void act(float delta) {
		if ((time < timeForBoom) && (!isBoom)) {
			// мы перед взрывом
			isBoom = true;
			
			//создвем спрайты
			peaces = new ArrayList<Sprite>();
			ComputeSpeedOfPeace();
			
			int countOfPeaces = (mode[MODE_COUNT_OF_PEACES] > 2) ? mode[MODE_COUNT_OF_PEACES] : (new Random()).nextInt(7)+3;
			
			for (int numberOfPeace = 0; numberOfPeace < countOfPeaces; numberOfPeace++) {
				Sprite peace = new Sprite(txtrPeace);
				peace.setSize(Gdx.graphics.getWidth() * size[1],
						Gdx.graphics.getWidth() * size[1]);
				double hisAngle = Math.PI * 2 * numberOfPeace
						/ countOfPeaces;
				peace.setCenterX((float) (this.getCenterX() + this.getWidth()
						* Math.cos(hisAngle) / 2));
				peace.setCenterY((float) (this.getCenterY() + this.getHeight()
						* Math.sin(hisAngle) / 2));

				peaces.add(peace);
			}

		}
		if (!isBoom) {
			// до взрыва
			time -= delta;
			angle += angularSpeed * delta;
			this.setY(this.getY() - delta * speed);
		} else {
			// расширяемся во время взрыва
			this.setSize(this.getWidth() + speedOfPeace * delta,
					this.getWidth() + speedOfPeace * delta);
			this.setX(this.getX() - speedOfPeace * delta / 2);
			this.setY(this.getY() - speedOfPeace * delta / 2);
			for (int i = 0; i < peaces.size(); i++) {
				Sprite peace = peaces.get(i);
				double hisAngle = Math.PI * 2 * i / peaces.size();
				peace.setCenterX((float) (this.getCenterX() + this.getWidth()
						* Math.cos(hisAngle) / 2));
				peace.setCenterY((float) (this.getCenterY() + this.getHeight()
						* Math.sin(hisAngle) / 2));
			}
			time -= delta;
		}
	}



	private void ComputeSpeedOfPeace() {
		// расстояния до всех углов (нам только сравнить)
		double[] path = new double[4];
		path[0] = this.getCenterX() * this.getCenterX()
				+ (Gdx.graphics.getHeight() - this.getCenterY())
				* (Gdx.graphics.getHeight() - this.getCenterY());

		path[1] = this.getCenterX() * this.getCenterX() + this.getCenterY()
				* this.getCenterY();
		path[2] = (Gdx.graphics.getWidth() - this.getCenterX())
				* (Gdx.graphics.getWidth() - this.getCenterX())
				+ (Gdx.graphics.getHeight() - this.getCenterY())
				* (Gdx.graphics.getHeight() - this.getCenterY());
		path[3] = (Gdx.graphics.getWidth() - this.getCenterX())
				* (Gdx.graphics.getWidth() - this.getCenterX())
				+ this.getCenterY() * this.getCenterY();

		//берем макс расстояние
		double max = path[0];
		for (double i : path)
			if (i > max)
				max = i;

		//2 т.к. мы увеличиваем диаметр
		speedOfPeace = (float) (Math.sqrt(max) * 2 / timeForBoom);
	}

}

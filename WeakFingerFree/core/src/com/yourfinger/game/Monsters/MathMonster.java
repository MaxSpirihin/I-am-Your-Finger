package com.yourfinger.game.Monsters;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.yourfinger.game.ExtraClasses.Finger;
import com.yourfinger.game.ExtraClasses.Utils;
import com.yourfinger.game.ImportantClasses.Monster;

/**
 * монстр, двигающийся по необычной траектории, описываемой некоторой
 * математической функцией
 */
public class MathMonster extends Monster {

	// значения массива режимов
	final int MODE_FUNCTION = 0;
	final int MODE_FUNCTION_PARAMETR = 1;
	final int MODE_ROTATION_DIRECTION = 2;
	final int MODE_TURN_SPEED = 3;

	// функции
	final int RANDOM_FUNCTION = 0;
	final int X_IN_SQUARE = 1;
	final int X_IN_CUBE = 2;
	final int SINUSOID = 3;
	final int EXPONENT = 4;
	final int X_COSX = 5;
	final int CONSTANT = 6;
	final int COUNT_OF_FUNCTIONS = 7;

	// режимы поворота
	final int CLOCKWISE = 1;
	final int COUNTERCLOCKWISE = 2;

	final float AMINATION_TIME = 0.2f;
	final float DEFAULT_TURN_TIME = 1f;

	boolean isCurrent; // показывает не закончил ли монстр свое
	// выступление.
	float time;// время выступления монстра.
	float speed;
	float angularSpeed;
	Animation animation;
	Finger finger;
	float angle;
	int[] mode;// режим работы, это для того, чтобы не писать разных
	float timeForNext;
	
	int mainMode;

	boolean userTurn;

	float x;



	// монстров с небольшими отличиями, а реализовать их в одном
	// классе с помощью режимов.

	public MathMonster(Finger finger, TextureAtlas atlas,
			String backgroundName, float time, float[] size, int mode[],
			float next, float anim) {
		super();

		// заполняем данные
		this.finger = finger;
		this.time = time;
		this.mode = mode;
		this.isCurrent = true;
		this.speed = 1 / this.time;
		this.angle = 0;
		this.userTurn = false;
		this.timeForNext = time - next;
		this.mainMode = mode[MODE_FUNCTION];

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

		// ставим размер, монстр круглый
		this.setSize(Gdx.graphics.getWidth() * 2 * size[0],
				Gdx.graphics.getWidth() * size[0] * 2);

		// если надо, ставим угловую скорость
		if (mode.length > 2) {
			userTurn = true;
			float timeForTurn = DEFAULT_TURN_TIME;

			if (mode.length > 3) {
				timeForTurn = mode[MODE_TURN_SPEED] * 1f / 1000;
			}

			switch (mode[MODE_ROTATION_DIRECTION]) {
			case CLOCKWISE:
				angularSpeed = -(360.0f / timeForTurn);
				break;
			case COUNTERCLOCKWISE:
				angularSpeed = (360.0f / timeForTurn);
				break;
			default:
				angularSpeed = 0;
			}
		}

		this.x = 0;
		UpdatePosition(0);

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
		return (Math.pow(finger.x - this.getCenterX(), 2)
				+ Math.pow(finger.y - this.getCenterY(), 2) < Math.pow(
				this.getWidth() / 2, 2));
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
		x += speed * delta;
		UpdatePosition(delta);

	}



	/**
	 * обновляет позицию и угол
	 */
	private void UpdatePosition(float delta) {

		float newX = 0, newY = 0;
		float oldX = this.getCenterX();
		float oldY = this.getCenterY();

		/*
		 * итак, дальше в зависимости от функции, мы должны получить новые
		 * коор-ты центра, единственная общность - Х меняется от 0 до 1, дальше
		 * как угодно получаем координаты т.ч. для добавления новой функции
		 * просто добавляем case со своей логикой
		 */

		if (mainMode == RANDOM_FUNCTION) {
			// выбираем рандомно
			Random rand = new Random();
			int newMode = rand.nextInt(COUNT_OF_FUNCTIONS * 2 - 1)
					- COUNT_OF_FUNCTIONS + 1;
			while (newMode == 0)
				newMode = rand.nextInt(COUNT_OF_FUNCTIONS * 2 - 1)
						- COUNT_OF_FUNCTIONS + 1;
			if (mode.length == 1)
				mode = new int[2];
			mainMode = newMode;
			mode[1] = 0;

		}

		switch (Math.abs(mainMode)) {
		case X_IN_SQUARE:
			// параболка

			if (mainMode > 0)
				newX = x * (Gdx.graphics.getWidth() + this.getWidth())
						- this.getWidth() / 2;
			else
				newX = (1 - x) * (Gdx.graphics.getWidth() + this.getWidth())
						- this.getWidth() / 2;

			newY = ((x - 0.5f) * (x - 0.5f) * 4
					* (1 - mode[MODE_FUNCTION_PARAMETR] * 0.01f) + mode[MODE_FUNCTION_PARAMETR] * 0.01f)
					* (Gdx.graphics.getHeight() + this.getHeight() / 2);
			break;
		case X_IN_CUBE:

			// х в кубике
			if (mainMode > 0)
				newX = x * Gdx.graphics.getWidth();
			else
				newX = (1 - x) * Gdx.graphics.getWidth();

			newY = ((1 - ((x - 0.5f) * (x - 0.5f) * (x - 0.5f) * 4 + 0.5f)))
					* (Gdx.graphics.getHeight() + this.getHeight() / 2);

			break;

		case SINUSOID:

			if (mode[MODE_FUNCTION_PARAMETR] == 0)
				mode[MODE_FUNCTION_PARAMETR] = 2;

			if (mainMode > 0)
				newX = (float) ((Math.sin(x * mode[MODE_FUNCTION_PARAMETR]
						* Math.PI) / 2 + 0.5f) * Gdx.graphics.getWidth());
			else
				newX = (float) ((1 - (Math.sin(x * mode[MODE_FUNCTION_PARAMETR]
						* Math.PI) / 2 + 0.5f)) * Gdx.graphics.getWidth());

			newY = (1 - x) * (Gdx.graphics.getHeight() + this.getHeight() / 2);

			break;

		case EXPONENT:

			if (mainMode > 0)
				newX = x * (Gdx.graphics.getWidth()) - this.getWidth() / 2;
			else
				newX = (1 - x) * (Gdx.graphics.getWidth()) - this.getWidth()
						/ 2;

			newY = (float) ((1 - (Math.exp(x) - 1) / (Math.E - 1)) * (Gdx.graphics
					.getHeight() + this.getHeight() / 2));

			break;

		case X_COSX:

			if (mode[MODE_FUNCTION_PARAMETR] < 2)
				mode[MODE_FUNCTION_PARAMETR] = 2;

			// смещения
			float xSize = (float) ((mode[MODE_FUNCTION_PARAMETR] - 1) * Math.PI + Math.PI / 2);
			float ySize = (float) Math.abs((xSize - Math.PI / 2)
					* Math.cos(xSize - Math.PI / 2));

			// так было проще понимать этот пи***ц
			float xIn = (x - 0.5f) * 2 * xSize;
			float yIn = (float) (xIn * Math.cos(xIn));

			if (mainMode > 0)
				newX = (float) (yIn / (2 * ySize) + 0.5f)
						* Gdx.graphics.getWidth();
			else
				newX = (float) (0.5f - yIn / (2 * ySize))
						* Gdx.graphics.getWidth();

			newY = (1 - x) * (Gdx.graphics.getHeight() + this.getHeight() / 2);

			break;
		case CONSTANT:

			if (mode[MODE_FUNCTION_PARAMETR] < 1)
				mode[MODE_FUNCTION_PARAMETR] = 50;

			if (mainMode > 0)
				newX = (mode[MODE_FUNCTION_PARAMETR]*0.01f) * Gdx.graphics.getWidth();
			else
				newX = (1 - mode[MODE_FUNCTION_PARAMETR]*0.01f) * Gdx.graphics.getWidth();

			newY = (1 - x) * (Gdx.graphics.getHeight() + this.getHeight() / 2);

			break;
		}

		// далее находим угол
		if (!userTurn)
			angle = Utils.GetAngle(oldX, oldY, newX, newY);
		else
			angle += angularSpeed * delta;

		this.setCenterPosition(newX, newY);
	}
}

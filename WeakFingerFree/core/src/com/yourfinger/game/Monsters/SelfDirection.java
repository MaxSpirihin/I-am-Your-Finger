package com.yourfinger.game.Monsters;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.yourfinger.game.ImportantClasses.Constants;
import com.yourfinger.game.ExtraClasses.Finger;
import com.yourfinger.game.ExtraClasses.Utils;
import com.yourfinger.game.ImportantClasses.Monster;

public class SelfDirection extends Monster {

	// значения массива режимов
	final int MODE_START_POSITION = 0;
	protected final int MODE_ACCELERATION = 1;
	protected final int MODE_FRICTION_FORCE = 2;
	

	// режимы старта
	final int CENTER_POSITION = 0;
	final int RANDOM_POSITION = -1;
	final int FINGER_POSITION = -2;

	final float AMINATION_TIME = 0.2f;
	protected final float TIME_FOR_EXIT = 0.2f;

	protected final float SIDE_TO_RADIUS = 0.5f;

	boolean isCurrent; // показывает не закончил ли монстр свое
						// выступление.

	protected float time;// время выступления монстра.
	Animation animation;
	protected Finger finger;
	float[] size;
	protected int[] mode;// режим работы, это для того, чтобы не писать разных
				// монстров с небольшими отличиями, а реализовать их в одном
				// классе с помощью режимов.
	float timeForNext;

	// скорости по x и y
	public float vx;
	public float vy; //для контроля из босса2
	
	protected float speedOfExit;


	public SelfDirection(Finger finger, TextureAtlas atlas,
			String backgroundName, float time, float[] size, int mode[],
			float next, float anim) {
		super();

		// заполняем данные
		this.finger = finger;
		this.time = time;
		this.mode = mode;
		this.isCurrent = true;
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

		vx = 0;
		vy = 0;
		speedOfExit = this.getWidth()/TIME_FOR_EXIT;

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
		// считаем его кругом т.к. коснуться мы можем лишь переда
		return (Math.pow(finger.x - this.getCenterX(), 2)
				+ Math.pow(finger.y - this.getCenterY(), 2) < Math.pow(
				this.getWidth() * SIDE_TO_RADIUS, 2));

	}



	@Override
	public void draw(Batch batch, float parentAlpha) {

		// с отрицательным временем будет вылет из-за анимации
		if (time > 0)
			batch.draw(animation.getKeyFrame(time, true), getX(), getY(), this
					.getWidth() / 2, this.getHeight() / 2, getWidth(),
					getHeight(), 1, 1, Utils.GetAngle(this.getCenterX(),
							this.getCenterY(), finger.x, finger.y));

	}



	@Override
	public void act(float delta) {
		time -= delta;
		if (time<TIME_FOR_EXIT)
		{
			this.setSize(this.getWidth() - speedOfExit * delta, this.getWidth()
					- speedOfExit * delta);
			this.setX(this.getX() + speedOfExit * delta / 2);
			this.setY(this.getY() + speedOfExit * delta / 2);
		}

		// выбираем ускорение  и силу трения, относительно размеров
		float acceleration = mode[MODE_ACCELERATION]*Gdx.graphics.getHeight()/Constants.STANDART_HEIGHT;
		float force = mode[MODE_FRICTION_FORCE]*Gdx.graphics.getHeight()/Constants.STANDART_HEIGHT;
		
		
		// ускорение к пальцу
		float ax;
		if (finger.x == this.getCenterX())
			ax = 0;
		else if (finger.x > this.getCenterX())
			ax = acceleration;
		else
			ax = -acceleration;

		// учет силы трения
		if (vx > 0)
			ax -= force;
		else
			ax += force;

		// вычисляем новые координаты x по правилам из школьной физики
		vx += ax * delta;
		this.setX(ax * delta * delta / 2 + vx * delta + this.getX());

		// все то же для y
		float ay;
		if (finger.y == this.getCenterY())
			ay = 0;
		else if (finger.y > this.getCenterY())
			ay = acceleration;
		else
			ay = -acceleration;


		if (vy > 0)
			ay -= force;
		else
			ay += force;

		vy += ay * delta;
		this.setY(ay * delta * delta / 2 + vy * delta + this.getY());
	}

}

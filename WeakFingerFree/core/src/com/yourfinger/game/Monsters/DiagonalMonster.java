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
 * очень гибкий монстр. Летит из любого места, под любым углом
 */
public class DiagonalMonster extends Monster {

	// значения массива режимов
	final int MODE_MODE = 0;
	// другие играют разную роль

	final float AMINATION_TIME = 0.2f;

	boolean isCurrent; // показывает не закончил ли монстр свое
	// выступление.
	float time;// время выступления монстра.
	protected float vx;

	protected float vy;
	float[] size;
	protected float angle;
	Animation animation;
	Finger finger;
	int mode[];
	float timeForNext;



	public DiagonalMonster(Finger finger, TextureAtlas atlas,
			String backgroundName, float time, float[] size, int mode[],
			float next, float anim) {
		super();

		// заполняем данные
		this.finger = finger;
		this.time = time;
		this.mode = mode;
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

		// ставим размер, монстр круглый
		this.setSize(Gdx.graphics.getWidth() * 2 * this.size[0],
				Gdx.graphics.getWidth() * 2 * this.size[0]);

		// вычисляем координаты, скорости и угол
		float[] coor = this.GetStartAndEndCoordinates();
		float x1 = coor[0];
		float y1 = coor[1];
		float x2 = coor[2];
		float y2 = coor[3];
		vx = (x2 - x1) / time;
		vy = (y2 - y1) / time;
		angle = Utils.GetAngle(x1, y1, x2, y2);

		this.setCenterPosition(x1, y1);
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
		this.setY(this.getY() + delta * vy);
		this.setX(this.getX() + delta * vx);
	}



	protected float[] GetStartAndEndCoordinates() {

		// теперь выбираем координаты начала и конца, пояснения в документе
		float x1 = 0, y1 = 0, x2 = 0, y2 = 0;
		switch (mode[MODE_MODE]) {
		case 0:
			// один из стандартных режимов
			switch (mode[1]) {
			case 0:
				x1 = Gdx.graphics.getWidth() / 2;
				y1 = Gdx.graphics.getHeight() + this.getHeight() / 2;
				x2 = x1;
				y2 = -this.getHeight() / 2;
				break;
			case 1:
				x1 = Gdx.graphics.getWidth() / 2;
				y1 = -this.getHeight() / 2;
				x2 = x1;
				y2 = Gdx.graphics.getHeight() + this.getHeight() / 2;
				break;
			case 2:
				x1 = -this.getWidth() / 2;
				y1 = Gdx.graphics.getHeight() / 2;
				x2 = Gdx.graphics.getWidth() + this.getWidth() / 2;
				y2 = y1;
				break;
			case 3:
				x1 = Gdx.graphics.getWidth() + this.getWidth() / 2;
				y1 = Gdx.graphics.getHeight() / 2;
				x2 = -this.getWidth() / 2;
				y2 = y1;
				break;
			case 4:
				x1 = -this.getWidth() / 2;
				y1 = Gdx.graphics.getHeight() + this.getHeight() / 2;
				x2 = Gdx.graphics.getWidth() + this.getWidth() / 2;
				y2 = -this.getHeight() / 2;
				break;
			case 5:
				x1 = -this.getWidth() / 2;
				y2 = Gdx.graphics.getHeight() + this.getHeight() / 2;
				x2 = Gdx.graphics.getWidth() + this.getWidth() / 2;
				y1 = -this.getHeight() / 2;
				break;
			case 6:
				x2 = -this.getWidth() / 2;
				y2 = Gdx.graphics.getHeight() + this.getHeight() / 2;
				x1 = Gdx.graphics.getWidth() + this.getWidth() / 2;
				y1 = -this.getHeight() / 2;
				break;
			case 7:
				x2 = -this.getWidth() / 2;
				y1 = Gdx.graphics.getHeight() + this.getHeight() / 2;
				x1 = Gdx.graphics.getWidth() + this.getWidth() / 2;
				y2 = -this.getHeight() / 2;
				break;
			}
			break;
		case 1:
			// рандомный выбор вылета
			Random rand = new Random();
			int side1 = rand.nextInt(4);
			int side2 = (side1 + 2) % 4;
			int offset1 = rand.nextInt(100);
			int offset2 = rand.nextInt(100);
			x1 = CoorFromInput(side1, offset1)[0];
			y1 = CoorFromInput(side1, offset1)[1];
			x2 = CoorFromInput(side2, offset2)[0];
			y2 = CoorFromInput(side2, offset2)[1];
			break;
		case 2:
			x1 = CoorFromInput(mode[1], mode[2])[0];
			y1 = CoorFromInput(mode[1], mode[2])[1];
			x2 = CoorFromInput((mode[1] + 2) % 4, mode[3])[0];
			y2 = CoorFromInput((mode[1] + 2) % 4, mode[3])[1];

			break;
		}

		float[] coor = new float[4];
		coor[0] = x1;
		coor[1] = y1;
		coor[2] = x2;
		coor[3] = y2;
		return coor;
	}



	/**
	 * координаты точки по стороне и смещению
	 * 
	 * @param side
	 *            - сторона
	 * @param offset
	 *            - смещение
	 * @return
	 */
	private float[] CoorFromInput(int side, int offset) {
		float x = 0, y = 0;

		switch (side) {
		case 0:
			// левая
			x = -this.getWidth() / 2;
			y = offset * (Gdx.graphics.getHeight() + this.getHeight()) / 100
					- this.getHeight() / 2;
			break;
		case 1:
			// верх
			y = Gdx.graphics.getHeight() + this.getHeight() / 2;
			x = offset * 1f * (Gdx.graphics.getWidth() + this.getWidth()) / 100
					- this.getWidth() / 2;
			break;
		case 2:
			// право
			x = Gdx.graphics.getWidth() + this.getWidth() / 2;
			y = offset * (Gdx.graphics.getHeight() + this.getHeight()) / 100
					- this.getHeight() / 2;
			break;
		case 3:
			// низ
			y = -this.getHeight() / 2;
			x = offset * 1f * (Gdx.graphics.getWidth() + this.getWidth()) / 100
					- this.getWidth() / 2;
			break;

		}

		float[] coor = new float[2];
		coor[0] = x;
		coor[1] = y;
		return coor;

	}
}

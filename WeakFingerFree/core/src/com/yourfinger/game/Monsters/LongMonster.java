package com.yourfinger.game.Monsters;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.yourfinger.game.ExtraClasses.Finger;
import com.yourfinger.game.ImportantClasses.Monster;

/**
 * ������������ ����� ��� ��������� � ����� ����� ����, ���� �������
 * ������������� � ����
 */
public class LongMonster extends Monster {

	// �������� ������� �������
	final int MODE_START_POSITION = 0;
	final int MODE_HOLE_SPEED = 1;
	final int MODE_DIRECTION = 2;

	// ������ ������
	final int CENTER_POSITION = 0;
	final int RANDOM_POSITION = -1;
	final int FINGER_POSITION = -2;

	// �������� ������� ��������
	final int SIZE_RELATIVE_HEIGHT = 0;
	final int SIZE_RELATIVE_HOLE_WIDTH = 1;

	final float HEIGHT_TO_WIDTH_RATIO = 10;

	boolean isCurrent; // ���������� �� �������� �� ������ ����
	// �����������.
	float time;// ����� ����������� �������.
	float speed;
	float holeSpeed = 0;
	TextureRegion texture;
	Finger finger;
	float[] size;
	int[] mode;// ����� ������
	float holeCoordinate;// �-���������� ������ �����
	float timeForNext;



	public LongMonster(Finger finger, TextureAtlas atlas,
			String backgroundName, float time, float[] size, int mode[],
			float next) {
		super();

		// ��������� ������
		this.finger = finger;
		this.time = time;
		this.mode = mode;
		this.size = size;
		this.isCurrent = true;
		this.speed = ((float) Gdx.graphics.getHeight() + 2
				* Gdx.graphics.getHeight() * this.size[SIZE_RELATIVE_HEIGHT])
				/ this.time;
		this.timeForNext = time - next;

		// ������ ��������
		texture = new TextureRegion(atlas.findRegion(backgroundName));

		// ������ ������, ������ �� ���� �����
		this.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()
				* this.size[SIZE_RELATIVE_HEIGHT]);

		// ������������� ��������� �������
		this.setX(0);
		this.setY(Gdx.graphics.getHeight());

		// ������ ��������� �����
		switch (mode[MODE_START_POSITION]) {
		case CENTER_POSITION:
			holeCoordinate = Gdx.graphics.getWidth() / 2;
			break;
		case RANDOM_POSITION:
			Random rand = new Random();
			holeCoordinate = rand.nextInt(Math.round(Gdx.graphics.getWidth()
					* (1 - this.size[SIZE_RELATIVE_HOLE_WIDTH])))
					+ Gdx.graphics.getWidth()
					* this.size[SIZE_RELATIVE_HOLE_WIDTH] / 2;
			break;
		case FINGER_POSITION:
			holeCoordinate = finger.x;
			break;
		default:
			holeCoordinate = mode[MODE_START_POSITION]
					* Gdx.graphics.getWidth()
					* (1 - this.size[SIZE_RELATIVE_HOLE_WIDTH]) / 100
					+ Gdx.graphics.getWidth()
					* this.size[SIZE_RELATIVE_HOLE_WIDTH] / 2;
		}

		if (mode.length != 1) {
			// ������ �������� �����
			if (mode[MODE_HOLE_SPEED] != 0)
				holeSpeed = Gdx.graphics.getWidth()
						* (1 - this.size[SIZE_RELATIVE_HOLE_WIDTH]) * 1000
						/ mode[MODE_HOLE_SPEED];

			if (mode.length != 2) {
				speed *= -1;
				this.setY(0);
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
		if ((finger.y >= this.getY())
				&& (finger.y <= this.getY() + this.getHeight())) {
			// ���� ����� �� ��������� ����� �� �������
			if (finger.x < (holeCoordinate - Gdx.graphics.getWidth()
					* this.size[SIZE_RELATIVE_HOLE_WIDTH]))
				return true;
			if (finger.x > (holeCoordinate + Gdx.graphics.getWidth()
					* this.size[SIZE_RELATIVE_HOLE_WIDTH]))
				return true;
		}
		return false;
	}



	@Override
	public void draw(Batch batch, float parentAlpha) {
		// ������ ����� �����
		batch.draw(texture, holeCoordinate - Gdx.graphics.getWidth()
				* this.size[SIZE_RELATIVE_HOLE_WIDTH] - getHeight()
				* this.HEIGHT_TO_WIDTH_RATIO, getY(), getHeight()
				* this.HEIGHT_TO_WIDTH_RATIO, getHeight());

		// ������ ������ �����
		batch.draw(texture, holeCoordinate + Gdx.graphics.getWidth()
				* this.size[SIZE_RELATIVE_HOLE_WIDTH], getY(), getHeight()
				* this.HEIGHT_TO_WIDTH_RATIO, getHeight());
	}



	@Override
	public void act(float delta) {
		if (holeSpeed != 0) {
			holeCoordinate += holeSpeed * delta;

			// ���� ��������� � �����, ������ �����������
			if (((holeCoordinate >= Gdx.graphics.getWidth()
					* (1 - this.size[SIZE_RELATIVE_HOLE_WIDTH] / 2)) && (holeSpeed > 0))
					|| (((holeCoordinate <= Gdx.graphics.getWidth()
							* this.size[SIZE_RELATIVE_HOLE_WIDTH] / 2)) && (holeSpeed < 0)))
				holeSpeed *= -1;

		}
		time -= delta;
		this.setY(this.getY() - delta * speed);
	}
}

package com.yourfinger.game.Monsters;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.yourfinger.game.ExtraClasses.Finger;

/**
 * машина 0 - машина в езде
 * 1 - стоит
 * 2 - направлена вперед - мы обгоняем
 * 3 - машина в езде на 4 - полосном шоссе
 */
public class Car extends LinearMonster {

	private static final int IN_DRIVE = 0;
	private static final int IN_STAY = 1;
	private static final int DRIVE_FORWARD = 2;
	private static final int IN_DRIVE_MINI = 3;


	boolean isCounter;
	TextureRegion region;
	int type;



	public Car(Finger finger, TextureAtlas atlas, String backgroundName,
			float time, float[] size, int[] mode, float next, float anim,
			float backgroundSpeed) {

		super(finger, atlas, backgroundName, time, size, new int[] { mode[1] },
				next, anim);

		type = mode[0];

		
		
		switch (mode[0]) {
		case IN_DRIVE:
			StartInDrive(mode, atlas, backgroundName, next, time);
			break;
		case IN_STAY:
			this.speed = backgroundSpeed;
			break;
		case DRIVE_FORWARD:
			angle = 180;
			//не надо выхода за грань
			if (getX() < 0)
				setX(0);
			if (getX() > Gdx.graphics.getWidth() - getWidth())
				setX(Gdx.graphics.getWidth() - getWidth());
			break;
		case IN_DRIVE_MINI:
			StartInDriveMini(mode, atlas, backgroundName, next, time);
			break;
		}

		
		
		// берем случайную текстуру
		int i = 1;
		while (atlas.findRegion(backgroundName, i) != null)
			i++;
		region = atlas.findRegion(backgroundName,
				new Random().nextInt(i - 1) + 1);

	}



	private void StartInDrive(int[] mode, TextureAtlas atlas,
			String backgroundName, float next, float time) {
		if (mode[1] == 1)
			isCounter = false;
		else if (mode[1] == 2)
			isCounter = true;
		else if (new Random().nextInt(4) == 0)
			isCounter = false;
		else
			isCounter = true;

		if (isCounter) {
			this.time = time * 0.75f;
			setCenterPosition(Gdx.graphics.getWidth() * 0.25f, getCenterY());
			angle = 0;
			this.speed = ((float) Gdx.graphics.getHeight() + 2 * this
					.getHeight()) / this.time;
			this.timeForNext = time - next;
		} else {
			this.time = time * 2f;
			setCenterPosition(Gdx.graphics.getWidth() * 0.75f, getCenterY());
			angle = 180;
			this.speed = ((float) Gdx.graphics.getHeight() + 2 * this
					.getHeight()) / this.time;
			this.timeForNext = time - next;
		}
	}
	
	
	private void StartInDriveMini(int[] mode, TextureAtlas atlas,
			String backgroundName, float next, float time) {
		if (mode[1] == 1)
			isCounter = false;
		else if (mode[1] == 2)
			isCounter = true;
		else if (new Random().nextInt(3) == 0)
			isCounter = false;
		else
			isCounter = true;
		
		boolean isFirst = (new Random().nextInt(2) == 0);

		if (isCounter) {
			
			this.time = time * 1f;
			if (isFirst)
				setCenterPosition(Gdx.graphics.getWidth() * 0.125f, getCenterY());
			else
				setCenterPosition(Gdx.graphics.getWidth() * 0.375f, getCenterY());
			angle = 0;
			this.speed = ((float) Gdx.graphics.getHeight() + 2 * this
					.getHeight()) / this.time;
			this.timeForNext = time - next;
		} else {
			this.time = time * 1.5f;
			if (isFirst)
				setCenterPosition(Gdx.graphics.getWidth() * 0.625f, getCenterY());
			else
				setCenterPosition(Gdx.graphics.getWidth() * 0.875f, getCenterY());
			angle = 180;
			this.speed = ((float) Gdx.graphics.getHeight() + 2 * this
					.getHeight()) / this.time;
			this.timeForNext = time - next;
		}
	}



	@Override
	public void draw(Batch batch, float parentAlpha) {

		batch.draw(region, getX(), getY(), this.getWidth() / 2,
				this.getHeight() / 2, getWidth(), getHeight(), 1, 1, angle);

	}



	@Override
	public boolean IsCurrent() {
		return ((time > 0) || (!NextReady()) || (this.getY() + this.getHeight() > 0));
	}

}

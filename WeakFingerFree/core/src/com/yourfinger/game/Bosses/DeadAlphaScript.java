package com.yourfinger.game.Bosses;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class DeadAlphaScript extends Actor {

	boolean isFirstMode = true;
	boolean isDone = false;

	TextureAtlas atlas;

	Sprite alpha;

	ArrayList<Boom> booms;

	// 2 часть
	TextureRegion pixel;
	boolean[][] pixels;
	int COUNT_OF_COLUMN = 14;
	int COUNT_OF_ROW;
	int emptyPixels;



	public DeadAlphaScript(TextureAtlas atlas, float x, float y, float width,
			float height) {
		this.atlas = atlas;
		alpha = new Sprite(atlas.findRegion("alpha"));
		alpha.setPosition(x, y);
		alpha.setSize(width, height);
		pixel = atlas.findRegion("pixel");

		booms = new ArrayList<DeadAlphaScript.Boom>();

		for (int i = 0; i < 30; i++)
			booms.add(new Boom(0));

		for (int i = 0; i < 120; i++)
			booms.add(new Boom(i * 0.04f));

	}



	@Override
	public void act(float delta) {

		if (isFirstMode) {
			for (Boom b : booms)
				b.act(delta);

			boolean nextMode = true;
			for (Boom b : booms)
				if (b.isCurrent())
					nextMode = false;

			if (nextMode) {
				isFirstMode = false;
				float heiToWid = alpha.getHeight() / alpha.getWidth();
				alpha.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getWidth()
						* heiToWid);
				alpha.setCenter(Gdx.graphics.getWidth() / 2,
						Gdx.graphics.getHeight() / 2);

				// создаем массив пикселей
				COUNT_OF_ROW = (int) Gdx.graphics.getHeight() * COUNT_OF_COLUMN
						/ Gdx.graphics.getWidth() + 1;
				pixels = new boolean[COUNT_OF_ROW][COUNT_OF_COLUMN];
				emptyPixels = COUNT_OF_COLUMN * COUNT_OF_ROW;
			}
		}
		else
		{
			int nextPixel = new Random().nextInt(emptyPixels);
			int currentPixel = 0;
			boolean stop = false;
			for (int row = 0; row < COUNT_OF_ROW; row++)
				for (int column = 0; column < COUNT_OF_COLUMN; column++)
					if (!stop)
						if (!pixels[row][column])
							if (currentPixel == nextPixel)
							{
								pixels[row][column] = true;
								stop = true;
							}
							else
								currentPixel++;
							
			emptyPixels--;
			
			if (emptyPixels < 20)
				isDone = true;
		}
	}



	@Override
	public void draw(Batch batch, float parentAlpha) {
		alpha.draw(batch);

		if (isFirstMode) {
			for (Boom s : booms)
				if (s.isCurrent())
					s.draw(batch);
		} else {
			for (int row = 0; row < COUNT_OF_ROW; row++)
				for (int column = 0; column < COUNT_OF_COLUMN; column++)
					if (pixels[row][column])
						batch.draw(
								pixel,
								column * Gdx.graphics.getWidth()
										/ COUNT_OF_COLUMN,
								row * Gdx.graphics.getWidth() / COUNT_OF_COLUMN,
								Gdx.graphics.getWidth() / COUNT_OF_COLUMN + 1,
								Gdx.graphics.getWidth() / COUNT_OF_COLUMN + 1);
		}
	}



	public boolean IsDone() {
		return isDone;
	}




	private class Boom extends Sprite {

		float ALL_TIME;
		float MIN_ALL_TIME = 0.1f;
		float MAX_ALL_TIME = 0.9f;
		float time;

		float SIZE;
		float MIN_SIZE = Gdx.graphics.getWidth() * 0.15f;
		float MAX_SIZE = Gdx.graphics.getWidth() * 0.6f;



		public Boom(float before) {
			super(atlas.findRegion("boom"));
			Random rand = new Random();
			SIZE = rand.nextFloat() * (MAX_SIZE - MIN_SIZE) + MIN_SIZE;
			ALL_TIME = rand.nextFloat() * (MAX_ALL_TIME - MIN_ALL_TIME)
					+ MIN_ALL_TIME;
			time = ALL_TIME + before;
			setSize(0, 0);
			setPosition(rand.nextFloat() * (Gdx.graphics.getWidth() - SIZE)
					+ SIZE / 2, rand.nextFloat()
					* (Gdx.graphics.getHeight() - SIZE) + SIZE / 2);

		}



		public void act(float delta) {
			time -= delta;
			float centerX = getX() + getWidth() / 2;
			float centerY = getY() + getHeight() / 2;

			setSize(SIZE * (1 - time / ALL_TIME), SIZE * (1 - time / ALL_TIME));
			setCenter(centerX, centerY);

		}



		public boolean isCurrent() {
			return (time > 0) && (time < ALL_TIME);
		}

	}

}

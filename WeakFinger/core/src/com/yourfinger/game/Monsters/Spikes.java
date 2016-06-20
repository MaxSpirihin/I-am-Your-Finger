package com.yourfinger.game.Monsters;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.yourfinger.game.ExtraClasses.Finger;
import com.yourfinger.game.ImportantClasses.Monster;

/**
 * ����, ��������� �� �������. ������� ���������� ������� ����� � ���� ��������.
 * ����� ������ ��������� ���� ����� �� ����� ����� 
 */
public class Spikes extends Monster {

	public static final float SPIKES_TIME = 0.3f;
	final int SPIKES_IN_ROW = 20;

	boolean isCurrent;
	float time;
	Finger finger;
	float[] size;
	int[] mode;
	float timeForNext;
	TextureRegion textureSpike;
	TextureRegion textureLight;

	float x;
	float y;
	float radius;



	public Spikes(Finger finger, TextureAtlas atlas, String backgroundName,
			float time, float[] size, int mode[], float next) {
		// ��������� ������
		this.finger = finger;
		this.time = time;
		this.mode = mode;
		this.size = size;
		this.isCurrent = true;
		this.timeForNext = time - next;

		// ������ ��������
		textureSpike = new TextureRegion(atlas.findRegion(backgroundName));
		textureLight = new TextureRegion(atlas.findRegion(backgroundName + "_light"));

		radius = Gdx.graphics.getWidth() * size[0] / 2;

		if (mode[0] >= 0)
			x = (Gdx.graphics.getWidth() - 2 * radius) * mode[0] / 100 + radius;
		else
			x = (Gdx.graphics.getWidth() - 2 * radius)
					* (new Random().nextInt(100)) / 100 + radius;
		if (mode[1] >= 0)
			y = (Gdx.graphics.getHeight() - 2 * radius) * mode[1] / 100
					+ radius;
		else
			y = (Gdx.graphics.getHeight() - 2 * radius)
					* (new Random().nextInt(100)) / 100 + radius;

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

		// ����
		return ((Math.pow(finger.x - x, 2) + Math.pow(finger.y - y, 2) > Math
				.pow(radius, 2)) && (time < SPIKES_TIME * 3 / 4) && (time > SPIKES_TIME / 4));
	}



	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		//���� ����� �����
		if ((time < SPIKES_TIME) && (time > 0)) {
			//������� ������ � ���-��
			float spSize = Gdx.graphics.getWidth() / SPIKES_IN_ROW;
			float spikesInColumn = ((int) Gdx.graphics.getHeight() / spSize) + 1;
			
			//������� ������
			float currentSize = GetCurrentSize(time, spSize);

			for (int column = 0; column < SPIKES_IN_ROW; column++)
				for (int row = 0; row < spikesInColumn; row++) {
					//��������� ������� ������ ����
					float centerX = column * spSize + spSize / 2;
					float centerY = row * spSize + spSize / 2;
					
					//���� �� � ���� ��������, ������
					if (Math.pow(centerX - x, 2) + Math.pow(centerY - y, 2) > Math
							.pow(radius, 2))
						batch.draw(textureSpike, centerX - currentSize / 2,
								centerY - currentSize / 2, currentSize,
								currentSize);
				}
		}

		batch.draw(textureLight, x - radius, y - radius, 2 * radius, 2 * radius);
	}



	//������ �������������� ����
	private float GetCurrentSize(float time, float size) {
		if (time > SPIKES_TIME / 2)
			time = SPIKES_TIME - time;

		return size * time * 2 / SPIKES_TIME;
	}



	@Override
	public void act(float delta) {
		time -= delta;
	}

}

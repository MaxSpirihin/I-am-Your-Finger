package com.yourfinger.game.Monsters;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.yourfinger.game.ExtraClasses.Finger;
import com.yourfinger.game.ImportantClasses.Monster;

public class Flow extends Monster {

	final float FLOW_SPEED = Gdx.graphics.getHeight() / 1;
	final float PART_OF_BOTTOM_SAVE = 0.3f;
	final float WIDTH_TO_HEIGHT_IN_FLOW = 5;
	final float TIME_FOR_ENTER = 0.3f;

	// ��������� ���������, ���������� �� MonsterInfo
	float liveTime;
	boolean isRandom;
	float timeOfOnePath;
	String backgroundName;
	float normalSaveSpeed;

	// �������
	Finger finger;
	TextureAtlas atlas;

	// ���������� �������
	float time;
	float timeForNext;
	float timeForChange;

	// �����������
	Sprite save;
	float saveSpeed;

	// �����
	TextureRegion txtrflow;
	float flowY;
	float flowHeight;



	public Flow(Finger finger, TextureAtlas atlas, String backgroundName,
			float time, float[] size, int mode[], float next) {
		super();

		// ��������� ������
		this.finger = finger;
		this.liveTime = time;
		this.time = time;
		this.atlas = atlas;
		this.timeForNext = time - next;
		this.backgroundName = backgroundName;

		// ������� �����������
		save = new Sprite(atlas.findRegion(backgroundName + "_save"));
		save.setSize(Gdx.graphics.getWidth() * size[0], Gdx.graphics.getWidth()
				* size[0] / 2);
		save.setCenter(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() / 2);
		normalSaveSpeed = (mode[0] * Gdx.graphics.getWidth()) / time;// ������
																		// ��������
																		// �����������
		timeOfOnePath = time / mode[0];// ����� �������� ����������� �� 1 �����
										// � ������
		saveSpeed = normalSaveSpeed;
		if (mode.length > 1)
			isRandom = true;

		// "�������" �����
		txtrflow = atlas.findRegion(backgroundName + "_flow");
		flowY = FLOW_SPEED * TIME_FOR_ENTER + Gdx.graphics.getHeight();
		flowHeight = (time - 2 * TIME_FOR_ENTER) * FLOW_SPEED
				- Gdx.graphics.getHeight();

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
		return !(((finger.x > save.getX())
				&& (finger.x < save.getX() + save.getWidth()) && (finger.y < save
				.getY())) || ((finger.y < flowY) || (finger.y > flowY
				+ flowHeight)));
	}



	@Override
	public void draw(Batch batch, float parentAlpha) {

		DrawFlow(batch);

		DrawSave(batch);

	}



	private void DrawFlow(Batch batch) {

		// �������� ������ 1�
		txtrflow = atlas.findRegion(backgroundName + "_flow_first");
		DrawFlowElement(batch, flowY);
		float paintY = flowY + Gdx.graphics.getWidth()
				/ WIDTH_TO_HEIGHT_IN_FLOW;
		txtrflow = atlas.findRegion(backgroundName + "_flow");

		while ((paintY < Gdx.graphics.getHeight())
				&& (paintY < flowY + flowHeight)) {

			// �������� ������ ���������, ���� ���
			if (paintY + Gdx.graphics.getWidth() / WIDTH_TO_HEIGHT_IN_FLOW >= flowY
					+ flowHeight) {
				txtrflow = atlas.findRegion(backgroundName + "_flow_last");
				DrawFlowElement(batch, paintY);
				txtrflow = atlas.findRegion(backgroundName + "_flow");
			} else
				DrawFlowElement(batch, paintY);

			paintY += Gdx.graphics.getWidth() / WIDTH_TO_HEIGHT_IN_FLOW;
		}
	}



	private void DrawSave(Batch batch) {
		// �����������
		if (flowY < save.getY() + save.getHeight() / 2)
			save.setRegion(atlas.findRegion(backgroundName + "_save_attack"));//���� ���� ��� ������
		
		//���� ��� ���� ��� �����, ������� �������������
		if ((time < TIME_FOR_ENTER) || (time > liveTime - TIME_FOR_ENTER)) {
			float lastWidth = save.getWidth();
			float lastHeight = save.getHeight();
			float centerX = save.getX() + save.getWidth() / 2;
			float centerY = save.getY() + save.getHeight() / 2;

			float percent;
			if (time < TIME_FOR_ENTER)
				percent = time / TIME_FOR_ENTER;
			else
				percent = (liveTime - time) / TIME_FOR_ENTER;

			save.setSize(lastWidth * percent, lastHeight * percent);
			save.setCenter(centerX, centerY);
			save.draw(batch);
			save.setSize(lastWidth, lastHeight);
			save.setCenter(centerX, centerY);
		} else {
			save.draw(batch);
		}
	}



	private void DrawFlowElement(Batch batch, float paintY) {
		if (paintY > save.getY()) {
			batch.draw(txtrflow, 0, paintY, Gdx.graphics.getWidth(),
					Gdx.graphics.getWidth() / WIDTH_TO_HEIGHT_IN_FLOW);
		} else {
			// ���������� �������� ������������
			float u = txtrflow.getU();
			float v = txtrflow.getV();
			float u2 = txtrflow.getU2();
			float v2 = txtrflow.getV2();

			// ������� � ������ ����� �����
			txtrflow.setRegion(txtrflow.getRegionX(), txtrflow.getRegionY(),
					(int) save.getX() * txtrflow.getRegionWidth()
							/ Gdx.graphics.getWidth(),
					txtrflow.getRegionHeight());
			batch.draw(txtrflow, 0, paintY, save.getX(),
					Gdx.graphics.getWidth() / WIDTH_TO_HEIGHT_IN_FLOW);

			// ���������� ������ � �������� ���������
			txtrflow.setRegion(u, v, u2, v2);

			// ������� � ������ ������ �����
			float x = (save.getX() + save.getWidth()) / Gdx.graphics.getWidth();
			txtrflow.setRegion(
					txtrflow.getRegionX()
							+ (int) (x * txtrflow.getRegionWidth()),
					txtrflow.getRegionY(),
					(int) ((1 - x) * txtrflow.getRegionWidth()),
					txtrflow.getRegionHeight());
			batch.draw(txtrflow, x * Gdx.graphics.getWidth(), paintY, (1 - x)
					* Gdx.graphics.getWidth(), Gdx.graphics.getWidth()
					/ WIDTH_TO_HEIGHT_IN_FLOW);

			// ���������� ������ � �������� ���������
			txtrflow.setRegion(u, v, u2, v2);

			// ���� ����� ��� ������������ ���������� ������������ ������
			if (paintY + Gdx.graphics.getWidth() / WIDTH_TO_HEIGHT_IN_FLOW > save
					.getY() + save.getHeight() / 2) {
				// ������ � �������� ��
				float topPer = (Gdx.graphics.getWidth()
						/ WIDTH_TO_HEIGHT_IN_FLOW + paintY - save.getY() - save
						.getHeight() * PART_OF_BOTTOM_SAVE)
						* WIDTH_TO_HEIGHT_IN_FLOW / Gdx.graphics.getWidth();
				txtrflow.setRegion(
						txtrflow.getRegionX()
								+ (int) (save.getX()
										* txtrflow.getRegionWidth() / Gdx.graphics
											.getWidth()), (int) (txtrflow
								.getRegionY()), (int) (save.getWidth()
								* txtrflow.getRegionWidth() / Gdx.graphics
								.getWidth()),
						(int) (txtrflow.getRegionHeight() * topPer));
				batch.draw(txtrflow, save.getX(),
						save.getY() + save.getHeight() * PART_OF_BOTTOM_SAVE,
						save.getWidth(),
						Gdx.graphics.getWidth() / WIDTH_TO_HEIGHT_IN_FLOW
								+ paintY - save.getY() - save.getHeight()
								* PART_OF_BOTTOM_SAVE);

				// ���������� ������ � �������� ���������
				txtrflow.setRegion(u, v, u2, v2);
			}

		}
	}



	@Override
	public void act(float delta) {
		time -= delta;
		flowY -= delta * FLOW_SPEED;

		if ((time > TIME_FOR_ENTER) && (time < liveTime - TIME_FOR_ENTER)) {
			//��� �� ������ � �����, ��� �������
			save.setX(save.getX() + delta * saveSpeed);
			if ((save.getX() > Gdx.graphics.getWidth() - save.getWidth())
					&& (saveSpeed > 0))
				saveSpeed *= -1;
			if ((save.getX() < 0) && (saveSpeed < 0))
				saveSpeed *= -1;

			if (isRandom) {
				timeForChange -= delta;
				if (timeForChange < 0) {
					UpdateMove();
				}
			}
		}
	}



	private void UpdateMove() {
		Random rand = new Random();
		if (saveSpeed == 0) {
			// �� �����
			if (rand.nextInt(2) == 0)
				saveSpeed = -normalSaveSpeed;
			else
				saveSpeed = normalSaveSpeed;
		} else {
			// �� ��������
			if (rand.nextInt(2) == 0)
				saveSpeed = 0;// ������
			else
				saveSpeed *= -1;// ������� �����������
		}

		if (saveSpeed == 0)
			timeForChange = (rand.nextInt(40) + 10) * timeOfOnePath / 100;
		else
			timeForChange = (rand.nextInt(100) + 10) * timeOfOnePath / 100;
	}

}

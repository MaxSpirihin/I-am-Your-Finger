package com.yourfinger.game.ImportantClasses;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.yourfinger.game.ExtraClasses.Finger;
import com.yourfinger.game.ExtraClasses.MonsterInfo;
import com.yourfinger.game.Helpers.MonsterReturner;

public class ArcadeLevel {

	int[] backgroundIndex;// ����� ���� �� ������
	ArrayList<MonsterInfo> monstersInfo;// ������ ���� � ��������
	ArrayList<MonsterInfo> startMonstersInfo;
	String textColor;
	float[] backgroundMoveTime;
	int numOfBackground;



	/**
	 * �������� �������� ����
	 */
	public float GetBackgroundSpeed() {
		if (backgroundMoveTime[numOfBackground] == 0)
			return 0;
		return Gdx.graphics.getHeight() / backgroundMoveTime[numOfBackground];
	}



	/**
	 * ������ ���� �� ���� ������
	 */
	public int GetBackgroundIndex() {
		return this.backgroundIndex[numOfBackground];
	}



	/**
	 * �������� ���������� �� ������� ������ �������
	 * 
	 * @param finger
	 *            - �����
	 * @param atlas
	 */
	public Monster GetNextMonster(Finger finger, TextureAtlas atlas) {

		int currentMonster = (new Random()).nextInt(monstersInfo.size());

		if (monstersInfo.get(currentMonster).next == 0)
			monstersInfo.get(currentMonster).next = monstersInfo
					.get(currentMonster).time;
		Monster monster = MonsterReturner.GetMonster(finger, atlas,
				monstersInfo.get(currentMonster), GetBackgroundSpeed());
		
		
		return monster;
	}



	/**
	 * ���� ������ �� ���� ������
	 * 
	 * @return
	 */
	public Color GetTextColor() {
		return Color.valueOf(textColor);
	}



	public void Start() {
		this.numOfBackground = (new Random()).nextInt(backgroundIndex.length);
		this.monstersInfo = new ArrayList<MonsterInfo>();
		for (MonsterInfo mI : this.startMonstersInfo)
		{
			this.monstersInfo.add(mI.Copy());
		}
	}
	
	/**
	 * ����������� ��������� ����
	 * @param count �� ������� ��� ��������� ��������
	 */
	public void IncreaseDifficulty(float count)
	{
		for (MonsterInfo mI : monstersInfo)
		{
			MonsterReturner.IncreaseDifficulty(mI, count);
		}
	}

}

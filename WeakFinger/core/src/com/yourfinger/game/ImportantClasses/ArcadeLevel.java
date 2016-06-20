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

	int[] backgroundIndex;// номер фона из атласа
	ArrayList<MonsterInfo> monstersInfo;// массив инфы о монстрах
	ArrayList<MonsterInfo> startMonstersInfo;
	String textColor;
	float[] backgroundMoveTime;
	int numOfBackground;



	/**
	 * скорость движения фона
	 */
	public float GetBackgroundSpeed() {
		if (backgroundMoveTime[numOfBackground] == 0)
			return 0;
		return Gdx.graphics.getHeight() / backgroundMoveTime[numOfBackground];
	}



	/**
	 * индекс фона на этом уровне
	 */
	public int GetBackgroundIndex() {
		return this.backgroundIndex[numOfBackground];
	}



	/**
	 * получаем следующего по порядку уровня монстра
	 * 
	 * @param finger
	 *            - палец
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
	 * цвет текста на этом уровне
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
	 * увеличивает сложность игры
	 * @param count во сколько раз увеличить скорость
	 */
	public void IncreaseDifficulty(float count)
	{
		for (MonsterInfo mI : monstersInfo)
		{
			MonsterReturner.IncreaseDifficulty(mI, count);
		}
	}

}

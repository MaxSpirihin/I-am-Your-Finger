package com.yourfinger.game.ImportantClasses;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.yourfinger.game.ExtraClasses.Finger;
import com.yourfinger.game.ExtraClasses.MonsterInfo;
import com.yourfinger.game.Helpers.MonsterReturner;

/**
 * �������� ���������� �� ������
 */
public class Level {

	int backgroundIndex;// ����� ���� �� ������
	ArrayList<MonsterInfo> monstersInfo;// ������ ���� � ��������
	int currentMonster;
	String textColor;
	float backgroundMoveTime;
	float extraTime;



	public Level() {

		// ���� ������� ��������� � ���������� ����� ������, ���������� ���
		// ����� � ������������ � �������, ������ ���� ����������� ������
		// JSON����, ����� ������ ���� ����������� ��� �������� ����� ��������

		// this.info = "This is test string ����� ��� ��������";
		// this.backgroundIndex = 1;
		// monstersInfo = new ArrayList<MonsterInfo>();
		// for (int i = 0; i < 10; i++) {
		// this.monstersInfo.add(new MonsterInfo("linear_monster", "monster",
		// 0.5f, 1));
		// }
		// // Json json = new Json();
		// // System.out.println(json.toJson(this));
		// currentMonster = 0;
	}


	/**
	 * �������� �������� ����
	 */
	public float GetBackgroundSpeed()
	{
		if (backgroundMoveTime == 0)
			return 0;
		return Gdx.graphics.getHeight()/backgroundMoveTime;
	}
	

	/**
	 * ������ ���� �� ���� ������
	 */
	public int GetBackgroundIndex() {
		return this.backgroundIndex;
	}



	/**
	 * ��������� ����� ������
	 * */
	public float GetLevelTime() {
		float time = 0;
		// ��������� ����� ����� ����������� ����.�������
		for (MonsterInfo m : this.monstersInfo) {
			if (m.next == 0)
				time += m.time;
			else
				time += m.next;
		}
		System.out.println("time = " + String.valueOf(time));
		// �� ����� � ��� ����� ��� ��������� �� ��������� ������
		// �������� ����� ��������� ����������� ������� �������
		// ������������ ������ �������
		float timeOfStart = 0;
		for (MonsterInfo m : this.monstersInfo) {
			float timeOfEnd = timeOfStart + m.time;

			if (timeOfEnd > time)
				time = timeOfEnd;

			if (m.next == 0)
				timeOfStart += m.time;
			else
				timeOfStart += m.next;
		}

		return time;
	}



	/**
	 * �������� ���������� �� ������� ������ �������
	 * 
	 * @param finger
	 *            - �����
	 * @param atlas
	 */
	public Monster GetNextMonster(Finger finger, TextureAtlas atlas) {
		if (currentMonster == this.monstersInfo.size())
			return null;
		if (monstersInfo.get(currentMonster).next == 0)
			monstersInfo.get(currentMonster).next = monstersInfo
					.get(currentMonster).time;
		Monster monster = MonsterReturner.GetMonster(finger, atlas,
				monstersInfo.get(currentMonster), GetBackgroundSpeed());
		currentMonster++;
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



	/**
	 * �� ����, ������������� �������
	 */
	public void ResetMonsterCount() {
		this.currentMonster = 0;
	}
	
	/**
	 * ��� ����� ��� ����������� ������������
	 */
	public float GetExtraTime()
	{
		return extraTime;
	}

}

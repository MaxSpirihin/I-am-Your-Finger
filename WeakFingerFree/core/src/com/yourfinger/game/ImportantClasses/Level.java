package com.yourfinger.game.ImportantClasses;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.yourfinger.game.ExtraClasses.Finger;
import com.yourfinger.game.ExtraClasses.MonsterInfo;
import com.yourfinger.game.Helpers.MonsterReturner;

/**
 * содержит информацию об уровне
 */
public class Level {

	int backgroundIndex;// номер фона из атласа
	ArrayList<MonsterInfo> monstersInfo;// массив инфы о монстрах
	int currentMonster;
	String textColor;
	float backgroundMoveTime;
	float extraTime;



	public Level() {

		// Если имеются трудности с написанием файла уровня, создавайте его
		// здесь и сериализуйте в консоль, вызвав этот конструктор вместо
		// JSONного, также юзайте этот конструктор для тестинга новых монстров

		// this.info = "This is test string чисто для проверки";
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
	 * скорость движения фона
	 */
	public float GetBackgroundSpeed()
	{
		if (backgroundMoveTime == 0)
			return 0;
		return Gdx.graphics.getHeight()/backgroundMoveTime;
	}
	

	/**
	 * индекс фона на этом уровне
	 */
	public int GetBackgroundIndex() {
		return this.backgroundIndex;
	}



	/**
	 * вычисляем всего уровня
	 * */
	public float GetLevelTime() {
		float time = 0;
		// вычисляем время после выступления посл.монстра
		for (MonsterInfo m : this.monstersInfo) {
			if (m.next == 0)
				time += m.time;
			else
				time += m.next;
		}
		System.out.println("time = " + String.valueOf(time));
		// но вдруг в это время еще выступает не последний монстр
		// вычислим время окончания выступления каждого монстра
		// относительно общего времени
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
	 * получаем следующего по порядку уровня монстра
	 * 
	 * @param finger
	 *            - палец
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
	 * цвет текста на этом уровне
	 * 
	 * @return
	 */
	public Color GetTextColor() {
		return Color.valueOf(textColor);
	}



	/**
	 * по сути, перезапускает уровень
	 */
	public void ResetMonsterCount() {
		this.currentMonster = 0;
	}
	
	/**
	 * доп время для исправления погрешностей
	 */
	public float GetExtraTime()
	{
		return extraTime;
	}

}

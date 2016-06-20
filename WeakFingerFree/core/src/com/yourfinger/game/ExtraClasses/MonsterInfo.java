package com.yourfinger.game.ExtraClasses;

/**
 * содержит данные для вызова монстра. Именно массив этих объектов хранится в
 * JSON – описании уровня.
 */
public class MonsterInfo {

	/**
	 * номер монстра
	 */
	public int id;
	/**
	 * имя текстурки в атласе
	 */
	public String backgroundName;
	/**
	 * время выступления
	 */
	public float time;

	/**
	 * относительные размер, подробнее - в документе
	 */
	public float[] size;
	/**
	 * установки режимов (см.документ для каждого)
	 */
	public int[] mode;

	/**
	 * время вылета след. монстра
	 */
	public float next;
	/**
	 * время кадра анимации
	 */
	public float anim;



	public MonsterInfo(int id, String backgroundName, float time, float[] size,
			int[] mode, float next, float anim) {
		this.id = id;
		this.backgroundName = backgroundName;
		this.time = time;
		this.size = size;
		this.mode = mode;
		this.next = next;
		this.anim = anim;
	}



	public MonsterInfo() {
	}
	
	public MonsterInfo Copy()
	{
		MonsterInfo mI = new MonsterInfo(id, backgroundName, time, size.clone(),mode.clone(),next,anim);
		return mI;
	}
}

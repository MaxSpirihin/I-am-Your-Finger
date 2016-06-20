package com.yourfinger.game.ExtraClasses;

/**
 * �������� ������ ��� ������ �������. ������ ������ ���� �������� �������� �
 * JSON � �������� ������.
 */
public class MonsterInfo {

	/**
	 * ����� �������
	 */
	public int id;
	/**
	 * ��� ��������� � ������
	 */
	public String backgroundName;
	/**
	 * ����� �����������
	 */
	public float time;

	/**
	 * ������������� ������, ��������� - � ���������
	 */
	public float[] size;
	/**
	 * ��������� ������� (��.�������� ��� �������)
	 */
	public int[] mode;

	/**
	 * ����� ������ ����. �������
	 */
	public float next;
	/**
	 * ����� ����� ��������
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

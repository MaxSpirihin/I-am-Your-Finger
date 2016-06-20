package com.yourfinger.game.ImportantClasses;

import com.badlogic.gdx.scenes.scene2d.Group;


public class Boss extends Group {
	
	protected int lives;
	
	public int GetLives()
	{
		return lives;
	}
	
	
	/**
	 * � ����� ����������� �� ���� �� ������� �� �������. ���������� true,
	 * ��������� ������ ��������������, ��� �������, ���� ��������.
	 */
	public boolean FingerIsDead() {
		return true;
	}
	
	
	public boolean IsAttacked()
	{
		return false;
	}
	
	
	public boolean IsDead()
	{
		return lives<=0;
	}
	
	
	/**
	 * ������ ��� ������� ��������� �������� ����� ����� ���������
	 * @return
	 */
	public boolean FingerCanBeLose()
	{
		return lives<=0;
	}
	

}

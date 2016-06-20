package com.yourfinger.game.ImportantClasses;

import com.badlogic.gdx.scenes.scene2d.Group;

/**
 * ������ ��� ���� ����� ��������. ������� ����� ������ ������������ ������. �
 * ����� �������� �����������. ��������� ������ �.�. ��� ��������� ������ �.�.
 * ��������� ������� ����� ���� ��� ������� ��� � �������.
 */
public class Monster extends Group {

	// �����������
	public Monster() {

	}



	/**
	 * ���������� �� �������� �� ������ ���� �����������.
	 */
	public boolean IsCurrent() {
		return false;
	}



	/**
	 * ���������� �� ���� �� ��������� ���������� �������
	 */
	public boolean NextReady() {
		return false;
	}



	/**
	 * � ����� ����������� �� ���� �� ������� �� �������. ���������� true,
	 * ��������� ������ ��������������, ��� �������, ���� ��������.
	 */
	public boolean FingerIsDead() {
		return true;
	}

}

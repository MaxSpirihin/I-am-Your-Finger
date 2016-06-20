package com.yourfinger.game.ImportantClasses;

import com.badlogic.gdx.scenes.scene2d.Group;

/**
 * шаблон для всех наших монстров. Объекты этого класса использовать нельзя. В
 * общем применям полиморфизм. Наследуем группу т.к. она наследует актера т.ч.
 * наследник монстра может быть как группой так и актером.
 */
public class Monster extends Group {

	// конструктор
	public Monster() {

	}



	/**
	 * показывает не закончил ли монстр свое выступление.
	 */
	public boolean IsCurrent() {
		return false;
	}



	/**
	 * показывает не пора ли выпускать следующего монстра
	 */
	public boolean NextReady() {
		return false;
	}



	/**
	 * – здесь проверяется не сдох ли пальчик от касания. Возвращает true,
	 * наследник ОБЯЗАН переопределить, так заметим, если забудешь.
	 */
	public boolean FingerIsDead() {
		return true;
	}

}

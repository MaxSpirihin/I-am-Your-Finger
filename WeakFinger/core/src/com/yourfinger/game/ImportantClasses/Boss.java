package com.yourfinger.game.ImportantClasses;

import com.badlogic.gdx.scenes.scene2d.Group;


public class Boss extends Group {
	
	protected int lives;
	
	public int GetLives()
	{
		return lives;
	}
	
	
	/**
	 * Ц здесь провер€етс€ не сдох ли пальчик от касани€. ¬озвращает true,
	 * наследник ќЅя«јЌ переопределить, так заметим, если забудешь.
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
	 * обычно при запуске последней анимации палец можно отпустить
	 * @return
	 */
	public boolean FingerCanBeLose()
	{
		return lives<=0;
	}
	

}

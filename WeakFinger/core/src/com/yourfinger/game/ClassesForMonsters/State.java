package com.yourfinger.game.ClassesForMonsters;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.yourfinger.game.ExtraClasses.Finger;

/**
 * Состояние Альфы - чисто структурирование кода
 */
public interface State {

	public void act(float delta);
	public void draw(Batch batch);
	public boolean FingerIsDead(Finger finger);
	public boolean IsCurrent();

}

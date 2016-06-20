package com.yourfinger.game.Monsters;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.yourfinger.game.ExtraClasses.Finger;
import com.yourfinger.game.ExtraClasses.Utils;

/**
 * аналог линейного монстра, он всегда крутится, но при этом воспринимается, как
 * крутящийся прямоугольник
 */
public class LinearTurnMonster extends LinearMonster {

	public LinearTurnMonster(Finger finger, TextureAtlas atlas,
			String backgroundName, float time, float[] size, int[] mode,
			float next, float anim) {
		super(finger, atlas, backgroundName, time, size, mode, next, anim);
	}



	@Override
	public boolean FingerIsDead() {
		return Utils.PoiinsIsInRectangle(finger.x, finger.y, this.getCenterX(),
				this.getCenterY(), this.getWidth(), this.getHeight(), angle);
	}

}

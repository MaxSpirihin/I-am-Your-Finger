package com.yourfinger.game.ClassesForMonsters;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.yourfinger.game.ExtraClasses.Finger;
import com.yourfinger.game.Monsters.BreakingMonster;

public class GrenadeForGitler extends BreakingMonster {

	float vx, vy;



	public GrenadeForGitler(Finger finger, TextureAtlas atlas,
			String backgroundName, float time, float[] size, float x, float y,
			float finalX, float finalY) {
		super(finger, atlas, backgroundName, time, size, new int[] { 0, 50, 70,
				2 }, 0, 0);

		this.setX(x);
		this.setY(y);

		vx = (finalX - x) / (time * 0.5f);
		vy = (finalY - y) / (time * 0.5f);

	}



	@Override
	public void act(float delta) {
		super.act(delta);
		if (!isBoom) {
			setY(getY() + delta * (speed + vy));
			setX(getX() + delta * vx);
		}

	}

}

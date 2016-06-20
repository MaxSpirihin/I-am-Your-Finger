package com.yourfinger.game.ClassesForMonsters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.yourfinger.game.ExtraClasses.Finger;
import com.yourfinger.game.ExtraClasses.Utils;
import com.yourfinger.game.Monsters.DiagonalMonster;


/**
 * огромное кол-во этих монстров летает на экране во время saveZone, наследник {@link DiagonalMonster}, 
 */
public class MassMonsterInSaveZone extends DiagonalMonster {

	Sprite saveZone;

	public MassMonsterInSaveZone(Finger finger, TextureAtlas atlas,
			String backgroundName, float time, float[] size, int[] mode,
			float next, float anim, Sprite saveZone) {
		super(finger, atlas, backgroundName, time, size, mode, next, anim);
		this.saveZone = saveZone;

		// вычисляем координаты, скорости и угол
		float[] coor = GetStartAndEndCoordinates();
		float x1 = coor[0];
		float y1 = coor[1];
		float x2 = coor[2];
		float y2 = coor[3];
		vx = (x2 - x1) / 0.8f;
		vy = (y2 - y1) / 0.8f;
		angle = Utils.GetAngle(x1, y1, x2, y2);
	}



	@Override
	public void act(float delta) {
		float oldX = this.getX();
		float oldY = this.getY();
		super.act(delta);
		angle = Utils.GetAngle(oldX, oldY, getX(), getY());

		float saveCenterX = saveZone.getX() + saveZone.getWidth() / 2;
		float saveCenterY = saveZone.getY() + saveZone.getHeight() / 2;
		boolean isInSaveZone = (Math.pow(saveCenterX - this.getCenterX(), 2)
				+ Math.pow(saveCenterY - this.getCenterY(), 2) < Math.pow(
				this.getWidth() / 2 + saveZone.getWidth() / 2, 2));

		float MORE = 1.0f;
		if (isInSaveZone) {
			if ((saveCenterX < this.getCenterX()) && (vx < 0))
				vx *= -MORE;

			if ((saveCenterY < this.getCenterY()) && (vy < 0))
				vy *= -MORE;

			if ((saveCenterX > this.getCenterX()) && (vx > 0))
				vx *= -MORE;

			if ((saveCenterY > this.getCenterY()) && (vy > 0))
				vy *= -MORE;

		}

		// ставим рикошеты от стен
		if ((this.getX() < 0) && (vx < 0)) {
			vx *= -1;
			this.setX(this.getX() + 5);
		}
		if ((this.getY() < 0) && (vy < 0)) {
			vy *= -1;
			this.setY(this.getY() + 5);
		}
		if ((this.getX() > Gdx.graphics.getWidth() - this.getWidth())
				&& (vx > 0)) {
			vx *= -1;
			this.setX(this.getX() - 5);
		}
		if ((this.getY() > Gdx.graphics.getHeight() - this.getHeight())
				&& (vy > 0)) {
			vy *= -1;
			this.setY(this.getY() - 5);
		}

	}

}

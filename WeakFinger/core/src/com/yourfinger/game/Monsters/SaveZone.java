package com.yourfinger.game.Monsters;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.yourfinger.game.ClassesForMonsters.CombinePath;
import com.yourfinger.game.ClassesForMonsters.DiagonalPath;
import com.yourfinger.game.ClassesForMonsters.MassMonsterInSaveZone;
import com.yourfinger.game.ClassesForMonsters.MiniNicePath;
import com.yourfinger.game.ClassesForMonsters.Path;
import com.yourfinger.game.ExtraClasses.Finger;
import com.yourfinger.game.ImportantClasses.Monster;

public class SaveZone extends Monster {

	final int COUNT_OF_MONSTERS = 150;
	final float TIME_FOR_EXIT = 0.5f;
	final float TIME_FOR_BRIGHT = 0.1f;
	final float TIME_FOR_ENTER = 0.7f;

	float time;
	Finger finger;
	float[] size;
	int[] mode;
	float timeForNext;
	float liveTime;

	//объекты
	Sprite saveZone;
	TextureRegion brightZone;//текстура последней вспышки
	Path path;
	List<MassMonsterInSaveZone> monsters;



	public SaveZone(Finger finger, TextureAtlas atlas, String backgroundName,
			float time, float[] size, int mode[], float next) {
		super();

		// заполняем данные
		this.finger = finger;
		this.time = time;
		this.liveTime = time;
		this.mode = mode;
		this.size = size;
		this.timeForNext = time - next;

		//справйт безопасной зоны
		saveZone = new Sprite(atlas.findRegion(backgroundName + "_save"));
		saveZone.setSize(0,0);
		saveZone.setCenter(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() / 2);
		brightZone = atlas.findRegion(backgroundName + "_save_bright");

		//создаем путь
		switch (mode[0])
		{case 0:
			path = new DiagonalPath(Gdx.graphics.getWidth()*size[0]/2, mode[1]);
			break;
		case 1:
			path = new MiniNicePath(time - TIME_FOR_ENTER - TIME_FOR_EXIT, Gdx.graphics.getWidth()*size[0]/2,mode[1]);
			break;
		case 2:
			path = new CombinePath(Gdx.graphics.getWidth()*size[0]/2 , time - TIME_FOR_ENTER - TIME_FOR_EXIT);
			break;
		default:
			path = new DiagonalPath(Gdx.graphics.getWidth()*size[0]/2,mode[1]);
			break;
		}

		//создаем монстров
		monsters = new LinkedList<MassMonsterInSaveZone>();
		for (int i = 1; i <= COUNT_OF_MONSTERS; i++) {
			MassMonsterInSaveZone s = new MassMonsterInSaveZone(finger, atlas,
					backgroundName, time, new float[] { size[1] },
					new int[] { 1 }, 0, 0, saveZone);
			monsters.add(s);
		}
	}



	@Override
	public boolean IsCurrent() {
		return ((time > 0) || (!NextReady()));
	}



	@Override
	public boolean NextReady() {
		return (time < timeForNext);
	}



	@Override
	public boolean FingerIsDead() {
		for (Monster m : monsters) {
			if (m.FingerIsDead())
				return true;
		}
		return false;
	}



	@Override
	public void draw(Batch batch, float parentAlpha) {
		saveZone.draw(batch);
		for (MassMonsterInSaveZone s : monsters)
			s.draw(batch, parentAlpha);
		
		//если время вспышки
		if (time < TIME_FOR_BRIGHT)
			batch.draw(brightZone, saveZone.getX(), saveZone.getY(),
					saveZone.getWidth(), saveZone.getHeight());
	}



	@Override
	public void act(float delta) {
		time -= delta;

		if (time < TIME_FOR_EXIT) {
			//время - расширяться для выхода
			float newSize = Gdx.graphics.getWidth() * (4 - size[0])
					* (1 - time / TIME_FOR_EXIT) + Gdx.graphics.getWidth()
					* size[0];
			saveZone.setSize(newSize, newSize);
		}
		if (time > liveTime - TIME_FOR_ENTER) {
			//время входа
			float newSize = Gdx.graphics.getWidth() * size[0]
					* (liveTime - time) / TIME_FOR_ENTER;
			saveZone.setSize(newSize, newSize);
			path.SetCoordinates(0, saveZone);
		} else {
			path.SetCoordinates(delta, saveZone);

			for (MassMonsterInSaveZone s : monsters)
				s.act(delta);
		}
	}

}

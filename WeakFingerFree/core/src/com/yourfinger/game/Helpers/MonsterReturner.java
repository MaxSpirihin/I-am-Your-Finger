package com.yourfinger.game.Helpers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.yourfinger.game.ExtraClasses.Finger;
import com.yourfinger.game.ExtraClasses.MonsterInfo;
import com.yourfinger.game.ImportantClasses.Monster;
import com.yourfinger.game.Monsters.*;

/**
 * возвращает экземпл€р монстра по monsterInfo
 */
public class MonsterReturner {

	final static int LINEAR_MONSTER = 1;
	final static int LONG_MONSTER = 2;
	final static int DIAGONAL_MONSTER = 3;
	final static int MATH_MONSTER = 4;
	final static int BOMB = 5;
	final static int SELF_DIRECTION = 6;
	final static int BREAKING_MONSTER = 7;
	final static int SPIKES = 8;
	final static int FLOW = 9;
	final static int SAVE_ZONE = 10;
	final static int LINEAR_TURN = 11;
	final static int CAR = 12;
	final static int EVIL_CAR = 13;
	final static int TANK = 14;
	final static int MACHINE_GUN = 15;


	/**
	 * возвращает экземпл€р монстра по monsterInfo, все параметры дл€
	 * конструктора мостра
	 * 
	 * @param finger
	 *            - палец
	 * @param atlas
	 * @param mI
	 *            - собственно инфо
	 * @param backgroundSpeed
	 *            - скорость фона, нужен некоторым монстрам
	 * @return
	 */
	public static Monster GetMonster(Finger finger, TextureAtlas atlas,
			MonsterInfo mI,float backgroundSpeed) {
		Monster monster = null;
		switch (mI.id) {
		case LINEAR_MONSTER:
			monster = new LinearMonster(finger, atlas, mI.backgroundName,
					mI.time, mI.size, mI.mode, mI.next, mI.anim);
			break;
		case LONG_MONSTER:
			monster = new LongMonster(finger, atlas, mI.backgroundName,
					mI.time, mI.size, mI.mode, mI.next);
			break;
		case DIAGONAL_MONSTER:
			monster = new DiagonalMonster(finger, atlas, mI.backgroundName,
					mI.time, mI.size, mI.mode, mI.next, mI.anim);
			break;
		case MATH_MONSTER:
			monster = new MathMonster(finger, atlas, mI.backgroundName,
					mI.time, mI.size, mI.mode, mI.next, mI.anim);
			break;
		case BOMB:
			monster = new Bomb(finger, atlas, mI.backgroundName, mI.time,
					mI.size, mI.mode, mI.next, mI.anim);
			break;
		case SELF_DIRECTION:
			monster = new SelfDirection(finger, atlas, mI.backgroundName,
					mI.time, mI.size, mI.mode, mI.next, mI.anim);
			break;
		case BREAKING_MONSTER:
			monster = new BreakingMonster(finger, atlas, mI.backgroundName,
					mI.time, mI.size, mI.mode, mI.next, mI.anim);
			break;
		case SPIKES:
			monster = new Spikes(finger, atlas, mI.backgroundName, mI.time,
					mI.size, mI.mode, mI.next);
			break;
		case FLOW:
			monster = new Flow(finger, atlas, mI.backgroundName, mI.time,
					mI.size, mI.mode, mI.next);
			break;
		case SAVE_ZONE:
			monster = new SaveZone(finger, atlas, mI.backgroundName, mI.time,
					mI.size, mI.mode, mI.next);
			break;
		case LINEAR_TURN:
			monster = new LinearTurnMonster(finger, atlas, mI.backgroundName,
					mI.time, mI.size, mI.mode, mI.next, mI.anim);
			break;
		case CAR:
			monster = new Car(finger, atlas, mI.backgroundName,
					mI.time, mI.size, mI.mode, mI.next, mI.anim, backgroundSpeed);
			break;
		case EVIL_CAR:
			monster = new EvilCar(finger, atlas, mI.backgroundName,
					mI.time, mI.size, mI.mode, mI.next, mI.anim);
			break;
		case TANK:
			monster = new Tank(finger, atlas, mI.backgroundName,
					mI.time, mI.size, mI.mode, mI.next);
			break;
		case MACHINE_GUN:
			monster = new MachineGun(finger, atlas, mI.backgroundName,
					mI.time, mI.size, mI.mode, mI.next);
			break;
		}

		return monster;
	}



	/**
	 * увеличивает сложность монстра
	 * 
	 * @param count
	 *            во сколько раз увеличить скорость
	 */
	public static void IncreaseDifficulty(MonsterInfo mI, float count) {
		switch (mI.id) {
		case SELF_DIRECTION:
			mI.mode[1] *= count;
			mI.mode[2] *= count;
			break;
		case SPIKES:
			mI.time = Spikes.SPIKES_TIME + (mI.time - Spikes.SPIKES_TIME)/count;
			mI.next = 0;
			break;
		case FLOW:
			mI.size[0]/=count;
			break;
		case SAVE_ZONE:
			if (mI.mode[0] == 0)
			{
				mI.mode[1]/=count;
				break;
			}
			mI.time /= count;
			break;
		case TANK:
		case MACHINE_GUN:
			mI.mode[1]/=count;
			break;
		default:
			mI.time /= count;
			mI.next /= count;
			break;
		}
	}

}

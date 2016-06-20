package com.yourfinger.game.Bosses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.yourfinger.game.ExtraClasses.Finger;
import com.yourfinger.game.ExtraClasses.Utils;
import com.yourfinger.game.ImportantClasses.Boss;
import com.yourfinger.game.ImportantClasses.Constants;

public class Boss1 extends Boss {

	final int LIVES_COUNT = 3;

	final float ANIMATION_TIME = 0.35f;
	final float ANIMATION_TIME_IN_ATTACKED = 0.15f;
	final float ANIMATION_TIME_BOOM = 0.1f;
	final float ANIMATION_TIME_FINAL_BOOM = 0.4f;

	final float RELATIVE_SIZE = 0.3f;

	final float TIME_FOR_ESCAPE = 1f;
	final float TIME_FOR_ATTACK = 2f;
	final float TIME_OF_BOOM = 0.4f;
	final float TIME_OF_FINAL_BOOM = 2f;

	final int BACKGROUND_INDEX = 1;

	final String TEXTURE_NAME_BOSS = "boss";
	final String TEXTURE_NAME_ATTACKED_BOSS = "attacked_boss";
	final String TEXTURE_NAME_BOOM = "dead_boss";

	final int TIME_OF_DANGER_MONSTER = 10;
	final int TIME_OF_ATTACK = 2;

	final int ACCELERATION_3_LIVE = 500;
	final int ACCELERATION_2_LIVE = 650;
	final int ACCELERATION_1_LIVE = 800;
	final float FRICTION_FORCE = 70;// сила трения (уже ускорения для гашения
									// инерции, направлена против движения)

	// анимации
	Animation animationMain, animationAttacked, animationBoom,
			animationFinalBoom;

	// ключевые объекты
	Finger finger;
	Sprite dead;

	// ключевые переменные
	float time;
	float angle;

	boolean rebirth;// показывает происходит ли в данные мемент процесс
					// перерождения

	// скорости по x и y
	float vx;
	float vy;
	
	//скорости побега
	float vxEscape;
	float vyEscape;

	// время до перерлждения
	float timeToRebirth;


	public Boss1(Finger finger, TextureAtlas atlas) {
		super();
		this.finger = finger;
		this.lives = LIVES_COUNT;
		time = 0;
		angle = 0;
		CreateAnimations(atlas);

		// ставим размер, монстр круглый
		this.setSize(Gdx.graphics.getWidth() * RELATIVE_SIZE,
				Gdx.graphics.getWidth() * RELATIVE_SIZE);

		vx = 0;
		vy = 0;
		rebirth = false;
		this.setCenterPosition(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() + this.getHeight() / 2);

	}



	void CreateAnimations(TextureAtlas atlas) {

		// главная
		String backgroundName = TEXTURE_NAME_BOSS;
		// добавим анимацию
		Array<TextureRegion> txtrBoss = new Array<TextureRegion>();
		if (atlas.findRegion(backgroundName, BACKGROUND_INDEX) != null) {
			// объект анимирован
			int i = 1;
			while (atlas.findRegion(backgroundName, i) != null) {
				txtrBoss.add(atlas.findRegion(backgroundName, i));
				i++;
			}
		} else {
			// для объекта одна текстурка
			txtrBoss.add(atlas.findRegion(backgroundName));
		}
		this.animationMain = new Animation(ANIMATION_TIME, txtrBoss);

		// когда босс доступен для атаки
		backgroundName = TEXTURE_NAME_ATTACKED_BOSS;
		// добавим анимацию
		Array<TextureRegion> txtrAttackedBoss = new Array<TextureRegion>();
		if (atlas.findRegion(backgroundName, 1) != null) {
			// объект анимирован
			int i = 1;
			while (atlas.findRegion(backgroundName, i) != null) {
				txtrAttackedBoss.add(atlas.findRegion(backgroundName, i));
				i++;
			}
		} else {
			// для объекта одна текстурка
			txtrAttackedBoss.add(atlas.findRegion(backgroundName));
		}
		this.animationAttacked = new Animation(ANIMATION_TIME_IN_ATTACKED,
				txtrAttackedBoss);

		backgroundName = TEXTURE_NAME_BOOM;
		// добавим анимацию
		Array<TextureRegion> txtrBoom = new Array<TextureRegion>();
		if (atlas.findRegion(backgroundName, 1) != null) {
			// объект анимирован
			int i = 1;
			while (atlas.findRegion(backgroundName, i) != null) {
				txtrBoom.add(atlas.findRegion(backgroundName, i));
				i++;
			}
		} else {
			// для объекта одна текстурка
			txtrBoom.add(atlas.findRegion(backgroundName));
		}
		this.animationBoom = new Animation(ANIMATION_TIME_BOOM, txtrBoom);
		this.animationFinalBoom = new Animation(ANIMATION_TIME_FINAL_BOOM,
				txtrBoom);
		dead = new Sprite();
	}



	@Override
	public boolean FingerIsDead() {
		if ((IsAttacked()) || (rebirth))
			return false;
		// круг
		return (Math.pow(finger.x - this.getCenterX(), 2)
				+ Math.pow(finger.y - this.getCenterY(), 2) < Math.pow(
				this.getWidth() / 2, 2));

	}



	@Override
	public boolean IsAttacked() {
		// т.е. TIME_OF_DANGER_MONSTER секунд он опасен, TIME_OF_ATTACK на удар
		return (Math.round(time)
				% (TIME_OF_DANGER_MONSTER + TIME_OF_ATTACK) > TIME_OF_DANGER_MONSTER
				- 1);
	}



	@Override
	public void draw(Batch batch, float parentAlpha) {

		if (rebirth) {
			// босса сгасили, он на этапе перерождения и бежит домой
			angle = Utils.GetAngle(this.getCenterX(), this.getCenterY(),
					Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight()
							+ this.getHeight() / 2);

			batch.draw(animationAttacked.getKeyFrame(0, true), getX(), getY(),
					this.getWidth() / 2, this.getHeight() / 2, getWidth(),
					getHeight(), 1, 1, angle);

			// рисуем взрыв, если еще надо
			if ((timeToRebirth < TIME_OF_BOOM)
					|| ((lives == 0) && ((timeToRebirth < TIME_OF_FINAL_BOOM)))) {
				Animation anim = (lives > 0) ? animationBoom
						: animationFinalBoom;
				batch.draw(anim.getKeyFrame(timeToRebirth),
						dead.getX() - this.getWidth() / 2,
						dead.getY() - this.getHeight() / 2,
						this.getWidth() * 2, this.getWidth() * 2);
			}
			return;
		}

		// вычисляем угол к пальцу и рисуем, что надо
		angle = Utils.GetAngle(this.getCenterX(), this.getCenterY(), finger.x,
				finger.y);

		if (!IsAttacked()) {
			batch.draw(animationMain.getKeyFrame(time, true), getX(), getY(),
					this.getWidth() / 2, this.getHeight() / 2, getWidth(),
					getHeight(), 1, 1, angle);
		} else {
			batch.draw(animationAttacked.getKeyFrame(time, true), getX(),
					getY(), this.getWidth() / 2, this.getHeight() / 2,
					getWidth(), getHeight(), 1, 1, angle);
		}

	}



	@Override
	public boolean IsDead() {
		if (lives > 0)
			return false;

		System.out.println(timeToRebirth);
		// если жизни кончились, подождем конца взрыва
		return (timeToRebirth > TIME_OF_FINAL_BOOM);
	}



	@Override
	public void act(float delta) {

		if (rebirth) {
			// сейчас идет перерождение
			timeToRebirth += delta;

			// он сваливает
			this.setX(this.getX() + vxEscape * delta);
			this.setY(this.getY() + vyEscape * delta);

			// не пора ли вернуться
			if ((timeToRebirth > TIME_FOR_ESCAPE) && (lives > 0))
				rebirth = false;
			return;
		}

		time += delta;

		// выбираем ускорение, относительно того, сколько жизней
		float acceleration = ACCELERATION_3_LIVE;
		switch (lives) {
		case 3:
			acceleration = ACCELERATION_3_LIVE;
			break;
		case 2:
			acceleration = ACCELERATION_3_LIVE;
			break;
		case 1:
			acceleration = ACCELERATION_3_LIVE;
			break;
		}
		
		//редактируем ускорение и силу трения относительно экрана
		acceleration*=Gdx.graphics.getHeight()*1f/Constants.STANDART_HEIGHT;
		float frictionForce = FRICTION_FORCE*Gdx.graphics.getHeight()/Constants.STANDART_HEIGHT;
		

		// ускорение к пальцу
		float ax;
		if (finger.x == this.getCenterX())
			ax = 0;
		else if (finger.x > this.getCenterX())
			ax = acceleration;
		else
			ax = -acceleration;

		// если босса можно атаковать, то он сваливает
		if (IsAttacked())
			ax *= -1;

		// учет силы трения
		if (vx > 0)
			ax -= frictionForce;
		else
			ax += frictionForce;

		// вычисляем новые координаты x по правилам из школьной физики
		vx += ax * delta;
		this.setX(ax * delta * delta / 2 + vx * delta + this.getX());

		// все то же для y
		float ay;
		if (finger.y == this.getCenterY())
			ay = 0;
		else if (finger.y > this.getCenterY())
			ay = acceleration;
		else
			ay = -acceleration;

		if (IsAttacked())
			ay *= -1;

		if (vy > 0)
			ay -= frictionForce;
		else
			ay += frictionForce;

		vy += ay * delta;
		this.setY(ay * delta * delta / 2 + vy * delta + this.getY());

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

		// если босса атаковали
		if ((IsAttacked())
				&& (Math.pow(finger.x - this.getCenterX(), 2)
						+ Math.pow(finger.y - this.getCenterY(), 2) < Math.pow(
						this.getWidth() / 2, 2))) {

			// ставим положение спрайта
			dead.setX(this.getX());
			dead.setY(this.getY());

			// вибрируем
			Gdx.input.vibrate(30);

			// скидываем время
			this.time = 0;

			// считаем скоростя для побега с поля
			vxEscape = (Gdx.graphics.getWidth() / 2 - this.getCenterX())
					/ TIME_FOR_ESCAPE;
			vyEscape = (Gdx.graphics.getHeight() + this.getHeight() / 2 - this
					.getCenterY()) / TIME_FOR_ESCAPE;

			
			//обнулим норм скорости
			vx=0;
			vy=0;
			
			// перерождение началось
			rebirth = true;
			timeToRebirth = 0;

			// мы его взгасили
			lives--;

			// если босс здох, выкинем его подальше
			if (lives < 1)
				this.setCenterPosition(-10000, 10000);
		}

	}

}

package com.yourfinger.game.Bosses;

import java.util.ArrayList;

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
import com.yourfinger.game.Monsters.SelfDirection;

public class Boss2 extends Boss {

	private static final float TIME_FOR_FINAL_BOOM = 2f;
	private static final float TIME_OF_MODE_ATTACKED = 6;
	private static final float TIME_OF_ROCKET_MODE = 4.5f;
	private static final float TIME_OF_NEXT_ROCKET = 0.4f;
	private final float RELATIVE_SPEED_OF_START_ROCKET = 1.5f;
	
	
	final float ANIMATION_TIME = 0.04f;
	final String TEXTURE_NAME_BOSS_MOVE = "boss";
	final String TEXTURE_NAME_BOSS_TOP = "boss_top";

	final int COUNT_OF_LIVES = 3;

	final int GAME_MODE_MOVE = 0;
	final float RELATIVE_WIDTH = 1f;
	final float HEIGHT_TO_WIDTH = 0.6f;

	final int GAME_MODE_ROCKET = 1;
	final int GAME_MODE_ATTACKED = 2;

	Animation animationMain, animationTop;

	float time;
	float timeForRocket;
	int gameMode;
	int currentMove;
	float vx;
	float vy;

	// ключевые объекты
	Finger finger;
	ArrayList<Sprite> rockets;
	TextureAtlas atlas;
	Sprite boom;
	SelfDirection selfRocket;

	boolean isBoom;
	boolean isEnd;



	public Boss2(Finger finger, TextureAtlas atlas) {
		super();
		//ключевые объекты
		this.finger = finger;
		gameMode = GAME_MODE_MOVE;
		this.lives = COUNT_OF_LIVES;
		this.atlas = atlas;
		CreateAnimations(atlas);
		
		//запускаем режим движени€
		this.setSize(Gdx.graphics.getWidth() * RELATIVE_WIDTH,
				Gdx.graphics.getWidth() * RELATIVE_WIDTH * HEIGHT_TO_WIDTH);
		this.setCenterPosition(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() + this.getHeight() / 2);
		time = 0;
		gameMode = GAME_MODE_MOVE;
		currentMove = 0;
	}


	/**
	 * создает все анимации
	 */
	void CreateAnimations(TextureAtlas atlas) {

		// главна€
		String backgroundName = TEXTURE_NAME_BOSS_MOVE;
		// добавим анимацию
		Array<TextureRegion> txtrBoss = new Array<TextureRegion>();
		if (atlas.findRegion(backgroundName, 1) != null) {
			// объект анимирован
			int i = 1;
			while (atlas.findRegion(backgroundName, i) != null) {
				txtrBoss.add(atlas.findRegion(backgroundName, i));
				i++;
			}
		} else {
			// дл€ объекта одна текстурка
			txtrBoss.add(atlas.findRegion(backgroundName));
		}
		this.animationMain = new Animation(ANIMATION_TIME, txtrBoss);

		// вид сверху
		backgroundName = TEXTURE_NAME_BOSS_TOP;
		// добавим анимацию
		Array<TextureRegion> txtrTop = new Array<TextureRegion>();
		if (atlas.findRegion(backgroundName, 1) != null) {
			// объект анимирован
			int i = 1;
			while (atlas.findRegion(backgroundName, i) != null) {
				txtrTop.add(atlas.findRegion(backgroundName, i));
				i++;
			}
		} else {
			// дл€ объекта одна текстурка
			txtrBoss.add(atlas.findRegion(backgroundName));
		}
		this.animationTop = new Animation(ANIMATION_TIME, txtrTop);

	}

	
	@Override
	public boolean IsDead()
	{
		return ((isEnd)&&(time>TIME_OF_MODE_ATTACKED+TIME_FOR_FINAL_BOOM));
	}

	@Override
	public boolean FingerCanBeLose()
	{
		return isEnd;
	}
	

	@Override
	public boolean FingerIsDead() {

		switch (gameMode) {
		case GAME_MODE_MOVE:
			// вертолет довольно сложный объект, область соприкосновени€ разбита
			// на 3 пр€моугольника
			if ((finger.x >= this.getX() + this.getWidth() * 0.25)
					&& (finger.x <= this.getX() + this.getWidth() * (1 - 0.25))
					&& (finger.y >= this.getY())
					&& (finger.y <= this.getY() + this.getHeight() * 0.4))
				return true;
			if ((finger.x >= this.getX() + this.getWidth() * 0.35)
					&& (finger.x <= this.getX() + this.getWidth() * (1 - 0.35))
					&& (finger.y >= this.getY() + this.getHeight() * 0.4)
					&& (finger.y <= this.getY() + this.getHeight() * 0.8))
				return true;
			if ((finger.x >= this.getX())
					&& (finger.x <= this.getX() + this.getWidth())
					&& (finger.y >= this.getY() + this.getHeight() * 0.8)
					&& (finger.y <= this.getY() + this.getHeight()))
				return true;
			break;
		case GAME_MODE_ROCKET:
			
			//с вертом
			if (Math.pow(finger.x - this.getCenterX(), 2)
					+ Math.pow(finger.y - this.getCenterY(), 2) < Math.pow(
					this.getWidth() / 2, 2))
				return true;

			//с ракетами
			for (Sprite rocket : rockets)
				if (Utils.PoiinsIsInRectangle(finger.x, finger.y, rocket.getX()
						+ rocket.getOriginX(),
						rocket.getY() + rocket.getOriginY(), rocket.getWidth(),
						rocket.getHeight(), rocket.getRotation()))
					return true;
			break;

		case GAME_MODE_ATTACKED:
			//с вертом
			if (Math.pow(finger.x - this.getCenterX(), 2)
					+ Math.pow(finger.y - this.getCenterY(), 2) < Math.pow(
					this.getWidth() / 2, 2))
				return true;

			// с ракетой
			if (selfRocket != null)
				if (selfRocket.FingerIsDead())
					return true;
		}

		return false;
	}



	@Override
	public void draw(Batch batch, float parentAlpha) {
		switch (gameMode) {
		case GAME_MODE_MOVE:
			int angle = 0;
			//наклон€ем дл€ реализма
			if (vx < 0)
				angle = 5;
			if (vx > 0)
				angle = -5;

			batch.draw(animationMain.getKeyFrame(time, true), getX(), getY(),
					this.getWidth() / 2, this.getHeight() / 2, getWidth(),
					getHeight(), 1, 1, angle);
			break;
		case GAME_MODE_ROCKET:
			//сначала ракеты, потом верт
			for (Sprite s : rockets)
				s.draw(batch);

			batch.draw(animationTop.getKeyFrame(time, true), getX(), getY(),
					getWidth(), getHeight());

			break;
		case GAME_MODE_ATTACKED:
			
			//самонавод€шка есои есть
			if (selfRocket != null)
				selfRocket.draw(batch, parentAlpha);
			
			//если это не конец рисуем верт
			if (!isEnd)
				batch.draw(animationTop.getKeyFrame(time, true), getX(), getY(),
					getWidth(), getHeight());

			//рисуем взрыв
			if ((isBoom)&&(boom!=null))
				boom.draw(batch);
			break;
		}
	}



	@Override
	public void act(float delta) {

		time += delta;

		switch (gameMode) {
		case GAME_MODE_MOVE:
			UpdateMove();
			//двигаем верт
			this.setY(getY() + vy * delta);
			this.setX(getX() + vx * delta);
			break;
		case GAME_MODE_ROCKET:
			timeForRocket += delta;
			UpdateRocket();
			//двигаем верт
			this.setY(getY() + vy * delta);
			this.setX(getX() + vx * delta);
			
			//двигаем ракеты
			for (Sprite r : rockets) {
				r.setX((float) (r.getX() + 1000
						* Math.sin(r.getRotation() * Math.PI / 180) * delta));
				r.setY((float) (r.getY() - 1000
						* Math.cos(r.getRotation() * Math.PI / 180) * delta));
			}
			break;
		case GAME_MODE_ATTACKED:
			UpdateAttacked(delta);
			
			//двигаем верт
			this.setY(getY() + vy * delta);
			this.setX(getX() + vx * delta);
			//вызываем действие самонавод€шки
			if (selfRocket != null)
				selfRocket.act(delta);
			break;
		}

	}



	@Override
	public boolean IsAttacked() {
		return gameMode == GAME_MODE_ATTACKED;
	}



	/**
	 * «десь заключена логика движени€ босса в режиме движени€
	 */
	private void UpdateMove() {

		//скорости и отступ
		final float HORIZONTAL_MOVE_TIME = 0.35f;
		final float VERTICAL_MOVE_TIME = 0.7f;
		final float VERTICAL_INDENT = Gdx.graphics.getHeight() / 4;

		switch (currentMove) {
		case 0:
			this.setCenterPosition(Gdx.graphics.getWidth() * 0.5f,
					Gdx.graphics.getHeight() + this.getHeight());
			vx = 0;
			vy = -Gdx.graphics.getHeight() / 3;
			currentMove++;
			break;
		case 1:
			if (this.getCenterY() < Gdx.graphics.getHeight() / 2) {
				vy = 0;
				vx = -Gdx.graphics.getWidth() / 1;
				currentMove++;
			}
			break;
		case 2:
			if (this.getX() < -this.getWidth()) {
				this.setCenterPosition(-this.getWidth()/2, Gdx.graphics.getHeight()-this.getHeight()*0.7f);
				vy = 0;
				vx = Gdx.graphics.getWidth() / HORIZONTAL_MOVE_TIME;
				currentMove++;
			}
			break;
		case 3:
			if (this.getX() > Gdx.graphics.getWidth()) {
				this.setY(this.getY() - VERTICAL_INDENT);
				vy = 0;
				vx = -Gdx.graphics.getWidth() / HORIZONTAL_MOVE_TIME;
				currentMove++;
			}
			break;
		case 4:
			if (this.getX() < -this.getWidth()) {
				this.setY(this.getY() - VERTICAL_INDENT);
				vy = 0;
				vx = Gdx.graphics.getWidth() / HORIZONTAL_MOVE_TIME;
				currentMove++;
			}
			break;
		case 5:
			if (this.getX() > Gdx.graphics.getWidth()) {
				this.setCenterPosition(Gdx.graphics.getWidth() * 0.8f,
						-this.getHeight() / 2);
				vx = 0;
				vy = Gdx.graphics.getHeight() / VERTICAL_MOVE_TIME;
				currentMove++;
			}
			break;
		case 6:
			if (this.getY() > Gdx.graphics.getHeight()) {
				this.setCenterPosition(Gdx.graphics.getWidth() * 0.2f,
						Gdx.graphics.getHeight() + this.getHeight() / 2);
				vx = 0;
				vy = -Gdx.graphics.getHeight() / VERTICAL_MOVE_TIME;
				currentMove++;
			}
			break;
		case 7:
			if (this.getY() < -this.getHeight()) {
				this.setCenterPosition(Gdx.graphics.getWidth() * 0.8f,
						-this.getHeight() / 2);
				vx = 0;
				vy = Gdx.graphics.getHeight() / VERTICAL_MOVE_TIME;
				currentMove++;
			}
			break;
		case 8:

			if (this.getY() > Gdx.graphics.getHeight()) {
				gameMode = GAME_MODE_ROCKET;
				rockets = new ArrayList<Sprite>();
				timeForRocket = 0;
				this.setSize(Gdx.graphics.getWidth() * RELATIVE_WIDTH,
						Gdx.graphics.getWidth() * RELATIVE_WIDTH
								* HEIGHT_TO_WIDTH);
				this.setPosition(Gdx.graphics.getWidth() / 2 - this.getWidth()
						/ 2, Gdx.graphics.getHeight());
				time = 0;
			}
		}
	}


	/**
	 * «десь заключена логика движени€ босса в режиме атаки ракетами
	 */
	private void UpdateRocket() {

		if ((time < TIME_OF_ROCKET_MODE)
				&& (this.getY() > Gdx.graphics.getHeight() - this.getHeight()
						* 0.9f)) {
			//это вход на сцену
			vx = 0;
			vy = -Gdx.graphics.getHeight() / 3;
		} else {
			if (time < TIME_OF_ROCKET_MODE) {
				//режим в разгаре
				
				//настраиваем скорость верта
				final float HELIC_SPEED = Gdx.graphics.getWidth() / 1;
				vy = 0;
				if (vx == 0)
					vx = HELIC_SPEED;
				if ((vx > 0)
						&& (this.getX() > Gdx.graphics.getWidth()
								- this.getWidth() / 2))
					vx = -HELIC_SPEED;
				if ((vx < 0) && (this.getX() < -this.getWidth() / 2))
					vx = HELIC_SPEED;

				if (timeForRocket > TIME_OF_NEXT_ROCKET) {
					//создаем 2 новые ракеты и добавл€ем к списку, если пора
					Sprite s = new Sprite(atlas.findRegion("rocket"));
					s.setSize(Gdx.graphics.getWidth()/24, Gdx.graphics.getWidth()/12);
					s.setCenter(this.getX() + this.getWidth() * 0.25f,
							this.getY() + this.getHeight() * 0.8f);
					s.setOrigin(s.getWidth() / 2, s.getHeight() / 2);
					s.rotate(Utils.GetAngle(this.getX() + this.getWidth()
							* 0.25f, this.getY() + this.getHeight() * 0.8f,
							finger.x, finger.y));
					rockets.add(s);

					s = new Sprite(atlas.findRegion("rocket"));
					s.setSize(Gdx.graphics.getWidth()/24, Gdx.graphics.getWidth()/12);
					s.setCenter(this.getX() + this.getWidth() * 0.75f,
							this.getY() + this.getHeight() * 0.8f);
					s.setOrigin(s.getWidth() / 2, s.getHeight() / 2);
					s.rotate(Utils.GetAngle(this.getX() + this.getWidth()
							* 0.75f, this.getY() + this.getHeight() * 0.8f,
							finger.x, finger.y));
					rockets.add(s);
					timeForRocket = 0;
				}
			} else {
				// врем€ ввалить назад дл€ нового типа
				if (this.getY() < Gdx.graphics.getHeight()) {
					vx = 0;
					vy = Gdx.graphics.getHeight() / 3;
				} else {
					// включаем 3 экран, очистим список ракет
					rockets.clear();
					gameMode = GAME_MODE_ATTACKED;
					vx = 0;
					vy = 0;
					this.setSize(Gdx.graphics.getWidth() * RELATIVE_WIDTH / 2,
							Gdx.graphics.getWidth() * HEIGHT_TO_WIDTH / 2);
					this.setPosition(
							Gdx.graphics.getWidth() / 2 - this.getWidth() / 2,
							Gdx.graphics.getHeight());
					selfRocket = null;
					time = 0;
				}
			}
		}

	}


	/**
	 * «десь заключена логика движени€ босса в режиме атакуемости
	 */
	private void UpdateAttacked(float delta) {
		if ((time < TIME_OF_MODE_ATTACKED)
				&& (this.getY() > Gdx.graphics.getHeight() - this.getHeight()
						* 0.9f)) {
			//идет вход на сцену
			vx = 0;
			vy = -Gdx.graphics.getHeight() / 3;
		} else {
			if (time < TIME_OF_MODE_ATTACKED) {

				if (selfRocket == null) {
					//создаем ракету
					selfRocket = new SelfDirection(finger, atlas,
							"self_rocket", 5, new float[] { 0.1f }, new int[] {
									0, 1000, 350 }, 0, 0);
					selfRocket.setCenterPosition(Gdx.graphics.getWidth() / 2,
							this.getY() - selfRocket.getHeight() * 0.5f);
					selfRocket.vy = -Gdx.graphics.getHeight()/RELATIVE_SPEED_OF_START_ROCKET;
					time = 0;
					vy = 0;
				}
				
				//скорость верта
				final float HELIC_SPEED = Gdx.graphics.getWidth() / 0.9f;
				if (vx == 0)
					vx = HELIC_SPEED;
				if ((vx > 0)
						&& (this.getX() > Gdx.graphics.getWidth()
								- this.getWidth() / 2))
					vx = -HELIC_SPEED;
				if ((vx < 0) && (this.getX() < -this.getWidth() / 2))
					vx = HELIC_SPEED;
				
				//если ракета упала, все, валим
				if (!selfRocket.IsCurrent())
				{
					time = TIME_OF_MODE_ATTACKED;
				}
				
				//если ракета попала в верт
				if ((Math.pow(selfRocket.getCenterX() - this.getCenterX(), 2)
						+ Math.pow(selfRocket.getCenterY() - this.getCenterY(),
								2) < Math.pow(
						this.getWidth() / 2 + selfRocket.getWidth() / 2, 2))
						&& (time > 0.5f)) {
					// режим BOOM 
					lives -= 1;
					isEnd = (lives == 0);
					isBoom = true;
					selfRocket = null;
					boom = new Sprite(atlas.findRegion("boom"));
					boom.setPosition(this.getCenterX(), this.getCenterY());
					boom.setSize(2, 2);
					time = 6;
				}
			} else {
				//пора отступать
				if (isBoom){
						float speed = isEnd ? 200 : 500;
						boom.setSize(boom.getWidth() + speed * delta, boom.getWidth()
								+ speed * delta);
						boom.setX(boom.getX() - speed * delta / 2);
						boom.setY(boom.getY() - speed * delta / 2);
					}
				// врем€ ввалить назад дл€ нового типа
				if (this.getY() < Gdx.graphics.getHeight()) {
					vx = 0;
					vy = Gdx.graphics.getHeight() / 3;
				} else if (!isEnd) {
					// включаем 1 экран
					boom = null;
					this.setSize(Gdx.graphics.getWidth() * RELATIVE_WIDTH,
							Gdx.graphics.getWidth() * RELATIVE_WIDTH * HEIGHT_TO_WIDTH);
					this.setCenterPosition(Gdx.graphics.getWidth() / 2,
							Gdx.graphics.getHeight() + this.getHeight() / 2);
					time = 0;
					gameMode = GAME_MODE_MOVE;
					currentMove = 0;
					isBoom = false;
				}
					
			}
		}

	}

}

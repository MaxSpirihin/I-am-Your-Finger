package com.yourfinger.game.Monsters;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.yourfinger.game.ExtraClasses.Finger;
import com.yourfinger.game.ImportantClasses.Monster;

public class Tank extends Monster {

	float time;
	float allTime;
	float timeForNext;
	TextureRegion txtrTank;
	Finger finger;
	TextureAtlas atlas;
	float[] size;
	String backgroundName;
	
	//вход
	static final float  TIME_ENTER = 0.3f;
	static final float  PART_ON_SCREEN = 0.7f;
	float speedEnter;
	
	//выход
	static final float TIME_EXIT = 0.7f; 
	float backSpeed;
	
	//основа
	List<LinearMonster> rockets;
	final float TIME_OF_BALL;
	final float MIN_TIME_OF_NEXT_BALL;
	final float MAX_TIME_OF_NEXT_BALL;
	float timeForBall;
	final float ABS_SPEED;
	float speed;


	public Tank(Finger finger, TextureAtlas atlas, String backgroundName,
			float time, float[] size, int mode[], float next) {
		// заполняем данные
		this.finger = finger;
		this.time = time;
		this.allTime = time;
		this.atlas = atlas;
		this.size = size;
		this.backgroundName = backgroundName;
		this.timeForNext = time - next;
		txtrTank = atlas.findRegion(backgroundName);

		this.setSize(Gdx.graphics.getWidth() * size[0], Gdx.graphics.getWidth()
				* size[1]);
		
		//инициализипуем константы по переданным параметрам
		ABS_SPEED = Gdx.graphics.getWidth()*1000/mode[0];
		TIME_OF_BALL = mode[1]*0.001f;
		MIN_TIME_OF_NEXT_BALL = mode[2]*0.001f;
		MAX_TIME_OF_NEXT_BALL = mode[3]*0.001f;
		
		
		//готовимся к възду
		setCenterPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight()
				+ this.getHeight() /2);
		speedEnter = this.getHeight() * PART_ON_SCREEN/TIME_ENTER;
		
		
		//это для выезда
		backSpeed = (Gdx.graphics.getHeight() + getHeight()*(1-PART_ON_SCREEN))/TIME_EXIT;
		
		
		//почав для атаки
		rockets = new LinkedList<LinearMonster>();
		timeForBall = 0;
		speed = ABS_SPEED;
		
	}



	@Override
	public void draw(Batch batch, float parentAlpha) {

		// с отрицательным временем будет вылет из-за анимации
		if (time > 0)
		{
			for (Monster m : rockets) {
				m.draw(batch, parentAlpha);
			}
			batch.draw(txtrTank, getX(), getY(), getWidth(), getHeight());
			
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

		for (Monster m : rockets) {
			if (m.FingerIsDead())
				return true;
		}
	
		return ((finger.x >= this.getX())
				&& (finger.x <= this.getX() + this.getWidth())
				&& (finger.y >= this.getY()) && (finger.y <= this.getY()
				+ this.getHeight()));

	}



	@Override
	public void act(float delta) {
		time -= delta;
		for (Monster m : rockets)
			m.act(delta);
		
		
		if (time > allTime - TIME_ENTER)
		{
			//идет вход
			setY(getY() - speedEnter*delta);
		}
		else if (time > TIME_EXIT)
		{
			//главная игра
			
			setX(getX() + delta * speed);
			if ((getCenterX() < 0) && (speed < 0))
				speed *= -1;
			if ((getCenterX() > Gdx.graphics.getWidth()) && (speed > 0))
				speed *= -1;
			
			
			timeForBall -= delta;
			if (timeForBall < 0) {
				LinearMonster m = new LinearMonster(finger, atlas, backgroundName+"_rocket",
						TIME_OF_BALL, new float[] { size[2], size[3] }, new int[] {0}, 0,
						0);
				m.setCenterPosition(getCenterX(), getY());
				rockets.add(m);
				timeForBall = new Random().nextFloat()
						* (MAX_TIME_OF_NEXT_BALL - MIN_TIME_OF_NEXT_BALL)
						+ MIN_TIME_OF_NEXT_BALL;
			}

		}
		else
		{
			//выход
			setY(getY() - backSpeed*delta);
		}
		
	}
}

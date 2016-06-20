package com.yourfinger.game.ClassesForMonsters;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class CombinePath implements Path {

	List<Path> paths;
	List<Float> startTimes;
	List<Float> endTimes;
	float time;



	public CombinePath(float radius, float allTime) {
		time = 0;

		startTimes = new LinkedList<Float>();
		endTimes = new LinkedList<Float>();
		paths = new LinkedList<Path>();
		
		startTimes.add(0f);
		endTimes.add(allTime * 0.05f);
		paths.add(new MiniNicePath(endTimes.get(0) - startTimes.get(0), radius,
				0));

		startTimes.add(allTime * 0.1f);
		endTimes.add(allTime * 0.25f);
		paths.add(new MiniNicePath(endTimes.get(1) - startTimes.get(1), radius,
				3));
		
		startTimes.add(allTime * 0.2501f);
		endTimes.add(allTime * 0.4f);
		paths.add(new MiniNicePath(endTimes.get(2) - startTimes.get(2), radius,
				3));
		
		startTimes.add(allTime * 0.45f);
		endTimes.add(allTime * 0.72f);
		paths.add(new MiniNicePath(endTimes.get(3) - startTimes.get(3), radius,
				5));
		
		startTimes.add(allTime * 0.77f);
		endTimes.add(allTime * 0.95f);
		paths.add(new MiniNicePath(endTimes.get(4) - startTimes.get(4), radius,
				4));
		
		startTimes.add(allTime * 0.99f);
		endTimes.add(allTime * 1.0f);
		paths.add(new MiniNicePath(endTimes.get(5) - startTimes.get(5), radius,
				0));

	}



	@Override
	public void SetCoordinates(float delta, Sprite object) {
		time+=delta;
		
		//ищем чье сейчас время выступления
		int numOfPath = paths.size()-1;
		while (time < startTimes.get(numOfPath))
			numOfPath--;
		
		if (numOfPath>0)
			numOfPath+=0;
		
		if ((time < endTimes.get(numOfPath))||(numOfPath == paths.size()-1))
		{
			paths.get(numOfPath).SetCoordinates(delta, object);
		}
		else
		{
			//мы движемся от конца одного к началу следующего по прямой
			paths.get(numOfPath).SetCoordinates(0, object);
			float startX = object.getX()+object.getWidth()/2;
			float startY = object.getY()+object.getHeight()/2;
			
			paths.get(numOfPath+1).SetCoordinates(0, object);
			float endX = object.getX()+object.getWidth()/2;
			float endY = object.getY()+object.getHeight()/2;
			
			float x = startX + (time - endTimes.get(numOfPath))*(endX-startX)/(startTimes.get(numOfPath+1) - endTimes.get(numOfPath));
			float y = startY + (time - endTimes.get(numOfPath))*(endY-startY)/(startTimes.get(numOfPath+1) - endTimes.get(numOfPath));
			
			
			object.setCenter(x,y );
			
		}
		
	}
}

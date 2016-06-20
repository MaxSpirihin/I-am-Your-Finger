package com.yourfinger.game.ClassesForMonsters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class MiniNicePath implements Path {
	
	float time;
	float allTime;
	int type;
	float radius;
	
	public MiniNicePath(float allTime, float radius, int type)
	{
		this.time = 0;
		this.allTime = allTime;	
		this.radius = radius;
		this.type = type;
		
	}

	@Override
	public void SetCoordinates(float delta, Sprite object) {
		time+=delta;
		int tempX = 0,tempY = 0; //в координатной сетке от 0 до 100
		
		switch (type)
		{
		case 0:
			tempX = 50;
			tempY = 50;
			break;
		case 1:
		{
			//x(t)=2cos(t)+cos(2t) max:3 min:-1.5
			//y(t)=2sin(t)-sin(2t) max:2.6 min:-2.6
			//t[0;7]
			float tempTime = time*7/allTime;
			float tempTempX = (float) (2*Math.cos(tempTime) + Math.cos(2*tempTime));
			float tempTempY = (float) (2*Math.sin(tempTime) - Math.sin(2*tempTime));
			tempX = (int) ((tempTempX + 1.5f)*100f/4.5f);
			tempY = (int) ((tempTempY + 3f)*100f/6f);
			
			break;
		}
		case 2:
		{
			//x(t)=tsin(t) max:14.17 min:-11.04
			//y(t)=tcos(t) max:12.6 min:-9.5
			//t[0;13]
			float tempTime = time*13/allTime;
			float tempTempX = (float) (tempTime*Math.sin(tempTime));
			float tempTempY = (float) (tempTime*Math.cos(tempTime));
			tempX = (int) ((tempTempX + 11.04f)*100f/25f);
			tempY = (int) ((tempTempY + 9.5f)*100f/22f);
			break;
		}
		case 3:
		{
			//r = 1
			//t[0;2*pi]
			float tempTime = (float) (time*2*Math.PI/allTime);
			float tempTempX = (float) (Math.sin(tempTime));
			float tempTempY = (float) (Math.cos(tempTime));
			tempX = (int) ((tempTempX + 1f)*100f/2f);
			tempY = (int) ((tempTempY + 1f)*100f/2f);
			break;
		}
		case 4:
		{
			//астрроида
			float tempTime = (float) (time*2*Math.PI/allTime);
			float tempTempX = (float) (Math.pow(Math.sin(tempTime),3));
			float tempTempY = (float) (Math.pow(Math.cos(tempTime),3));
			tempX = (int) ((tempTempX + 1f)*100f/2f);
			tempY = (int) ((tempTempY + 1f)*100f/2f);
			break;
		}
		case 5:
		{
			//лесенка
			int tempTime = (int) (time*1100/allTime);
			if ((tempTime/100)%2 == 0)
			{
				if ((tempTime/200)%2 == 0)
					tempX = tempTime%100;
				else
					tempX =100 - tempTime%100;
				tempY =100 - (tempTime/200)*20;
			}
			else
			{
				tempY = 100 - ((tempTime - 100)/200)*20;
				tempY -= (tempTime%100)/5;
				if (((tempTime/100-1)/2)%2 == 0)
					tempX =100;
				else
					tempX = 0;
			}
			break;
		}
		case 6:
		{
			//r = cos (4theta)
			//t[0;2*pi]
			float tempTime = (float) (time*2*Math.PI/allTime);
			float tempTempX = (float) (Math.cos(4*tempTime)*Math.sin(tempTime));
			float tempTempY = (float) (Math.cos(4*tempTime)*Math.cos(tempTime));
			tempX = (int) ((tempTempX + 1f)*100f/2f);
			tempY = (int) ((tempTempY + 1f)*100f/2f);
			break;
		}
		}
		
		object.setCenterX((Gdx.graphics.getWidth() - 2*radius)*tempX/100 + radius);
		object.setCenterY((Gdx.graphics.getHeight() - 2*radius)*tempY/100 + radius);
	}

}

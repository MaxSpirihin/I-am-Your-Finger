package com.yourfinger.game.ExtraClasses;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * ����� �������� ���. ����������� ������
 */
public class Utils {

	/**
	 * ���������� ����, �� ������� ���� ���������, ������������ ��� 0 ����
	 * ������ � ������������ ������ ( �1,y1 ) ������ ������� �������, ����� ��
	 * ������� �� ������ � ������������ ( �2,y2 )
	 * 
	 * @param x1
	 *            - �������������� ������
	 * @param y1
	 *            - �������������� ������
	 * @param x2
	 *            - ������������ ������
	 * @param y2
	 *            - ������������ ������
	 * @return ���� � ��������
	 */
	public static float GetAngle(float x1, float y1, float x2, float y2) {

		// � ������ ��������� �� �������
		float angle = 0;

		if ((x2 > x1) && (y2 > y1))
			angle = (float) (Math.atan((y2 - y1)
					/ (x2 - x1)) * 180 / Math.PI) + 90;

		if ((x2 < x1) && (y2 > y1))
			angle = (float) (Math.atan((x1 - x2)
					/ (y2 - y1)) * 180 / Math.PI) + 180;

		if ((x2 < x1) && (y2 < y1))
			angle = (float) (Math.atan((x2 - x1)
					/ (y1 - y2)) * 180 / Math.PI);

		if ((x2 > x1) && (y2 < y1))
			angle = (float) (Math.atan((x2 - x1)
					/ (y1 - y2)) * 180 / Math.PI);
		
		//���������� ������� ������
		if (y1==y2)
		{
			if (x1>x2)
				angle = 270;
			else
				angle = 90;
		}
		
		if ((x1==x2)&&(y1<y2))
			angle = 180;
		
		return angle;

	}


	/**
	 * ��������� ������ �� ����� � �������������
	 * @param x - ����-�� �����
	 * @param y - ����-�� �����
	 * @param centerX - ����� ��������������
	 * @param centerY - ����� ��������������
	 * @param width - ������ ��������������
	 * @param height - ������ ��������������
	 * @param alpha - ���� �������� ��-�� ������ ������� �������
	 * @return
	 */
	public static boolean PoiinsIsInRectangle(float x,float y,float centerX,float centerY,float width,float height, float alpha)
	{
		float pointX = (float) (x * Math.cos(alpha*Math.PI/180) + y
				* Math.sin(alpha*Math.PI/180));
		float pointY = (float) (-x * Math.sin(alpha*Math.PI/180) + y
				* Math.cos(alpha*Math.PI/180));

		float newCenterX = (float) (centerX
				* Math.cos(alpha*Math.PI/180) + centerY
				* Math.sin(alpha*Math.PI/180));
		float newCenterY = (float) (-centerX
				* Math.sin(alpha*Math.PI/180) + centerY
				* Math.cos(alpha*Math.PI/180));

		if  ((pointX > newCenterX - width/2)
				&& (pointX < newCenterX + width/2)
				&& (pointY > newCenterY - height/2)
				&& (pointY < newCenterY + height/2))
		{
			System.out.println(String.valueOf(pointX)+" "+String.valueOf(pointY)+" "+String.valueOf(newCenterX)+" "+String.valueOf(newCenterY));
			return true;
		}
		return false;
	}

	
	public static TextureRegion CutTexture(TextureRegion source, int part, boolean isLeft)
	{
		if (isLeft)
		{
			return source.split(source.getRegionWidth()*part/100, source.getRegionHeight()) [0][0];
		}
		else
		{
			//source.flip(true, false);
			TextureRegion result = source.split(source.getRegionWidth()*part/100, source.getRegionHeight()) [0][0];
			//source.flip(true, false);
			//result.flip(true, false);
			return result;
		}
	}
}

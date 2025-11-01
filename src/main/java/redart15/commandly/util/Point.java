package redart15.commandly.util;

import java.util.Objects;

public class Point {
	private float x,y,z;

	public Point(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Point(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public static int intDistance(Point p, Point q){
		return (int)Math.ceil(floatDistance(p,q));
	}

	public static float floatDistance(Point p, Point q){
		float dx = p.getFloatX() - q.getFloatX();
		float dy = p.getFloatY() - q.getFloatY();
		float dz = p.getFloatZ() - q.getFloatZ();
		return (float)(Math.sqrt(dx * dx + dy * dy + dz * dz));
	}

	@Override
	public int hashCode() {
		return Objects.hash(x,y,z);
	}

	@Override
	public boolean equals(Object obj){
		if(obj == null)				return false;
		if(obj instanceof Point) 	return false;
		Point that = (Point)obj;
		return Float.compare(this.x, that.getFloatX()) == 0
			&& Float.compare(this.y, that.getFloatY()) == 0
			&& Float.compare(this.z, that.getFloatZ()) == 0;
	}

	// a bunch of boilerplate
	public int getIntX() 	  {return 	(int)Math.ceil(x);}
	public int getIntY() 	  {return  	(int)Math.ceil(y);}
	public int getIntZ() 	  {return  	(int)Math.ceil(z);}
	public float getFloatX()  {return 					x;}
	public float getFloatY()  {return 					y;}
	public float getFloatZ()  {return  					z;}
	public void setX(float x) {				   this.x = x;}
	public void setY(float y) {				   this.y = y;}
	public void setZ(float z) {				   this.z = z;}
}

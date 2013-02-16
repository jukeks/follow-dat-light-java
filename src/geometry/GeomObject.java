package geometry;

import java.awt.Color;

public interface GeomObject {	
	public Point intersects(Ray ray);
	public Color colorAt(Point point);
}

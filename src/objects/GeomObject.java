package objects;

import java.awt.Color;

import de.yvert.geometry.Ray;
import de.yvert.geometry.Vector3;

public interface GeomObject {
	public static final double maxDistance = 10000;
	
	public Vector intersects(Ray ray);
	public Color colorAt(Vector point);
}

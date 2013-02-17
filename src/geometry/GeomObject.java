package geometry;

import followdatlight.Color;

public interface GeomObject {	
	public Double intersects(Ray ray);
	public Color colorAt(Point point);
	public Vector normal(Point point);
	public float reflectivity();
	public float transmittivity();
}

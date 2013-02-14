package objects;

import java.awt.Color;



public class Sphere implements GeomObject {
	private Color color;
	Point position;
	double radius;
	
	public Sphere(Point position, double radius) {
		this.position = position;
		this.radius = radius;
		color = new Color(255, 0, 0);
		
	}

	@Override
	public Vector intersects(Ray ray) {
		/*
		double distance = sphere.getBoundingSphere().intersects2(ray, maxDistance);
		if (distance == -1.0) {
			return null;
		}*/
		
		if (sphere.point.distance(ray.v) < sphere.radius) {
			System.out.println("HERE!");
		}
		return null;
		
		/*
		Vector3 origin = ray.p;
		Vector3 direction = ray.v;
		return origin.add(direction).normalize().scale(distance);
		*/
		
		
	}

	@Override
	public Color colorAt(Vector3 point) {
		return color;
	}
}

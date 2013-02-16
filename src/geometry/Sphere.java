package geometry;

import java.awt.Color;

public class Sphere implements GeomObject {
	private Color color;
	Point p;
	double r;

	public Sphere(Point position, double radius) {
		this.p = position;
		this.r = radius;
		color = new Color(255, 0, 0);
	}

	@Override
	public Point intersects(Ray ray) {
		// Compute A, B and C coefficients
		double a = ray.v.dot(ray.v);
		double b = 2 * ray.v.dot(ray.p);
		double c = ray.p.dot(ray.p) - (r * r);

		// Find discriminant
		double disc = b * b - 4 * a * c;

		// if discriminant is negative there are no real roots, so return
		// false as ray misses sphere
		if (disc < 0) {
			return null;
		}
		
		// compute q as described above
		double distSqrt = Math.sqrt(disc);
		double q;
		if (b < 0) {
			q = (-b - distSqrt) / 2.0;
		} else {
			q = (-b + distSqrt) / 2.0;
		}
		// compute t0 and t1
		double t0 = q / a;
		double t1 = c / q;

		// make sure t0 is smaller than t1
		if (t0 > t1) {
			// if t0 is bigger than t1 swap them around
			double temp = t0;
			t0 = t1;
			t1 = temp;
		}

		// if t1 is less than zero, the object is in the ray's negative
		// direction
		// and consequently the ray misses the sphere
		if (t1 < 0) {
			return null;
		}

		// if t0 is less than zero, the intersection point is at t1
		if (t0 < 0) {
			return ray.travel(t1);
		}

		// else the intersection point is at t0
		else {
			return ray.travel(t0);
		}
	}

	@Override
	public Color colorAt(Point point) {
		return color;
	}
}

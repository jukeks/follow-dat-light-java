package geometry;

import java.util.ArrayList;

import followdatlight.Color;

public class Cube implements GeomObject {
	Color color;
	float reflectivity = 0.05f;
	float transmittivity = 0.05f;
	Quad abcd, abgh, gfeh, fcde, ahed, bcfg;
	ArrayList<Quad> quads = new ArrayList<Quad>();
	
	
	public Cube(Point o, double k) {
		Point a = new Point(o.x, o.y, o.z);
		Point b = new Point(a.x + k, a.y, a.z);
		Point c = new Point(a.x + k, a.y + k, a.z);
		Point d = new Point(a.x, a.y + k, a.z);
		Point e = new Point(a.x, a.y + k, a.z - k);
		Point f = new Point(a.x + k , a.y + k, a.z - k);
		Point g = new Point(a.x + k, a.y, a.z - k);
		Point h = new Point(a.x, a.y, a.z - k);
		
		abcd = new Quad(a,b,c,d, new Color(220, 128, 220));
		abgh = new Quad(a,b,g,h);
		gfeh = new Quad(g,f,e,h);
		fcde = new Quad(f,c,d,e);
		ahed = new Quad(a, h, e, d);
		bcfg = new Quad(b, c, f, g);
		
		quads.add(abcd);
		quads.add(abgh);
		quads.add(gfeh);
		quads.add(fcde);
		quads.add(ahed);
		quads.add(bcfg);
		
		color = new Color(128, 128, 128);
	}

	@Override
	public Double intersects(Ray ray) {
		double minDistance = Double.MAX_VALUE;
		boolean intersected = false;
		for (Quad q : quads) {
			Double d = q.intersects(ray);
			if (d != null && d < minDistance) {
				minDistance = d;
				intersected = true;
			}
		}
		
		if (intersected) {
			return minDistance;
		} else {
			return null;
		}
	}

	@Override
	public Color colorAt(Point point) {
		for (Quad q : quads) {
			if (q.pointInSurface(point)) {
				return q.colorAt(point);
			}
		}
		
		System.out.println("ALERM");
		
		return null;
	}

	@Override
	public Vector normal(Point point) {
		for (Quad q : quads) {
			if (q.pointInSurface(point)) {
				return q.normal(point);
			}
		}
		
		System.out.println("ALERM");
		
		return null;
	}

	@Override
	public float reflectivity() {
		return reflectivity;
	}

	@Override
	public float transmittivity() {
		return transmittivity;
	}

	public static class Quad extends Plane {
		Point 
		p1,
		p2,
		p3,
		p4;
		Color color = new Color(128, 0,0);
		
		ArrayList<Point> points = new ArrayList<Point>();
		
		public Vector normal(Point p) {
			return super.normal(p);
		}
		
		private double minX() {
			double min = Double.MAX_VALUE;
			for (Point p : points) {
				if (p.x < min) {
					min = p.x;
				}
			}
			
			return min;
		}
		
		private double maxX() {
			double max = -Double.MAX_VALUE;
			for (Point p : points) {
				if (p.x > max) {
					max = p.x;
				}
			}
			
			return max;
		}
		
		private double minY() {
			double min = Double.MAX_VALUE;
			for (Point p : points) {
				if (p.y < min) {
					min = p.y;
				}
			}
			
			return min;
		}
		
		private double maxY() {
			double max = -Double.MAX_VALUE;
			for (Point p : points) {
				if (p.y > max) {
					max = p.y;
				}
			}
			
			return max;
		}
		
		private double minZ() {
			double min = Double.MAX_VALUE;
			for (Point p : points) {
				if (p.z < min) {
					min = p.z;
				}
			}
			
			return min;
		}
		
		private double maxZ() {
			double max = -Double.MAX_VALUE;
			for (Point p : points) {
				if (p.z > max) {
					max = p.z;
				}
			}
			
			return max;
		}
		
		public Quad(Point p1, Point p2, Point p3, Point p4) {
			super(p1, p2, p3);
			/*
			this.p1 = p1;
			this.p2 = p2;
			this.p3 = p3;
			this.p4 = p4;*/
			points.add(p1);
			points.add(p2);
			points.add(p3);
			points.add(p4);
		}
		
		public Quad(Point p1, Point p2, Point p3, Point p4, Color c) {
			this(p1, p2, p3, p4);
			color = c;
		}
		
		@Override
		public Color colorAt(Point p) {
			return color;
		}
		
		public Double intersects(Ray ray) {
			Double distance = super.intersects(ray);
			if (distance == null) {
				return null;
			}
			
			Point is = ray.travel(distance);
			if (pointInSurface(is)) {
				return distance;
			}
			
			return null;
		}
		
		public boolean pointInSurface(Point p) {
			// check if point is within limits
			if (p.x < minX() || p.x > maxX() ||
				p.y < minY() || p.y > maxY() ||
				p.z < minZ() || p.z > maxZ()) {
				return false;
			}
			
			return true;
		}
	}
}

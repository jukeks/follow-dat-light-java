package geometry;

import java.awt.Color;



public class Plane implements GeomObject {
	Color base = new Color(0, 0, 0);
	Color other = new Color(255, 255, 255);
	Vector n;
	double k;
	
	public Plane(Point p, Vector v) {
        n = v.normalized();
        k = n.dot(p);
	}

	@Override
	public Point intersects(Ray ray) {
/*
		def _intersect_line3_plane(L, P):
			d = P.n.dot(L.v)
		    if not d:
		        # Parallel
		        return None
		    u = (P.k - P.n.dot(L.p)) / d
		    if not L._u_in(u):
		        return None
		    return Point3(L.p.x + u * L.v.x,
		                  L.p.y + u * L.v.y,
		                  L.p.z + u * L.v.z)
*/
		double d = n.dot(ray.v);
		if (d == 0) {
			return null;
		}
		
		double u = (k - (n.dot(ray.p))) / d;
		
	    return new Point(ray.p.x + u * ray.v.x,
		                ray.p.y + u * ray.v.y,
		                ray.p.z + u * ray.v.z);	
	}

	@Override
	public Color colorAt(Point point) {
        if ((((int)(Math.abs(point.x) + 0.5)) + ((int)(Math.abs(point.y) + 0.5)) + ((int)(Math.abs(point.z) + 0.5))) % 2 != 0) {
        	return base;
        } else {
        	return other;
        }
	}

}

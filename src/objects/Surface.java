package objects;

import java.awt.Color;

import de.yvert.geometry.Plane;
import de.yvert.geometry.Ray;
import de.yvert.geometry.Vector3;

public class Surface implements GeomObject {
	private Plane surface;
	Color base = new Color(0, 0, 0);
	Color other = new Color(255, 255, 255);
	
	public Surface() {
		surface = new Plane(new Vector3(0, 0, 0), new Vector3(0, 0, 1));
	}

	@Override
	public Vector intersects(Ray ray) {
		Vector res = surface.intersect(ray, new Vector3());
		if (res == null) {
			return null;
		}
		
		System.out.println("JES");
		return res;
	}

	@Override
	public Color colorAt(Vector point) {
        if (((Math.abs(point.getX()) + 0.5) +Math.abs(point.getY()) + 0.5 + Math.abs(point.getZ()) + 0.5) % 2 != 0) {
        return base;
        } else {
        	return other;
        }
	}

}

package followdatlight;

import geometry.GeomObject;
import geometry.Point;
import geometry.Sphere;
import geometry.Plane;
import geometry.Vector;

import java.util.ArrayList;
import java.util.Random;


public class World {
	/*
    self.camera = Camera()

    self.lights = [#Point3(20, 20, 20),
                   Point3(-20, 20, 20),
                   #Point3(1, 20, 20),
    ]

    self.objects = [#Sphere(Point3( 3,  2,    2), 2.0, Point3(1.0, 0.0, 0.0)),
                    Sphere(Point3(-2,  4,  -10), 3.0),
                    #Sphere(Point3(-5,  1.2, 10), 1.2, Point3(0.0, 0.0, 1.0)),
                    #Surface(Point3(1, 0, 1), Point3(1, 0, 0), Point3(0, 0, 1)),
                    #Surface(Point3(-10, 0, 0), Point3(-10, 1, 0), Point3(-10, 0, 1)),
                    #Surface(Point3(10, 0, 0), Point3(10, 1, 0), Point3(10, 0, 1)),
    ]
    		
   */
	
	ArrayList<GeomObject> objects;
	ArrayList<Point> lights;
	
	public World() {
		objects = new ArrayList<GeomObject>();
		lights = new ArrayList<Point>();
		
		//lights.add(new Point(0, 20, 0));
		lights.add(new Point(-20, 20, 20));
		/*
		objects.add(new Sphere(new Point(9, 5, -8), 2.2, new Color(0, 0, 220)));
		objects.add(new Sphere(new Point(0, 2, 2), 2));
		objects.add(new Sphere(new Point(-5, 2, 5), 3, new Color(0, 220, 0)));
		*/
		
		for (int i = 0; i < 10; ++i) {
			objects.add(randomSphere());
		}
		
		objects.add(new Plane(new Point(0, 0, 0), new Vector(0, 1, 0)));
		//objects.add(new Plane(new Point(0, -5, 0), new Vector(0, 1, 0)));
	}
	
	private Point randomPoint() {
		return new Point(Math.random() * 25 - 12,
				Math.random() * 12,
				Math.random() * -25);
	}
	
	private Sphere randomSphere() {
		return new Sphere(randomPoint(), Math.random() * 3, randomColor(20, 220));
	}
	
	private Color randomColor(int l, int h) {
		return new Color((int)(Math.random() * (h - l) + l),
				(int)(Math.random() * (h - l) + l),
				(int)(Math.random() * (h - l) + l));		
	}
}

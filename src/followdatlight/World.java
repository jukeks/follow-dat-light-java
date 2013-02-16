package followdatlight;

import geometry.GeomObject;
import geometry.Point;
import geometry.Sphere;
import geometry.Plane;
import geometry.Vector;

import java.util.ArrayList;


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
		
		lights.add(new Point(0, 20, 20));
		//objects.add(new Sphere(new Vector3(-10, -10, 0), 2));
		objects.add(new Sphere(new Point(0, 0, 0), 10));
		//objects.add(new Sphere(new Point(-1, 0, 0), 3));
		
		objects.add(new Plane(new Point(0, 0, 0), new Vector(0, 1, 0)));
	}
}

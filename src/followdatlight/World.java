package followdatlight;

import java.util.ArrayList;

import de.yvert.geometry.Vector3;

import objects.GeomObject;
import objects.Sphere;
import objects.Surface;

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
	ArrayList<Vector3> lights;
	
	public World() {
		objects = new ArrayList<GeomObject>();
		lights = new ArrayList<Vector3>();
		
		lights.add(new Vector3(20, 20, 20));
		//objects.add(new Sphere(new Vector3(-10, -10, 0), 2));
		objects.add(new Sphere(new Vector3(1, 1, 0), 2));
		objects.add(new Sphere(new Vector3(-1, 0, 0), 2));
		
		//objects.add(new Surface());
	}
}

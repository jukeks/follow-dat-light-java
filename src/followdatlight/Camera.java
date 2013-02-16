package followdatlight;

import geometry.Point;
import geometry.Vector;


public class Camera {
	/*
    self.position = Point3(0, 5, 20)
    self.look_at = Point3(0, 0, 0)
    self.up_vector = Vector3(0, 1, 0)   # Camera's up vector
    self.fov = 50                       # View angle
	*/
    Vector upVector;
    double fov = 20;
    Point position, lookAt;
	
	public Camera() {
		this.position = new Point(0, 5, 20);
		this.lookAt = new Point(0, 0, 0);
		this.upVector = new Vector(0, 1, 0);
	}
	
}

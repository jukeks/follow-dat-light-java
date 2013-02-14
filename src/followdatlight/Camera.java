package followdatlight;

import de.yvert.geometry.Vector3;

public class Camera {
	/*
    self.position = Point3(0, 5, 20)
    self.look_at = Point3(0, 0, 0)
    self.up_vector = Vector3(0, 1, 0)   # Camera's up vector
    self.fov = 50                       # View angle
	*/
    Vector3 position, lookAt, upVector;
    double fov = 50;
	
	public Camera() {
		this.position = new Vector3(0, 20, 20);
		this.lookAt = new Vector3(0, 0, 0);
		this.upVector = new Vector3(0, 1, 0);
	}
	
}

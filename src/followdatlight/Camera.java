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
    double focalLength = 500;
    Point position, lookAt;
	
	public Camera() {
		this.position = new Point(0, 5, 20);
		this.lookAt = new Point(0, 0, 0);
		this.upVector = new Vector(1, 1, 1);
	}
	
	public FieldOfView fieldOfView(int width, int height) {
		double fovWidth = 2 * Math.atan2(width, 2 * focalLength);
		double fovHeight = 2 * Math.atan2(height, 2 * focalLength);
		
		return new FieldOfView(fovWidth, fovHeight);
	}
	
	public static class FieldOfView {
		final double width, height;
		
		public FieldOfView(double fovW, double fovH) {
			this.height = fovH;
			this.width = fovW;
		}
	}
	
}

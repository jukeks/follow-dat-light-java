package followdatlight;

import geometry.GeomObject;
import geometry.Point;
import geometry.Ray;
import geometry.Vector;

import java.awt.Color;

public class Tracer {
	World world;
	Camera camera;
	Canvas canvas;
	int timesHit = 0;
	
	public Tracer(World world, Camera camera, Canvas canvas) {
		this.world = world;
		this.camera = camera;
		this.canvas = canvas;
	}
	
	public void castRays() {
		/*
        camera_position = self.camera.position
        eye_ray = Ray3(self.camera.position, self.camera.look_at)
        right_vector = eye_ray.v.cross(self.camera.up_vector).normalize()
        up_vector = right_vector.cross(eye_ray.v).normalize() *-1 # TODO: FIND OUT WHY

        width = self.width
        height = self.height

        
        # TODO: calculate this more intelligently
        pixel_width = 0.02
		
        x_comp = right_vector.normalize() * ((x - width/2) * pixel_width)
        y_comp = up_vector.normalize() * ((y - height/2) * pixel_width)

        cur_vec = eye_ray.v + x_comp + y_comp
        cur_ray = Ray3(camera_position, cur_vec)
        self.canvas.save_color(x, y, self.trace(cur_ray))*/
		
		Vector eyeVec = camera.lookAt.add(camera.position).scale(-1);
		Vector rightVec = eyeVec.cross(camera.upVector).normalized();
		Vector upVec = rightVec.cross(eyeVec).normalized();
		double pixelWidth = 0.02;
		
		for (int x = 0; x < canvas.width; ++x) {
			for (int y = 0; y < canvas.height; ++y) {
				Vector xComp = rightVec.scale((x - canvas.width/2) * pixelWidth);
				Vector yComp = upVec.scale((y - canvas.width/2) * pixelWidth);
				
				Vector currentVec = eyeVec.add(xComp).add(yComp);
				Ray currentRay = new Ray(camera.position, currentVec);
				
				canvas.setPixel(x, y, trace(currentRay));
			}
		}

	}
	
	public Intersection intersect(Ray ray) {
		double minDistance = -1;
		GeomObject closestHitObj = null;
		Point closestHitPoint = null;
		
		for (GeomObject obj : world.objects) {
			Point intersectionPoint = obj.intersects(ray);
			if (intersectionPoint == null) {
				continue;
			}
			
			double distance = ray.p.distance(intersectionPoint);
			
			if (distance < minDistance || minDistance == -1) {
				minDistance = distance;
				closestHitObj = obj;
				closestHitPoint = intersectionPoint;
			}
		}
		
		if (closestHitObj != null) {
			return new Intersection(closestHitObj, closestHitPoint);
		}
		
		return null;
	}
	
	public Color trace(Ray ray) {
		Color black = new Color(0, 0, 0);
		Intersection is = intersect(ray);
		if (is == null) {
			return black;
		}
		
		GeomObject hitObject = is.hitObject;
		Point hitPoint = is.hitPoint;
		
		return hitObject.colorAt(hitPoint);
		/*
		for (Point light : world.lights) {
			Ray lightRay = new Ray(hitPoint, light);
			
			is = intersect(lightRay);
			if (is == null) {
				return hitObject.colorAt(hitPoint);
			}
		}
		
		return black;*/
	}
	
	private static class Intersection {
		GeomObject hitObject;
		Point hitPoint;
		
		public Intersection(GeomObject obj, Point p) {
			hitObject = obj;
			hitPoint = p;
		}
	}
}

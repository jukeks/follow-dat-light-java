package followdatlight;

import java.awt.Color;

import objects.GeomObject;

import de.yvert.geometry.Ray;
import de.yvert.geometry.Vector3;

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
		
		Vector3 eyeVec = camera.position.add(camera.lookAt);
		Vector3 rightVec = eyeVec.cross(camera.upVector).normalize();
		Vector3 upVec = rightVec.cross(eyeVec).normalize();
		double pixelWidth = 0.02;
		
		Ray currentRay = new Ray();
		currentRay.p = camera.position;
		
		for (int x = 0; x < canvas.width; ++x) {
			for (int y = 0; y < canvas.height; ++y) {
				Vector3 xComp = rightVec.scale((x - canvas.width/2) * pixelWidth);
				Vector3 yComp = upVec.scale((y - canvas.width/2) * pixelWidth);
				
				Vector3 currentVec = eyeVec.add(xComp).add(yComp);
				
				//System.out.println(currentVec + " " + currentVec.distance(new Vector3(0, 0, 0)));
				

				currentRay.v = currentVec;
				currentRay.update();
				
				canvas.setPixel(x, y, trace(currentRay));
			}
		}

	}
	
	public Intersection intersect(Ray ray) {
		double minDistance = -1;
		GeomObject closestHitObj = null;
		Vector3 closestHitPoint = null;
		
		for (GeomObject obj : world.objects) {
			Vector3 intersectionPoint = obj.intersects(ray);
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
		/*
		Ray lightRay = new Ray();
		lightRay.p = is.hitPoint;
		for (Vector3 light : world.lights) {
			lightRay.v = light.add(lightRay.p.scale(-1));
			lightRay.update();
			
			is = intersect(ray);
			if (is == null) {
				return hitObject.colorAt(lightRay.p);
			}
			
			return new Color(0, 128, 0);
		}
		
		return black;*/
		return hitObject.colorAt(is.hitPoint);
	}
	
	private static class Intersection {
		GeomObject hitObject;
		Vector3 hitPoint;
		
		public Intersection(GeomObject obj, Vector3 p) {
			hitObject = obj;
			hitPoint = p;
		}
	}
}

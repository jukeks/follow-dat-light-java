package followdatlight;

import geometry.GeomObject;
import geometry.Point;
import geometry.Ray;
import geometry.Vector;

import followdatlight.Color;

public class Tracer {
	World world;
	Camera camera;
	Canvas canvas;
	int recursionLimit = 10;
	boolean multisampling = true;
	int xSampleCount = 4;
	int ySampleCount = 4;
	
	public Tracer(World world, Camera camera, Canvas canvas) {
		this.world = world;
		this.camera = camera;
		this.canvas = canvas;
	}
	
	public Tracer(World world, Camera camera, Canvas canvas, int recursionLimit) {
		this.world = world;
		this.camera = camera;
		this.canvas = canvas;
		this.recursionLimit = recursionLimit;
	}
	
	public void castRays() {
		final int processors = Runtime.getRuntime().availableProcessors();
		Thread[] threads = new Thread[processors];
		
		for(int i=0; i < processors; i++) {
			final int id = i;
			threads[i] = new Thread() {
				@Override()
				public void run() {
					castRays(id, processors);
				}
			};
			
			threads[i].start();
		}
		
		for (Thread thread : threads) {
			while (true) {
				try {
					thread.join();
					break;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private void castRays(int id, int workerCount) {
		/*
		fovRadians = math.pi * (self.fieldOfView / 2.0) / 180.0
        halfWidth = math.tan(fovRadians)
        halfHeight = 0.75 * halfWidth
        width = halfWidth * 2
        height = halfHeight * 2
        pixelWidth = width / (canvas.width - 1)
        pixelHeight = height / (canvas.height - 1) 
		 
		 */
		
		double fovRadians = Math.PI * camera.fov / 2.0 / 180.0;
		double halfWidth = Math.tan(fovRadians);
		double halfHeight = 0.75 * halfWidth;
		double width = halfWidth * 2;
		double height = halfHeight * 2;
		
		
		double pixelWidth = width / (canvas.width - 1);
		double pixelHeight = height / (canvas.height - 1);
		
		pixelWidth = 0.02 * 1024.0 / (double)canvas.width;
		pixelHeight = pixelWidth;
		
		Vector eyeVec = camera.lookAt.add(camera.position).scale(-1);
		Vector rightVec = camera.upVector.cross(eyeVec).normalized();
		Vector upVec = rightVec.cross(eyeVec).normalized();
		
		Color[] samples = new Color[xSampleCount * ySampleCount];
		double sampleWidth = pixelWidth / xSampleCount;
		double sampleHeight = pixelWidth / ySampleCount;
		
		for (int x = id; x < canvas.width; x += workerCount) {
			for (int y = 0; y < canvas.height; ++y) {
				if (multisampling) {
					int sample = 0;

					for (int sampleX = 0; sampleX < this.xSampleCount; ++sampleX) {
						for (int sampleY = 0; sampleY < this.ySampleCount; ++sampleY) {
							Vector xComp = rightVec.scale((x - canvas.width/2) * pixelWidth + sampleX * sampleWidth - pixelWidth/2);
							Vector yComp = upVec.scale((y - canvas.width/2) * pixelHeight + sampleY * sampleHeight - pixelHeight/2);
							
							Vector currentVec = eyeVec.add(xComp).add(yComp);
							Ray currentRay = new Ray(camera.position, currentVec);
							samples[sample++] = trace(currentRay);
						}
					}
					
					canvas.setPixel(x, y, average(samples));
					
				} else {
					Vector xComp = rightVec.scale((x - canvas.width/2) * pixelWidth);
					Vector yComp = upVec.scale((y - canvas.width/2) * pixelHeight);
					
					Vector currentVec = eyeVec.add(xComp).add(yComp);
					Ray currentRay = new Ray(camera.position, currentVec);
					
					canvas.setPixel(x, y, trace(currentRay));
				}
			}
		}

	}
	
	private Color average(Color[] samples) {
		double r = 0, g = 0, b = 0;
		int len = samples.length;
		
		for (int i = 0; i < len; ++i) {
			r += (double)samples[i].getRed()/len;
			g += (double)samples[i].getGreen()/len;
			b += (double)samples[i].getBlue()/len;
		}
		
		return new Color((int)r, (int)g, (int)b);
	}
	
	
	public Intersection intersect(Ray ray) {
		double minDistance = Double.MAX_VALUE;
		double distanceLimit = 0.000000000001;
		
		GeomObject closestHitObj = null;
		Point closestHitPoint = null;
		
		for (GeomObject obj : world.objects) {
			Point intersectionPoint = obj.intersects(ray);
			if (intersectionPoint == null) {
				continue;
			}
			
			double distance = ray.p.distance(intersectionPoint);
			
			if (distance < minDistance && distance > distanceLimit) {
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
		return trace(ray, 0);
	}
	
	public Color trace(Ray ray, int recursionLevel) {
		Color black = new Color(0, 0, 0);
		Color color = black;
		Intersection is = intersect(ray);
		if (is == null) {
			return color;
		}
		
		GeomObject hitObject = is.hitObject;
		Point hitPoint = is.hitPoint;
		
		for (Point light : world.lights) {
			Ray lightRay = new Ray(hitPoint, light);
			
			is = intersect(lightRay);
			if (is == null) {
				color = hitObject.colorAt(hitPoint);
				break;
			} else {
                color = is.hitObject.colorAt(is.hitPoint).scale(is.hitObject.transmittivity());
			}
		}
		
		/*
        # reflection
        if self.max_recursion_depth > current_recursion_depth:
            normal = hit_object.normal(is_point)
            reflected_ray = Ray3(is_point, normal)
            reflected_color = self.trace(reflected_ray, current_recursion_depth + 1)

            color = self.add_colors(self.calculate_color(reflected_color,
                hit_object.reflectivity),
                color)*/
		
		
		if (recursionLevel < recursionLimit) {
			Vector normal = hitObject.normal(hitPoint);
			Ray reflection = new Ray(hitPoint, normal);
			Color reflectedColor = trace(reflection, recursionLevel + 1);
			color = color.add(reflectedColor.scale(hitObject.reflectivity()));
		}
		
		return color;
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

package followdatlight;

import java.util.ArrayList;

import geometry.GeomObject;
import geometry.Point;
import geometry.Ray;
import geometry.Vector;

import followdatlight.Color;

public class Tracer {
	World world;
	Camera camera;
	Canvas canvas;
	int recursionLimit = 3;
	boolean multisampling = true;
	int xSampleCount = 4;
	int ySampleCount = 64;
	
	private static final double EPSILON = 0.0001;
	
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
		
		System.out.println("Using " + processors + " threads.");
		
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
		
		GeomObject closestHitObj = null;
		Point closestHitPoint = null;
		
		for (GeomObject obj : world.objects) {
			Double distance = obj.intersects(ray);
			if (distance == null) {
				continue;
			}
			
			if (distance < minDistance && distance > EPSILON) {
				minDistance = distance;
				closestHitObj = obj;
				closestHitPoint = ray.travel(distance);
			}
		}
		
		if (closestHitObj != null) {
			return new Intersection(closestHitObj, closestHitPoint);
		}
		
		return null;
	}
	
	private boolean blocked(Ray ray) {
		for (GeomObject obj : world.objects) {
			Double time = obj.intersects(ray);
			
			if (time != null && time > EPSILON) {
				return true;
			}
		}
		
		return false;
	}
	
	private Point[] visibleLights(Point p) {
		ArrayList<Point> visibleLights = new ArrayList<Point>();
		for (Point light : world.lights) {
			if (lightIsVisible(p, light)) {
				visibleLights.add(light);
			}
		}
		
		return visibleLights.toArray(new Point[0]);
	}
	
	private boolean lightIsVisible(Point p, Point light) {
		Ray r = new Ray(p, light);

		if (blocked(r)) {
			return true;
		}
		
		return false;
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
		
		GeomObject hitObject = is.object;
		Point hitPoint = is.point;
		/*
		if (true)
			return is.hitObject.colorAt(hitPoint);
		*/
		for (Point light : world.lights) {
			Ray lightRay = new Ray(hitPoint, light);
			Intersection blocked = intersect(lightRay);
			if (blocked == null) {
				color = hitObject.colorAt(hitPoint);
				break;
			} else {
                color = hitObject.colorAt(hitPoint).scale(blocked.object.transmittivity());
			}
		}
		
		if (recursionLevel < recursionLimit) {
			Vector normal = hitObject.normal(hitPoint);
			Ray reflection = new Ray(hitPoint, ray.v.reflect(normal));
			Color reflectedColor = trace(reflection, recursionLevel + 1);
			color = color.add(reflectedColor.scale(hitObject.reflectivity()));
		}
		
		return color;
	}
	
	private static class Intersection {
		GeomObject object;
		Point point;
		
		public Intersection(GeomObject obj, Point p) {
			object = obj;
			point = p;
		}
	}
}

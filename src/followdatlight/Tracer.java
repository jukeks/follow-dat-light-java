package followdatlight;

import java.util.ArrayList;
import java.util.Random;

import geometry.GeomObject;
import geometry.Point;
import geometry.Ray;
import geometry.Vector;

import followdatlight.Camera.FieldOfView;
import followdatlight.Color;

public class Tracer {
	World world;
	Camera camera;
	Canvas canvas;
	int recursionLimit;
	boolean multisampling = true;
	int xSampleCount = 4;
	int ySampleCount = 4;
	
	boolean depthOfField = false;
	
	private static final double EPSILON = 0.0001;
	
	private Random random;
	
	public Tracer(World world, Camera camera, Canvas canvas) {
		this(world, camera, canvas, 3);
	}
	
	public Tracer(World world, Camera camera, Canvas canvas, int recursionLimit) {
		this.world = world;
		this.camera = camera;
		this.canvas = canvas;
		this.recursionLimit = recursionLimit;
		
		random = new Random();
	}
	
	public void castRays() {
		final int processors = 4; //Runtime.getRuntime().availableProcessors();
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
	
	private double random() {
		return (random.nextDouble() - 0.5) * 0.05;
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
		
        double fovRadians = Math.PI * (45 / 2.0) / 180.0;
        double halfWidth = Math.tan(fovRadians);
        double halfHeight = (double)canvas.height/canvas.width * halfWidth;
		double width = halfWidth * 2;
		double height = halfHeight * 2;
		
		double pixelWidth = width / (canvas.width - 1);
		double pixelHeight = height / (canvas.height - 1);
		
		System.out.println("" + pixelWidth + "x" + pixelHeight);
		
		//pixelWidth = 0.02 * 1024.0 / (double)canvas.width;
		//pixelHeight = pixelWidth;
		
		pixelWidth = 0.5;
		pixelHeight = 0.5;
		
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
					
				} else if (depthOfField) {
					
				/*
					  pixelCenterCordinate = L + (pixelWidth) * (j) * u + (pixelHeight) * (i) * v; 
					  // L is leftmost corner of image plane that we derived in image plane setup
					  rayDirection = pixelCenterCordinate - rayStart;
					  SbVec3f pointAimed = camera.getCameraPosition() + 15 * rayDirection;
					  //pointAimed is the position of pixel on focal plane in specified ray direction and 15 is my focal length (you can change accordingly)
					  rayDirection.normalize();
					  float r = 1;
					  for (int di =0; di < 25; di++){ // shooting 25 random rays
					    float du = rand()/float(RAND_MAX+1);//generating random number
					    float dv = rand()/float(RAND_MAX+1);

					    // creating new camera position(or ray start using jittering)
					    SbVec3f start=camera.getCameraPosition()-(r/2)*u-(r/2)*v+r*(du)*u+r*(dv)*v; 
					    
					    //getting the new direction of ray
					    SbVec3f direction = pointAimed - start;*/
					
					
					
					Vector xComp = rightVec.scale((x - canvas.width/2) * pixelWidth);
					Vector yComp = upVec.scale((y - canvas.width/2) * pixelHeight);
					
					Color[] dofsamples = new Color[16];
					for (int i = 0; i < dofsamples.length; ++i) {
						Vector dx = rightVec.scale(random());
						Vector dy = upVec.scale(random());
						Point origin = camera.position.add(dx).add(dy);
						Vector pointAimed = camera.lookAt.add(origin).scale(-1);
						Vector currentVec = pointAimed.add(xComp).add(yComp);
						Ray currentRay = new Ray(camera.position, currentVec);
						dofsamples[i] = trace(currentRay);
					}
					
					canvas.setPixel(x, y, average(dofsamples));
					
				} else {
					//System.out.println(x * pixelWidth - canvas.width/2);
					Vector xComp = rightVec.scale(x * pixelWidth - canvas.width/2);
					Vector yComp = upVec.scale(y * pixelHeight - canvas.height/2);
					
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

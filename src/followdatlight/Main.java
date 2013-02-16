package followdatlight;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
    	long start = System.currentTimeMillis();
    	Canvas c = new Canvas(1920, 1080);
    	long canvas = System.currentTimeMillis();
    	printTime("Canvas ", start, canvas);
    	
    	start = System.currentTimeMillis();
    	Tracer t = new Tracer(new World(), new Camera(), c, 10);
    	long tracer = System.currentTimeMillis();
    	printTime("Tracer ", start, tracer);
    	
    	start = System.currentTimeMillis();
    	t.castRays();
    	long tracing = System.currentTimeMillis();
    	printTime("Tracing", start, tracing);
    	
    	start = System.currentTimeMillis();
    	c.write();
    	long writing = System.currentTimeMillis();
    	printTime("Writing", start, writing);
    }
    
    public static void printTime(String what, long t1, long t2) {
    	System.out.println(what + " " + ((t2 - t1)/1000.0) + " seconds");
    }
}

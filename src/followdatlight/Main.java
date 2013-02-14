package followdatlight;

import java.awt.Color;
import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
    	Canvas c = new Canvas(1024, 768);
    	Tracer t = new Tracer(new World(), new Camera(), c);
    	t.castRays();
    	/*
    	for (int i = 0; i < 1024; ++i) {
    		for (int j = 0; j < 768; ++j) {
    			c.setPixel(i, j, new Color(i % 255, j % 255, (i + j) % 255));
    		}
    	}*/
    	
    	c.write();
    }
}

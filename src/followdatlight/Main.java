package followdatlight;

import java.awt.Color;
import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
    	Canvas c = new Canvas(1024*2, 1024*2);
    	Tracer t = new Tracer(new World(), new Camera(), c);
    	t.castRays();
    	c.write();
    }
}

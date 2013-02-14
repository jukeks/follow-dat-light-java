package followdatlight;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import ar.com.hjg.pngj.ImageInfo;
import ar.com.hjg.pngj.ImageLine;
import ar.com.hjg.pngj.ImageLineHelper;
import ar.com.hjg.pngj.PngWriter;

public class Canvas {
	int width, height;
	String filename = "trace.png";
	ImageInfo img;
	ImageLine[] lines;
	PngWriter png;
	
	public Canvas(int width, int height) {
		this.width = width;
		this.height = height;
		
		img =  new ImageInfo(width, height, 8, false);
		lines = new ImageLine[height];
		for (int i = 0; i < height; ++i) {
			lines[i] = new ImageLine(img);
		}
	}
	
	public void setPixel(int x, int y, Color c) {
		ImageLineHelper.setPixelRGB8(lines[y], x, c.getRGB());
	}
		
       
    public void write() throws FileNotFoundException {
        png = new PngWriter(new FileOutputStream(this.filename), img); 
        for (int line = 0; line < png.imgInfo.rows; ++line) {
            png.writeRow(lines[line], line);
        }
        
        png.end();
	}
	
	
}

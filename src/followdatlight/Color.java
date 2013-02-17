package followdatlight;

import java.awt.color.ColorSpace;

public class Color extends java.awt.Color {

	public Color(int rgb) {
		super(rgb);
		// TODO Auto-generated constructor stub
	}

	public Color(int rgba, boolean hasalpha) {
		super(rgba, hasalpha);
		// TODO Auto-generated constructor stub
	}

	public Color(int r, int g, int b) {
		super(r, g, b);
		// TODO Auto-generated constructor stub
	}

	public Color(float r, float g, float b) {
		super(r, g, b);
		// TODO Auto-generated constructor stub
	}

	public Color(ColorSpace cspace, float[] components, float alpha) {
		super(cspace, components, alpha);
		// TODO Auto-generated constructor stub
	}

	public Color(int r, int g, int b, int a) {
		super(r, g, b, a);
		// TODO Auto-generated constructor stub
	}

	public Color(float r, float g, float b, float a) {
		super(r, g, b, a);
		// TODO Auto-generated constructor stub
	}

	public Color scale(float f) {
		HSL hsl = RGB2HSL(this);

		return HSL2RGB(hsl.h, hsl.s, hsl.l * f);
	}

	public Color add(Color c) {
		int r = (this.getRed() + c.getRed());
		int g = (this.getGreen() + c.getGreen());
		int b = (this.getBlue() + c.getBlue());
		r = r > 255 ? 255 : r;
		g = g > 255 ? 255 : g;
		b = b > 255 ? 255 : b;

		return new Color(r, g, b);
	}

	public static Color HSL2RGB(double h, double s, double l) {
		double v;
		double r, g, b;
		r = l; // default to gray
		g = l;
		b = l;
		v = (l <= 0.5) ? (l * (1.0 + s)) : (l + s - l * s);
		if (v > 0) {
			double m;
			double sv;
			int sextant;
			double fract, vsf, mid1, mid2;

			m = l + l - v;
			sv = (v - m) / v;
			h *= 6.0;
			sextant = (int) h;
			fract = h - sextant;
			vsf = v * sv * fract;
			mid1 = m + vsf;
			mid2 = v - vsf;
			switch (sextant) {
			case 0:
				r = v;
				g = mid1;
				b = m;
				break;
			case 1:
				r = mid2;
				g = v;
				b = m;
				break;
			case 2:
				r = m;
				g = v;
				b = mid1;
				break;
			case 3:
				r = m;
				g = mid2;
				b = v;
				break;
			case 4:
				r = mid1;
				g = m;
				b = v;
				break;
			case 5:
				r = v;
				g = m;
				b = mid2;
				break;
			}
		}

		return new Color((int) (r * 255), (int) (g * 255), (int) (b * 255));
	}

	public static HSL RGB2HSL(Color rgb) {
		double r = rgb.getRed() / 255.0;
		double g = rgb.getGreen() / 255.0;
		double b = rgb.getBlue() / 255.0;
		double v;
		double m;
		double vm;
		double r2, g2, b2;

		double h = 0; // default to black
		double s = 0;
		double l = 0;
		v = Math.max(r, g);
		v = Math.max(v, b);
		m = Math.min(r, g);
		m = Math.min(m, b);

		l = (m + v) / 2.0;
		if (l <= 0.0) {
			return new HSL(h, s, l);
		}
		
		vm = v - m;
		s = vm;
		
		if (s > 0.0) {
			s /= (l <= 0.5) ? (v + m) : (2.0 - v - m);
		} else {
			return new HSL(h, s, l);
		}
		r2 = (v - r) / vm;
		g2 = (v - g) / vm;
		b2 = (v - b) / vm;
		
		if (r == v) {
			h = (g == m ? 5.0 + b2 : 1.0 - g2);
		} else if (g == v) {
			h = (b == m ? 1.0 + r2 : 3.0 - b2);
		} else {
			h = (r == m ? 3.0 + g2 : 5.0 - r2);
		}
		
		h /= 6.0;

		return new HSL(h, s, l);
	}

	public static class HSL {
		double h, s, l;

		public HSL(double h, double s, double l) {
			this.h = h;
			this.s = s;
			this.l = l;
		}
	}
}

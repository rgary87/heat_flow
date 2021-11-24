package fr.rgary.heatflow.display;

/**
 * Class Color.
 */
public class Color {
    public double R = 0;
    public double G = 0;
    public double B = 0;
    public double A = 0;

    public Color(final double r, final double g, final double b) {
        R = r;
        G = g;
        B = b;
    }

    public Color(final double r, final double g, final double b, final double a) {
        R = r;
        G = g;
        B = b;
        A = a;
    }

    public Color(final int r, final int g, final int b) {
        R = r / 255d;
        G = g / 255d;
        B = b / 255d;
    }

    @Override
    public String toString() {
        return "Color{" +
               "R=" + R +
               ", G=" + G +
               ", B=" + B +
               '}';
    }
}

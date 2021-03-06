public class Color {
    private double r;
    private double g;
    private double b;
    private double z;

    public Color(double rx, double gy, double bz, double zcoord) {
        r = rx;
        g = gy;
        b = bz;
        z = zcoord;
    }

    public Color(double rx, double gy, double bz) {
        r = rx;
        g = gy;
        b = bz;
        z = -1 * Double.MAX_VALUE;
    }

    public double getColor(int col) { //red is 0, green is 1, blue is 2
        if (col == 0) {
            return r;
        } else if (col == 1) {
            return g;
        } else if (col == 2) {
            return b;
        } else {
            return -1; //if you inputted something wrong
        }
    }

    public double getZ() {
        return z;
    }

    public void setZ(double zcoord) {
        z = zcoord;
    }

    public boolean setColor(int col, double val) { //red is 0, green is 1, blue is 2
        if (val == -1) {
            return false; //the getColor did something wrong
        }
        if (col == 0) {
            r = val;
            return true;
        } else if (col == 1) {
            g = val;
            return true;
        } else if (col == 2) {
            b = val;
            return true;
        } else {
            return false; //if you inputted something wrong
        }
    }

    public void limit() {
        for (int i = 0; i < 3; i++) {
            if (getColor(i) < 0) {
                setColor(i, 0);
            }
            if (getColor(i) > 255) {
                setColor(i, 255);
            }
        }
    }
}

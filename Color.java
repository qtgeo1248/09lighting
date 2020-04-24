public class Color {
    private int r;
    private int g;
    private int b;
    private double z;

    public Color(int rx, int gy, int bz, double zcoord) {
        r = rx;
        g = gy;
        b = bz;
        z = zcoord;
    }

    public Color(int rx, int gy, int bz) {
        r = rx;
        g = gy;
        b = bz;
        z = -1 * Double.MAX_VALUE;
    }

    public int getColor(int col) { //red is 0, green is 1, blue is 2
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

    public boolean setColor(int col, int val) { //red is 0, green is 1, blue is 2
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
}

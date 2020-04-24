public class Vector {
    private double x;
    private double y;
    private double z;

    public Vector(double nx, double ny, double nz) {
        x = nx;
        y = ny;
        z = nz;
    }

    public Vector(PointMatrix mat, int i, int j) { //goes from point i to point j
        x = mat.getEl(0, j) - mat.getEl(0, i);
        y = mat.getEl(1, j) - mat.getEl(1, i);
        z = mat.getEl(2, j) - mat.getEl(2, i);
    }

    public double getEl(int i) {
        if (i == 0) {
            return x;
        } if (i == 1) {
            return y;
        } else {
            return z;
        }
    }

    public double dotProd(Vector v2) {
        double cur = 0;
        for (int i = 0; i < 3; i++) {
            cur += getEl(i) * v2.getEl(i);
        }
        return cur;
    }

    public Vector crossProd(Vector v2) { //this one times v2
        double nx = getEl(1) * v2.getEl(2) - getEl(2) * v2.getEl(1);
        double ny = getEl(2) * v2.getEl(0) - getEl(0) * v2.getEl(2);
        double nz = getEl(0) * v2.getEl(1) - getEl(1) * v2.getEl(0);
        Vector toReturn = new Vector(nx, ny, nz);
        return toReturn;
    }
}

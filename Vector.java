import java.lang.Math;

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

    public Vector mult(double k) {
        Vector toReturn = new Vector(x * k, y * k, z * k);
        return toReturn;
    }

    public Vector minus(Vector v2) { //this minus v2
        Vector toReturn = new Vector(x - v2.getEl(0), y - v2.getEl(1), z - v2.getEl(2));
        return toReturn;
    }

    public Vector crossProd(Vector v2) { //this one times v2
        double nx = getEl(1) * v2.getEl(2) - getEl(2) * v2.getEl(1);
        double ny = getEl(2) * v2.getEl(0) - getEl(0) * v2.getEl(2);
        double nz = getEl(0) * v2.getEl(1) - getEl(1) * v2.getEl(0);
        Vector toReturn = new Vector(nx, ny, nz);
        return toReturn;
    }

    public static Vector norm(PointMatrix mat, int i) {
        Vector v1 = new Vector(mat, i, i + 1);
        Vector v2 = new Vector(mat, i, i + 2);
        Vector norm = v1.crossProd(v2);
        return norm;
    }

    public Vector normalize() {
        return mult(1 / Math.sqrt(x * x + y * y + z * z));
    }

    public Color lighting(Vector eye, Vector ray, Color amb, Color light, Color kAmb, Color kDiff, Color kSpec) {
        Color toReturn = new Color(0, 0, 0);
        Vector newNorm = normalize();
        Vector newEye = eye.normalize();
        Vector newRay = ray.normalize();
        for (int i = 0; i < 3; i++) {
            double L = newNorm.ambLight(amb, kAmb, i) + newNorm.diffLight(newRay, light, kDiff, i) + newNorm.specLight(newEye, newRay, light, kSpec, i);
            toReturn.setColor(i, L);
        }
        toReturn.limit();
        return toReturn;
    }

    private double ambLight(Color amb, Color kAmb, int col) {
        return amb.getColor(col) * kAmb.getColor(col);
    }

    private double diffLight(Vector ray, Color light, Color kDiff, int col) {
        return light.getColor(col) * kDiff.getColor(col) * dotProd(ray);
    }

    private double specLight(Vector eye, Vector ray, Color light, Color kSpec, int col) {
        Vector cur1 = mult(2 * dotProd(ray));
        Vector cur = cur1.minus(ray);
        return light.getColor(col) * kSpec.getColor(col) * Math.pow(cur.dotProd(eye), 5);
    }
}

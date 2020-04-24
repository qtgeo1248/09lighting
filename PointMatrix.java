import java.lang.Math;
import java.util.ArrayList;

public class PointMatrix {
    ArrayList<double[]> mat;
    int maxCols;

    public PointMatrix() {
        mat = new ArrayList<double[]>();
        maxCols = 0;
    }

    public int numRows() {
        return 4;
    }

    public int numCols() {
        return maxCols;
    }

    public double getEl(int r, int c) {
        return mat.get(c)[r];
    }

    public void replace(int r, int c, double val) {
        mat.get(c)[r] = val;
    }

    public void addPoint(double x, double y) {
        double[] toAdd = new double[numRows()];
        toAdd[0] = x;
        toAdd[1] = y;
        for (int i = 2; i < toAdd.length - 1; i++) {
            toAdd[i] = 0;
        }
        toAdd[toAdd.length - 1] = 1.0;
        mat.add(toAdd);
        maxCols++;
    }

    public void addPoint(double x, double y, double z) {
        double[] toAdd = new double[numRows()];
        toAdd[0] = x;
        toAdd[1] = y;
        toAdd[2] = z;
        for (int i = 3; i < toAdd.length - 1; i++) {
            toAdd[i] = 0;
        }
        toAdd[toAdd.length - 1] = 1.0;
        mat.add(toAdd);
        maxCols++;
    }

    public void addEdge(double x0, double y0, double x1, double y1) {
        addPoint(x0, y0);
        addPoint(x1, y1);
    }

    public void addEdge(double x0, double y0, double z0, double x1, double y1, double z1) {
        addPoint(x0, y0, z0);
        addPoint(x1, y1, z1);
    }

    public void addPolygon(double x0, double y0, double x1, double y1, double x2, double y2) {
        addPoint(x0, y0);
        addPoint(x1, y1);
        addPoint(x2, y2);
    }

    public void addPolygon(double x0, double y0, double z0, double x1, double y1, double z1, double x2, double y2, double z2) {
        addPoint(x0, y0, z0);
        addPoint(x1, y1, z1);
        addPoint(x2, y2, z2);
    }

    public boolean ident() { //must be square matrix
        clear();
        for (int i = 0; i < 4; i++) {
            double[] toAdd = new double[4];
            for (int j = 0; j < 4; j++) {
                if (j == i) {
                    toAdd[j] = 1;
                } else {
                    toAdd[j] = 0;
                }
            }
            mat.add(toAdd);
        }
        maxCols = mat.size();
        return true;
    }

    public PointMatrix copy() {
        PointMatrix toReturn = new PointMatrix();
        for (int i = 0; i < mat.size(); i++) {
            toReturn.addPoint(0, 0, 0);
            for (int j = 0; j < mat.get(i).length; j++) {
                toReturn.replace(j, i, mat.get(i)[j]);
            }
        }
        return toReturn;
    }

    public void clear() {
        mat = new ArrayList<double[]>();
        maxCols = 0;
    }

    public boolean rightMult(PointMatrix m2) { //the input is on the right
        if (numCols() == m2.numRows()) {
            for (int j = 0; j < m2.numCols(); j++) {
                double[] cur = new double[m2.numRows()];
                for (int i = 0; i < cur.length; i++) {
                    cur[i] = 0;
                    for (int k = 0; k < m2.numRows(); k++) {
                        cur[i] += getEl(i, k) * m2.getEl(k, j);
                    }
                }
                for (int i = 0; i < cur.length; i++) {
                    m2.replace(i, j, cur[i]);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean leftMult(PointMatrix m1) { //the input is on the left
        if (m1.numCols() == numRows()) {
            for (int j = 0; j < numCols(); j++) {
                double[] cur = new double[numRows()];
                for (int i = 0; i < cur.length; i++) {
                    cur[i] = 0;
                    for (int k = 0; k < numRows(); k++) {
                        cur[i] += m1.getEl(i, k) * getEl(k, j);
                    }
                }
                for (int i = 0; i < cur.length; i++) {
                    replace(i, j, cur[i]);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public void beTranslate(double x, double y, double z) {
        ident();
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < maxCols; j++ ) {
                if (i == j) {
                    replace(i, j, 1.0);
                } else if (j == maxCols - 1) {
                    if (i == 0) {
                        replace(i, j, x);
                    } else if (i == 1) {
                        replace(i, j, y);
                    } else if (i == 2) {
                        replace(i, j, z);
                    }
                } else {
                    replace(i, j, 0);
                }
            }
        }
    }

    public void beScale(double x, double y, double z) {
        ident();
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < maxCols; j++) {
                if (i == j) {
                    if (i == 0) {
                        replace(i, j, x);
                    } else if (i == 1) {
                        replace(i, j, y);
                    } else if (i == 2) {
                        replace(i, j, z);
                    } else {
                        replace(i, j, 1);
                    }
                } else {
                    replace(i, j, 0);
                }
            }
        }
    }

    public void beRotZ(double theta) {
        ident();
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < maxCols; j++) {
                if (i == j) {
                    if (i <= 1) {
                        replace(i, j, Math.cos(Math.toRadians(theta)));
                    } else {
                        replace(i, j, 1);
                    }
                } else if (i + j == 1) {
                    replace(i, j, (i * 2 - 1) * Math.sin(Math.toRadians(theta)));
                } else {
                    replace(i, j, 0);
                }
            }
        }
    }

    public void beRotX(double theta) {
        ident();
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < maxCols; j++) {
                if (i == j) {
                    if (Math.abs(i - 1.5) == 0.5) {
                        replace(i, j, Math.cos(Math.toRadians(theta)));
                    } else {
                        replace(i, j, 1);
                    }
                } else if (Math.abs(i - 1.5) == 0.5 && Math.abs(j - 1.5) == 0.5) {
                    replace(i, j, (i * 2 - 3) * Math.sin(Math.toRadians(theta)));
                } else {
                    replace(i, j, 0);
                }
            }
        }
    }

    public void beRotY(double theta) {
        ident();
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < maxCols; j++) {
                if (i == j) {
                    if (i % 2 == 0) {
                        replace(i, j, Math.cos(Math.toRadians(theta)));
                    } else {
                        replace(i, j, 1);
                    }
                } else if (i + j == 2) {
                    replace(i, j, (j - 1) * Math.sin(Math.toRadians(theta)));
                }
            }
        }
    }

    public String toString() {
        String out = "";
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < maxCols; j++) {
                out += getEl(i, j) + " ";
            }
            out = out.substring(0, out.length() - 1);
            out += "\n";
        }
        out = out.substring(0, out.length() - 1);
        return out;
    }

    public void addCircle(double x, double y, double z, double r, int steps) {
        for (int i = 0; i <= steps; i++) {
            double t = 1.0 / steps * i;
            double x0 = r * Math.cos(2 * Math.PI * t) + x;
            double y0 = r * Math.sin(2 * Math.PI * t) + y;
            addPoint(x0, y0, z);
            if (i > 0 && i < steps) {
                addPoint(x0, y0, z);
            }
        }
    }

    public void addBezier(double x0, double y0, double x1, double y1, double x2, double y2, double x3, double y3, int steps) {
        PointMatrix bez = new PointMatrix();
        bez.beBezier();
        for (int i = 0; i <= steps; i++) {
            double t = 1.0 / steps * i;
            PointMatrix coefsx = new PointMatrix();
            coefsx.addPoint(0, 0, 0);
            coefsx.replace(0, 0, x0);
            coefsx.replace(1, 0, x1);
            coefsx.replace(2, 0, x2);
            coefsx.replace(3, 0, x3);
            coefsx.leftMult(bez);
            PointMatrix coefsy = new PointMatrix();
            coefsy.addPoint(0, 0, 0);
            coefsy.replace(0, 0, y0);
            coefsy.replace(1, 0, y1);
            coefsy.replace(2, 0, y2);
            coefsy.replace(3, 0, y3);
            coefsy.leftMult(bez);
            double x = 0;
            double y = 0;
            for (int k = 0; k < 4; k++) {
                x += Math.pow(t, k) * coefsx.getEl(3 - k, 0);
                y += Math.pow(t, k) * coefsy.getEl(3 - k, 0);
            }
            addPoint(x, y);
            if (i > 0 && i < steps) {
                addPoint(x, y);
            }
        }
    }

    private void beBezier() {
        ident();
        replace(0, 0, -1);
        replace(0, 1, 3);
        replace(0, 2, -3);
        replace(0, 3, 1);
        replace(1, 0, 3);
        replace(1, 1, -6);
        replace(1, 2, 3);
        replace(1, 3, 0);
        replace(2, 0, -3);
        replace(2, 1, 3);
        replace(2, 2, 0);
        replace(2, 3, 0);
        replace(3, 0, 1);
        replace(3, 1, 0);
        replace(3, 2, 0);
        replace(3, 3, 0);
    }

    public void addHermite(double x0, double y0, double x1, double y1, double rx0, double ry0, double rx1, double ry1, int steps) {
        PointMatrix her = new PointMatrix();
        her.beHermite();
        for (int i = 0; i <= steps; i++) {
            double t = 1.0 / steps * i;
            PointMatrix coefsx = new PointMatrix();
            coefsx.addPoint(0, 0, 0);
            coefsx.replace(0, 0, x0);
            coefsx.replace(1, 0, x1);
            coefsx.replace(2, 0, rx0);
            coefsx.replace(3, 0, rx1);
            coefsx.leftMult(her);
            PointMatrix coefsy = new PointMatrix();
            coefsy.addPoint(0, 0, 0);
            coefsy.replace(0, 0, y0);
            coefsy.replace(1, 0, y1);
            coefsy.replace(2, 0, ry0);
            coefsy.replace(3, 0, ry1);
            coefsy.leftMult(her);
            double x = 0;
            double y = 0;
            for (int k = 0; k < 4; k++) {
                x += Math.pow(t, k) * coefsx.getEl(3 - k, 0);
                y += Math.pow(t, k) * coefsy.getEl(3 - k, 0);
            }
            addPoint(x, y);
            if (i > 0 && i < steps) {
                addPoint(x, y);
            }
        }
    }

    private void beHermite() {
        ident();
        replace(0, 0, 2);
        replace(0, 1, -2);
        replace(0, 2, 1);
        replace(0, 3, 1);
        replace(1, 0, -3);
        replace(1, 1, 3);
        replace(1, 2, -2);
        replace(1, 3, -1);
        replace(2, 0, 0);
        replace(2, 1, 0);
        replace(2, 2, 1);
        replace(2, 3, 0);
        replace(3, 0, 1);
        replace(3, 1, 0);
        replace(3, 2, 0);
        replace(3, 3, 0);
    }

    public void addBox(double x, double y, double z, double w, double h, double d) {
        addPolygon(x, y, z, x, y - h, z, x + w, y, z);
        addPolygon(x, y - h, z, x + w, y - h, z, x + w, y, z);
        addPolygon(x + w, y, z, x + w, y - h, z, x + w, y, z - d);
        addPolygon(x + w, y - h, z, x + w, y - h, z - d, x + w, y, z - d);
        addPolygon(x + w, y, z - d, x + w, y - h, z - d, x, y - h, z - d);
        addPolygon(x, y, z - d, x + w, y, z - d, x, y - h, z - d);
        addPolygon(x, y, z, x, y, z - d, x, y - h, z);
        addPolygon(x, y, z - d, x, y - h, z - d, x, y - h, z);
        addPolygon(x, y, z, x + w, y, z, x, y, z - d);
        addPolygon(x + w, y, z, x + w, y, z - d, x, y, z - d);
        addPolygon(x, y - h, z - d, x + w, y - h, z - d, x + w, y - h, z);
        addPolygon(x, y - h, z, x, y - h, z - d, x + w, y - h, z);
    }

    public void addSphere(double x, double y, double z, double r, int steps) {
        double[][] points = makeSphere(x, y, z, r, steps);
        for (int i = 0; i < steps; i++) {
            for (int j = 0; j < steps; j++) {
                int cur = i * (steps + 1) + j;
                int num = steps + 1;
                int mod = steps * (steps + 1);
                if (j < steps - 1) {
                    addPolygon(points[cur][0], points[cur][1], points[cur][2],
                               points[cur + 1][0], points[cur + 1][1], points[cur + 1][2],
                               points[(cur + num + 1) % mod][0], points[(cur + num + 1) % mod][1], points[(cur + num + 1) % mod][2]);
                }
                if (j > 0) {
                    addPolygon(points[cur][0], points[cur][1], points[cur][2],
                               points[(cur + num + 1) % mod][0], points[(cur + num + 1) % mod][1], points[(cur + num + 1) % mod][2],
                               points[(cur + num) % mod][0], points[(cur + num) % mod][1], points[(cur + num) % mod][2]);
                }
            }
        }
    }

    private double[][] makeSphere(double x, double y, double z, double r, int steps) {
        double[][] toReturn = new double[steps * (steps + 1)][3];
        for (int i = 0; i < steps; i++) {
            for (int j = 0; j <= steps; j++) {
                double curx = r * Math.cos(Math.PI * j / steps) + x;
                double cury = r * Math.sin(Math.PI * j / steps) * Math.cos(2 * Math.PI * i / steps) + y;
                double curz = r * Math.sin(Math.PI * j / steps) * Math.sin(2 * Math.PI * i / steps) + z;
                toReturn[i * (steps + 1) + j][0] = curx;
                toReturn[i * (steps + 1) + j][1] = cury;
                toReturn[i * (steps + 1) + j][2] = curz;
            }
        }
        return toReturn;
    }

    public void addTorus(double x, double y, double z, double r, double R, int steps) {
        double[][] points = makeTorus(x, y, z, r, R, steps);
        for (int i = 0; i < steps; i++) {
            for (int j = 0; j < steps; j++) {
                int cur = i * steps + j;
                int offset = 0;
                if (j == steps - 1) {
                    offset = -1 * steps;
                }
                int mod = steps * steps;
                addPolygon(points[cur][0], points[cur][1], points[cur][2],
                           points[(cur + steps) % mod][0], points[(cur + steps) % mod][1], points[(cur + steps) % mod][2],
                           points[(cur + steps + 1 + offset) % mod][0], points[(cur + steps + 1 + offset) % mod][1], points[(cur + steps + 1 + offset) % mod][2]);
                addPolygon(points[cur][0], points[cur][1], points[cur][2],
                           points[(cur + steps + 1 + offset) % mod][0], points[(cur + steps + 1 + offset) % mod][1], points[(cur + steps + 1 + offset) % mod][2],
                           points[(cur + 1 + offset) % mod][0], points[(cur + 1 + offset) % mod][1], points[(cur + 1 + offset) % mod][2]);
            }
        }
    }

    private double[][] makeTorus(double x, double y, double z, double r, double R, int steps) {
        double[][] toReturn = new double[steps * steps][3];
        for (int i = 0; i < steps; i++) {
            for (int j = 0; j < steps; j++) {
                double curx = Math.cos(2 * Math.PI * i / steps) * (r * Math.cos(2 * Math.PI * j / steps) + R) + x;
                double cury = r * Math.sin(2 * Math.PI * j / steps) + y;
                double curz = -1 * Math.sin(2 * Math.PI * i / steps) * (r * Math.cos(2 * Math.PI * j / steps) + R) + z;
                toReturn[i * steps + j][0] = curx;
                toReturn[i * steps + j][1] = cury;
                toReturn[i * steps + j][2] = curz;
            }
        }
        return toReturn;
    }
}

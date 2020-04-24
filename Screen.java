import java.io.*;
import java.lang.Math;

public class Screen {
    private Color[][] screen;

    public Screen(int x, int y) {
        screen = new Color[y][x];
        for (int i = 0; i < screen.length; i++) {
            for (int j = 0; j < screen[i].length; j++) {
                screen[i][j] = new Color(0, 0, 0);
            }
        }
    }

    public void plot(double x, double y, double z, Color ex, Vector eye) {
        int newy = screen.length - 1 - (int)y;
        if (x >= 0 && x < screen[0].length && newy >= 0 && newy < screen.length) {
            if (z > screen[newy][(int)x].getZ()) {
                for (int i = 0; i < 3; i++) {
                     if (!screen[newy][(int)x].setColor(i, ex.getColor(i))) {
                         System.exit(1);
                     }
                }
                screen[newy][(int)x].setZ(z);
            }
        }
    }

    public void clear() {
        for (int i = 0; i < screen.length; i++) {
            for (int j = 0; j < screen[i].length; j++) {
                screen[i][j] = new Color(0, 0, 0);
            }
        }
    }

    public void file(String file) {
        try {
            FileWriter writer = new FileWriter(file);
            writer.write("P3 " + screen[0].length + " " + screen.length + " 255");
            for (int i = 0; i < screen.length; i++) {
                for (int j = 0; j < screen[i].length; j++) {
                    Color cur = screen[i][j];
                    for (int col = 0; col < 3; col++) {
                        writer.write(" " + screen[i][j].getColor(col));
                    }
                }
            }
            writer.write(" ");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void drawLines(PointMatrix mat, Color ex) {
        for (int j = 0; j < mat.numCols() - 1; j += 2) {
            line(mat.getEl(0, j), mat.getEl(1, j), mat.getEl(2, j),
                 mat.getEl(0, j + 1), mat.getEl(1, j + 1), mat.getEl(2, j + 1), ex);
        }
    }

    public void drawPolygons(PointMatrix mat, Color ex) {
        for (int j = 0; j < mat.numCols() - 2; j += 3) {
            Color thing = new Color(0, 255 - 17 * (15 - (((j / 3) % 10) + 4)), 0);
            if (isFront(mat, j)) {
                scanFill(mat.getEl(0, j), mat.getEl(1, j), mat.getEl(2, j),
                         mat.getEl(0, j + 1), mat.getEl(1, j + 1), mat.getEl(2, j + 1),
                         mat.getEl(0, j + 2), mat.getEl(1, j + 2), mat.getEl(2, j + 2), thing);
            }
        }
    }

    private void scanFill(double x0, double y0, double z0, double x1, double y1, double z1, double x2, double y2, double z2, Color ex) {
        //x0, y0 should be at the bottom; x2, y2 should be at the top
        if (y0 > y1) {
            scanFill(x1, y1, z1, x0, y0, z0, x2, y2, z2, ex);
            return;
        } else if (y0 > y2) {
            scanFill(x2, y2, z2, x1, y1, z1, x0, y0,z0, ex);
            return;
        } else if (y1 > y2) {
            scanFill(x0, y0, z0, x2, y2, z2, x1, y1, z1, ex);
            return;
        } else {
            double dxL = (x2 - x0) / (y2 - y0);
            double xL = x0;
            double dzL = (z2 - z0) / (y2 - y0);
            double zL = z0;
            if (y0 != y1) {
                double dxR = (x1 - x0) / (y1 - y0);
                double xR = x0;
                double dzR = (z1 - z0) / (y1 - y0);
                double zR = z0;
                for (double y = y0; y < y1; y++) {
                    scanLine(xL, zL, xR, zR, y, ex);
                    xL += dxL;
                    xR += dxR;
                    zL += dzL;
                    zR += dzR;
                }
            } else {
                scanLine(x0, z0, x1, z1, y0, ex);
            }
            if (y1 != y2) {
                double dxR = (x2 - x1) / (y2 - y1);
                double xR = x1;
                double dzR = (z2 - z1) / (y2 - y1);
                double zR = z1;
                for (double y = y1; y <= y2; y++) {
                    scanLine(xL, zL, xR, zR, y, ex);
                    xL += dxL;
                    xR += dxR;
                    zL += dzL;
                    zR += dzR;
                }
            } else {
                scanLine(x1, z1, x2, z2, y1, ex);
            }
        }
    }

    private void scanLine(double x0, double z0, double x1, double z1, double y, Color ex) {
        if (x0 > x1) {
            scanLine(x1, z1, x0, z0, y, ex);
        } else {
            double z = z0;
            double dz = (z1 - z0) / (x1 - x0);
            for (double x = x0; x <= x1; x++) {
                plot(x, y, z, ex);
                z += dz;
            }
        }
    }

    private boolean isFront(Vector eye, PointMatrix mat, int i) {
        Vector norm = Vector.norm(mat, i);
        return norm.dotProd(eye) > 0;
    }

    private Color lighting(Vector eye, PointMatrix mat, int i) {
        Vector norm = Vector.norm(mat, i);
    }

    public void line(double x0, double y0, double z0, double x1, double y1, double z1, Color ex) {
        if (x1 < x0) {
            line(x1, y1, z1, x0, y0, z0, ex);
        } else {
            double m = (y1 - y0) / (x1 - x0);
            double a = y1 - y0;
            double b = x0 - x1;
            if (m >= 0 && m <= 1) { //Octant 1
                double y = y0;
                double d = 2 * a + b;
                double z = z0;
                double dz = (z1 - z0) / (x1 - x0);
                for (double x = x0; x <= x1; x++) {
                    plot(x, y, z, ex);
                    if (d > 0) {
                        y++;
                        d += 2 * b;
                    }
                    d += 2 * a;
                    z += dz;
                }
            } else if (m <= 0 && m >= -1) { //Octant 8
                double y = y0;
                double d = 2 * a + b;
                double z = z0;
                double dz = (z1 - z0) / (x1 - x0);
                for (double x = x0; x <= x1; x++) {
                    plot(x, y, z, ex);
                    if (d < 0) {
                        y--;
                        d -= 2 * b;
                    }
                    d += 2 * a;
                    z += dz;
                }
            } else if (m >= 1) { //Octant 2
                double x = x0;
                double d = a + 2 * b;
                double z = z0;
                double dz = (z1 - z0) / (y1 - y0);
                for (double y = y0; y <= y1; y++) {
                    plot(x, y, z, ex);
                    if (d < 0) {
                        x++;
                        d += 2 * a;
                    }
                    d += 2 * b;
                    z += dz;
                }
            } else { //Octant 7
                double x = x0;
                double d = a + 2 * b;
                double z = z0;
                double dz = (z1 - z0) / (y1 - y0);
                for (double y = y0; y >= y1; y--) {
                    plot(x, y, z, ex);
                    if (d > 0) {
                        x++;
                        d += 2 * a;
                    }
                    d -= 2 * b;
                    z += dz;
                }
            }
        }
    }
}

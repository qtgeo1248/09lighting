import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.lang.Integer;
import java.util.Arrays;
import java.io.IOException;
import java.util.Stack;

public class Driver {
    final static int XMAX = 500;
    final static int YMAX = 500;

    public static void parse(String file) {
        PointMatrix edges = new PointMatrix();
        PointMatrix polygons = new PointMatrix();
        Stack<PointMatrix> cs = new Stack<PointMatrix>();
        PointMatrix ident = new PointMatrix();
        ident.ident();
        cs.push(ident);
        Screen s = new Screen(XMAX, YMAX);
        Color ex = new Color(0, 255, 0);
        try {
            File f = new File(file);
            Scanner in = new Scanner(f);
            while (in.hasNextLine()) {
                String line = in.nextLine();
                if (line.equals("line")) {
                    int[] co = parseArgs(in.nextLine());
                    edges.addEdge(co[0], co[1], co[2], co[3], co[4], co[5]);
                    edges.leftMult(cs.peek());
                    s.drawLines(edges, ex);
                    edges.clear();
                } else if (line.equals("circle")) {
                    int[] co = parseArgs(in.nextLine());
                    edges.addCircle(co[0], co[1], co[2], co[3], 1000);
                    edges.leftMult(cs.peek());
                    s.drawLines(edges, ex);
                    edges.clear();
                } else if (line.equals("bezier")) {
                    int[] co = parseArgs(in.nextLine());
                    edges.addBezier(co[0], co[1], co[2], co[3], co[4], co[5], co[6], co[7], 1000);
                    edges.leftMult(cs.peek());
                    s.drawLines(edges, ex);
                    edges.clear();
                } else if (line.equals("hermite")) {
                    int[] co = parseArgs(in.nextLine());
                    edges.addHermite(co[0], co[1], co[2], co[3], co[4], co[5], co[6], co[7], 1000);
                    edges.leftMult(cs.peek());
                    s.drawLines(edges, ex);
                    edges.clear();
                } else if (line.equals("box")) {
                    int[] co = parseArgs(in.nextLine());
                    polygons.addBox(co[0], co[1], co[2], co[3], co[4], co[5]);
                    polygons.leftMult(cs.peek());
                    s.drawPolygons(polygons, ex);
                    polygons.clear();
                } else if (line.equals("sphere")) {
                    int[] co = parseArgs(in.nextLine());
                    polygons.addSphere(co[0], co[1], co[2], co[3], 20);
                    polygons.leftMult(cs.peek());
                    s.drawPolygons(polygons, ex);
                    polygons.clear();
                } else if (line.equals("torus")) {
                    int[] co = parseArgs(in.nextLine());
                    polygons.addTorus(co[0], co[1], co[2], co[3], co[4], 20);
                    polygons.leftMult(cs.peek());
                    s.drawPolygons(polygons, ex);
                    polygons.clear();
                } else if (line.equals("scale")) {
                    int[] r = parseArgs(in.nextLine());
                    PointMatrix temp = new PointMatrix();
                    temp.beScale(r[0], r[1], r[2]);
                    temp.leftMult(cs.peek());
                    cs.pop();
                    cs.push(temp);
                } else if (line.equals("move")) {
                    int[] p = parseArgs(in.nextLine());
                    PointMatrix temp = new PointMatrix();
                    temp.beTranslate(p[0], p[1], p[2]);
                    temp.leftMult(cs.peek());
                    cs.pop();
                    cs.push(temp);
                } else if (line.equals("rotate")) {
                    String args = in.nextLine();
                    int[] theta = parseArgs(args.substring(2));
                    PointMatrix temp = new PointMatrix();
                    if (args.charAt(0) == 'x') {
                        temp.beRotX(theta[0]);
                    } else if (args.charAt(0) == 'y') {
                        temp.beRotY(theta[0]);
                    } else {
                        temp.beRotZ(theta[0]);
                    }
                    temp.leftMult(cs.peek());
                    cs.pop();
                    cs.push(temp);
                } else if (line.equals("push")) {
                    PointMatrix toCopy = cs.peek().copy();
                    cs.push(toCopy);
                } else if (line.equals("pop")) {
                    cs.pop();
                } else if (line.equals("display")) {
                    s.file("solid.ppm");
                    Runtime.getRuntime().exec("convert solid.ppm solid.png");
                    Runtime.getRuntime().exec("display solid.png");
                } else if (line.equals("save")) {
                    String next = in.nextLine();
                    s.file(next);
                    Runtime.getRuntime().exec("convert " + next + " " + next);
                } else if (line.equals("clear")) {
                    s.clear();
                } else if (line.equals("quit")) {
                    return;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int[] parseArgs(String coords) {
        String[] cur = coords.split(" ");
        int[] out = new int[cur.length];
        for (int i = 0; i < cur.length; i++) {
            out[i] = Integer.parseInt(cur[i]);
        }
        return out;
    }
    public static void main(String[] args) {
        if (args.length == 0) {
            parse("script");
        } else {
            parse("myscript");
        }
    }
}

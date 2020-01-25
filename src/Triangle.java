import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Triangle implements Iterable<Point> {
	public Point a;
	public Point b;
	public Point c;
	public ArrayList<Color> colors = new ArrayList<Color>();
	public Color color = new Color(1, 1, 1);
	
	public Triangle(Point a, Point b, Point c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}
	
	@Override
	public String toString() {
		return a + " " + b + " " + c;
	}
	
	public Triangle sharesEdge(Point p) {
		for(Triangle t : Main.triangles) {
			if(t.contains(p)) {
				if((t.contains(a) && t.contains(b)) || (t.contains(b) && t.contains(c)) || (t.contains(a) && t.contains(c))) {
//					System.out.println(Main.triangles + " - triangles array before");
//					System.out.println(t + " - found t2");
					return t;
				}
			}
		}

		return null;
	}
	
	public boolean inTriangle(Point p) {
		double totalOfThree = Triangle.getArea(a, b, p) + Triangle.getArea(a, c, p) + Triangle.getArea(b, c, p);
		double totalArea = Triangle.getArea(a, b, c);
		
		return Math.abs(totalOfThree - totalArea) <= Main.tolerance;
	}
	
	public boolean inCircle(Point p) {
		CircleThree.Circle middle = CircleThree.circleFromPoints(new CircleThree.Point(a), new CircleThree.Point(b), new CircleThree.Point(c));

		double distance = Math.sqrt(Math.pow(middle.center.x - p.x, 2) + Math.pow(middle.center.y - p.y, 2));
		
//		System.out.println(middle);
		
		return middle.radius >= distance;
	}
	
	public double getArea() {
		return Math.abs((a.x - c.x) * (b.y - a.y) - (a.x - b.x) * (c.y - a.y)) / 2;
	}
	
	public static double getArea(Point a, Point b, Point c) {
		return Math.abs((a.x - c.x) * (b.y - a.y) - (a.x - b.x) * (c.y - a.y)) / 2;
	}
	
	public boolean contains(Point p) {
		return a.equals(p) || b.equals(p) || c.equals(p);
	}
	
	@Override
	public boolean equals(Object t) {
		if(!(t instanceof Triangle)) {
			return false;
		}
		
		Triangle tri  = (Triangle) t;
		
		int pairs = 0;
		
		for(Point i : tri) {
			for(Point j : this) {
				if(i.equals(j)) {
					pairs++;
				}
			}
		}
		
		return pairs >= 3;
	}

	@Override
	public Iterator<Point> iterator() {
		return Arrays.asList(new Point[] {a, b, c}).iterator();
	}
}

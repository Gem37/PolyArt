public class CircleThree { 

	static final double TOL = 0.0000001;

	public static Circle circleFromPoints(Point p1, Point p2, Point p3) {


		Point p4 = null;

		if (p1.y == p2.y) {
			p4 = p2;
			p2 = p3;
			p3 = p4;
		}
		
		if (p3.y == p2.y) {
			p4 = p2;
			p2 = p1;
			p1 = p4;
		}


		final double offset = Math.pow(p2.x,2) + Math.pow(p2.y,2);
		final double bc =   ( Math.pow(p1.x,2) + Math.pow(p1.y,2) - offset )/2.0;
		final double cd =   (offset - Math.pow(p3.x, 2) - Math.pow(p3.y, 2))/2.0;
		final double det =  (p1.x - p2.x) * (p2.y - p3.y) - (p2.x - p3.x)* (p1.y - p2.y); 

//		if (Math.abs(det) < TOL) {
////			System.out.println(p1);
////			System.out.println(p2);
////			System.out.println(p3);
////			throw new IllegalArgumentException("The difference is smaller than 0.0000001.");
//			p1.x += 1;
//			p1.y += 1;
//			p2.x -= 1;
//			p2.y -= 1;
//		}

		final double idet = 1/det;

		final double centerx =  (bc * (p2.y - p3.y) - cd * (p1.y - p2.y)) * idet;
		final double centery =  (cd * (p1.x - p2.x) - bc * (p2.x - p3.x)) * idet;
		final double radius = 
				Math.sqrt( Math.pow(p2.x - centerx,2) + Math.pow(p2.y-centery,2));

		return new Circle(new Point(centerx,centery),radius);
	}

	static class Circle
	{
		final Point center;
		final double radius;
		public Circle(Point center, double radius)
		{
			this.center = center; this.radius = radius;
		}
		@Override 
		public String toString()
		{
			return new StringBuilder().append("Center= ").append(center).append(", r=").append(radius).toString();
		}
	}

	static class Point
	{
		double x;
		double y;

		public Point(double x, double y)
		{
			this.x = x; this.y = y;
		}
		public Point(java.awt.Point a)
		{
			this.x = a.x; this.y = a.y;
		}
		@Override
		public String toString()
		{
			return "("+x+","+y+")";
		}

	}

	public static void main(String[] args)
	{
		Point p1 = new Point(0.0,1.0);
		Point p2 = new Point(1.0,0.0);
		Point p3 = new Point(2.0,1.0);
		Circle c = circleFromPoints(p1, p2, p3);
		System.out.println(c);
	}

}
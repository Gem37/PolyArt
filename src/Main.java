import java.awt.Color;
import java.awt.Component;
import java.awt.FileDialog;
import java.awt.Graphics;
import java.awt.Image;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.awt.Paint;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import jcanny.JCanny;

public class Main extends JPanel implements KeyListener {

	public static String fileName = "test.png";

	static ImageIcon originalImage = new ImageIcon();
	static ImageIcon convertedImage = new ImageIcon();

	static final int numberOfPoints = 1000;

	final static int tolerance = 1;
	
	final static double fraction = 0.9;
	final static int deviations = 3;
	final static int proximityAllowance = 10;
	
	static final int scalingFactor = 4;
	static final int offset = 0;
	public static ArrayList<Triangle> triangles = new ArrayList<Triangle>();
	public static ArrayList<Point> addedPoints = new ArrayList<Point>();

	public static boolean done = false;

	public static Point[] points;
	public static int currentPoint = 0;

	public static Color[][] pixels;

	static Main panel = new Main();

	public static void main(String[] args) {

		JFrame frame = new JFrame();
		frame.add(panel);
		frame.setSize(1440,812);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addKeyListener(panel);



		FileDialog fileChooser = new FileDialog(frame, "Test", FileDialog.LOAD);
		String image_path;

		File file = null;

		fileChooser.setVisible(true);
		image_path = fileChooser.getDirectory() + fileChooser.getFile();


		file = new File(image_path);


		//		if (fileChooser.showOpenDialog(panel) == JFileChooser.APPROVE_OPTION) {
		//			file = fileChooser.getSelectedFile();
		//		} else {
		//			file = new File("test.png");
		//		}


		BufferedImage image = null;

		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		originalImage.setImage(image);
		originalImage.setImage(originalImage.getImage().getScaledInstance(originalImage.getIconWidth() / scalingFactor, originalImage.getIconHeight() / scalingFactor, Image.SCALE_SMOOTH));

		panel.setSize(originalImage.getIconWidth(), originalImage.getIconHeight());

		pixels = createArray(originalImage);

		//		pixels = triangulateImage(pixels, randomPoints());


		//		convertedImage.setImage(createImage(pixels));
		//		convertedImage.setImage(convertedImage.getImage().getScaledInstance(convertedImage.getIconWidth() / scalingFactor, convertedImage.getIconHeight() / scalingFactor, Image.SCALE_SMOOTH));




//		points = randomPoints();
		
		points = edgePoints();
		triangulatePoints();
	}



	public static Point[] randomPoints() {
		Point[] points = new Point[numberOfPoints];

		Random r = new Random();
		r.setSeed(1);

		for(int i = 0; i < numberOfPoints; i++) {
			points[i] = new Point((int) (r.nextDouble() * originalImage.getIconWidth()), (int) (r.nextDouble() * originalImage.getIconHeight()));
		}

		return points;
	}
	
	public static Point[] edgePoints() {
		ArrayList<Point> points = new ArrayList<Point>();
		
		BufferedImage bi = new BufferedImage(originalImage.getIconWidth(), originalImage.getIconHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = bi.createGraphics();
		// paint the Icon to the BufferedImage.
		originalImage.paintIcon(null, g, 0,0);
		g.dispose();
		
		BufferedImage jCannyEdges = JCanny.CannyEdges(bi, deviations, fraction);
		
		Boolean[][] jCannyBooleans = createBooleanArray(jCannyEdges);
		
		for(int i = 0; i < jCannyBooleans.length; i++) {
			for(int j = 0; j < jCannyBooleans[0].length; j++) {
				if(jCannyBooleans[i][j]) {
					//add point
					points.add(new Point(i, j));
					
					//TODO: remove close points
					
					//square
//					for(int x = -proximityAllowance; x < proximityAllowance; x++) {
//						for(int y = -proximityAllowance; y < proximityAllowance; y++) {
//							if((i + x >= 0 && j + y >= 0) && (i + x < jCannyBooleans.length && j + y < jCannyBooleans[0].length)) {
//								jCannyBooleans[i + x][j + y] = false;
//							}
//						}
//					}
					
					//circle
//					for(int x = -proximityAllowance; x < proximityAllowance; x++) {
//						for(int y = -proximityAllowance; y < proximityAllowance; y++) {
//							if((i + x >= 0 && j + y >= 0) && (i + x < jCannyBooleans.length && j + y < jCannyBooleans[0].length)) {
//								if() {
//									jCannyBooleans[i + x][j + y] = false;
//								}
//							}
//						}
//					}
				}
			}
		}
		
		
		return  points.toArray(new Point[0]);
	}

	//	public static Color[][] triangulateImage(Color[][] pixels, Point[] points) {
	////		for(int i = 0; i < points.length; i++) {
	////			pixels[points[i].x][points[i].y] = new Color(255, 0, 0);
	////		}
	//		
	//		for(int i = 0; i < pixels.length; i++) {
	//			for(int j = 0; j < pixels[i].length; j++) {
	//				
	//			}
	//		}
	//		
	//		return pixels;
	//	}


	public void paintComponent(Graphics g) {
		//		g.drawImage(originalImage.getImage(), 0, 0, null);
		//		g.drawImage(convertedImage.getImage(), originalImage.getIconWidth() + 50, 0, null);

		if(done) {
			for(Triangle t : triangles) {
				//				g.drawLine(t.a.x + offset, t.a.y + offset, t.b.x + offset, t.b.y + offset);
				//				g.drawLine(t.b.x + offset, t.b.y + offset, t.c.x + offset, t.c.y + offset);
				//				g.drawLine(t.a.x + offset, t.a.y + offset, t.c.x + offset, t.c.y + offset);

				int x = Math.min(Math.min(t.a.x, t.b.x), t.c.x);
				int y = Math.min(Math.min(t.a.y, t.b.y), t.c.y);
				int xMax = Math.max(Math.max(t.a.x, t.b.x), t.c.x);
				int yMax = Math.max(Math.max(t.a.y, t.b.y), t.c.y);

				for(int i = x; i < xMax; i++) {
					for(int j = y; j < yMax; j++) {
						if(t.inTriangle(new Point(i, j))) {
							t.colors.add(pixels[i][j]);
						}
					}
				}

				float rAverage = 0;
				float gAverage = 0;
				float bAverage = 0;

				for(int i = 0; i < t.colors.size(); i++) {
					rAverage += (t.colors.get(i).getRed() - rAverage) / (i + 1);
					gAverage += (t.colors.get(i).getGreen() - gAverage) / (i + 1);
					bAverage += (t.colors.get(i).getBlue() - bAverage) / (i + 1);
					//					rAverage += t.colors.get(i).getRed();
					//					gAverage += t.colors.get(i).getGreen();
					//					bAverage += t.colors.get(i).getBlue();
				}


				rAverage /= /* t.colors.size()  * */ 255;
				gAverage /= /* t.colors.size()  * */ 255;
				bAverage /= /* t.colors.size()  * */ 255;


				//				System.out.println(rAverage);
				//				System.out.println(gAverage);
				//				System.out.println(bAverage);


				g.setColor(new Color(rAverage, gAverage, bAverage));
				g.fillPolygon(new int[] {t.a.x + offset, t.b.x + offset, t.c.x + offset}, new int[] {t.a.y + offset, t.b.y + offset, t.c.y + offset}, 3);
			}
			//			g.drawImage(originalImage.getImage(), offset, offset, this);
		}
		
//		BufferedImage bi = new BufferedImage(originalImage.getIconWidth(), originalImage.getIconHeight(), BufferedImage.TYPE_INT_RGB);
//		Graphics g1 = bi.createGraphics();
//		// paint the Icon to the BufferedImage.
//		originalImage.paintIcon(null, g1, 0,0);
//		g1.dispose();
//		
//		g.drawImage(JCanny.CannyEdges(bi, deviations, fraction), 0, 0, this);
	}

	public static void triangulatePoints() {
		//		int pointsDone = 0;
		//		
		//		triangles.add(new Triangle(points[0], points[1], points[2]));
		//		System.out.println(Arrays.toString(new Point[] {points[0], points[1], points[2]}) + " - first triangle");
		//		
		//		for(Point p : points) {
		//			addedPoints.add(p);
		//			
		//			if(pointsDone <= 2) {
		//				pointsDone++;
		//			} else {
		//				addPoint(p, points);
		//			}
		//		}

		Point a = new Point(0, 0);
		Point b = new Point(0, originalImage.getIconHeight());
		Point c = new Point(originalImage.getIconWidth(), 0);
		Point d = new Point(originalImage.getIconWidth(), originalImage.getIconHeight());

		triangles.add(new Triangle(a, b, c));
		triangles.add(new Triangle(b, c, d));
		addedPoints.add(a);
		addedPoints.add(b);
		addedPoints.add(c);
		addedPoints.add(d);

		done = true;
		panel.repaint();
	}

	public static void addPoint(Point a, Point[] points) {
		//		Point b = new Point(10000000, 10000000);
		//		Point c = new Point(10000000, 10000001);

		ArrayList<Triangle> trianglesToCheck = new ArrayList<Triangle>();

		Triangle triangleInside = null;


		for(Triangle t : triangles) {
			if(t.inTriangle(a)) {
				triangleInside = t;
				break;
			}
		}

		//		if(triangleInside == null) {
		//			for(Point p : addedPoints) { // Closest two points
		//				if(!(p == a)) {
		//					if(p.distance(a) < b.distance(a)) {
		//						c = b;
		//						b = p;
		//					} else if(p.distance(a) < c.distance(a)) {
		//						c = p;
		//					}
		//				}
		//			}
		//			
		//			Triangle newTriangle = new Triangle(a, b, c);
		//			triangles.add(newTriangle);
		//			trianglesToCheck.add(newTriangle);
		//		} else {
		triangles.remove(triangleInside);

		//		System.out.println(triangleInside + " - Triangle Inside"); // Null for some reason - maybe because its on a point or line exactly?
		//		System.out.println(a);

		//		for(Triangle t : triangles) {
		//			System.out.println(t);
		//		}



		//		System.out.println(newTriangle1 + " - New Triangle");

		//	}

		Triangle newTriangle1 = new Triangle(a, triangleInside.a, triangleInside.b);
		triangles.add(newTriangle1);
		trianglesToCheck.add(newTriangle1);

		Triangle newTriangle2 = new Triangle(a, triangleInside.b, triangleInside.c);
		triangles.add(newTriangle2);
		trianglesToCheck.add(newTriangle2);

		Triangle newTriangle3 = new Triangle(a, triangleInside.a, triangleInside.c);
		triangles.add(newTriangle3);
		trianglesToCheck.add(newTriangle3);
		//		}

		while(trianglesToCheck.size() > 0) {
			Triangle t = trianglesToCheck.get(0);

			//			System.out.println(triangles.indexOf(t) + " - index");
			//			System.out.println(t);

			//			if(triangles.indexOf(t) == 3) {
			//				System.out.println("uh oh");
			//			}

			//			System.out.println("Currently checking triangle: " + t);
			//			System.out.println("checking points: " + addedPoints);
//			if(!triangles.contains(t)) {
				for(Point p : addedPoints) {
					Triangle t2 = t.sharesEdge(p);

					if(t.inCircle(p) && t2 != null && !t.equals(t2) && !t.contains(p)) {
						//	System.out.println("Performing Flip");
						ArrayList<Point> t1New = new ArrayList<Point>();
						ArrayList<Point> t2New = new ArrayList<Point>();

						boolean addedOne = false;


						Set<Point> set = new HashSet<>();
						ArrayList<Point> list = new ArrayList<Point>();

						set.add(t.a);
						set.add(t.b);
						set.add(t.c);
						set.add(t2.a);
						set.add(t2.b);
						set.add(t2.c);

						list.addAll(set);

						//					System.out.println(t.a + " - t.a");
						//					System.out.println(t.b + " - t.b"); // is equal to p
						//					System.out.println(t.c + " - t.c");
						//					System.out.println(p + " - p"); // is equal to t.b

						//					for(Point p2 : list) {
						//						System.out.println(p2);
						//					}

						for(Point p1 : list) {
							if((t).contains(p1) && (t2).contains(p1)) {
								if(!addedOne) {
									t1New.add(p1);
									addedOne = true;
								} else {
									t2New.add(p1);
								}
							} else {
								t1New.add(p1);
								t2New.add(p1);
							}
						}

						//					System.out.println(t1New);


						Triangle t1NewTriangle = new Triangle(t1New.get(0), t1New.get(1), t1New.get(2));
						Triangle t2NewTriangle = new Triangle(t2New.get(0), t2New.get(1), t2New.get(2));

						//					System.out.println(t + " - t");
						//					System.out.println(t1NewTriangle + " - t1NewTriangle");

						//					System.out.println(p);
						//					System.out.println(triangles.indexOf(t) + " - index");


						//					if(triangles.indexOf(t) == -1) {
						//						for(Triangle print : triangles) {
						//							System.out.println(print);
						//						}
						//					}
						
						if(triangles.indexOf(t) != -1) {
							triangles.set(triangles.indexOf(t), t1NewTriangle);
							triangles.set(triangles.indexOf(t2), t2NewTriangle);
						}
						
						//					System.out.println(triangles + " - triangles array after");
						//					
						//					System.out.println(triangles.get(0) + " - triangles[0]");
						//					System.out.println(triangles.get(1) + " - triangles[1]");
						//					
						//					System.out.println(t2 + " - error t2");
						//					
						//					System.out.println(triangles.indexOf(t2) + " - index of t2");
						//
						//					System.out.println(t2 + " - t2");
						//					System.out.println(t2NewTriangle + " - t2NewTriangle");


						if(!trianglesToCheck.contains(t1NewTriangle)) {
							trianglesToCheck.add(t1NewTriangle);
						}

						if(!trianglesToCheck.contains(t2NewTriangle)) {
							trianglesToCheck.add(t2NewTriangle);
						}

						//					System.out.println(trianglesToCheck.size());

						break;
					} else {
						//					System.out.println(t.inCircle(p) + " - t.inCircle(p)");
						//					System.out.println(t2 + " - t2");
						//					System.out.println(t.contains(p) + " - t.contains(p)");
						//					System.out.println(p + " - p");
						//					System.out.println();
					}
				}
//			}

			trianglesToCheck.remove(0);
		}
		//		System.out.println("Checking " + i + " triangles");
		addedPoints.add(a);
	}

	//	public static Point[] sharesEdge(Point a, Point b, Point c, Point p) {
	//		for(Point[] t : triangles) {
	//			List<Point> t1 = Arrays.asList(t);
	//
	//			if(t1.contains(p)) {
	//				if((t1.contains(a) && t1.contains(b)) || (t1.contains(b) && t1.contains(c)) || (t1.contains(a) && t1.contains(c))) {
	//					System.out.println(triangles + " - triangles array before");
	//					System.out.println(Arrays.toString((Point[]) t1.toArray()) + " - found t2");
	//					return (Point[]) t1.toArray();
	//				}
	//			}
	//		}
	//
	//		return null;
	//	}
	//
	//	public static boolean inTriangle(Point a, Point b, Point c, Point p) {
	//		double totalOfThree = areaOfTriangle(a, b, p) + areaOfTriangle(a, c, p) + areaOfTriangle(b, c, p);
	//		double totalArea = areaOfTriangle(a, b, c);
	//
	//		return totalArea == totalOfThree;
	//	}
	//
	//	public static double areaOfTriangle(Point a, Point b, Point c) {
	//		return Math.abs((a.x - c.x) * (b.y - a.y) - (a.x - b.x) * (c.y - a.y)) / 2;
	//	}
	//
	//	public static boolean inCircle(Point a, Point b, Point c, Point d) {
	//		CircleThree.Circle middle = CircleThree.circleFromPoints(new CircleThree.Point(a), new CircleThree.Point(b), new CircleThree.Point(c));
	//
	//		double distance = Math.sqrt(Math.pow(middle.center.x - d.x, 2) + Math.pow(middle.center.y - d.y, 2));
	//
	//		return middle.radius >= distance;
	//	}

	public static Color[][] createArray(ImageIcon icon) {
		BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = image.createGraphics();
		icon.paintIcon(null, g, 0, 0);
		g.dispose();

		Color[][] pixels = new Color[image.getWidth()][image.getHeight()];

		for(int y = 0; y < image.getHeight(); y++) {
			for(int x = 0; x < image.getWidth(); x++) {
				pixels[x][y] = new Color(image.getRGB(x, y));
			}
		}


		return pixels;
	}
	
	public static Boolean[][] createBooleanArray(BufferedImage image) {

		Boolean[][] pixels = new Boolean[image.getWidth()][image.getHeight()];

		for(int y = 0; y < image.getHeight(); y++) {
			for(int x = 0; x < image.getWidth(); x++) {
				pixels[x][y] = image.getRGB(x, y) == 16777215;
			}
		}

		return pixels;
	}


	public static BufferedImage createImage(Color[][] pixels) {
		BufferedImage image = new BufferedImage(pixels.length, pixels[0].length, BufferedImage.TYPE_INT_RGB);

		for(int y = 0; y < pixels[0].length; y++) {
			for(int x = 0; x < pixels.length; x++) {
				image.setRGB(x, y, pixels[x][y].getRGB());
			}
		}

		return image;
	}

	public static BufferedImage getScreenShot(Component component) {
		BufferedImage image = new BufferedImage(component.getWidth(), component.getHeight(), BufferedImage.TYPE_INT_RGB);
		// call the Component's paint method, using
		// the Graphics object of the image.
		component.paint(image.getGraphics()); // alternately use .printAll(..)
		return image;
	}

	@Override
	public void keyTyped(KeyEvent e) {
//		System.out.println("Key")
		if(e.getKeyChar() == 's') {
			JFileChooser fC = new JFileChooser();
			if(fC.showSaveDialog(panel) == JFileChooser.APPROVE_OPTION) {
				File file = fC.getSelectedFile();

				BufferedImage image = getScreenShot(panel);

				try {
					if(file.getName().endsWith(".jpg")) {
						ImageIO.write(image, "jpg", file);
					} else if(file.getName().endsWith(".png")) {
						ImageIO.write(image, "png", file);
					} else {
						File newFile = new File(file.getPath() + ".jpg");
						System.out.println(newFile.getName());
						ImageIO.write(image, "jpg", newFile);
					}

				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} else if(e.getKeyChar() == ' ') {
			if(currentPoint + 100 >= points.length) {
				for(int i = 0; currentPoint < points.length; i++) {
					addPoint(points[currentPoint], points);
					currentPoint++;
				}
				
				System.out.println("Done!");
			} else {
				for(int i = 0; i < 100; i++) {
					addPoint(points[currentPoint], points);
					currentPoint++;
				}
			}
			repaint();
		} else if(currentPoint < points.length) {
			addPoint(points[currentPoint], points);
			currentPoint++;
			repaint();
			
			if(currentPoint == points.length) {
				System.out.println("Done!");
			}
		} else {
			System.out.println("Done!");
		}
	}



	@Override
	public void keyPressed(KeyEvent e) {

	}



	@Override
	public void keyReleased(KeyEvent e) {

	}
}

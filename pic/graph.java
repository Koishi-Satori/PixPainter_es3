import javax.imageio.ImageIO;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.awt.Color;
import java.util.Vector;

import static java.lang.StrictMath.*;
import static java.lang.System.*;

public class graph {
	public static BufferedImage image = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);
	public static File file = new File("D:/ProjectJava/PixPainter_es3/pic/graph.png");
	public static Graphics2D g2D = image.createGraphics();

	public static void main(String[] args) {
		file = new File("C:/Users/DELL/Desktop/graph.png");
		g2D.setFont(new Font("", Font.PLAIN, 20));
		g2D.setColor(Color.red);
		g2D.draw(new Ellipse2D.Double(450, 0, 100, 100));
		g2D.draw(new Ellipse2D.Double(290, 150, 100, 100));
		g2D.draw(new Ellipse2D.Double(610, 150, 100, 100));
		g2D.draw(new Ellipse2D.Double(130, 300, 100, 100));
		g2D.draw(new Ellipse2D.Double(450, 300, 100, 100));
		g2D.draw(new Ellipse2D.Double(770, 300, 100, 100));
		g2D.draw(new Ellipse2D.Double(770, 550, 100, 100));
		g2D.setColor(Color.green);
		g2D.draw(new Ellipse2D.Double(400, 700, 200, 200));
		g2D.setColor(Color.cyan);
		g2D.drawLine(470, 90, 370, 160);
		g2D.drawLine(530, 90, 630, 160);
		g2D.drawLine(310, 240, 210, 310);
		g2D.drawLine(370, 240, 470, 310);
		g2D.drawLine(690, 240, 790, 310);
		g2D.setColor(Color.pink);
		g2D.drawLine(550, 350, 770, 350);
		g2D.drawString("start", 500 - g2D.getFont().getSize() * 5 / 3, 50);
		g2D.drawString("a1", 340 - g2D.getFont().getSize() * 2 / 3, 200);
		g2D.drawString("a2", 660 - g2D.getFont().getSize() * 2 / 3, 200);
		g2D.drawString("b1", 180 - g2D.getFont().getSize() * 2 / 3, 350);
		g2D.drawString("b2", 500 - g2D.getFont().getSize() * 2 / 3, 350);
		g2D.drawString("b3", 820 - g2D.getFont().getSize() * 2 / 3, 350);
		g2D.drawString("rua~~", 820 - g2D.getFont().getSize() * 5 / 3, 600);
		g2D.drawString("end", 500 - g2D.getFont().getSize() * 3 / 3, 800);
		int[] xs = {180, 500, 500, 500, 340};
		int[] ys = {400, 700, 400, 700, 250};
		g2D.drawPolyline(xs, ys, 5);
		xs = new int[]{500, 820, 500, 770};
		ys = new int[]{700, 400, 700, 600};
		g2D.drawPolyline(xs, ys, 4);
		g2D.drawLine(550, 350, 770, 350);
		g2D.dispose();
		try {
			ImageIO.write(image, "png", file);
			image = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
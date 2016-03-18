package merge2d;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.sun.xml.internal.messaging.saaj.soap.ver1_1.Header1_1Impl;

public class Generator {
	
	public List<Point> uniformly(int count, double width, double height) {
		List<Point> list = new ArrayList<>();
		int severity_size = Severity.values().length;
		Random rand = new Random();
		double point_r = Point.DEFAULT_POINT_R;
		
		for (int i=0; i<count; i++) {
			Severity severity = Severity.values()[rand.nextInt(severity_size)];
			double x = point_r + rand.nextDouble() * (width - 2*point_r);
			double y = point_r + rand.nextDouble() * (height - 2*point_r);
			list.add(new Point(severity, x, y));
		}
		
		return list;
	}
	
	public List<Point> clusters(int clusters, int count, double width, double height) {
		List<Point> list = new ArrayList<>();
		int severity_size = Severity.values().length;
		Random rand = new Random();
		
		double delta_count = Math.ceil(Math.sqrt((double) clusters));
		double delta_x = width / delta_count;
		double delta_y = height / delta_count;
		int centers = (int) delta_count;

		double min_side = Math.min(width, height);
		
		
		for (int c=0; c<count; c++) {
			double angle = rand.nextDouble() * Math.PI * 2;
			double length = rand.nextDouble() * min_side / (2*delta_count + 1);
			Severity severity = Severity.values()[rand.nextInt(severity_size)];
			
			double x_c = delta_x * (rand.nextInt(centers-1) + 1);
			double y_c = delta_y * (rand.nextInt(centers-1) + 1);
			
			double x = x_c + length * Math.cos(angle);
			double y = y_c + length * Math.sin(angle);
			
			list.add(new Point(severity, x, y));
//			list.add(new Point(severity, x_c, y_c));
		}
		
		return list;
	}
	
}

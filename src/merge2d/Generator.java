package merge2d;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
		int row_count = (int) (delta_count > count ? delta_count+1 : delta_count);
		double delta_x = width / (row_count+1);
		double delta_y = height / (row_count+1);

		double min_side = Math.min(width, height);
		
		int doze = count / clusters;
		int current_cluster = 0;
		for (int i=1; i <= row_count; i++) {
			for (int j=1; j <= row_count && current_cluster < clusters; j++) {
				for (int current=0; current < doze; current++) {
					double angle = rand.nextDouble() * Math.PI * 2;
					double length = rand.nextDouble() * min_side / (double)(2*(row_count + 2));
					Severity severity = Severity.values()[rand.nextInt(severity_size)];
					
					double x_c = delta_x * i;
					double y_c = delta_y * j;
					
					double x = x_c + length * Math.cos(angle);
					double y = y_c + length * Math.sin(angle);
					
					list.add(new Point(severity, x, y));
				}
				current_cluster++;
			}
		}
		
		return list;
	}
	
}

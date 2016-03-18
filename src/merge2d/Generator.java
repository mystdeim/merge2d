package merge2d;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Generator {
	
	public List<Point> generate(int count, double width, double height) {
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
	
}

package merge2d;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.Circle;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class PointNode extends Parent {
	
	public static final double R_D = 5; 
	public static final double R_K = 5; 
	public static final double RANK_W = 5; 
	
	// Constructor
	// -----------------------------------------------------------------------------------------------------------------
	
	public PointNode() {
		init();
	}
	
	// Public
	// -----------------------------------------------------------------------------------------------------------------
	
	public void addCount(Severity severity, int count) {
		Integer value = map.get(severity);
		if (null != value) map.put(severity, value + count); 
		else map.put(severity, count);
	}
	
	public int getCount() {
		return map.entrySet().stream().mapToInt(entry -> entry.getValue()).sum();
	}
	
	public int getCount(Severity severity) {
		if (map.containsKey(severity)) return map.get(severity).intValue();
		else return 0;
	}
		
	public void initShape() {
		getChildren().clear();
		int rank = Helpers.rank(getCount());
		
		// Text
        initEventCountText();
        
        eventCount.setLayoutY(font_h / 3.0);
        
        double r = (Helpers.rank(getCount()) * rank_w) / 2.0;
        eventCount.setLayoutX(-r);
        getChildren().add(eventCount); 
		
        // Ring
        double r_inner = r + r_d;
//        if (r_inner < R_MIN) r_inner = R_MIN;
        double r_outer = r_inner + rank*r_k;
		Circle innerCircle = new Circle(r_inner);
		Circle outerCircle = new Circle(r_outer);
		Shape ring = Path.subtract(outerCircle, innerCircle);
		ring.setStroke(Color.BLACK);
        ring.setFill(Color.GREEN);
//        getChildren().add(ring);
        
        Circle white_c = new Circle(r_inner);
        Color c = new Color(1, 1, 1, 0.7);
        white_c.setFill(c);
        getChildren().add(white_c);
        white_c.toBack();
        
        double angle = 0;
        double count = getCount();
        Shape shape = null;
        if (count > 0) {
	        for (Severity severity : Severity.values()) {
	        	double count_s = getCount(severity);
	        	if (count_s > 0) {
	        		double arc = 360.0 * count_s / count;
		        	if (arc < 360) {
		        		shape = drawSemiRing3(0, 0, r_outer, r_inner, 
		        				getFillColorBySeverity(severity), severity.getColor(), 
		        				angle, arc);
	        		} else {
	        			shape = drawCircle(r_inner, r_outer, severity.getColor(), getFillColorBySeverity(severity));
	        		}
	                getChildren().add(shape);
		            angle += arc;
	        	}
	        }
        }
        
        if (0 == angle) {
        	getChildren().add(drawCircle(r_inner, r_outer, Color.BLACK, Color.GRAY));
        }
	}
	
	public boolean canMerge(PointNode s, double range) {
		return Math.sqrt(
				Math.pow(getLayoutX() - s.getLayoutX(), 2) + Math.pow(getLayoutY() - s.getLayoutY(), 
				2)) < range;
//		return false;
	}
	
	public void merge(PointNode node) {
		for (Severity s : Severity.values()) {
			addCount(s, node.getCount(s));
		}
	}

	// Init
	// -----------------------------------------------------------------------------------------------------------------
	
	private void init() {
		r_d = R_D;
		r_k = R_K;
		rank_w = RANK_W;
		map = new HashMap<>();
	}
	
	// Properties
	// -----------------------------------------------------------------------------------------------------------------
		
	private double r_d;
	private double r_k;
	private double rank_w;
	private Map<Severity, Integer> map;

	private Text eventCount;
	private Font font;
	private double font_h;
	
	// Helpers
	// -----------------------------------------------------------------------------------------------------------------
	
	private Shape drawCircle(double r_inner, double r_outer, Color stroke_color, Color fill_color) {
		Circle innerCircle = new Circle(r_inner);
		Circle outerCircle = new Circle(r_outer);
		Shape ring = Path.subtract(outerCircle, innerCircle);
		ring.setStroke(stroke_color);
        ring.setFill(fill_color);
        return ring;
	}
	
	private Path drawSemiRing3(
			double centerX, double centerY, double radius, double innerRadius, Color bgColor, Color strkColor,
			double param_angle_from, double param_angle_arc
			) {
				
		double angle_from = Math.PI / 180.0 * param_angle_from;
		double angle = Math.PI / 180.0 * (param_angle_from + param_angle_arc);
		
		double x = centerX + Math.cos(angle_from) * innerRadius;
		double y = centerY - Math.sin(angle_from) * innerRadius;
		
        Path path = new Path();
        path.setFill(bgColor);
        path.setStroke(strkColor);
        path.setFillRule(FillRule.EVEN_ODD);

        MoveTo moveTo = new MoveTo();
        moveTo.setX(x);
        moveTo.setY(y);

        ArcTo arcToInner = new ArcTo();
        arcToInner.setXAxisRotation(50);
        double inner_x = centerX + Math.cos(angle) * innerRadius;
        double inner_y = centerY - Math.sin(angle) * innerRadius;
        arcToInner.setX(inner_x);
        arcToInner.setY(inner_y);
//        if (angle > Math.PI) arcToInner.setLargeArcFlag(true);
        if (param_angle_arc > 180) arcToInner.setLargeArcFlag(true);
        arcToInner.setRadiusX(innerRadius);
        arcToInner.setRadiusY(innerRadius);

        MoveTo moveTo2 = new MoveTo();
        moveTo2.setX(x);
        moveTo2.setY(y);

        LineTo hLineToRightLeg = new LineTo();
		x = centerX + Math.cos(angle_from) * radius;
		y = centerY - Math.sin(angle_from) * radius;
        hLineToRightLeg.setX(x);
        hLineToRightLeg.setY(y);

        ArcTo arcTo = new ArcTo();
        double outer_x = centerX + Math.cos(angle) * radius;
        double outer_y = centerY - Math.sin(angle) * radius;
        arcTo.setX(outer_x);
        arcTo.setY(outer_y);
//        if (angle > Math.PI) arcTo.setLargeArcFlag(true);
        if (param_angle_arc > 180) arcTo.setLargeArcFlag(true);
        arcTo.setRadiusX(radius);
        arcTo.setRadiusY(radius);

        LineTo hLineToLeftLeg = new LineTo();
        hLineToLeftLeg.setX(inner_x);
        hLineToLeftLeg.setY(inner_y);

        path.getElements().add(moveTo);
        path.getElements().add(arcToInner);
        path.getElements().add(moveTo2);
        path.getElements().add(hLineToRightLeg);
        path.getElements().add(arcTo);
        path.getElements().add(hLineToLeftLeg);
        
        path.setCursor(Cursor.HAND);

        return path;
    }
	
	private void initEventCountText() {
		if (null == eventCount) {
	        eventCount = new Text();
	        eventCount.setFill(Color.BLACK);
	        eventCount.setTextAlignment(TextAlignment.CENTER);
	        eventCount.setFont(font);
		}
		eventCount.setText(Long.toString(getCount()));
	}
	
	private Color getFillColorBySeverity(Severity severity) {
		Color color = severity.getColor();
		Color color_result = new Color(color.getRed(), color.getGreen(), color.getBlue(), 0.9);
		return color_result;
	}

}

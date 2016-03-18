package merge2d;

public class Point {

	public static final double DEFAULT_POINT_R = 2.0;
	
	public Point(Severity severity, double x, double y) {
		this.severity = severity;
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getR() {
		return DEFAULT_POINT_R;
	}
	public Severity getSeverity() {
		return severity;
	}
	public void setSeverity(Severity severity) {
		this.severity = severity;
	}
	
	private double x;
	private double y;
	private Severity severity;
	
}

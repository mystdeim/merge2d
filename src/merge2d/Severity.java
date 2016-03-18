package merge2d;

import javafx.scene.paint.Color;

public enum Severity {
	CRITICAL, HIGH, MEDIUM, LOW, INFO;
	
	public Color getColor() {
		switch (this) {
		case CRITICAL:
			return Color.web("#f26655ff").darker();
		case HIGH:
			return Color.web("#f26655ff");
		case MEDIUM:
			return Color.web("#ffbc00ff");
		case LOW:
			return Color.web("#68cad3ff");
		case INFO:
			return Color.web("#9ed392ff");
		default:
			return Color.GRAY;
		}
	}
}

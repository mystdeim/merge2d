package merge2d.app;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.layout.StackPane;
import merge2d.Generator;
import merge2d.Point;

public class ApplicationController implements Initializable {
	
	private static final int 	DEFAULT_COUNT = 100;
	
	// FXML Properties
	// -----------------------------------------------------------------------------------------------------------------
	@FXML private Button btnCreate;
	@FXML private StackPane drawingAnchor;
//	@FXML private Canvas drawingField;
	private ResizableCanvas newCanvas;
	@FXML private Spinner<Number> spinnerCount;
	@FXML private IntegerSpinnerValueFactory countValueFactory;
	
	// Handles
	// -----------------------------------------------------------------------------------------------------------------
	
	@FXML private void onCreate(ActionEvent event) {
		isDrawing = true;
		changeParams();
	}
	
	@FXML private void onClear(ActionEvent event) {
		isDrawing = false;
		listPoints.clear();
		drawPoint();
	}

	// Init
	// -----------------------------------------------------------------------------------------------------------------
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		countValueFactory.setValue(DEFAULT_COUNT);
		
		//
//		  drawingField.widthProperty().bind(drawingAnchor.widthProperty());
//        drawingField.heightProperty().bind(drawingAnchor.heightProperty());
//        drawingAnchor.widthProperty().addListener(evt -> changeSize());
//        drawingAnchor.heightProperty().addListener(evt -> changeSize());
//        drawingField.widthProperty().addListener(evt -> changeSize());
//        drawingField.heightProperty().addListener(evt -> changeSize());
		
		newCanvas = new ResizableCanvas();
		drawingAnchor.getChildren().add(newCanvas);
		newCanvas.widthProperty().bind(drawingAnchor.widthProperty());
        newCanvas.heightProperty().bind(drawingAnchor.heightProperty());
		newCanvas.widthProperty().addListener(evt -> changeSize());
		newCanvas.heightProperty().addListener(evt -> changeSize());
	}
	
	private void changeSize() {
//		System.out.printf("w: %0.2f; h: %0.2f \n", drawingField.getWidth(), drawingField.getHeight());
		System.out.printf("Anchor: w: %.2f; h: %.2f \n", drawingAnchor.getWidth(), drawingAnchor.getHeight());
		System.out.printf("Canvas: w: %.2f; h: %.2f \n", newCanvas.getWidth(), newCanvas.getHeight());
	}
	
	// Handles
	// -----------------------------------------------------------------------------------------------------------------
		
	private List<Point> listPoints = new ArrayList<>();
	private Generator generator = new Generator();
	private boolean isDrawing = false;
	
	// Helpers
	// -----------------------------------------------------------------------------------------------------------------
	
	private void changeParams() {
		if (isDrawing) {
			listPoints.addAll(generator.generate(
					spinnerCount.getValue().intValue(), 
					newCanvas.getWidth(), newCanvas.getHeight()));
			drawPoint();
		}
	}
	
	private void drawPoint() {
		final GraphicsContext gc = newCanvas.getGraphicsContext2D();
		Platform.runLater(() -> {
			gc.clearRect(0, 0, newCanvas.getWidth(), newCanvas.getHeight());
			listPoints.forEach(p -> {
				gc.setFill(p.getSeverity().getColor());
				gc.fillOval(p.getX() - p.getR(), p.getY() - p.getR(), 2*p.getR(), 2*p.getR());
			});
		});
	}
	
	// Classes
	// -----------------------------------------------------------------------------------------------------------------
	
	class ResizableCanvas extends Canvas {
		 
        public ResizableCanvas() {
//            widthProperty().addListener(evt -> draw());
//            heightProperty().addListener(evt -> draw());
        }
 
        @Override
        public boolean isResizable() {
            return true;
        }
 
        @Override
        public double prefWidth(double height) {
//            return getWidth();
        	return 0;
        }
 
        @Override
        public double prefHeight(double width) {
//            return getHeight();
        	return 0;
        }
    }
	
	
}

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
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.layout.StackPane;
import merge2d.Generator;
import merge2d.Point;

public class ApplicationController implements Initializable {
	
	private static final int 	DEFAULT_CLUSTERS = 5;
	private static final int 	DEFAULT_COUNT = 100;
	
	// FXML Properties
	// -----------------------------------------------------------------------------------------------------------------
	@FXML private Button btnCreate;
	@FXML private StackPane drawingAnchor;
	@FXML private Spinner<Number> spinnerCount;
	@FXML private IntegerSpinnerValueFactory countValueFactory;
	@FXML private Spinner<Number> spinnerClusters;
	@FXML private IntegerSpinnerValueFactory clustersValueFactory;
	
	// Handles
	// -----------------------------------------------------------------------------------------------------------------
	
	@FXML private void onCreate(ActionEvent event) {
		isDrawing = true;
		changeParams();
	}
	
	@FXML private void onClear(ActionEvent event) {
		isDrawing = false;
		listPoints.clear();
		draw();
	}

	// Init
	// -----------------------------------------------------------------------------------------------------------------
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		clustersValueFactory.setValue(DEFAULT_CLUSTERS);
//		spinnerClusters.setValueFactory(clustersValueFactory);
		countValueFactory.setValue(DEFAULT_COUNT);
		
		//
		drawingAnchor.getChildren().add(drawingField);
		drawingField.widthProperty().bind(drawingAnchor.widthProperty());
        drawingField.heightProperty().bind(drawingAnchor.heightProperty());
        drawingAnchor.widthProperty().addListener(evt -> changeSize());
        drawingAnchor.heightProperty().addListener(evt -> changeSize());
        drawingField.widthProperty().addListener(evt -> changeSize());
        drawingField.heightProperty().addListener(evt -> changeSize());		
	}
	
	private void changeSize() {
//		System.out.printf("w: %0.2f; h: %0.2f \n", drawingField.getWidth(), drawingField.getHeight());
		System.out.printf("Anchor: w: %.2f; h: %.2f \n", drawingAnchor.getWidth(), drawingAnchor.getHeight());
		System.out.printf("Canvas: w: %.2f; h: %.2f \n", drawingField.getWidth(), drawingField.getHeight());
		draw();
	}
	
	// Handles
	// -----------------------------------------------------------------------------------------------------------------

	private ResizableCanvas drawingField = new ResizableCanvas();
	private List<Point> listPoints = new ArrayList<>();
	private Generator generator = new Generator();
	private boolean isDrawing = false;
	
	// Helpers
	// -----------------------------------------------------------------------------------------------------------------
	
	private void changeParams() {
		if (isDrawing) {
			listPoints.addAll(generator.clusters(
					clustersValueFactory.getValue().intValue(),
					countValueFactory.getValue().intValue(), 
					drawingField.getWidth(), drawingField.getHeight()));
//			listPoints.addAll(generator.uniformly(
//					spinnerCount.getValue().intValue(), 
//					drawingField.getWidth(), drawingField.getHeight()));
			draw();
		}
	}
	
	private void draw() {
		final GraphicsContext gc = drawingField.getGraphicsContext2D();
		Platform.runLater(() -> {
			gc.clearRect(0, 0, drawingField.getWidth(), drawingField.getHeight());
			listPoints.forEach(p -> {
				gc.setFill(p.getSeverity().getColor());
				gc.fillOval(p.getX() - p.getR(), p.getY() - p.getR(), 2*p.getR(), 2*p.getR());
			});
		});
	}	
	
	// Classes
	// -----------------------------------------------------------------------------------------------------------------
	
	private class ResizableCanvas extends Canvas {
 
        @Override
        public boolean isResizable() {
            return true;
        }
 
        @Override
        public double prefWidth(double height) {
            return 0;
        }
 
        @Override
        public double prefHeight(double width) {
            return 0;
        }
    }
}

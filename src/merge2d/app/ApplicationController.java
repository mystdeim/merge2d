package merge2d.app;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.layout.StackPane;
import merge2d.Generator;
import merge2d.Merging;
import merge2d.PointNode;
import merge2d.Point;

public class ApplicationController implements Initializable {
	
	// FXML Properties
	// -----------------------------------------------------------------------------------------------------------------
	@FXML private Button btnCreate;
	@FXML private Button btnClear;
	@FXML private StackPane drawingAnchor;
	@FXML private Spinner<Integer> spinnerCount;
	private IntegerSpinnerValueFactory countValueFactory;
	@FXML private Spinner<Integer> spinnerClusters;
	private IntegerSpinnerValueFactory clustersValueFactory;
	
	@FXML private Button btnMerge;
	@FXML private Button btnUnmerge;
	@FXML private Button btnMergeClear;
	@FXML private Label lblStepsValue;
	@FXML private Label lblTimeValue;
	
	// Handles
	// -----------------------------------------------------------------------------------------------------------------
	
	@FXML 
	private void onCreate(ActionEvent event) {
		isDrawing = true;
		changeParams();
	}
	
	@FXML 
	private void onClear(ActionEvent event) {
		isDrawing = false;
		listPoints.clear();
		draw();
	}
	
	@FXML 
	private void onMerge(ActionEvent event) {
		List<PointNode> first_list = new ArrayList<>();
		for (Point p : listPoints) {
			PointNode pn = new PointNode();
			pn.setLayoutX(p.getX());
			pn.setLayoutY(p.getY());
			pn.addCount(p.getSeverity(), 1);
			first_list.add(pn);
		}
		merging.stupid(first_list);		
		setNodes(first_list);
		
		draw();
	}
	
	@FXML 
	private void onMerge1(ActionEvent event) {
		List<PointNode> first_list = new ArrayList<>();
		for (Point p : listPoints) {
			PointNode pn = new PointNode();
			pn.setLayoutX(p.getX());
			pn.setLayoutY(p.getY());
			pn.addCount(p.getSeverity(), 1);
			first_list.add(pn);
		}
		merging.merge1(first_list);		
		setNodes(first_list);
		
		draw();
	}
	
	@FXML 
	private void onMerge2(ActionEvent event) {
		List<PointNode> first_list = new LinkedList<>();
		for (Point p : listPoints) {
			PointNode pn = new PointNode();
			pn.setLayoutX(p.getX());
			pn.setLayoutY(p.getY());
			pn.addCount(p.getSeverity(), 1);
			first_list.add(pn);
		}
		merging.merge2(first_list);		
		setNodes(first_list);
		
		draw();
	}
	
	@FXML 
	private void onMergeClear(ActionEvent event) {
		Platform.runLater(() -> {
			nodesPane.getChildren().clear();
		});
	}

	// Init
	// -----------------------------------------------------------------------------------------------------------------
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		// Spinners
		clustersValueFactory = new IntegerSpinnerValueFactory(1, 100, 1);
		spinnerClusters.setValueFactory(clustersValueFactory);
		
		countValueFactory = new IntegerSpinnerValueFactory(1, 1_000_000, 100_000);
		spinnerCount.setValueFactory(countValueFactory);
		
		// Canvas
		drawingAnchor.getChildren().add(drawingField);
		drawingField.widthProperty().bind(drawingAnchor.widthProperty());
        drawingField.heightProperty().bind(drawingAnchor.heightProperty());
        drawingAnchor.widthProperty().addListener(evt -> changeSize());
        drawingAnchor.heightProperty().addListener(evt -> changeSize());
        drawingField.widthProperty().addListener(evt -> changeSize());
        drawingField.heightProperty().addListener(evt -> changeSize());		
        
        // Nodes pane
        nodesPane.setManaged(false); 
        drawingAnchor.getChildren().add(nodesPane);
        
//        nodesPane.setLayoutX(0);
//        nodesPane.setLayoutY(0);
	}
	
	private void changeSize() {
//		System.out.printf("w: %0.2f; h: %0.2f \n", drawingField.getWidth(), drawingField.getHeight());
		System.out.printf("A(%.2f; %.2f) C(%.2f; %.2f) N(%.2f; %.2f) \n", 
				drawingAnchor.getWidth(), drawingAnchor.getHeight(), 
				drawingField.getWidth(), drawingField.getHeight(),
				nodesPane.getLayoutBounds().getWidth(), nodesPane.getLayoutBounds().getHeight()
		);
		draw();
	}
	
	// Handles
	// -----------------------------------------------------------------------------------------------------------------

	private ResizableCanvas drawingField = new ResizableCanvas();
//	@FXML
	private Group nodesPane = new Group();
//	private Parent nodesPane = new Pane();
	private List<Point> listPoints = new ArrayList<>();
	private Generator generator = new Generator();
	private Merging merging = new Merging();
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
	
	private void setNodes(Collection<PointNode> nodes) {
		Platform.runLater(() -> {
			nodesPane.getChildren().clear();
			nodesPane.getChildren().addAll(nodes);
			nodes.forEach(n -> n.initShape());
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

	@FXML public void onMerge() {}
}

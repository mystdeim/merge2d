package merge2d.app;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage stage) {
		try {
//			BorderPane root = new BorderPane();
//			Scene scene = new Scene(root,400,400);
//			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
//			stage.setScene(scene);
//			stage.show();
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Application.fxml"));
	        final BorderPane root = loader.load();
//	        final ApplicationController controller = loader.getController();
	        
	        final Scene scene = new Scene(root, 1200, 1000);
	        stage.setScene(scene);
	        stage.setTitle("v0.1");
	        stage.show();
	        
	        
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

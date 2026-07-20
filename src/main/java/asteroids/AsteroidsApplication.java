package asteroids;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AsteroidsApplication extends Application {
    
    public static DoubleProperty width = new SimpleDoubleProperty(800);
    public static DoubleProperty height = new SimpleDoubleProperty(800);
    
    @Override
    public void start(Stage stage) {
        
        
        
        PaneView pane = new PaneView(width, height);
                
        Scene scene = new Scene(pane.getView());
        
        pane.setKeysFunction(scene);
        
        width.bind(scene.widthProperty());
        height.bind(scene.heightProperty());
        
        stage.setTitle("Asteroids");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
        System.out.println("Hello, world!");
    }

    public static int partsCompleted() {
        // State how many parts you have completed using the return value of this method
        return 4;
    }
}

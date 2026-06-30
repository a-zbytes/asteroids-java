package asteroids;

import asteroids.domain.Asteroid;
import asteroids.domain.LifeIcon;
import asteroids.domain.Projectile;
import asteroids.domain.Ship;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class AsteroidsApplication extends Application {
    
    public static DoubleProperty width = new SimpleDoubleProperty(400);
    public static DoubleProperty height = new SimpleDoubleProperty(800);
    
    @Override
    public void start(Stage stage) {
        
        PaneView pane = new PaneView(width, height);
        
        Scene scene = new Scene(pane.getView());
        
        Map<KeyCode, Boolean> pressedKeys = pane.getKeys();
        
        scene.setOnKeyPressed(event ->  {
            pressedKeys.put(event.getCode(), true);  
        });
        
        scene.setOnKeyReleased(event -> {
            pressedKeys.put(event.getCode(), false); 
        });
        

        scene.widthProperty().addListener((obs, oldV, newV) -> {
            width.set((double)newV);
        });
        
        scene.heightProperty().addListener((obs, oldV, newV) -> {
            height.set((double) newV);
        });
        
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
        return 0;
    }

}

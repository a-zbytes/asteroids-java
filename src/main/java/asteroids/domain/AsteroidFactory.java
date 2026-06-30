
package asteroids.domain;

import java.util.Random;
import javafx.scene.shape.Polygon;


public class AsteroidFactory {
    
    public Polygon create() {
        double baseRadius = 15;
        Polygon asteroid = new Polygon();
        
        Random random = new Random();
        int vertices = random.nextInt(5) + 5;
        
        for (int i = 0; i < vertices; i++) {
            double value = (2 * Math.PI / vertices) * i;
            
            double currentRadius = random.nextGaussian() * 5 + baseRadius;
            
            double x = Math.cos(value) * currentRadius;
            double y = Math.sin(value)* currentRadius;
            
            asteroid.getPoints().addAll(x,y);
        }
        
        return asteroid;
    }
}

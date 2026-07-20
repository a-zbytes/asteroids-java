
package asteroids.domain;


import javafx.scene.shape.Polygon;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;


public class AsteroidFactoryTest {
                
    @RepeatedTest(5)
    @DisplayName("creating an Asteroid should return a polygon with 5 to 9 sides")        
    void create_shouldReturnPolygonWithFiveToNineSides() {
        Polygon polygon = new AsteroidFactory().create();
        int sides = polygon.getPoints().size()/2;
        assertTrue(sides >= 5 && sides <= 9);
    }
    
}

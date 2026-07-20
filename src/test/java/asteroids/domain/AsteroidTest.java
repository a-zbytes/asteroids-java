
package asteroids.domain;

import javafx.geometry.Point2D;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


public class AsteroidTest {
    Asteroid asteroid;
    
    @BeforeEach
    void setUp() {
        asteroid = new Asteroid(100, 200, () -> 0);
    }
    
    
    @Test
    @DisplayName("constructor should initialize asteroid with correct position")
    void constructor_shouldInitializeAsteroidWithCorrectPosition(){
        assertAll(
            () -> assertEquals(100, asteroid.getCharacter().getTranslateX(), "x position should match constructor value"),
            () -> assertEquals(200, asteroid.getCharacter().getTranslateY(), "y position should match constructor value")
        );
    }
    
    @Nested
    @DisplayName("move")
    class Move {
        
        @Test
        @DisplayName("should move and rotate asteroid without gravity")
        void should_moveAndRotateAsteroidWithoutGravity() {
            Point2D currentMovement = asteroid.getMovement();
            
            double expectedX = 100 + currentMovement.getX();
            double expectedY = 200 + currentMovement.getY();
            
            double rotationBeforeMove = asteroid.getCharacter().getRotate();
            asteroid.move();
            
            assertAll(
                () -> assertNotEquals(rotationBeforeMove, asteroid.getCharacter().getRotate(), "asteroid should rotate after moving"),
                () -> assertEquals(expectedX, asteroid.getCharacter().getTranslateX(), 0.0001, "x position should match movement"),
                () -> assertEquals(expectedY, asteroid.getCharacter().getTranslateY(), 0.0001, "y position should match movement")
            );
        }
        
        @Test
        @DisplayName("should move and rotate asteroid with gravity")
        void should_moveAndRotateAsteroidWithGravity() {
            Asteroid asteroidWithGravity = new Asteroid(100, 200, () -> 3.0);
            Point2D movementBeforeMove = asteroidWithGravity.getMovement();
            
            double expectedX = 100 + movementBeforeMove.getX();
            double expectedY = 200 + movementBeforeMove.getY() + 3.0;
            
            double rotationBeforeMove = asteroidWithGravity.getCharacter().getRotate();
            asteroidWithGravity.move();
            
            assertAll(
                () -> assertNotEquals(rotationBeforeMove, asteroidWithGravity.getCharacter().getRotate(), "asteroid should rotate after moving"),
                () -> assertEquals(expectedX, asteroidWithGravity.getCharacter().getTranslateX(), 0.0001, "x position should match movement"),
                () -> assertEquals(expectedY, asteroidWithGravity.getCharacter().getTranslateY(), 0.0001, "y position should match movement with gravity")
            );
        }
    }
    
    @Test
    @DisplayName("get point should return asteroid point value")
    void getPoint_shouldReturnAsteroidPointValue() {
        assertEquals(1000, asteroid.getPoint());
    }
    
}

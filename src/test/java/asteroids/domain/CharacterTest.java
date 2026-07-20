
package asteroids.domain;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class CharacterTest {
    Character character;
    
    @BeforeEach
    void setUp() {
        Polygon polygon = new Polygon(-5, 5,
                                      -5, -5,
                                       10, 0);
        character = new Character(polygon, 100, 200);
    }
    
    @Test
    @DisplayName("constructor should initialize character with correct position and state")
    void constructor_shouldInitializeCharacterWithCorrectPositionAndState() {
        assertAll(
            () -> assertEquals(100, character.getCharacter().getTranslateX(), "x position should match constructor value"),
            () -> assertEquals(200, character.getCharacter().getTranslateY(), "y position should match constructor value"),
            () -> assertEquals(Point2D.ZERO, character.getMovement(), "character movement should start at zero"),
            () -> assertTrue(character.isAlive(), "character should be alive")
        );
    }
    
    @Test
    @DisplayName("get character should return the original polygon")
    void getCharacter_shouldReturnOriginalPolygon() {
        Polygon samePolygon = new Polygon(-5, 5,
                                      -5, -5,
                                       10, 0);
        Character sameCharacter = new Character(samePolygon, 100, 200);
        
        assertSame(samePolygon, sameCharacter.getCharacter());
    }
    
    
    
    @Nested
    @DisplayName("move")
    class Move {
        
        @Test
        @DisplayName("should not move the character when movement is zero")
        void shouldNotMoveTheCharacter_whenMovementIsZero() {
            character.move();
            assertAll(
                () -> assertEquals(100, character.getCharacter().getTranslateX(), "x position should be unchanged"),
                () -> assertEquals(200, character.getCharacter().getTranslateY(), "y position should be unchanged")
            );
        }
        
        @Test
        @DisplayName("should move the character based on movement vector")
        void shouldMoveCharacter_whenMovementVectorIsSet() {
            character.setMovement(new Point2D(10,20));
            character.move();
            assertAll(
                () -> assertEquals(110, character.getCharacter().getTranslateX(), "x position should match movement"),
                () -> assertEquals(220, character.getCharacter().getTranslateY(), "y position should match movement")
            );
        }
    }
    
    @Nested
    @DisplayName("warp")
    class Warp {
        
        @Test
        @DisplayName("should wrap character to the left when it passes the right boundary")
        void shouldWrapCharacterToLeft_whenPassingRightBoundary() {
            character.getCharacter().setTranslateX(810);
            character.warp();
            assertAll(
                () -> assertEquals(10, character.getCharacter().getTranslateX(), "x position should wrap to the left side"),
                () -> assertEquals(200, character.getCharacter().getTranslateY(), "y position should be unchanged")
            ); 
        }
        
        @Test
        @DisplayName("should wrap character to the right when it passes the left boundary")
        void shouldWrapCharacterToRight_whenPassingLeftBoundary() {
            character.getCharacter().setTranslateX(-5);
            character.warp();
            assertAll(
                () -> assertEquals(795, character.getCharacter().getTranslateX(), "x position should wrap to the right side"),
                () -> assertEquals(200, character.getCharacter().getTranslateY(), "y position should be unchanged")
            );
        }
        
        @Test
        @DisplayName("should wrap character to the top when it passes the bottom boundary")
        void shouldWrapCharacterToTop_whenPassingBottomBoundary() {
            character.getCharacter().setTranslateY(850);
            character.warp();
            assertAll(
                () -> assertEquals(100, character.getCharacter().getTranslateX(), "x position should be unchanged"),
                () -> assertEquals(50, character.getCharacter().getTranslateY(), "y position should wrap to the top side")
            );
        }
        
        @Test
        @DisplayName("should wrap character to the bottom when it passes the top boundary")
        void shouldWrapCharacterToBottom_whenPassingTopBoundary() {
            character.getCharacter().setTranslateY(-200);
            character.warp();
            assertAll(
                () -> assertEquals(100, character.getCharacter().getTranslateX(), "x position should be unchanged"),
                () -> assertEquals(600, character.getCharacter().getTranslateY(), "y position should wrap to the bottom side")
            );
        }
    }
    
    @Test
    @DisplayName("turn left should rotate character to the left")
    void turnLeft_shouldRotateCharacterLeft(){
        double initialRotation = character.getCharacter().getRotate();
        double expected = initialRotation - 5;
        
        character.turnLeft();
        assertEquals(expected, character.getCharacter().getRotate(), 0.001);
    }
    
    @Test
    @DisplayName("turn right should rotate character to the right")
    void turnRight_shouldRotateCharacterRight(){
        double initialRotation = character.getCharacter().getRotate();
        double expected = initialRotation + 5;
        
        character.turnRight();
        assertEquals(expected, character.getCharacter().getRotate(), 0.001);
    }
    
    @Nested
    @DisplayName("accelerate")
    class Accelerate {
        
        @Test
        @DisplayName("should add acceleration to movement vector when at rest")
        void should_addAccelerationToMovementVector_WhenAtRest() {
            Point2D initialMovement = character.getMovement();
            
            double value = character.getCharacter().getRotate();
            double x = Math.cos(Math.toRadians(value));
            double y = Math.sin(Math.toRadians(value));
            
            Point2D expectedMovement = initialMovement.add(new Point2D(x,y).multiply(0.05));
            character.accelerate();
            assertEquals(expectedMovement, character.getMovement());
        }
        
        @Test
        @DisplayName("should add acceleration to existing movement vector")
        void should_addAccelerationToExistingMovementVector() {
            Point2D newMovement = new Point2D(10,20);
            character.setMovement(newMovement);
            
            double value = character.getCharacter().getRotate();
            double x = Math.cos(Math.toRadians(value));
            double y = Math.sin(Math.toRadians(value));
            
            Point2D expectedMovement = newMovement.add(new Point2D(x,y).multiply(0.05));
            character.accelerate();
            assertEquals(expectedMovement, character.getMovement());
        }
    }
    
    @Nested
    @DisplayName("collide")
    class Collide {
        
        Character otherCharacter;
        Ship ship;
        
        @BeforeEach
        void collisionSetUp() {
            Polygon polygon = new Polygon(-10, 10,
                                      -10, -10,
                                       20, 0);
            otherCharacter = new Character(polygon, 100, 200);
            ship = new Ship(100,200);
        }
        
        @Test
        @DisplayName("collide should return true when two characters collide")
        void collide_shouldReturnTrueWhenTwoCharactersCollide() {
            assertTrue(character.collide(otherCharacter));
        }
        
        @Test
        @DisplayName("collide should return false when two characters do not collide")
        void collide_shouldReturnFalseWhenTwoCharactersDoNotCollide() {
            character.getCharacter().setTranslateX(300);
            character.getCharacter().setTranslateY(500);
            
            assertFalse(character.collide(otherCharacter));
        }
        
        @Test
        @DisplayName("collide should return false when ship is invincible")
        void collide_shouldReturnFalseWhenShipIsInvincible() {
            ship.respawn(100, 200);
            assertFalse(character.collide(ship));
        }
        
        @Test
        @DisplayName("collide should return true when ship is vulnerable and colliding")
        void collide_shouldReturnTrueWhenShipIsVulnerableAndColliding() {
            assertTrue(ship.collide(character));
        }
    }
    
    @Test
    @DisplayName("set movement should update character movement")
    void setMovement_shouldUpdateCharacterMovement() {
        Point2D newMovement = new Point2D(2,5);
        character.setMovement(newMovement);
        
        assertEquals(newMovement, character.getMovement());
    }
    
    @Test
    @DisplayName("set alive should update character alive state")
    void setAlive_shouldUpdateCharacterAliveState() {
        character.setAlive(false);
        assertFalse(character.isAlive());
    }
    
    @Test
    @DisplayName("get point should return character point value")
    void getPoint_shouldReturnCharacterPointValue() {
        assertEquals(0, character.getPoint());
    }
    
    
}

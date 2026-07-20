
package asteroids.domain;

import javafx.geometry.Point2D;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


public class ShipTest {
    Ship ship;
    
    @BeforeEach
    void setUp() {
        ship = new Ship(100,200);
    }
    
    @Test
    @DisplayName("Constructor should initialize ship with correct position and rotation")        
    void constructor_shouldInitializeShipWithCorrectPositionAndRotation() {
        
        assertAll(
                () -> assertEquals(-90, ship.getCharacter().getRotate(), "ship rotation should be -90 degrees"),
                () -> assertEquals(100, ship.getCharacter().getTranslateX(), "x position should match constructor value"),
                () -> assertEquals(200, ship.getCharacter().getTranslateY(), "y position should match constructor value")
        );      
    }
    
    @Test
    @DisplayName("respawning should reset the ship's state")
    void respawn_shouldResetShipState(){
        ship.respawn(200, 300);
        assertAll(
                () -> assertEquals(-90, ship.getCharacter().getRotate(), "ship angle should be -90 degrees"),
                () -> assertEquals(200, ship.getCharacter().getTranslateX(), "x position should match respawn value"),
                () -> assertEquals(300, ship.getCharacter().getTranslateY(), "y position should match respawn value"),
                () -> assertEquals(Point2D.ZERO, ship.getMovement(), " movement should reset to 0"),
                () -> assertTrue(ship.isInvincible(), "ship should become invincible after respawn"),
                () -> assertTrue(ship.isAlive(), "ship should be alive after respawn")
        );
    }
       
   
    @Nested
    @DisplayName("Updating Invincibility")        
    class UpdateInvincibility {
        @BeforeEach
        void nestedSetUp() {
            ship.respawn(200, 300);
        }
        
        @Test
        @DisplayName("should show ship normally during first half of blink cycle")
        void shouldShowShipNormally_duringFirstHalfOfBlinkCycle() {
            ship.updateInvincibility(100_000_000L);
            assertEquals(1.0, ship.getCharacter().getOpacity(), 0.01);
        }
        
        @Test
        @DisplayName("should make ship transparent during second half of blink cycle")
        void shouldMakeShipTransparent_duringSecondHalfOfBlinkCycle() {
            ship.updateInvincibility(300_000_000L);
            assertEquals(0.3, ship.getCharacter().getOpacity(), 0.01);
        }
        
        @Test
        @DisplayName("should disable invicibilty after three seconds")
        void shouldDisableInvicibilty_afterThreeSeconds() {
            ship.updateInvincibility(System.nanoTime() + 4_000_000_000L);
            assertFalse(ship.isInvincible());
        }
    }
     
}

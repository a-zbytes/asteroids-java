
package asteroids.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class ProjectileTest {
    
    @Test
    @DisplayName("constructor should initialize projectile with ship's position and rotation")
    void constructor_shouldInitializeProjectileWithShipsPositionAndRotation() {
        Ship ship = new Ship(100,200);
        ship.getCharacter().setRotate(30);
        
        Projectile projectile = new Projectile(ship);
        
        assertAll(
            () -> assertEquals(30, projectile.getCharacter().getRotate(), "projectile rotation should match ship's"),
            () -> assertEquals(100, projectile.getCharacter().getTranslateX(), "projectile x position should match ship's"),
            () -> assertEquals(200, projectile.getCharacter().getTranslateY(), "projectile y position should match ship's")
        );
    }
}


package asteroids.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LifeIconTest {
    
    @Test
    @DisplayName("constructor should initialize life icon with correct position and rotation")
    void constructor_shouldInitializeLifeIconWithCorrectPositionAndRotation() {
        LifeIcon lifeIcon = new LifeIcon(100,200);
        assertAll(
            () -> assertEquals(-90, lifeIcon.getCharacter().getRotate(), "life icon rotation should be -90 degrees"),
            () -> assertEquals(100, lifeIcon.getCharacter().getTranslateX(), "x position should match constructor value"),
            () -> assertEquals(200, lifeIcon.getCharacter().getTranslateY(), "y position should match constructor value")
        );
    }
}

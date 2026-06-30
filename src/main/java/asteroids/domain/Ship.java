
package asteroids.domain;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;


public class Ship extends Character {
    public boolean invincible;
    public long invincibilityTime;
    
    public Ship(double x, double y) {
        super(new Polygon(-5, 5,
                          -5, -5,
                          10, 0), x, y);
        
        super.getCharacter().setRotate(-90);
    }
    
    public void respawn(double x, double y) {
        getCharacter().setRotate(-90);
        getCharacter().setTranslateX(x);
        getCharacter().setTranslateY(y);
        this.setMovement(Point2D.ZERO);
        this.invincible = true;
        this.invincibilityTime = System.nanoTime() + 3_000_000_000L;
        
        this.setAlive(true);
    }
    
    public boolean isInvincible() {
        return invincible;
    }
    
    public void updateInvincibility(long currentNanoTime) {
        if (invincible) {
            long milliseconds = currentNanoTime/1_000_000L;
            long cycleTime = milliseconds % 500;
            if (cycleTime < 250) {
                getCharacter().setOpacity(1.0);
            } else {
                getCharacter().setOpacity(0.3);
            }
        
            if (currentNanoTime > invincibilityTime) {
                invincible = false;
                getCharacter().setOpacity(1.0);
            }
        }    
    }
}

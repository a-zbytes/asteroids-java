
package asteroids.domain;

import javafx.scene.shape.Polygon;


public class LifeIcon extends Character{
    
    public LifeIcon(double x, double y) {
        super(new Polygon (8, 0,
                          -6, -5,
                          -6, 5), x, y);
        
        super.getCharacter().setRotate(-90);
    }
}

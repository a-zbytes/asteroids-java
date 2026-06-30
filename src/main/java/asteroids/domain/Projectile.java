
package asteroids.domain;

import javafx.scene.shape.Polygon;


public class Projectile extends Character {
   
    public Projectile(Ship ship) {
        super(new Polygon(-2, -2, 
                           2, -2, 
                           2, 2, 
                          -2, 2), ship.getCharacter().getTranslateX(), ship.getCharacter().getTranslateY());
        
        super.getCharacter().setRotate(ship.getCharacter().getRotate());
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asteroids.domain;

import javafx.scene.shape.Polygon;

/**
 *
 * @author user
 */
public class LifeIcon extends Character{
    
    public LifeIcon(double x, double y) {
        super(new Polygon (8, 0,
                          -6, -5,
                          -6, 5), x, y);
        
        super.getCharacter().setRotate(-90);
    }
}

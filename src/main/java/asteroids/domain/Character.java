
package asteroids.domain;

import asteroids.AsteroidsApplication;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class Character {
    private Polygon character;
    private Point2D movement;
    private boolean alive;
    
    public Character(Polygon character, double x, double y) {
        this.character = character;
        character.setTranslateX(x);
        character.setTranslateY(y);
        this.movement = new Point2D(0,0);
        
        this.alive = true;
    }
    
    public Polygon getCharacter() {
        return character;
    }
    
    public void move() {
        character.setTranslateX(character.getTranslateX() + movement.getX());
        character.setTranslateY(character.getTranslateY() + movement.getY());
        warp();
    }
    
    public void warp() {
        double x = character.getTranslateX();
        double y = character.getTranslateY();
        double width = AsteroidsApplication.width.get();
        double height = AsteroidsApplication.height.get();
        
        if (x > width) {
            character.setTranslateX(x % width);
            
        } else if (x < 0) {
            double value = ((x % width) + width) % width;
            character.setTranslateX(value);
        }
        
        if (y > height) {
            character.setTranslateY(y % height);
            
        } else if (y < 0) {
            double value = ((y % height) + height) % height;
            character.setTranslateY(value);
        }
    }
    
    public void turnLeft() {
        character.setRotate(character.getRotate() - 5);
    }
    
    
    public void turnRight() {
        character.setRotate(character.getRotate() + 5);
    }
    
    public void accelerate() {
        double value = character.getRotate();
        double x = Math.cos(Math.toRadians(value));
        double y = Math.sin(Math.toRadians(value));
        
        movement = movement.add(new Point2D(x, y).multiply(0.05));
    }
    
    public boolean collide(Character other) {
        if (this instanceof Ship && ((Ship)this).isInvincible()) {
            return false;
        }
        
        if (other instanceof Ship && ((Ship)other).isInvincible()) {
            return false;
        }
        
        Shape collisionArea = Shape.intersect(character, other.getCharacter());
        return collisionArea.getBoundsInLocal().getWidth() > 0;  
    }
    
    public void setAlive(boolean value) {
        this.alive = value;
    }
    
    public boolean isAlive() {
        return this.alive;
    }
    
    public Point2D getMovement() {
        return movement;
    }
    
    public void setMovement(Point2D movement) {
        this.movement = movement;
    }
    
}

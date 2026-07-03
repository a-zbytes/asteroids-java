
package asteroids.domain;

import java.util.Random;
import java.util.function.DoubleSupplier;


public class Asteroid extends Character {
    private double rotation;
    private DoubleSupplier gravitySupplier;
    
    public Asteroid(double x, double y, DoubleSupplier gravitySupplier) {
        super(new AsteroidFactory().create() , x, y);
        this.gravitySupplier = gravitySupplier;
        
        Random random = new Random();
        
        super.getCharacter().setRotate(random.nextInt(360));
        rotation = random.nextDouble() - 0.5;
        
        double accelerateBy = random.nextInt(10) + 1;
        
        for (int i = 0; i < accelerateBy; i++) {
            accelerate();
        }
    }
    
    @Override
    public void move() {
        super.move();
        super.getCharacter().setTranslateY(getCharacter().getTranslateY() + gravitySupplier.getAsDouble());
        super.getCharacter().setRotate(getCharacter().getRotate() + rotation);
    }
    
    @Override
    public int getPoint() {
        return 1000;
    }
}

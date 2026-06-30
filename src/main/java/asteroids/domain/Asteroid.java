
package asteroids.domain;

import java.util.Random;


public class Asteroid extends Character {
    private double rotation;
    
    public Asteroid(double x, double y) {
        super(new AsteroidFactory().create() , x, y);
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
        super.getCharacter().setTranslateY(getCharacter().getTranslateY() + 0);
        super.getCharacter().setRotate(getCharacter().getRotate() + rotation);
    }
}


package asteroids;

import asteroids.domain.Asteroid;
import asteroids.domain.LifeIcon;
import asteroids.domain.Projectile;
import asteroids.domain.Ship;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class PaneView {
    private Pane pane;
    private Map<KeyCode, Boolean> pressedKeys;
    private DoubleProperty width;
    private DoubleProperty height;
    
    public PaneView(DoubleProperty width, DoubleProperty height) {
        this.width = width;
        this.height = height;
    }
    
    public Parent getView() {
        
        pane = new Pane();
        pane.setPrefSize(width.get(), height.get());
        
        pressedKeys = new HashMap<>();
        
        Ship ship = new Ship(width.get()/2, height.get()/2);
        pane.getChildren().add(ship.getCharacter());
        
        List<Asteroid> asteroids = new ArrayList<>();
        Random random = new Random();
        
        List<Projectile> projectiles = new ArrayList<>();
        
        Text pointText = new Text(10, 20, "Points: 0");
        pointText.setFont(Font.font(null, FontWeight.EXTRA_BOLD, 15));

        AtomicInteger points = new AtomicInteger();
        
        pane.getChildren().add(pointText);
        
        List<LifeIcon> lifeIcons = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            LifeIcon lifeIcon = new LifeIcon((17 + (i * 15)), 40);
            lifeIcons.add(lifeIcon);
            pane.getChildren().add(lifeIcon.getCharacter());
        }
        
        
        new AnimationTimer() {
            long previous;
            long constant = 100_000_000L;
            boolean scoreChanged;
            
            public void handle(long now) {
                
                ship.updateInvincibility(System.nanoTime());
                asteroids.forEach(asteroid -> {
                    if (ship.collide(asteroid)) {
                        ship.setAlive(false);
                        asteroid.setAlive(false);
                    }
                });
                
                if (!ship.isAlive()) {
                    
                    if (lifeIcons.size() > 0) {
                        LifeIcon lifeIcon = lifeIcons.remove(lifeIcons.size() - 1);
                        pane.getChildren().remove(lifeIcon.getCharacter());
                    }
                    
                    if (lifeIcons.size() > 0) {
                        projectiles.forEach(projectile -> projectile.setAlive(false));
                        asteroids.forEach(asteroid -> {
                            if (!asteroid.isAlive()) {
                                pane.getChildren().remove(asteroid.getCharacter());
                            }        
                        });
                        
                        asteroids.removeIf(asteroid -> !asteroid.isAlive());
                        ship.respawn(width.get()/2, height.get()/2);
                        
                    } else {
                        gameOver();
                        stop();
                        return;
                    }   
                }
                
                projectiles.forEach(projectile -> {
                    asteroids.forEach(asteroid -> {
                        if (projectile.collide(asteroid)) {
                            projectile.setAlive(false);
                            asteroid.setAlive(false);
                        }
                    });
                });
                
                projectiles.stream()
                        .filter(projectile -> !projectile.isAlive())
                        .forEach(projectile -> pane.getChildren().remove(projectile.getCharacter()));
                
                asteroids.stream()
                        .filter(asteroid -> !asteroid.isAlive())
                        .forEach(asteroid -> {
                            points.addAndGet(1000);
                            scoreChanged = true;
                            pane.getChildren().remove(asteroid.getCharacter());
                });
                
                if (scoreChanged) {
                    pointText.setText("Points: " + points);
                    scoreChanged = false;
                }
                
                projectiles.removeIf(projectile -> !projectile.isAlive());
                asteroids.removeIf(asteroid -> !asteroid.isAlive());
                
                if (Math.random() < 0.005) {
                    Asteroid asteroid = new Asteroid(random.nextInt(width.intValue()), random.nextInt(height.intValue()/3));
                    if (!(asteroid.collide(ship))) {
                        asteroids.add(asteroid);
                        pane.getChildren().add(asteroid.getCharacter());
                    }
                }
                
                if ((pressedKeys.getOrDefault(KeyCode.SPACE, Boolean.FALSE)) && (projectiles.size() < 3) && (now - previous > constant)) {
                    Projectile projectile = new Projectile(ship);
                        
                    projectile.accelerate();
                    projectile.setMovement(projectile.getMovement().normalize().multiply(3));
                        
                    projectiles.add(projectile);
                    pane.getChildren().add(projectile.getCharacter());
                    this.previous = now;
                }
                
                
                if (pressedKeys.getOrDefault(KeyCode.LEFT, Boolean.FALSE)) {
                    ship.turnLeft();
                }
                
                if (pressedKeys.getOrDefault(KeyCode.RIGHT, Boolean.FALSE)) {
                    ship.turnRight();
                }
                
                if (pressedKeys.getOrDefault(KeyCode.UP, Boolean.FALSE)) {
                    ship.accelerate();
                }
                
                ship.move();
                asteroids.forEach(asteroid -> asteroid.move());
                projectiles.forEach(projectile -> projectile.move());
            }
        }.start();
        
        return pane;
    }
    
    public Map<KeyCode, Boolean> getKeys() {
        return pressedKeys;
    } 
    
    public void gameOver() {
        Text text = new Text("Game Over");
        text.setFont(Font.font(null, FontWeight.EXTRA_BOLD, 30));
        text.setTextOrigin(VPos.CENTER);
        text.setY(height.get()/2);
        text.setWrappingWidth(width.get());
        text.setTextAlignment(TextAlignment.CENTER);
        text.setX(0);
        pane.getChildren().add(text);
    }
}

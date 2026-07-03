
package asteroids;

import asteroids.domain.Asteroid;
import asteroids.domain.GameState;
import asteroids.domain.LifeIcon;
import asteroids.domain.Projectile;
import asteroids.domain.Character;
import asteroids.domain.Ship;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.DoubleSupplier;
import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class PaneView {
    private Pane pane;
    private DoubleProperty width;
    private DoubleProperty height;
    private GameState state;
    private Text text;
    private Random random;
    private Button restart;
    private Map<KeyCode, Boolean> pressedKeys;
    private Ship ship;
    private List<Projectile> projectiles;
    private List<Asteroid> asteroids;
    List<LifeIcon> lifeIcons;
    private Text pointText;
    private AtomicInteger points;
    boolean gravity;
    private Button gravityButton;
    private DoubleSupplier gravitySupplier;
    private double gravityValue;
    
    
    
    public PaneView(DoubleProperty width, DoubleProperty height) {
        this.width = width;
        this.height = height;
        
        pane = new Pane();
        
        random = new Random();
        pressedKeys = new HashMap<>();
        projectiles = new ArrayList<>();
        asteroids = new ArrayList<>();
        lifeIcons = new ArrayList<>();
        
        text = new Text("");
        
        pointText = new Text(10, 20, "Points: 0");
        
        points = new AtomicInteger();
        
        restart = new Button("Restart");
        
        gravityButton = new Button("Gravity: Off");
        
        gravitySupplier = () -> gravityValue;
        
        createShip();
    }
    
    public Parent getView() {
        
        pane.setPrefSize(width.get(), height.get());
        
        pointText.setFont(Font.font(null, FontWeight.EXTRA_BOLD, 15));
        
        pane.getChildren().add(pointText);
        
        showStatus();
        setState(GameState.START);
        
        createLifeIcons();
                
        restart.setFocusTraversable(false);
        restart.setOpacity(0.7);
        restart.setFont(Font.font(null, FontWeight.BOLD, 15));
        
        restart.setOnAction(event -> {
            restart();
        });
        
        gravityButton.setFocusTraversable(false);
        gravityButton.setOpacity(0.7);
        
        gravityButton.setOnAction(event -> {
            if (state == GameState.START) {
                
                if (gravity) {
                    gravity = false;
                    gravityValue = 0.0;
                    gravityButton.setText("Gravity: Off");
                    
                } else {
                    gravity = true;
                    gravityValue = 0.1;
                    gravityButton.setText("Gravity: On");
                }
            }
            
        });
        
        VBox vbox = new VBox(5);
        vbox.getChildren().addAll(restart, gravityButton);
        vbox.setAlignment(Pos.TOP_RIGHT);
        
        HBox hbox = new HBox();
        hbox.prefWidthProperty().bind(pane.widthProperty());
        hbox.setAlignment(Pos.TOP_RIGHT);
        hbox.getChildren().add(vbox);
        pane.getChildren().add(hbox);
             
        
        new AnimationTimer() {
            long previous;
            long constant = 100_000_000L;
            boolean scoreChanged;
            int wave = 1;
            long waveBlink;
            double spawnIncrease;
            
            public void handle(long now) {
                
                if(state == GameState.GAME_OVER) {
                    return;
                }
                
                if (state == GameState.GAME_WON) {
                    return;
                }
                
                if(pressedKeys.getOrDefault(KeyCode.ENTER, false)) {
                    if (state == GameState.START) {
                        setState(GameState.PLAYING);
                        spawnAsteroids(5, asteroids, ship);
                    }
                }
                
                if (state == GameState.START) {
                    return;
                }
                
                if (state == GameState.PAUSE) {
                    return;
                }
                
                if (ship.isInvincible()) {
                    ship.updateInvincibility(now);
                }
                
                if (state == GameState.WAVE_CLEAR) {
                    boolean delay = waveDelay(waveBlink, now);
                    if (delay) {
                        return;
                    }
                    
                    setState(GameState.PLAYING);
                    spawnAsteroids(5 + (wave -1), asteroids, ship);
                }
                
                projectiles.forEach(projectile -> {
                    asteroids.forEach(asteroid -> {
                        if (projectile.collide(asteroid)) {
                            projectile.setAlive(false);
                            asteroid.setAlive(false);
                        }
                    });
                });
                
                asteroids.forEach(asteroid -> {
                    if (!asteroid.isAlive()) {
                        points.addAndGet(asteroid.getPoint());
                        scoreChanged = true;
                    }
                });
                
                purgeDead(projectiles);
                purgeDead(asteroids);
                
                if (scoreChanged) {
                    pointText.setText("Points: " + points);
                    scoreChanged = false;
                }
                                
                if (asteroids.isEmpty()) {
                    if (wave < 10) {
                        wave++;
                        spawnIncrease = Math.min((wave - 1) * 0.0017, 0.015);
                        purgeAll(asteroids);
                        purgeAll(projectiles);
                        setState(GameState.WAVE_CLEAR);
                        text.setText("Wave " + wave);
                        waveBlink = now + 3_000_000_000L;
                        ship.setMovement(Point2D.ZERO);
                    
                        if (gravity) {
                            gravityValue += 0.1;
                        }
                        
                    } else {
                        setState(GameState.GAME_WON);
                    }
                    return;
                }
                
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
                        purgeAll(projectiles);
                        purgeDead(asteroids);
                        
                        ship.respawn(width.get()/2, height.get()/2);
                        
                    } else {
                        setState(GameState.GAME_OVER);
                        return;
                    }   
                }

                if (Math.random() < 0.005 + spawnIncrease) {
                    Asteroid asteroid = createAsteroid();
                    if (!(asteroid.collide(ship))) {
                        asteroids.add(asteroid);
                        pane.getChildren().add(asteroid.getCharacter());
                    }
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
                move(asteroids);
                move(projectiles);
            }
        }.start();
        
        return pane;
    }
    
    public void purgeDead(List<? extends Character> characters) {
        characters.stream()
                .filter(character -> !character.isAlive())
                .forEach(character -> {
                    pane.getChildren().remove(character.getCharacter());
                }); 
        characters.removeIf(character -> !character.isAlive());
    }
    
    public void purgeAll(List<? extends Character> characters) {
        characters.forEach(character -> pane.getChildren().remove(character.getCharacter()));
        characters.clear();
    }
    
    public void move(List<? extends Character> characters) {
        characters.forEach(character -> character.move());
    }
    
    public void showStatus() {
        text.setText("");
        text.setFont(Font.font(null, FontWeight.EXTRA_BOLD, 30));
        
        StackPane stack = new StackPane(text);
        stack.prefWidthProperty().bind(pane.widthProperty());
        stack.prefHeightProperty().bind(pane.heightProperty());
        StackPane.setMargin(text, new Insets(0,0,60,0));
        
        pane.getChildren().add(stack);
    }
    
    public void setState(GameState state) {
        this.state = state;
        text.setText(state.getMessage());
    }
    
    public Asteroid createAsteroid() {
        return new Asteroid(random.nextInt(width.intValue()), random.nextInt(height.intValue()/3), gravitySupplier);
    }
    
    public void spawnAsteroids(int count, List<Asteroid> asteroids, Ship ship) {
        for (int i = 0; i < count; i++) {
            Asteroid asteroid = createAsteroid();
            while (asteroid.collide(ship)) {
                asteroid = createAsteroid();
            }
            asteroids.add(asteroid);
            pane.getChildren().add(asteroid.getCharacter());
        }
    }
    
    public boolean waveDelay(long waveBlink, long now) {
        if (waveBlink > now) {
            long milliseconds = now/1_000_000L;
            long cycleTime = milliseconds % 500;
            
            if (cycleTime < 250) {
                text.setOpacity(0.3);
            } else {
                text.setOpacity(1.0);
            }
            return true;
        } else {
            return false;
        } 
    }
    
    public void shoot(Ship ship, List<Projectile> projectiles) {
        if (projectiles.size() < 3) {
            Projectile projectile = new Projectile(ship);
                        
            projectile.accelerate();
            projectile.setMovement(projectile.getMovement().normalize().multiply(3));
                        
            projectiles.add(projectile);
            pane.getChildren().add(projectile.getCharacter());
        }
    }

    public void playOrPause() {
        if (state == GameState.PLAYING) {
            setState(GameState.PAUSE);
            
        } else if (state == GameState.PAUSE) {
            setState(GameState.PLAYING);
        }
    }
    
    public void setRestart(Button restart) {
        this.restart = restart;
    }
    
    public void setKeysFunction(Scene scene) {
        scene.setOnKeyPressed(event ->  {
            KeyCode code = event.getCode();
            
            boolean isFresh = !pressedKeys.getOrDefault(code, false);
            
            if (isFresh) {
                if (code == KeyCode.SPACE) {
                    shoot(ship, projectiles);
                    
                } else if (code == KeyCode.ENTER) {
                    playOrPause();
                } 
            }
            pressedKeys.put(event.getCode(), true);  
        });
        
        scene.setOnKeyReleased(event -> {
            pressedKeys.put(event.getCode(), false); 
        });
    }
    
    public void restart() {
        purgeAll(asteroids);
        purgeAll(projectiles);
        purgeAll(lifeIcons);
        
        pane.getChildren().remove(ship.getCharacter());
        pressedKeys.clear();

        createShip();
        createLifeIcons();
        
        if (gravity) {
            gravityValue = 0.1;
        } else {
            gravityValue = 0.0;
        }
        
        pointText.setText("Points: 0");
        points.set(0);
        setState(GameState.START);
    }
    
    public void createShip() {
        ship = new Ship(width.get()/2, height.get()/2);
        pane.getChildren().add(ship.getCharacter());
    }
    
    public void createLifeIcons() {
        lifeIcons.clear();
        for (int i = 0; i < 3; i++) {
            LifeIcon lifeIcon = new LifeIcon((17 + (i * 15)), 40);
            lifeIcons.add(lifeIcon);
            pane.getChildren().add(lifeIcon.getCharacter());
        }
    }
}

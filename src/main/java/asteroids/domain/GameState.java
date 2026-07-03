
package asteroids.domain;


public enum GameState {
    START("Press ENTER to Start"),
    PLAYING(),
    PAUSE("Game Paused, Press ENTER to Resume"),
    WAVE_CLEAR(),
    GAME_OVER("Game Over"),
    GAME_WON("CRITICAL ERROR: Space Defended. you broke the simulation :)");
    
    private String message;
    
    GameState(String message) {
        this.message = message;
    }
    
    GameState() {
        this.message = "";
    }
    
    public String getMessage() {
        return message;
    }
   
}

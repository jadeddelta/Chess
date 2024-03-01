import engine.MEngine;
import engine.evaluation.MEvaluation;
import ui.MGame;

/** The main execution class for the chess game. */
public class Main {

    public static void main(String[] args) {
        MGame game = new MGame(new MEngine(new MEvaluation(), 4));
        game.startGame();
    }
}
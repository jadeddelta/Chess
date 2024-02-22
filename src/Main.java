
import boards.Bitboards;
import engine.MEngine;
import engine.evaluation.MEvaluation;
import ui.MGame;

public class Main {


    public static void main(String[] args) {
        System.out.println("this is going to be a super cool chess program");
        Bitboards test = new Bitboards();
        System.out.println(test);
        MGame game = new MGame(new MEngine(new MEvaluation(), 3));
        game.startGame();
    }
}
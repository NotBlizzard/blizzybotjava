package blizzybotjava;


import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

/**
 * Created by Anthony on 4/23/2015.
 */
public class Application {
    public static void main(String[] args) throws URISyntaxException, ExecutionException, InterruptedException {
        String[] rooms = {"rooms","go", "here"};
        Bot c = new Bot("name", "password", "sim.smogon.com:8000", rooms);
        c.Connect();
    }
}

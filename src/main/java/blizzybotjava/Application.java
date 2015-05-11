package blizzybotjava;


import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

/**
 * Created by Anthony on 4/23/2015.
 */
public class Application {
    public static void main(String[] args) throws URISyntaxException, ExecutionException, InterruptedException {
        String[] rooms = {"lobby"};
        Bot c = new Bot("bot_name", "bot_password","server:8000","your_user_name", rooms);
        c.Connect();
    }
}

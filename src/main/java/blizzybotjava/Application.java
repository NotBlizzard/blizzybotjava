package blizzybotjava;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * Created by Anthony on 4/23/2015.
 */
public class Application {
    public static void main(String[] args) throws URISyntaxException, ExecutionException, InterruptedException {
        Properties prop = new Properties();
        try {
            InputStream input = new FileInputStream("config.properties");
            prop.load(input);
            String username = prop.getProperty("username");
            String password = prop.getProperty("password");
            String server = prop.getProperty("server");
            String owner = prop.getProperty("owner");
            String[] rooms = prop.getProperty("rooms").replace(" ", "").split(",");
            Bot c = new Bot(username, password,server, owner, rooms);
            c.Connect();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}

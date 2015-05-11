package blizzybotjava;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;

/**
 * Created by Anthony on 5/10/2015.
 */
interface CommandHandler {

}
public class CommandList {
    public Map<String, Callable<String>> commands = new HashMap<String, Callable<String>>();
    private String user;
    private String owner;
    private String arguments;
    public CommandList(String u, String a, String o) {
        commands.put("hello", this::hello);
        commands.put("pick", this::pick);
        commands.put("echo", this::echo);
        commands.put("about", this::about);
        this.user = u.substring(1);
        this.owner = o;
        this.arguments = a;

    }
    public String about() {
        return "BlizzyBotJava. Made in Java 8.";
    }
    public String hello() {
        return "Hello " + user;
    }
    public String pick() {
        String[] choices = arguments.split(",");
        return choices[new Random().nextInt(choices.length)];
    }
    public String echo() {
        if (!user.equalsIgnoreCase(owner)) {
            return "";
        } else {
            return arguments;
        }
    }
}

package blizzybotjava;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.ws.WebSocket;
import com.ning.http.client.ws.WebSocketTextListener;
import com.ning.http.client.ws.WebSocketUpgradeHandler;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Anthony on 4/23/2015.
 */
public class Bot {
    final String url = "http://play.pokemonshowdown.com/action.php";
    AsyncHttpClient c = new AsyncHttpClient();

    private String name;
    private String server;
    private String passw;
    private String[] rooms;
    private WebSocket ws;
    private CloseableHttpClient httpclient = HttpClients.createDefault();


    public Bot(String n, String p, String s, String[] r) {
        this.name = n;
        this.passw = p;
        this.server = "ws://" + s + "/showdown/websocket";
        this.rooms = r;
    }

    public String Get(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = httpclient.execute(httpGet);
        HttpEntity r = response.getEntity();
        return EntityUtils.toString(r);
    }

    public String GetAssertion(String url, String username, String password, String id, String challenge) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("act", "login"));
        params.add(new BasicNameValuePair("name", username));
        params.add(new BasicNameValuePair("pass", password));
        params.add(new BasicNameValuePair("challengekeyid", id));
        params.add(new BasicNameValuePair("challenge", challenge));
        httpPost.setEntity(new UrlEncodedFormEntity(params));
        CloseableHttpResponse response = httpclient.execute(httpPost);
        HttpEntity r = response.getEntity();
        String str = EntityUtils.toString(r);
        return str;
    }

    public void Connect() throws InterruptedException, ExecutionException {
        WebSocket websocket = c.prepareGet(server).execute(new WebSocketUpgradeHandler.Builder().addWebSocketListener(new WebSocketTextListener() {

            @Override
            public void onOpen(WebSocket w) {
                Bot.this.ws = w;

            }

            @Override
            public void onMessage(String message) {
                System.out.println(message);
                String[] messages = message.split("\\|");
                switch (messages[1]) {
                    case "challstr":
                        String key = messages[2];
                        String challenge = messages[3];
                        if (passw.equals("")) {
                            String get_url = url + "?act=getassertion&userid=" + name + "&challengekeyid=" + key + "&challenge=" + challenge;
                            try {
                                Get(get_url);
                                Bot.this.ws.sendMessage("|/trn " + name + ",0," + Get(get_url));
                            } catch (IOException ie) {
                                System.out.println(ie);
                            }
                        } else {
                            try {
                                Bot.this.ws.sendMessage("|/trn " + name + ",0," + GetAssertion(url, name, passw, key, challenge));
                            } catch (IOException ie) {
                                System.out.println(ie);
                            }

                        }
                        for (String room : rooms) {
                            Bot.this.ws.sendMessage("|/join " + room);
                            System.out.println("ok");
                        }
                        break;
                }
            }


            public void onClose(WebSocket webSocket) {
                Bot.this.ws.sendMessage("|/logout");
            }

            public void onError(Throwable throwable) {
            }
        }).build()).get();
    }
}

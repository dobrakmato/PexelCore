package eu.matejkormuth.pexel.PexelCore.bans;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import eu.matejkormuth.pexel.PexelCore.Pexel;
import eu.matejkormuth.pexel.PexelCore.core.Log;

public class BanListServer {
    HttpServer server;
    
    public BanListServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.info("Pozdrav zo stredomoria!");
                try {
                    BanListServer.this.server = HttpServer.create(new InetSocketAddress(
                            45198), 20);
                    BanListServer.this.server.createContext("/bans",
                            new BanListHandler());
                    BanListServer.this.server.setExecutor(null);
                    BanListServer.this.server.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.info("Koniec!");
            }
        }).start();
        
    }
    
    public void stop() {
        Log.info("Stredomorie sa s vami luci :(");
        this.server.stop(200);
    }
    
    public class BanListHandler implements HttpHandler {
        @Override
        public void handle(final HttpExchange t) throws IOException {
            String response = "<h1>Banlist</h1>";
            for (BanBase ban : Pexel.getBans().getBans()) {
                response += "<pre style=\"background:#ccc;margin:16px;font-size:9px;\">"
                        + ban.toString() + "</pre>";
            }
            
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
        
    }
}

package eu.matejkormuth.pexel.master.api;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class APIServer {
    public static final class A {
        public A(final String name) {
            this.name = name;
        }
        
        public String name    = "alpha";
        public long   freeRam = 5882452;
        public long   usedRam = (long) (2521000 + (Math.random() * 600000));
    }
    
    public APIServer() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(4500), 50);
            server.createContext("/api/", new HttpHandler() {
                @Override
                public void handle(final HttpExchange paramHttpExchange)
                        throws IOException {
                    
                    A a = new A("alpha");
                    String resp = new Gson().toJson(a);
                    paramHttpExchange.sendResponseHeaders(200, resp.length());
                    paramHttpExchange.getResponseBody().write(resp.getBytes());
                    paramHttpExchange.close();
                }
            });
            
            server.createContext("/admin", new HttpHandler() {
                @Override
                public void handle(final HttpExchange paramHttpExchange)
                        throws IOException {
                    String resp = new String(
                            Files.readAllBytes(Paths.get("C:\\Users\\M\\Documents\\My Web Sites\\matejkormuth.eu\\serverstatus.html")),
                            StandardCharsets.UTF_8);
                    paramHttpExchange.sendResponseHeaders(200, resp.length());
                    paramHttpExchange.getResponseBody().write(resp.getBytes());
                    paramHttpExchange.close();
                }
            });
            server.setExecutor(null);
            System.out.println("Starting APIServer: " + server.getAddress().toString());
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// @formatter:off
/*
 * Pexel Project - Minecraft minigame server platform. 
 * Copyright (C) 2014 Matej Kormuth <http://www.matejkormuth.eu>
 * 
 * This file is part of Pexel.
 * 
 * Pexel is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * Pexel is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 */
// @formatter:on
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
                Log.info("Starting BAN-Server...");
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
        Log.info("Stopping BAN-SERVER...");
        this.server.stop(0);
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

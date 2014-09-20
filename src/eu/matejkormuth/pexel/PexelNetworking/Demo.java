package eu.matejkormuth.pexel.PexelNetworking;

import org.bukkit.Bukkit;

import eu.matejkormuth.pexel.PexelNetworking.packets.CrossServerChatMessage;

public class Demo {
    public void a() {
        Request request = new Request(new CrossServerChatMessage("test"));
        request.setOnRespone(new ResponeEventHandler() {
            @Override
            public void onRespone(final Request requset, final Respone respone) {
                Bukkit.broadcastMessage("onRespone: "
                        + ((CrossServerChatMessage) respone.getContent()).message);
            }
        });
    }
}

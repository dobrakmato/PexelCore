package eu.matejkormuth.pexel.master;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Messenger implements PayloadHandler {
    private final Map<Class<? extends Request>, Handle> methods = new HashMap<Class<? extends Request>, Handle>();
    private final CallbackHandler                       listener;
    private final Protocol                              protocol;
    
    public Messenger(final CallbackHandler listener, final Protocol protocol,
            final PluginMessageComunicator comunicator) {
        this.listener = listener;
        this.protocol = protocol;
    }
    
    /**
     * Adds specified valid responder and registers valid handler methods.
     * 
     * @param responder
     *            valid responder object
     */
    @SuppressWarnings("unchecked")
    public void addResponder(final Object responder) {
        // Register all valid methods.
        for (Method m : responder.getClass().getDeclaredMethods()) {
            Class<?>[] types = m.getParameterTypes();
            // If accepts only one parameter.
            if (types.length == 1) {
                // And that is some request.
                if (types[0].isAssignableFrom(Request.class)) {
                    // And that request is supported by protocol.
                    if (this.protocol.supportsRequest((Class<? extends Request>) types[0])) {
                        this.methods.put((Class<? extends Request>) types[0],
                                new Handle(responder, m));
                    }
                }
            }
        }
    }
    
    private void invokeHandler(final Request request) {
        try {
            Object response = this.methods.get(request.getClass()).invoke(
                    new Object[] { request });
            if (response instanceof Response) {
                ServerInfo.localServer().sendResponse((Response) response);
            }
        } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void handleMessage(final ServerInfo sender, final byte[] payload) {
        PayloadDataType type = PayloadDataType.fromType(payload[0]);
        switch (type) {
            case REQUEST:
                this.decodeRequest(sender, payload);
                break;
            case RESPONSE:
                this.decodeResponse(sender, payload);
                break;
            case OTHER:
                
                break;
        }
    }
    
    private void decodeResponse(final ServerInfo sender, final byte[] payload) {
        long requestID = ByteUtils.readLong(payload, 1);
        int responseType = ByteUtils.readInt(payload, 9);
        
        byte[] data = new byte[payload.length - 13];
        System.arraycopy(payload, 13, data, 0, payload.length - 13);
        
        try {
            // Create request object.
            Response request = this.protocol.getRequest(requestType).newInstance();
            request.requestID = requestID;
            request.fromByteArray(data);
            
            // Find and invoke handler.
            this.invokeHandler(request);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    
    private void decodeRequest(final ServerInfo sender, final byte[] payload) {
        long requestID = ByteUtils.readLong(payload, 1);
        int requestType = ByteUtils.readInt(payload, 9);
        
        byte[] data = new byte[payload.length - 13];
        System.arraycopy(payload, 13, data, 0, payload.length - 13);
        
        try {
            // Create request object.
            Request request = this.protocol.getRequest(requestType).newInstance();
            request.requestID = requestID;
            request.fromByteArray(data);
            
            // Find and invoke handler.
            this.invokeHandler(request);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    
}

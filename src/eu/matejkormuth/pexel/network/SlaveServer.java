package eu.matejkormuth.pexel.network;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class SlaveServer extends ServerInfo implements Requestable {
    
    // Requestable interface
    protected AtomicLong             lastRequestID = new AtomicLong();
    protected Map<Long, Callback<?>> callbacks     = new HashMap<Long, Callback<?>>(255);
    
    protected Messenger              messenger;
    protected MessageComunicator     comunicator;
    protected ServerInfo             masterServerInfo;
    protected Protocol               protocol;
    protected Configuration          config;
    protected Logger                 log;
    
    public SlaveServer(final String name) {
        super(name);
        
        this.log = new Logger("SlaveServer");
        this.log.info("Loading Slave server...");
        
        // Load configuration.
        File f = new File("./config.xml");
        if (!f.exists()) {
            this.log.info("Configuration file not found, generating default one!");
            Configuration.createDefault(f);
        }
        this.log.info("Loading configuration...");
        this.config = Configuration.load(f);
        
        this.side = ServerSide.LOCAL;
        
        this.protocol = new PexelProtocol();
        
        this.messenger = new Messenger(new CallbackHandler(this), this.protocol);
        
        this.masterServerInfo = new ServerInfo("master") {
            @Override
            public void sendRequest(final Request request) {
                SlaveServer.this.comunicator.send(SlaveServer.this.masterServerInfo,
                        request.toByteBuffer().array());
            }
            
            @Override
            public void sendResponse(final Response response) {
                SlaveServer.this.comunicator.send(SlaveServer.this.masterServerInfo,
                        response.toByteBuffer().array());
            }
        };
        
        this.comunicator = new NettyClientComunicator(this.messenger,
                this.config.getAsInt("port"), this.config.getAsString("masterIp"),
                this.config.getAsString("authKey"), this);
        
        ServerInfo.setLocalServer(this);
    }
    
    protected SlaveServer(final boolean fromMaster, final String name) {
        super(name);
        
        // Does not register this as local server.
        this.side = ServerSide.REMOTE;
    }
    
    public void sendToMaster(final Message message) {
        
    }
    
    @Override
    public void sendRequest(final Request request) {
        if (this.side == ServerSide.REMOTE) {
            // Sending from master
            PexelMaster.getInstance().send(request, this);
        }
        else {
            throw new RuntimeException("Can't send request to local server.");
        }
    }
    
    @Override
    public void sendResponse(final Response response) {
        if (this.side == ServerSide.REMOTE) {
            // Sending from master
            PexelMaster.getInstance().send(response, this);
        }
        else {
            throw new RuntimeException("Can't send response to local server.");
        }
    }
    
    @Override
    public long nextRequestID() {
        return this.lastRequestID.getAndIncrement();
    }
    
    @Override
    public void registerCallback(final long requestID, final Callback<?> callback) {
        this.callbacks.put(requestID, callback);
    }
    
    @Override
    public Callback<?> getCallback(final long requestID) {
        return this.callbacks.get(requestID);
    }
    
    @Override
    public void removeCallback(final long requestID) {
        this.callbacks.remove(requestID);
    }
    
    public ServerInfo getMasterServerInfo() {
        return this.masterServerInfo;
    }
}

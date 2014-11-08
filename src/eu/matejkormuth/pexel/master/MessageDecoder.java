package eu.matejkormuth.pexel.master;

public class MessageDecoder implements PayloadHandler {
    private final RequestProcessor processor;
    private final ResponeListener  listener;
    
    public MessageDecoder(final RequestProcessor processor,
            final ResponeListener listener) {
        this.processor = processor;
        this.listener = listener;
    }
    
    @Override
    public void receiveMessage(final ServerInfo sender, final byte[] payload) {
        DataType type = DataType.fromType(payload[0]);
        switch (type) {
            case REQUEST:
                
                break;
            case RESPONSE:
                
                break;
        }
    }
    
}

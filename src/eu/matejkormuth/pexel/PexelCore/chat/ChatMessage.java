package eu.matejkormuth.pexel.PexelCore.chat;

import org.bukkit.ChatColor;

/**
 * Class that represents chat message.
 */
public class ChatMessage {
    private String            rawMessage;
    private ChannelSubscriber sender;
    
    /**
     * @param rawMessage
     * @param sender
     */
    public ChatMessage(final String rawMessage, final ChannelSubscriber sender) {
        this.rawMessage = rawMessage;
        this.sender = sender;
    }
    
    /**
     * @return the rawMessage
     */
    public String getRawMessage() {
        return this.rawMessage;
    }
    
    /**
     * @param rawMessage
     *            the rawMessage to set
     */
    public void setRawMessage(final String rawMessage) {
        this.rawMessage = rawMessage;
    }
    
    /**
     * @return the sender
     */
    public ChannelSubscriber getSender() {
        return this.sender;
    }
    
    /**
     * @param sender
     *            the sender to set
     */
    public void setSender(final ChannelSubscriber sender) {
        this.sender = sender;
    }
    
    public String getFormattedMessage() {
        return ChatColor.GRAY + this.sender.getName() + " > " + this.rawMessage;
    }
}

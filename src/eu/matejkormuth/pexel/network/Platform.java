package eu.matejkormuth.pexel.network;

/**
 * Platform that is master server running on.
 */
public enum Platform {
    /**
     * Master is running with minecraft proxy server.
     */
    MINECRAFT_PROXY,
    /**
     * Master is running as standalon server (not yet implemented).
     */
    STANDALONE;
}

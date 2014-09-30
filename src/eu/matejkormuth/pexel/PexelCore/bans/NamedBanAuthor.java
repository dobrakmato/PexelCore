package eu.matejkormuth.pexel.PexelCore.bans;

public class NamedBanAuthor implements BanAuthor {
    private final String name;
    
    public NamedBanAuthor(final String authorName) {
        this.name = authorName;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
}

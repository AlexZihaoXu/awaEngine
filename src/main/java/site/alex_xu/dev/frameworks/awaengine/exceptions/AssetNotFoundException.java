package site.alex_xu.dev.frameworks.awaengine.exceptions;

public class AssetNotFoundException extends RuntimeException {
    public AssetNotFoundException(String content) {
        super(content);
    }
}

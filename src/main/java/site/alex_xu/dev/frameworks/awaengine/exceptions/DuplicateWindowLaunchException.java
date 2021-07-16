package site.alex_xu.dev.frameworks.awaengine.exceptions;

public class DuplicateWindowLaunchException extends RuntimeException {
    public DuplicateWindowLaunchException(String content) {
        super(content);
    }
}

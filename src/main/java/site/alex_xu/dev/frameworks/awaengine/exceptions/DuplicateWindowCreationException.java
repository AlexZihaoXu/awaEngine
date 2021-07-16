package site.alex_xu.dev.frameworks.awaengine.exceptions;

public class DuplicateWindowCreationException extends RuntimeException {
    public DuplicateWindowCreationException(String content) {
        super(content);
    }
}

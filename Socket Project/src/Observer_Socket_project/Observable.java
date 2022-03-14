package Observer_Socket_project;

import java.io.IOException;

public interface Observable {
    void notifyAll(String message) throws IOException;
}

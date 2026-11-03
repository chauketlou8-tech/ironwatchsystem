package Repositories;

import sockets.Client;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.function.Consumer;

public class ServerRepository {
    public ServerRepository(Consumer<BufferedImage> stream) throws IOException {
        Client client = new Client(stream);
    }
}

package sockets;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

public class Client {
    private final DataInputStream in;
    private final BufferedReader reader;
    private final OutputStreamWriter out;
    private final Socket client;

    private boolean connected = false;
    private int counter = 0;

    public Client(Consumer<BufferedImage> stream) throws IOException {
        this.client = new Socket("localhost", 4001);

        this.out = new OutputStreamWriter(client.getOutputStream());
        this.reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        this.in = new DataInputStream(client.getInputStream());

        System.out.println("Connecting to the server...");

        this.out.write("Open Connection");
        this.out.flush();

        while (true) {
            try {
                String msg = reader.readLine();
                System.out.println(msg);

                if(msg.equals("Connected")) {
                    this.out.write("start");
                    this.out.flush();
                    continue;
                }

                if(msg.equals("Camera open")) {
                    connected = true;
                }

                if(msg.equals("Camera stopped")) {
                    this.out.write("capture");
                    this.out.flush();
                    continue;
                }

                if(msg.equals("Image captured")) {
                    this.out.write("close");
                    this.out.flush();
                    continue;
                }

                if(msg.equals("recording stopped")) {
                    this.out.write("stop streaming");
                    this.out.flush();

                    int size = in.readInt();
                    byte[] data = in.readNBytes(size);

                    BufferedImage image = ImageIO.read(new ByteArrayInputStream(data));
                    stream.accept(image);
                    continue;
                }

                if(msg.equals("closed")) {
                    System.exit(0);
                }

                while (connected) {
                    this.out.write("stream");
                    this.out.flush();

                    int size = in.readInt();
                    byte[] data = in.readNBytes(size);

                    BufferedImage image = ImageIO.read(new ByteArrayInputStream(data));

                    stream.accept(image);
                    counter++;

                    if (counter == 10) {
                        this.out.write("stop recording");
                        this.out.flush();

                        connected = false;
                    }
                }
            }

            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

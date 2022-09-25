package org.example;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 8888);
        try {
            final SocketChannel socketChannel = SocketChannel.open();
            socketChannel.connect(socketAddress);

            try (Scanner scan = new Scanner(System.in);
                 socketChannel) {

                final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);

                String msg;
                while (true) {
                    System.out.println("Enter string with spacebars");
                    msg = scan.nextLine();

                    if ("end".equals(msg)) {
                        break;
                    }

                    socketChannel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));

                    System.out.println("Client going sleep while socketChannel writing");
                    Thread.sleep(2000);

                    int bytesCount = socketChannel.read(inputBuffer);
                    System.out.println(new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8));
                    inputBuffer.clear();
                }

            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

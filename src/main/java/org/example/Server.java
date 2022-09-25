package org.example;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Server {
    public static void main(String[] args) throws IOException {
        final ServerSocketChannel serverChannel = ServerSocketChannel.open();

        serverChannel.bind(new InetSocketAddress("127.0.0.1", 8888));

        while (true) {
            try (SocketChannel socketChannel = serverChannel.accept();
                 serverChannel) {

                final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);

                while (socketChannel.isConnected()) {

                    int bytesCount = socketChannel.read(inputBuffer);

                    if (bytesCount == -1) break;

                    final String msg = new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8);
                    inputBuffer.clear();

                    String[] msgArr = msg.split("\\s+");
                    StringBuilder sb = new StringBuilder();
                    for (String s : msgArr) {
                        sb.append(s);
                    }

                    String outString = sb.toString();
                    socketChannel.write(ByteBuffer.wrap(outString.getBytes(StandardCharsets.UTF_8)));

                    //Server doing something while socketChannel writing
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
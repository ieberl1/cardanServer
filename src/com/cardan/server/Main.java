package com.cardan.server;


/*
    This program:
(DONE)    1. listen on UDP 22333
(DONE)    2. Print received bytes
(DONE)    3. Send ACK to sender w/ total # of bytes received
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Main {
    /**
     * <p>Listen on a UDP port forever and when data is received print the data and reply to the sender with the total
     * number of bytes received
     * </p>
     * @param args  N/A
     */
    public static void main(String[] args)
    {
        int listenPort = 22333;
        int total_received = 0;

        // Set up socket to listen on
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(listenPort);
        } catch (SocketException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        System.out.println("Listening on localhost:" + listenPort);

        // Listen forever
        while(true)
        {
            byte[] receive_buf = new byte[1460];
            DatagramPacket data_in = new DatagramPacket(receive_buf, receive_buf.length);  // 1460 is max amount to expect
            try {
                // Get and store info about the received packet
                socket.receive(data_in);
                int data_len = data_in.getLength();
                String data_in_str = new String(data_in.getData()).trim();
                InetAddress client_addr = data_in.getAddress();
                int client_port = data_in.getPort();

                // Update count of received bytes
                total_received += data_len;
                // Print the data received
                System.out.println(data_in_str);

                // Send a response (the total # bytes received)
                byte[] resp = String.valueOf(total_received).getBytes();
                DatagramPacket data_out = new DatagramPacket(resp, resp.length, client_addr, client_port);
                // This would cause a buffer overflow eventually, but its what the design required
                socket.send(data_out);
            }
            catch (SocketException e)
            {
                System.out.println("A socket error has occurred.");
                System.out.println("Error: " + e.getMessage());
                System.exit(-1);
            }
            catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }
}

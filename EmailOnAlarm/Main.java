//Francois Viviers - u18055461
//Keelan Cross - u19151952

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class Main {

    public static void sendEmail() throws IOException {
        Socket smtpSocket = new Socket("localhost", 25);
        OutputStream out = smtpSocket.getOutputStream();
        BufferedReader in = new BufferedReader(new BufferedReader(new InputStreamReader(smtpSocket.getInputStream())));
        out.write("HELO localhost\r\n".getBytes());
        String response = in.readLine();
        System.out.println(response);
        out.write("MAIL FROM: <admin@assignment2.com>\r\n".getBytes());
        response = in.readLine();
        System.out.println(response);
        out.write("RCPT TO: <user1@assignment2.com>\r\n".getBytes());
        response = in.readLine();
        System.out.println(response);
        out.write("DATA\r\n".getBytes());
        response = in.readLine();
        System.out.println(response);
        out.write("Subject: Alarm\r\n".getBytes());
        out.write("\r\n".getBytes());
        out.write("Your Alarm has gone off and somebody is inside your house.\r\n".getBytes());
        out.write("\r\n".getBytes());
        out.write(".\r\n".getBytes());
        response = in.readLine();
        System.out.println(response);
        out.write("QUIT\r\n".getBytes());
        response = in.readLine();
        System.out.println(response);
        smtpSocket.close();
    }

    public static void main(String[] args) throws IOException {

        System.out.println("Type 'stop' to stop the program");
        boolean run = true;
        while (run) {
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            String input = userInput.readLine();
            if (input.equals("d")) {
                sendEmail();
            }
            if (input.equals("w")) {
                sendEmail();
            }
            if (input.equals("stop")) {
                run = false;
            }
        }
    }
}



//Francois Viviers - u18055461
//Keelan Cross - u19151952

import java.io.IOException;
import java.net.Socket;

public class singletonConnection {
    private static singletonConnection single_instance = null;
    String ip = "";
    int port = 0;
    Socket socket = null;

    public singletonConnection(String ip, int port) throws IOException {
        this.ip = ip;
        this.port = port;
        socket = new Socket(ip, port);
    }

    public static singletonConnection getInstance(String ip, int port) throws IOException {
        if(single_instance == null){
            single_instance = new singletonConnection(ip, port);
        }
        return single_instance;
    }

    public static void destroySingleton() throws IOException {
        single_instance = null;
    }

}

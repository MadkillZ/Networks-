//Francois Viviers - u18055461
//Keelan Cross - u19151952

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        //listen to port 8080
        try(ServerSocket serverSocket = new ServerSocket(8080))//23 for telnet connections
        {
//            Socket telnetSocket = new Socket("localhost", 23);
//            InputStream telnetInput = telnetSocket.getInputStream();
//            BufferedReader telnetReader = new BufferedReader(new InputStreamReader(telnetInput));
//
//            OutputStream telnetOutput = telnetSocket.getOutputStream();
//            PrintWriter telnetWriter = new PrintWriter(telnetOutput, true);

            while(true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");


                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);


                writer.println("Which server do you want to connect to?");
                String ip = reader.readLine();

                singletonConnection test = singletonConnection.getInstance(ip, 23);
                Socket telnetSocket = test.socket;
                InputStream telnetInput = telnetSocket.getInputStream();
                BufferedReader telnetReader = new BufferedReader(new InputStreamReader(telnetInput));

                OutputStream telnetOutput = telnetSocket.getOutputStream();
                PrintWriter telnetWriter = new PrintWriter(telnetOutput, true);


                TimeUnit.SECONDS.sleep(1);
                writer.println("\033[32m");
                while(telnetReader.ready()) {
                    int c = telnetReader.read();
                    writer.write((char)c);
                }
                writer.println("\033[0m");
//                System.out.println("Does it get here");
//                writer.println("\033[32m");
//                writer.println("");
//                writer.println("\033[0m");

                String text = "";
                String[] smlString;

                do{
                    text = reader.readLine();
                    if(text == null){
                        break;
                    }
                    smlString = text.split(" ");
                    if(smlString[0].equals("tasklist") || smlString[0].equals("ps")){
                        writer.println("\033[32m");
                        writer.println("You can't use ps/tasklist command");
                        writer.println("\033[0m");
                    }
                    else{
                        if(text.contains(";")){
                            ArrayList<Integer> spiceyIndexes = new ArrayList<Integer>();
                            String[] smlString2 = text.split(";");
                            for(int i = 0; i < smlString2.length; i++) {
                                if(smlString2[i].equals("tasklist") || smlString2[i].equals(" tasklist") || smlString2[i].equals("ps") || smlString2[i].equals(" ps")){
                                    writer.println("\033[32m");
                                    writer.println("You can't use ps/tasklist command");
                                    writer.println("\033[0m");
                                    spiceyIndexes.add(i);
                                }
                            }
                            if(spiceyIndexes.size() != 0){
                                String newString = "";
                                int counter = 0;
                                for(int i = 0; i < spiceyIndexes.size(); i++){
                                    smlString2 = reconstruct(smlString2, spiceyIndexes.get(i) - counter);
                                    counter++;
                                }
                                for(int index = 0; index < smlString2.length; index++){
                                    if(index != smlString2.length - 1){
                                        newString += smlString2[index] + ";";
                                    } else {
                                        newString += smlString2[index];
                                    }
                                }
                                telnetWriter.println(newString);
                                telnetWriter.flush();
                                writer.println("\033[32m");
                                TimeUnit.SECONDS.sleep(1);
                                while(telnetReader.ready()) {
                                    int c = telnetReader.read();
                                    writer.write((char)c);
                                }
                                writer.println("\033[0m");
                            } else {
                                telnetWriter.println(text);
                                telnetWriter.flush();
                                writer.println("\033[32m");
                                TimeUnit.SECONDS.sleep(1);
                                while(telnetReader.ready()) {
                                    int c = telnetReader.read();
                                    writer.write((char)c);
                                }
                                writer.println("\033[0m");
                            }
                        } else {
                            telnetWriter.println(text);
                            telnetWriter.flush();
                            writer.println("\033[32m");
                            TimeUnit.SECONDS.sleep(1);
                            while(telnetReader.ready()) {
                                int c = telnetReader.read();
                                writer.write((char)c);
                            }
                            writer.println("\033[0m");
                        }
                    }
                }while(text != null && !text.equals("exit"));

                writer.println("\033[32m");
                writer.println("Disconnected from proxy server");
                writer.println("\033[0m");
                socket.close();
                telnetSocket.close();
                singletonConnection.destroySingleton();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String[] reconstruct(String[] smlString, int i){
        String[] newStringA = new String[smlString.length - 1];
        if(newStringA.length == 0) {
            return newStringA;
        }
        for(int j = 0; j < i; j++){
            newStringA[j] = smlString[j];
        }
        for(int j = i; j < newStringA.length; j++){
            newStringA[j] = smlString[j + 1];
        }
        return newStringA;
    }
}
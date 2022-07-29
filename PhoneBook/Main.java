//Francois u18055461
//Keelan u19151952

import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception{
        String temp = "";

        System.out.println("Enter the port you wish to listen to: ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String port = reader.readLine();
        int portNum = Integer.parseInt(port);

        try (ServerSocket serverSocket = new ServerSocket(portNum)) {
            System.out.println("Server started.\nListening for messages.");
            while (true) {
                try (Socket client = serverSocket.accept()) {
                    System.out.println("New Message: " + client.toString());
                    //Read the request
                    InputStreamReader input = new InputStreamReader(client.getInputStream());
                    BufferedReader br = new BufferedReader(input);
                    //puts the string together
                    StringBuilder sb = new StringBuilder();
                    //temporary for incoming stream
                    String line;
                    System.out.println("======REQUEST======");
                    line = br.readLine();
                    while (!isBlank(line)) {
                        sb.append(line + "\r\n");
                        line = br.readLine();
                    }

                    String request = sb.toString();
                    System.out.println("Request: " + request);
                    //Sending back a message
                    //Basic html for index.html
                    OutputStream clientOutput = client.getOutputStream();
                    clientOutput.write("HTTP/1.1 200 OK\r\n".getBytes());
                    clientOutput.write("\r\n".getBytes());
                    //clientOutput.write(("").getBytes()); --> html form
                    clientOutput.flush();

                    //changing response depending on route
                    //if route is /0, /1, /2, /3, /4, /5, /6, /7, /8, /9
                    String firstLine = request.split("\n")[0];
                    String fourthLine = request.split("\n")[3];
                    //System.out.println("Fourth Line: " + fourthLine);
                    //String content = fourthLine.split(" ")[1];
                    //content.trim();
                    //System.out.println("Content: " + content);
                    //System.out.println("Content to int: " + Integer.parseInt(content));
                    //System.out.println("First Line: " + firstLine);
                    String getPost = firstLine.split(" ")[0];
                    //System.out.println("getPost: " + getPost);
                    String resource = firstLine.split(" ")[1];
                    String[] data = resource.split("&");
                    if (getPost.equals("GET")){
                        if(resource.contains("/?add")){
                            //clientOutput.write(("Testing").getBytes());
                            String name = data[1].substring(data[1].indexOf("=")+1);
                            name.trim();
                            clientOutput.write(("Name:" + name).getBytes());
                            String num = data[2].substring(data[2].indexOf("=")+1);
                            num.trim();
                            if(insert(num, name)){
                                clientOutput.write(("\nSuccessfully added to database.").getBytes());
                            }
                            else{
                                clientOutput.write(("\nFailed to add to database.").getBytes());
                            }
                        }
                        else if(resource.contains("/?searchNum")){
                            //clientOutput.write(("Testing").getBytes());
                            String nr = data[0].substring(data[0].indexOf("=")+1);
                            nr.trim();
                            String trash2 = SearchNr(nr);
                            clientOutput.write(("Name: " + trash2).getBytes());
                        }
                        else if(resource.contains("/?searchName")){
                            //clientOutput.write(("Testing").getBytes());
                            String name = data[0].substring(data[0].indexOf("=")+1);
                            name.trim();
                            List<String[]> trash = SearchName(name);
                            for(String[] s : trash){
                                clientOutput.write(("Name: " + s[0] + " Number: " + s[1]).getBytes());
                                clientOutput.write(("\n").getBytes());
                            }
                        }
                        else if(resource.contains("/?delete")){
                            //clientOutput.write(("Testing").getBytes());
                            String nr = data[0].substring(data[0].indexOf("=")+1);
                            nr.trim();
                            if (remove(nr)) {
                                clientOutput.write(("Deleted Successfully!").getBytes());
                            }
                            else {
                                clientOutput.write(("Could not delete!").getBytes());
                            }
                        }
                        else if(resource.contains("/?print")) {
                            List<String> stuff = print();
                            for(String s : stuff){
                                clientOutput.write(s.getBytes());
                                clientOutput.write("\n".getBytes());
                            }
                        }
                    }
                    else if(getPost.equals("POST")){
                        String temp1 = br.readLine();
                        System.out.println(temp1);
                        String temp2 = br.readLine();
                        System.out.println(temp2);

                        String[] tempArr = temp2.split(" ");
                        String decider = tempArr[2].substring(tempArr[2].indexOf("=")+1);
                        decider.trim();
                        System.out.println("Decider: " + decider);

                        //stuff
                        if(decider.equals("\"add\"")){
                            //clientOutput.write(("Testing").getBytes());
                            String name = data[1].substring(data[1].indexOf("=")+1);
                            name.trim();
                            clientOutput.write(("Name:" + name).getBytes());
                            String num = data[2].substring(data[2].indexOf("=")+1);
                            num.trim();
                            if(insert(num, name)){
                                clientOutput.write(("\nSuccessfully added to database.").getBytes());
                            }
                            else{
                                clientOutput.write(("\nFailed to add to database.").getBytes());
                            }
                        }
                        else if(decider.equals("\"searchNum\"")){
                            //clientOutput.write(("Testing").getBytes());
                            String nr = data[0].substring(data[0].indexOf("=")+1);
                            nr.trim();
                            String trash2 = SearchNr(nr);
                            clientOutput.write(("Name: " + trash2).getBytes());
                        }
                        else if(decider.equals("\"searchName\"")){
                            //clientOutput.write(("Testing").getBytes());
                            String name = data[0].substring(data[0].indexOf("=")+1);
                            name.trim();
                            List<String[]> trash = SearchName(name);
                            for(String[] s : trash){
                                clientOutput.write(("Name: " + s[0] + " Number: " + s[1]).getBytes());
                                clientOutput.write(("\n").getBytes());
                            }
                        }
                        else if(decider.equals("\"delete\"")){
                            //clientOutput.write(("Testing").getBytes());
                            String nr = data[0].substring(data[0].indexOf("=")+1);
                            nr.trim();
                            if (remove(nr)) {
                                clientOutput.write(("Deleted Successfully!").getBytes());
                            }
                            else {
                                clientOutput.write(("Could not delete!").getBytes());
                            }
                        }
                        else if(decider.equals("\"print\"")) {
                            List<String> stuff = print();
                            for(String s : stuff){
                                clientOutput.write(s.getBytes());
                                clientOutput.write("\n".getBytes());
                            }
                        }
                    }
                    //closes socket1
                    client.close();
                }
            }
        }
    }

    //This method returns true if the given string is empty or contains only white space code points, otherwise false.
    public static boolean isBlank(String str) {
        if (str == null) {
            return true;
        }
        int strLen = str.length();
        if (strLen == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String SearchNr(String nr) {
        //Reading File
        List<String[]> Full = new ArrayList<String[]>();
        try {
            File myFile = new File("phonebook.txt");
            Scanner myReader = new Scanner(myFile);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] arr = data.split(" ");
                Full.add(arr);
            }
            myReader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        //Searching
        for (String[] strings : Full) {
            if (Objects.equals(strings[1], nr)) {
                return strings[0];
            }
        }
        return " ";
    }

    public static List<String[]> SearchName(String name)
    {
        //Reading File
        List<String[]> Full = new ArrayList<String[]>();
        try {
            File myFile = new File("phonebook.txt");
            Scanner myReader = new Scanner(myFile);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] arr = data.split(" ");
                Full.add(arr);
            }
            myReader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        List<String[]> search = new ArrayList<>();
        //Searching
        for (String[] strings : Full) {
            if (strings[0].contains(name)) {
                search.add(strings);
            }
        }
        return search;
    }

    public static boolean insert(String nr, String name) {
        //Reading File
        //System.out.println("Insert Called");
        if(!Objects.equals(SearchNr(nr), " ")) {
            System.out.println(nr + "Already Exists!");
            return false;
        }
        String[] new1 = {name,nr};
        List<String[]> Full = new ArrayList<String[]>();
        try {
            File myFile = new File("phonebook.txt");
            Scanner myReader = new Scanner(myFile);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] arr = data.split(" ");
                Full.add(arr);
            }
            Full.add(new1);
            //System.out.println("Full: " + Full.get(0));
            myReader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return false;
        }
        //Writing to File
        try {
            FileWriter myWriter = new FileWriter("phonebook.txt");
            for (String[] strings : Full)
            {
                myWriter.write(strings[0] + " " + strings[1] + "\n");
            }
            //myWriter.write("Files in Java might be tricky, but it is fun enough!");
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
            return true;
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean remove(String nr)
    {
        boolean removed = false;
        //Reading File
        List<String[]> Full = new ArrayList<String[]>();
        try {
            File myFile = new File("phonebook.txt");
            Scanner myReader = new Scanner(myFile);
            while (myReader.hasNextLine())
            {
                String data = myReader.nextLine();
                String[] arr = data.split(" ");
                Full.add(arr);
            }
            myReader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        //Searching
        for (String[] strings : Full) {
            if (Objects.equals(strings[1], nr))
            {
                Full.remove(strings);
                removed = true;
                break;
            }
        }

        if(removed) {
            try {
                FileWriter myWriter = new FileWriter("phonebook.txt");
                for (String[] strings : Full) {
                    myWriter.write(strings[0] + " " + strings[1] + "\n");
                }
                //myWriter.write("Files in Java might be tricky, but it is fun enough!");
                myWriter.close();
                System.out.println("Successfully Removed: " + nr);
                return true;
            }
            catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
        else {
            System.out.println("Number not found!");
        }
        return false;
    }

    public static List<String> print() {
        //Reading File
        List<String> printArr = new ArrayList<String>();
        List<String[]> Full = new ArrayList<String[]>();
        try {
            File myFile = new File("phonebook.txt");
            Scanner myReader = new Scanner(myFile);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] arr = data.split(" ");
                Full.add(arr);
            }
            myReader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        //Printing
        for(String[] strings : Full) {
            printArr.add(strings[0] + " " + strings[1]);
        }
        return printArr;
    }
}
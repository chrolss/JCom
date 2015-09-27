import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class JCom {
	
	private
		//Used by all
		boolean listen;
		boolean isServer;
		Scanner input = new Scanner(System.in);
		String message;
		//Used by server
		ServerSocket server; 	
		Socket client;			
		BufferedReader serverIn;
		PrintWriter serverOut;
		//Used by client
		Socket serverCon;	
		PrintWriter clientOut;
		BufferedReader clientIn;
		

	public JCom(String _type) {
		if (_type.equals("server")){
			String temp = checkIP();
			this.isServer = true;
			System.out.println("Server address is " + temp);
		}
		if (_type.equals("client")){
			String temp = checkIP();
			this.isServer = false;
			System.out.println("Client address is " + temp);
		}
	}

	public void startServer(){
		  try{
		    this.server = new ServerSocket(4321); 
		  } catch (IOException e) {
		    System.out.println("Could not listen on port 4321");
		    System.exit(-1);
		  }
		try{
		    this.client = server.accept();
		    this.listen = true;
		  } catch (IOException e) {
		    System.out.println("Accept failed: 4321");
		    System.exit(-1);
		  }
		  try{
			   this.serverIn = new BufferedReader(new InputStreamReader(client.getInputStream()));
			   this.serverOut = new PrintWriter(client.getOutputStream(), true);
			  } catch (IOException e) 
			  {
			    System.out.println("Initializing reader/writer error");
			    System.exit(-1);
			  }
	}
	
	public void serverRead(){
		      try{
		       String line = serverIn.readLine();
		       System.out.println(line);
		       serverOut.println("Got your message");
		     } catch (IOException e) {
		        System.out.println("Read failed");
		        System.exit(-1);
		     }
	}
	

	public void connectToServer(){
	//Create socket connection
	   try{
	     this.serverCon = new Socket("192.168.7.2", 4321);
	     this.clientOut = new PrintWriter(serverCon.getOutputStream(), true);
	     this.clientIn = new BufferedReader(new InputStreamReader(serverCon.getInputStream()));
	     this.listen = false;
	   } catch (UnknownHostException e) {
	     System.out.println("Unknown host: 192.168.7.2");
	     System.exit(1);
	   } catch  (IOException e) {
	     System.out.println("No I/O");
	     System.exit(1);
	   }
	}
	
	public void communicateWithServer(){
		if (!listen){
			System.out.print("Message: ");
			message = input.nextLine();
			clientOut.println(message);
			this.listen = true;
		}
		if (listen){
			try{
				message = clientIn.readLine();
				System.out.println("Text received: " + message);
				listen = false;
			} catch (IOException e){
				System.out.println("Read failed");
				System.exit(1);
			}
		}
	} 
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scan = new Scanner(System.in);
		System.out.println("Choose server or client");
		String choice = scan.nextLine();
		JCom com = new JCom(choice);
		if (com.isServer){
			com.startServer();
			while(true){
				com.serverRead();	
			}
		}
		if (!com.isServer){
			com.connectToServer();
			while(true){
				com.communicateWithServer();
			}
		}
		scan.close();
	}

	public static String checkIP(){
		try {
			String add = InetAddress.getLocalHost().getHostAddress();
			return add;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Error";
		}	
	}
}

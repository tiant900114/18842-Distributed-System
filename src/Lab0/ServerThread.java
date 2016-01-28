package Lab0;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class ServerThread implements Runnable {
	private ServerSocket socket;
	ConcurrentLinkedQueue<Message> q;
	
	ConcurrentMap<String, Socket> sockets;
	ConcurrentMap<String, Integer> seqnums;	
	
	public ServerThread(String ip, Integer port, ConcurrentLinkedQueue<Message> q, ConcurrentMap<String, Socket> sockets, ConcurrentMap<String, Integer> seqnums)
	{
		try {
			socket = new ServerSocket(port);
			System.out.println("Listening on port " + port);
			this.q = q;
			this.sockets = sockets;
			this.seqnums = seqnums;
			
		} catch (Exception e) {
			System.out.println("Unable to listen on port " + port);
			System.exit(1);
		}
		
	}
	
	public void run() {
		 try {
			 while (true) {
				 Socket clientSocket = socket.accept();				 
				 System.out.println("New connection accepted.");
				 WorkerThread wt = new WorkerThread(clientSocket, q, sockets, seqnums);
				 new Thread(wt).start();
			 }
		 } catch (Exception e) {
			 System.out.println("Server thread exits");
			 try {
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
			}
		 }
	}
}

package Lab0;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ServerThread implements Runnable {
	private ServerSocket socket;
	ConcurrentLinkedQueue<Message> q;
	
	public ServerThread(String ip, Integer port, ConcurrentLinkedQueue<Message> q)
	{
		try {
			socket = new ServerSocket(port);
			System.out.println("Listening on port " + port);
			this.q = q;
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
				 WorkerThread wt = new WorkerThread(clientSocket, q);
				 new Thread(wt).start();
			 }
		 } catch (Exception e) {
			 
		 }
	}
}

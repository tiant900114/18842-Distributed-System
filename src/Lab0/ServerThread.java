package Lab0;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread implements Runnable {
	private ServerSocket socket;
	
	public ServerThread(String ip, Integer port)
	{
		try {
			socket = new ServerSocket(port);
		} catch (Exception e) {
			
		}
	}
	
	public void run() {
		 try {
			 while (true) {
				 Socket clientSocket = socket.accept();
				 WorkerThread wt = new WorkerThread(clientSocket);
				 new Thread(wt).start();
			 }
		 } catch (Exception e) {
			 
		 }
	}
}

package Lab0;

import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WorkerThread implements Runnable {
	
	private Socket clientSocket;
	ConcurrentLinkedQueue<Message> q;
	
	public WorkerThread(Socket s, ConcurrentLinkedQueue<Message> q)
	{
		clientSocket = s;
		this.q = q;
	}
	
	public void run() {
		try {
			InputStream in = clientSocket.getInputStream();
			while (true) {
				ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(in));
				Message m = (Message) is.readObject();
				q.add(m);
			}
		} catch (Exception e) {
			 
		}
	}
}

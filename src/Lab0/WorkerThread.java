package Lab0;

import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WorkerThread implements Runnable {
	
	private Socket clientSocket;
	ConcurrentLinkedQueue<Message> q;
	
	public WorkerThread(Socket s, ConcurrentLinkedQueue<Message> q)
	{
		System.out.println("Worker Thread is created");
		clientSocket = s;
		this.q = q;
	}
	
	public void run() {
		try {
			while (true) {
				InputStream in = clientSocket.getInputStream();
				ObjectInputStream is = new ObjectInputStream(in);
				Message m = (Message) is.readObject();
				System.out.println("Message Received.");
				q.add(m);
			}
		} catch (Exception e) {
			 System.out.println("Unable to receive message");
		}
	}
}

package Lab0;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class WorkerThread implements Runnable {
	
	private Socket clientSocket;
	private String name;
	
	ConcurrentLinkedQueue<Message> q;
	ConcurrentMap<String, Socket> sockets;
	ConcurrentMap<String, Integer> seqnums;

	public WorkerThread(Socket s, ConcurrentLinkedQueue<Message> q, ConcurrentMap<String, Socket> sockets, ConcurrentMap<String, Integer> seqnums)
	{
		//System.out.println("Worker Thread is created");
		clientSocket = s;
		this.q = q;
		this.sockets = sockets;
		this.seqnums = seqnums;
		
		//receive the first message from client to identify who it is
		try {
			ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
			Message m = (Message) in.readObject();
			name = m.get_src();
			sockets.putIfAbsent(name, clientSocket);
			seqnums.putIfAbsent(name, 0);
			System.out.println("Connection established with " + name);
		} catch (Exception e) {
			System.out.println("Unable to receive the first message.");
			sockets.remove(name);
			seqnums.remove(name);
			try {
				clientSocket.close();
			} catch (IOException e1) {
				System.out.println("Unable to close the client socket");
			}
		}
	}
	
	public void run() {
		try {
			while (true) {
				InputStream in = clientSocket.getInputStream();
				ObjectInputStream is = new ObjectInputStream(in);
				Message m = (Message) is.readObject();
				q.add(m);
			}
		} catch (Exception e) {
			 System.out.println(name+" has exited!");
		}
		
		try {
			clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to close the client socket");
		}
	}
}

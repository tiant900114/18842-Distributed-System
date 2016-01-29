package Lab0;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public class ClientThread implements Runnable{
	private ConcurrentMap<String, Socket> sockets;
	private ConcurrentMap<String, Integer> seqnums;
	ConcurrentLinkedQueue<Message> q;
	private String name;
	Node n;
	private String self;
	
	public ClientThread(String name, Node n, ConcurrentMap<String, Socket> sockets, String self, ConcurrentMap<String, Integer> seqnums, ConcurrentLinkedQueue<Message> q)
	{
		this.sockets = sockets;
		this.name = name;
		this.n = n;
		this.self = self;
		this.seqnums = seqnums;
		this.q = q;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String ip = n.get_ip();
		int port = n.get_port();
		
		Socket s = null;
		
		//Try connecting to server every 4 seconds
		while (!sockets.containsKey(name)) {
			try {
				s = new Socket(ip, port);
				System.out.println("Connection with " + name + " established.");
				sockets.putIfAbsent(name, s);
				seqnums.putIfAbsent(name, 0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					
				}
			}
		}
		
		//Send the first message to server to identify client itself
		try {
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			Message m = new Message(name, "", "");
			m.set_source(self);
			out.writeObject(m);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to send the first message");
		}
		
		//receive message from server
		try {
			while (true) {
				ObjectInputStream in = new ObjectInputStream(s.getInputStream()); 
				Message m = (Message) in.readObject();
				q.add(m);
			}
		} catch (Exception e) {
			System.out.println(name + " has exited.");
		}
	}
}

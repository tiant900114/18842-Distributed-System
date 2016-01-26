package Lab0;

import java.io.*;
import java.net.*;

public class WorkerThread implements Runnable {
	
	private Socket clientSocket;
	byte[] buf = new byte[1024];
	
	public WorkerThread(Socket s)
	{
		clientSocket = s;
	}
	
	public void run() {
		try {
			InputStream in = clientSocket.getInputStream();
			while (true) {
				in.read(buf);
				System.out.println(buf);
			}
		} catch (Exception e) {
			 
		}
	}
}

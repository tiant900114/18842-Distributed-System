package Lab0;

import java.util.*;
import java.util.concurrent.*;
import java.io.*;
import java.net.*;

import org.yaml.snakeyaml.Yaml;

public class MessagePasser {
	List<Map<String, String>> config =  null;
	String self = "";
	
	ConcurrentMap<String, Socket> sockets = new ConcurrentHashMap<String, Socket>();
	ConcurrentMap<String, Integer> seqnums = new ConcurrentHashMap<String, Integer>();
	ConcurrentLinkedQueue<Message> q = new ConcurrentLinkedQueue<Message>();
	
	Map<String, Node> hosts = new HashMap<String, Node>();
	
	
	private void ParseConfigFile(String filename)
	{	
		Object data = null;
		InputStream input = null;
		
		try {
			input = new FileInputStream(new File(filename));
			Yaml yaml = new Yaml(); 
			data = yaml.load(input);
			input.close();
			
			//parse the input data
			Map<String, Object> m0 = (Map<String, Object>) data;
			config = (List<Map<String, String>>) m0.get("configuration");
			
		} catch (Exception ex) {
			System.out.println("Exception: " + ex);
		}
	}
	
	public MessagePasser(String configuration_filename, String local_name)
	{
		String ip, name, localIP = "";
		int localPort = 0, port;
		self = local_name;
		
		ParseConfigFile(configuration_filename);
		
		boolean flag = false;
		for (Map m: config) {
			ip = (String) m.get("ip");
			port = (int) m.get("port");
			name = (String) m.get("name");
			
			if (name.equals(self)) {
				System.out.println("My name is " + self);
				flag = true;
				localIP = ip;
				localPort = port;
			}
			else if (name.compareTo(self) > 0){
				hosts.put(name, new Node(ip, port));
			}
		}
		
		if (flag == false) {
			System.out.println("local name doesn't exist in the configuration file.");
			System.exit(1);
		}
		
		//Start new threads to connect to other nodes
		for (Map.Entry<String, Node> e : hosts.entrySet()) {
			new Thread(new ClientThread(e.getKey(), e.getValue(), sockets, self, seqnums, q)).start();
		}
		
		//run the server thread
		ServerThread st = new ServerThread(localIP, localPort, q, sockets, seqnums);
		new Thread(st).start();
	}
	
	public void send(Message message)
	{	
		String dest = message.get_dest();
		if (!sockets.containsKey(dest)) {
			System.out.println("Unknown host " + dest);
		}
		else {
			int seqnum = seqnums.get(dest);
			message.set_seqNum(seqnum);
			message.set_source(self);
			seqnums.put(dest, seqnum+1);
			
			try {
				OutputStream out = sockets.get(dest).getOutputStream();
				ObjectOutputStream os = new ObjectOutputStream(out);
				os.writeObject(message);
				os.flush();
			} catch (Exception e) {
				System.out.println("Unable to send message to " + dest + ", try again later.");
				sockets.remove(dest);
				seqnums.remove(dest);
			}
		}
	}
	
	public Message receive()
	{
		Message m = null;
		if (!q.isEmpty())
			m = q.poll();
		return m;
	}
}

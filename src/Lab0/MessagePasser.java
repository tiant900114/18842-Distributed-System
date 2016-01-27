package Lab0;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.io.*;
import java.net.*;

import org.yaml.snakeyaml.Yaml;

public class MessagePasser {
	List<Map<String, String>> config =  null;
	String self = "";
	
	Map<String, Socket> sockets = new HashMap<String, Socket>();
	Map<String, Node> hosts = new HashMap<String, Node>();
	Map<String, Integer> seqnums = new HashMap<String, Integer>();	
	ConcurrentLinkedQueue<Message> q = new ConcurrentLinkedQueue<Message>();
	
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
	
	private void make_connection(String dest, Node n)
	{
		Socket s = null;
		
		String ip = n.get_ip();
		int port = n.get_port();
		
		try {
			s = new Socket(ip, port);
			sockets.put(dest, s);
			seqnums.put(dest, 0);
			n.set_connected();
			
		} catch (Exception e) {
			System.out.println("Connection failed, please try again later.");
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
				flag = true;
				localIP = ip;
				localPort = port;
			}
			
			else {
				hosts.put(name, new Node(ip, port, name));
			}
		}
		
		if (flag == false) {
			System.out.println("local name doesn't exist in the configuration file.");
			System.exit(1);
		}
		
		//run the server thread
		ServerThread st = new ServerThread(localIP, localPort, q);
		new Thread(st).start();
	}
	
	public void send(Message message)
	{
		message.set_source(self);
		
		String dest = message.get_dest();
		int seqnum = seqnums.get(dest);
		message.set_seqNum(seqnum);
		seqnums.put(dest, seqnum+1);
		
		Node n = hosts.get(dest);
		if (!n.is_connected())
			make_connection(dest, n);
		
		try {
			OutputStream out = sockets.get(dest).getOutputStream();
			ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(out));
			os.writeObject(message);
		} catch (Exception e) {
			System.out.println("Unable to send message to " + dest + ", try again later.");
			n.unset_connected();
			sockets.remove(dest);
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

package Lab0;

public class Node {
	private String ip;
	private int port;
	
	public Node(String ip, int port)
	{
		this.ip = ip;
		this.port = port;
	}
		
	public String get_ip()
	{
		return ip;
	}
	
	public int get_port()
	{
		return port;
	}	
}

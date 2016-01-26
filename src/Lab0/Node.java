package Lab0;

public class Node {
	private String ip;
	private int port;
	private boolean connected;
	
	public Node(String ip, int port, String name)
	{
		this.ip = ip;
		this.port = port;
		connected = false;
	}
	
	public boolean is_connected()
	{
		return connected;
	}
	
	public String get_ip()
	{
		return ip;
	}
	
	public int get_port()
	{
		return port;
	}
	
	public void set_connected()
	{
		connected = true;
	}
	
	public void unset_connected()
	{
		connected = false;
	}
}

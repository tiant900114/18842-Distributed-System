package Lab0;

import java.io.Serializable; 

public class Message implements Serializable {
	private int seqnum = 0;
	private String src;
	private String dest;
	private String kind;
	private Object data;
	
	public Message(String dest, String kind, Object data)
	{
		this.dest = dest;
		this.kind = kind;
		this.data = data;
	}
	
	public void set_source(String source)
	{
		src = source;
	}
	
	public void set_seqNum(int sequenceNumber)
	{
		seqnum = sequenceNumber;
	}
	
	public String get_dest()
	{
		return dest;
	}
}

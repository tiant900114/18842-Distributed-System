package Lab0;

public class MessageReceiver implements Runnable {
	
	MessagePasser mp;
	
	public MessageReceiver(MessagePasser mp)
	{
		this.mp = mp;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true)
		{
			Message m = mp.receive();
			if (m != null) {
				System.out.println("from " + m.get_src() + " : " + m.get_data());
			}
		}
	}
}

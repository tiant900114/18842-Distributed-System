package Lab0;

import java.io.IOException;
import java.util.*;

public class Main {

	public static void main(String[] args) {
		
		if (args.length != 1) {
			System.out.println("Only one argument is required: local name.");
			System.exit(1);
		}
		
		String self = args[0];	
		MessagePasser mp = new MessagePasser("testconfig.txt", self);
		//new Thread(new MessageReceiver(mp)).start();
		
		int seqnum = 0;
		String input = "";
		Scanner s = new Scanner(System.in);
		while (!(input = (s.nextLine()).trim()).equals("exit")) {
			Message m1 = mp.receive();
			if (m1 != null) {
				System.out.println("from " + m1.get_src() + " : " + m1.get_data());
			}
			
			String str[] = input.split(":", 2);
			if (str.length == 1) {
				System.out.println("Message format is \"Dest: Message\"");
				continue;
			}
			String dest = str[0].trim();
			String msg = str[1].trim();

			Message m = new Message(dest, "", msg);
			mp.send(m);
		}
	}
}

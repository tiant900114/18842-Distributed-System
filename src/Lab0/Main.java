package Lab0;

import java.util.*;

public class Main {

	public static void main(String[] args) {
		
		if (args.length != 1) {
			System.out.println("Only one argument is required: local name.");
			System.exit(1);
		}
		String self = args[0];	
		//MessagePasser mp = new MessagePasser("lab0config.txt", self);
		
		int seqnum = 0;
		String input = "";
		Scanner s = new Scanner(System.in);
		while (!(input = (s.nextLine()).trim()).equals("exit")) {
			
			String str[] = input.split(":", 2);
			if (str.length == 1) {
				System.out.println("Message format is \"Dest: Message\"");
				continue;
			}
			String dest = str[0].trim();
			String msg = str[1].trim();
			
			Message m = new Message(dest, "", msg);
			//mp.Send(msg);
		}
	}
}

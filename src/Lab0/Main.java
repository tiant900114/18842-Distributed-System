package Lab0;

import java.util.*;

public class Main {

	public static void main(String[] args) {
		
		if (args.length != 1) {
			System.out.println("Only one argument is required: local name.");
			System.exit(1);
		}
		
		String self = args[0];	
		MessagePasser mp = new MessagePasser("testconfig.txt", self);
		
		String input = "";
		Scanner s = new Scanner(System.in);
		while (!(input = (s.nextLine()).trim()).equals("exit")) {
			String str[] = input.split(":", 2);
			if (input.length() != 0) {
				if (str.length == 1) {
					System.out.println("Message format is \"Dest: Message\"");
				}
				else {
					String dest = str[0].trim();
					String msg = str[1].trim();

					Message m = new Message(dest, "", msg);
					mp.send(m);					
				}
			}
			
			Message m1 = mp.receive();
			if (m1 != null) {
				System.out.println("from " + m1.get_src() + " : " + m1.get_data());
			}
		}
		
		s.close();
		System.exit(0);
	}
}

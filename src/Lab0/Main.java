package Lab0;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.*;

public class Main {
	private static String fetch_config_file()
	{
		String filename = "config.txt";
		
		try {
			URLConnection con = new URL("https://www.andrew.cmu.edu/user/tiant/testconfig.txt").openConnection();
			Scanner s = new Scanner(con.getInputStream());
			s.useDelimiter("\\Z");
			String content = s.next();
			s.close();
			FileOutputStream out = new FileOutputStream(filename, false);
			out.write(content.getBytes());
			out.close();
		} catch (Exception e) {
			System.out.println("Unable to fetch configuration file");
			System.exit(1);
		}
		
		return filename;
	}

	public static void main(String[] args) throws IOException {
		
		if (args.length != 1) {
			System.out.println("Only one argument is required: local name.");
			System.exit(1);
		}
		
		String filename = fetch_config_file();
		
		String self = args[0];	
		MessagePasser mp = new MessagePasser(filename, self);
		
		HashSet<String> kindSet = new HashSet<>(Arrays.asList("Ack", "Reply"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			System.out.println("main thread is too hyper to sleep.");
		}
		while (true) {
			System.out.println();
			System.out.println(self+": what do you want to do?");
			System.out.println("1. Send Message");
			System.out.println("2. Receive Message");
			System.out.println("3. Exit");
			System.out.println();
			
			double userChoice;
			try{
				userChoice = Double.parseDouble(br.readLine().trim());
			}
			catch(Exception e){
				continue;
			}
			System.out.println();
			if(userChoice == 1){
				String dest = "";
				while (dest.length() == 0) {
					System.out.print("Enter Destination: ");
					dest = br.readLine().trim();
					if(dest.equals(self)){
						System.out.println("Dont be stupid "+self+"! You cannot send message to yourself!\n");
						dest = "";
					}
				}

				String kind = "";
				while (kind.length() == 0) {
					System.out.print("Enter Kind of Message: ");
					kind = br.readLine().trim();
					if(!kindSet.contains(kind)){
						kind = "";
						System.out.println("Can only be - Ack, Reply");
					}
				}

				String msg = "";
				while (msg.length() == 0) {
					System.out.print("Enter Message: ");
					msg = br.readLine().trim();
				}

				Message m = new Message(dest, kind, msg);
				mp.send(m);									
			}
			else if(userChoice == 2){
				// receive
				Message m1 = mp.receive();
				if (m1 != null) {
					System.out.println();
					System.out.println("Source: " + m1.get_src());
					System.out.println("Message: "+  m1.get_data());
					System.out.println();
				}
				else{
					System.out.println("No messages for you "+self+"!");
				}
			}
			else if(userChoice == 3){
				br.close();
				System.exit(0);
			}
			else{
				System.out.println();
				System.out.println("Invalid Choice. Please try again!");
				System.out.println();
			}
			
		}
	}
}

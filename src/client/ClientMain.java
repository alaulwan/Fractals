package client;

public class ClientMain {

	public static void main(String[] args) {
		if (args.length > 8) {
			Client c = new Client(args);
			c.satrtClient();
		} else {
			System.out.println("The client needs at least nine arguments to run.\n"
					+ "Please read the attached document.");
		}
	}

}

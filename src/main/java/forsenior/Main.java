package forsenior;

import java.io.File;
import java.io.IOException;

public class Main {
	public static void main(String... args) {
		String credentials = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");

		if (credentials == null || !new File(credentials).exists()) {
			System.err.println("Please set correct environment variable GOOGLE_APPLICATION_CREDENTIALS");
			System.exit(1);
		} else if (args.length == 0) {
			System.err.println("Need sound file");
			System.exit(1);
		} else if (!new File(args[0]).exists()) {
			System.err.println("File not exists: " + args[0]);
			System.exit(1);
		}

		try {
			SpeechToText
				.process(args[0])
				.forEach(MorphemeAnalyzer::analyze);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

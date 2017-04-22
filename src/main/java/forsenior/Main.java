package forsenior;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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

		try (InputStream is = ClassLoader.getSystemResourceAsStream("forsenior.properties")) {
			Properties prop = new Properties();
			prop.load(is);
			TextToSpeech tts = new TextToSpeech(prop.getProperty("ttsid"), prop.getProperty("ttssecret"));
			SpeechToText
				.process(args[0])
//				.forEach(MorphemeAnalyzer::analyze)
				.forEach(tts::process);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

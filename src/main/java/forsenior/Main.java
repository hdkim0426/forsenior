package forsenior;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javazoom.jlgui.basicplayer.BasicPlayer;

public class Main {
	public static void main(String... args) {
		String credentials = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");

		if (credentials == null || !new File(credentials).exists()) {
			System.err.println("Please set correct environment variable GOOGLE_APPLICATION_CREDENTIALS");
			System.exit(1);
		}

		try (InputStream is = ClassLoader.getSystemResourceAsStream("forsenior.properties")) {
			Properties prop = new Properties();
			prop.load(is);
			TextToSpeech tts = new TextToSpeech(prop.getProperty("ttsid"), prop.getProperty("ttssecret"));
			
			// recording
			Recoder recoder = new Recoder(5000, new File("b.wav"));
			File recordedFile = recoder.record();
			System.out.println("Recorded file: " + recordedFile.getAbsolutePath());
			
			// result of recording -> String
			List<String> textList = SpeechToText.process(recordedFile.getAbsolutePath());
			
			// String -> Sentence
//			List<Sentence> sentenceList = MorphemeAnalyzer.analyze(textList);
			
			// Sentence -> String
//			List<String> toAnswer(sentenceList);
			
			// String -> speech file
			String text = String.join(", ", textList);
			System.out.println("Recorded text: " + text);
			String response = tts.process(text);
			System.out.println("Speech file: " + response);
			
			// Play speech file
			BasicPlayer player = new BasicPlayer();
			player.open(new File(response));
			player.play();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

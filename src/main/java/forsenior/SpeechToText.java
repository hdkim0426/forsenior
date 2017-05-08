package forsenior;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.cloud.speech.spi.v1.SpeechClient;
import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.protobuf.ByteString;

public class SpeechToText {
	public static List<String> process(final String fileName) throws IOException, Exception {
		// Instantiates a client
		try (final SpeechClient speech = SpeechClient.create()) {
		    // Reads the audio file into memory
		    final Path path = Paths.get(fileName);
		    final byte[] data = Files.readAllBytes(path);
		    final ByteString audioBytes = ByteString.copyFrom(data);

		    // Builds the sync recognize request
		    RecognitionConfig config = RecognitionConfig.newBuilder()
		        .setEncoding(AudioEncoding.LINEAR16)
		        .setSampleRateHertz(44100)
		        .setLanguageCode("ko-KR")
		        .build();
		    RecognitionAudio audio = RecognitionAudio.newBuilder()
		        .setContent(audioBytes)
		        .build();

		    // Performs speech recognition on the audio file
		    List<String> result = new ArrayList<>();
		    RecognizeResponse response = speech.recognize(config, audio);
		    List<SpeechRecognitionResult> srrList = response.getResultsList();
		    return srrList.stream()
		    	.map(SpeechRecognitionResult::getAlternativesList)
		    	.flatMap(List::stream)
		    	.map(SpeechRecognitionAlternative::getTranscript)
		    	.collect(Collectors.toList());
//		    for (SpeechRecognitionResult r: srrList) {
//		    	List<SpeechRecognitionAlternative> sraList = r.getAlternativesList();
//		    	for (SpeechRecognitionAlternative sra: sraList)
//		    		result.add(sra.getTranscript());
//		    }

//		    return result;
		}
	}
}

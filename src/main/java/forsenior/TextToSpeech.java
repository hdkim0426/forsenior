package forsenior;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

public class TextToSpeech {
	private static final String API_URL = "https://openapi.naver.com/v1/voice/tts.bin";

	private String id;
	private String secret;

	public TextToSpeech(final String id, final String secret) {
		this.id = id;
		this.secret = secret;
	}

	public void process(final String text) {
        try {
        	final URL url = new URL(API_URL);
            final HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("X-Naver-Client-Id", id);
            con.setRequestProperty("X-Naver-Client-Secret", secret);
            // post request
            String postParams = "speaker=mijin&speed=0&text=" + URLEncoder.encode(text, "UTF-8");
            con.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            	wr.writeBytes(postParams);
            	wr.flush();
            }
            if (con.getResponseCode() == 200) {
            	try (InputStream is = con.getInputStream()) {
            		int read = 0;
            		byte[] bytes = new byte[1024];
            		String tempname = Long.valueOf(new Date().getTime()).toString() + ".mp3";
            		try (OutputStream outputStream = new FileOutputStream(new File(tempname))) {
            			while ((read =is.read(bytes)) != -1) {
            				outputStream.write(bytes, 0, read);
            			}
            		}
            	}
            } else {
            	try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream()))) {
            		String inputLine;
            		StringBuffer response = new StringBuffer();
            		while ((inputLine = br.readLine()) != null) {
            			response.append(inputLine);
            		}
            		System.out.println(response.toString());
            	}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}

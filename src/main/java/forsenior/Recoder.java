package forsenior;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Control;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Recoder {
	private AudioFormat format;
	private DataLine.Info info;
	private TargetDataLine line;
	private long recordTimeMillis;
	private File file;
	
	public Recoder(long recordTimeMillis) throws Exception { this(recordTimeMillis, File.createTempFile("voice", ".wav")); }
	public Recoder(long recordTimeMillis, File file) throws Exception {
		format = new AudioFormat(44100, 16, 1, true, true);
		info = new DataLine.Info(TargetDataLine.class, format);
		this.recordTimeMillis = recordTimeMillis;
		this.file = file;
		if (!AudioSystem.isLineSupported(info))
			throw new Exception("Line not supported");
	}

	public File record() {
		new Thread(() -> {
			try {
				Thread.sleep(recordTimeMillis);
			} catch (InterruptedException e) {
				// Nothing to do
			}
			this.finish();
		}).start();
		this.start();
		return file;
	}

	private void start() {
		System.out.println("Recording start.");
		try {
			line = (TargetDataLine) AudioSystem.getLine(info);
			line.open(format);
			line.start();
			try (AudioInputStream stream = new AudioInputStream(line)) {
				AudioSystem.write(stream, AudioFileFormat.Type.WAVE, file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void finish() {
		line.stop();
		line.close();
		System.out.println("Done.");
	}
}

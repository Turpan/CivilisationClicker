package civilisationClicker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MusicPlayer implements Runnable{
	static boolean loop = true;
	static int lastPlayed;
	boolean muted;
	BooleanControl control = null;
	FloatControl volumeControl = null;
	int numberOfMusicFiles;
	double volume;
	String dataFileLocation;
	String[] musicFileLocation;
	private SourceDataLine musicLine;
	public void play() {
		new Thread(this).start();
	}
	@Override
	public void run() {
		File musicFile = new File(nextSong());
		AudioInputStream musicInputStream = null;
		try {
			musicInputStream = AudioSystem.getAudioInputStream(musicFile);
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AudioFormat musicFormat = musicInputStream.getFormat();
		musicLine = null;
		DataLine.Info musicInfo = new DataLine.Info(SourceDataLine.class, musicFormat);
		try {
			musicLine = (SourceDataLine) AudioSystem.getLine(musicInfo);
			musicLine.open(musicFormat);
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setVolume(volume);
		musicLine.start();
		control = (BooleanControl)musicLine.getControl(BooleanControl.Type.MUTE);
		control.setValue(muted);
		int nBytesRead = 0;
		byte[] abData = new byte[8192];
		while (nBytesRead != -1) {
			try {
				nBytesRead = musicInputStream.read(abData, 0, abData.length);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (nBytesRead >= 0) {
				//int nBytesWritten = musicLine.write(abData, 0, nBytesRead);
				musicLine.write(abData, 0, nBytesRead);				
			}
		}
		if (loop) {
			play();
		}
		return;
	}
	void muteLine() {
		control = (BooleanControl)musicLine.getControl(BooleanControl.Type.MUTE);
		if (control.getValue() == true) {
			control.setValue(false);
			muted = false;
		} else {
			control.setValue(true);
			muted = true;
		}
	}
	void setVolume(double volume) {
		this.volume = MathFunctions.audioLogScaling(volume);
		if (musicLine.isControlSupported(FloatControl.Type.VOLUME)) {
	        volumeControl = (FloatControl)musicLine.getControl( FloatControl.Type.VOLUME );
	        double min = volumeControl.getMinimum();
	        double max = volumeControl.getMaximum();
	        double value = ((max - min) * volume) + min;
	        volumeControl.setValue((float) value);
	    } else if (musicLine.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
	    	volumeControl = (FloatControl)musicLine.getControl( FloatControl.Type.MASTER_GAIN );
	        double min = volumeControl.getMinimum();
	        double max = volumeControl.getMaximum();
	        double value = ((max - min) * volume) + min;
	        volumeControl.setValue((float) value);
	    }
	}
	String nextSong() {
		int nextSong = 0;
		Random rand = new Random();
		while (nextSong == lastPlayed || nextSong == 0) {
			nextSong = rand.nextInt(numberOfMusicFiles) + 1;
		}
		lastPlayed = nextSong;
		return musicFileLocation[nextSong-1];
	}
	void loadMusic() {
		File dataFile = new File(dataFileLocation);
		Scanner scan;
		try {
			scan = new Scanner(dataFile);
			scan.useDelimiter("\\Z");
			String content = scan.next();
			scan.close();
			content = content.replaceAll("[\r\n]+", "");
			Scanner musicFileScanner = new Scanner(content);
			musicFileScanner.useDelimiter(";");
			numberOfMusicFiles = musicFileScanner.nextInt();
			initialiseArrays();
			for (int i=0; i<numberOfMusicFiles; i++) {
				musicFileScanner.next();
				musicFileLocation[i] = musicFileScanner.next();
			}
			musicFileScanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	void initialiseArrays() {
		musicFileLocation = new String[numberOfMusicFiles];
	}
}

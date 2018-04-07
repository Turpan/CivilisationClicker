package civilisationClicker;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundEngine {
	static Clip genericClick;
	static Clip provinceClick;
	static Clip buttonClicks[][];
	static double volume;
	static FloatControl clickSoundControl = null;
	static void playClickSound() {
		genericClick.setFramePosition(0);
		genericClick.start();
	}
	static void playProvinceClickSound() {
		provinceClick.setFramePosition(0);
		provinceClick.start();
	}
	static void playPointClickSound(int screenType) {
		Random rand = new Random();
		int n = rand.nextInt(3);
		buttonClicks[screenType][n].setFramePosition(0);
		buttonClicks[screenType][n].start();
	}
	static void loadClickerSounds() {
		try {
			genericClick = AudioSystem.getClip();
			provinceClick = AudioSystem.getClip();
			//Just using the integer 3 because that's how many sounds we have per 
			//button, but if necessary we can alter the number of columns in a row
			buttonClicks = new Clip[DataBase.screenTypes.size()][3];
			for (int i = 0; i<buttonClicks.length; i++) {
				for (int j = 0; j<buttonClicks[i].length;j++) {
					buttonClicks[i][j] = AudioSystem.getClip();
					loadGenericClip(DataBase.screenTypes.get(i) + "-click" + (j+1) +".wav", buttonClicks[i][j]);
				}
			}
		} catch (LineUnavailableException e1) {
			e1.printStackTrace();
		}
		loadGenericClip("genericclick.wav", genericClick);
		loadGenericClip("provinceclick.wav", provinceClick);
	}
	static void loadGenericClip(String fileName, Clip clipName){
		//Creates audioInputStream with the file(in Sound) outputs it as a clip
		// NOTE: assumes file is in sound folder. Include the .wav in the name
		// Also, like, the clip has to be initialised as AudioSystem.getClip()
		File clipFile = new File("sound/"+fileName);
		try {
			AudioInputStream clipAudioStream = AudioSystem.getAudioInputStream(clipFile);
			clipName.open(clipAudioStream);
		} catch (UnsupportedAudioFileException |IOException | LineUnavailableException e ) {
			e.printStackTrace();
		}
	}
	static void clipVolumeChange(Clip clip, double volume) {
		if (clip.isControlSupported(FloatControl.Type.VOLUME)) {
			clickSoundControl = (FloatControl)clip.getControl( FloatControl.Type.VOLUME );
	        double min = clickSoundControl.getMinimum();
	        double max = clickSoundControl.getMaximum();
	        double value = ((max - min) * volume) + min;
	        clickSoundControl.setValue((float) value);
	    } else if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
	    	clickSoundControl = (FloatControl)clip.getControl( FloatControl.Type.MASTER_GAIN );
	        double min = clickSoundControl.getMinimum();
	        double max = clickSoundControl.getMaximum();
	        double value = ((max - min) * volume) + min;
	        clickSoundControl.setValue((float) value);
	    }
	}
	static void setVolume(double newVolume) {
		volume = MathFunctions.audioLogScaling(newVolume);
		SoundEngine.clipVolumeChange(genericClick, volume);
		SoundEngine.clipVolumeChange(provinceClick, volume);
		for (int i = 0; i<buttonClicks.length; i++) {
			for (int j = 0; j<buttonClicks[i].length;j++) {
				clipVolumeChange(buttonClicks[i][j], volume);
			}
		}
	}
}
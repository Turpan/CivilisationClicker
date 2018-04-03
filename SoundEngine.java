package civilisationClicker;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundEngine {
	static Clip genericClick;
	static Clip provinceClick;
	static double volume;
	static FloatControl clickSoundControl = null;
	private SoundEngine() {
	}
	
	static {
		loadSounds();
		
	}
	static void playClickSound() {
		genericClick.setFramePosition(0);
		genericClick.start();
	}
	static void playProvinceClickSound() {
		provinceClick.setFramePosition(0);
		provinceClick.start();
	}
	static void loadSounds() {
		File clickFile = new File("sound/genericclick.wav");
		File provinceClickFile = new File("sound/provinceclick.wav");
		try {
			AudioInputStream clickstream = AudioSystem.getAudioInputStream(clickFile);
			AudioInputStream provinceClickStream = AudioSystem.getAudioInputStream(provinceClickFile);
			genericClick = AudioSystem.getClip();
			provinceClick = AudioSystem.getClip();
			genericClick.open(clickstream);
			provinceClick.open(provinceClickStream);
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
	}
}

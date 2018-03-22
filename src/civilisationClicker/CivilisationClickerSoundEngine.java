package civilisationClicker;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class CivilisationClickerSoundEngine {
	Clip genericClick;
	Clip provinceClick;
	CivilisationClickerSoundEngine() {
		loadSounds();
	}
	void playClickSound() {
		genericClick.setFramePosition(0);
		genericClick.start();
	}
	void playProvinceClickSound() {
		provinceClick.setFramePosition(0);
		provinceClick.start();
	}
	void loadSounds() {
		File clickFile = new File("sound/genericclick.wav");
		File provinceClickFile = new File("sound/provinceclick.wav");
		try {
			AudioInputStream clickstream = AudioSystem.getAudioInputStream(clickFile);
			AudioInputStream provinceClickStream = AudioSystem.getAudioInputStream(provinceClickFile);
			genericClick = AudioSystem.getClip();
			provinceClick = AudioSystem.getClip();
			genericClick.open(clickstream);
			provinceClick.open(provinceClickStream);
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

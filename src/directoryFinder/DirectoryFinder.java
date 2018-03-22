package directoryFinder;

public class DirectoryFinder{
	public String findOwnDirectory() {
		String directory = "";
		directory = this.getClass().getProtectionDomain().getCodeSource().getLocation().toString();
		StringBuilder directoryBuilder = new StringBuilder(directory);
		int length = directoryBuilder.length();
		directoryBuilder.delete(length - 4, length); //remember to remove the directory builder stuff when you package this as a jar
		directoryBuilder.delete(0, 6);
		directory = directoryBuilder.toString();
		return directory;
	}
}

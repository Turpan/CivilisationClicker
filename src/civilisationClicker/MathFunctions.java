package civilisationClicker;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MathFunctions {
	public static String withSuffix(double count) { // taken from https://stackoverflow.com/questions/9769554/how-to-convert-number-into-k-thousands-m-million-and-b-billion-suffix-in-jsp
	    if (count < 1000) return "" + (int) count;
	    int exp = (int) (Math.log(count) / Math.log(1000));
	    return String.format("%.1f %c",
	                         count / Math.pow(1000, exp),
	                         "kMGTPE".charAt(exp-1));
	}
	public static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) { //code taken from https://stackoverflow.com/questions/10245220/java-image-resize-maintain-aspect-ratio
	    int original_width = imgSize.width;
	    int original_height = imgSize.height;
	    int bound_width = boundary.width;
	    int bound_height = boundary.height;
	    int new_width = original_width;
	    int new_height = original_height;
	    if (original_width > bound_width) {
	        new_width = bound_width;
	        new_height = (new_width * original_height) / original_width;
	    }
	    if (new_height > bound_height) {
	        new_height = bound_height;
	        new_width = (new_height * original_width) / original_height;
	    }
	    return new Dimension(new_width, new_height);
	}
	public static int parseInt(String toParse) {
		int parsedInt = 0;
		try {
			parsedInt = Integer.parseInt(toParse);
		} catch (NumberFormatException e){
			parsedInt = 0;
		}
		return parsedInt;
	}
	public static int parseInt(String toParse, int defaultOutcome) {
		int parsedInt = 0;
		try {
			parsedInt = Integer.parseInt(toParse);
		} catch (NumberFormatException e){
			parsedInt = defaultOutcome;
		}
		return parsedInt;
	}
	public static double parseDouble(String toParse) {
		double parsedDouble = 0;
		try {
			parsedDouble = Double.parseDouble(toParse);
		} catch (NumberFormatException e){
			parsedDouble = 0;
		}
		return parsedDouble;
	}
	public static double parseDouble(String toParse, double defaultOutcome) {
		double parsedDouble = 0;
		try {
			parsedDouble = Double.parseDouble(toParse);
		} catch (NumberFormatException e){
			parsedDouble = defaultOutcome;
		}
		return parsedDouble;
	}
	private static byte[] createChecksum(File file) throws Exception {
		InputStream input = new FileInputStream(file);
		byte[] buffer = new byte[2048];
		MessageDigest digest = MessageDigest.getInstance("MD5");
		int bytesRead = -1;
		do {
			bytesRead = input.read(buffer);
			if (bytesRead > -1) {
				digest.update(buffer, 0, bytesRead);
			}
		} while (bytesRead > -1);
		input.close();
		return digest.digest();
	}
	public static String getMD5CheckSum(File file) throws Exception{
		byte[] bytes = createChecksum(file);
		String result = "";
		for (int i=0; i<bytes.length; i++) {
			result += Integer.toString( ( bytes[i] & 0xff ) + 0x100, 16).substring( 1 );
		}
		return result;
	}
	public static String getMD5CheckSum(String data) {
		byte[] stringData;
		try {
			stringData = data.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			stringData = data.getBytes();
		}
		String result = "";
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			System.out.println("Please report this bug.");
			return result; //this should never happen
		}
		byte[] digest = md.digest(stringData);
		for (int i=0; i<digest.length; i++) {
			result += Integer.toString( ( digest[i] & 0xff ) + 0x100, 16).substring( 1 );
		}
		return result;
	}
}

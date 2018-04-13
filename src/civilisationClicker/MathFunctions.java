package civilisationClicker;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

public class MathFunctions {
	static final Font systemFont = new JLabel().getFont();
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
	public static double audioLogScaling(double inputVol) {
		if (inputVol == 0) {
			return 0.0;
		} else {
			return java.lang.Math.log10(inputVol)/2;
		}
	}
	public static String[] splitString(String toSplit, int width) {
		List<String> lines = new ArrayList<String>();
		boolean linesRemaining = false;
		do {
			int splitPoint = findSplitPoint(toSplit, width, systemFont);
			linesRemaining = toSplit.length() != splitPoint;
			if (String.valueOf(toSplit.charAt(0)).equals(" ")) {
				lines.add(toSplit.substring(1, splitPoint));
			} else {
				lines.add(toSplit.substring(0, splitPoint));
			}
			StringBuilder sb = new StringBuilder(toSplit);
			sb.delete(0, splitPoint);
			toSplit = sb.toString();
		} while (linesRemaining);
		String[] result = new String[lines.size()];
		return lines.toArray(result);
	}
	public static String[] splitString(String toSplit, int width, Font font) {
		List<String> lines = new ArrayList<String>();
		boolean linesRemaining = false;
		do {
			int splitPoint = findSplitPoint(toSplit, width, font);
			linesRemaining = toSplit.length() != splitPoint;
			if (String.valueOf(toSplit.charAt(0)).equals(" ")) {
				lines.add(toSplit.substring(1, splitPoint));
			} else {
				lines.add(toSplit.substring(0, splitPoint));
			}
			StringBuilder sb = new StringBuilder(toSplit);
			sb.delete(0, splitPoint);
			toSplit = sb.toString();
		} while (linesRemaining);
		String[] result = new String[lines.size()];
		return lines.toArray(result);
	}
	private static int findSplitPoint(String toSplit, int width, Font font) {
		int currentWidth = 0;
		int splitPoint = 0;
		int lastSpace = 0;
		for (int i=0; i<toSplit.length(); i++)  {
			int charWidth = getCharWidth(toSplit.charAt(i), font);
			currentWidth += charWidth;
			if (currentWidth > width) {
				splitPoint = i - 1;
				break;
			} else {
				splitPoint = i;
			}
		}
		if (splitPoint == toSplit.length() - 1) return toSplit.length();
		for (int i=0; i<splitPoint; i++) {
			if (String.valueOf(toSplit.charAt(i)).equals(" ")) {
				lastSpace = i;
			}
		}
		if (lastSpace != 0) splitPoint = lastSpace;
		return splitPoint;
	}
	public static int getStringWidth(String toTest) {
		int width = 0;
		char[] chars = toTest.toCharArray();
		for (char Char : chars) {
			width += getCharWidth(Char, systemFont);
		}
		return width;
	}
	public static int getStringWidth(String toTest, Font font) {
		int width = 0;
		char[] chars = toTest.toCharArray();
		for (char Char : chars) {
			width += getCharWidth(Char, font);
		}
		return width;
	}
	private static int getCharWidth(char Char, Font font) {
		JLabel label = new JLabel();
		FontMetrics metrics = label.getFontMetrics(font);
		int width = metrics.charWidth(Char);
		return width;
	}
	public static int getCharHeight(Font font) {
		JLabel label = new JLabel();
		FontMetrics metrics = label.getFontMetrics(font);
		return metrics.getHeight();
	}
	public static boolean canDisplay(char toDisplay) {
		return systemFont.canDisplay(toDisplay);
	}
	public static boolean canDisplay(char toDisplay, Font font) {
		return font.canDisplay(toDisplay);
	}
}

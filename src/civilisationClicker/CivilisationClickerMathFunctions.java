package civilisationClicker;

import java.awt.Dimension;

public class CivilisationClickerMathFunctions {
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
	public static double parseDouble(String toParse, int defaultOutcome) {
		double parsedDouble = 0;
		try {
			parsedDouble = Double.parseDouble(toParse);
		} catch (NumberFormatException e){
			parsedDouble = defaultOutcome;
		}
		return parsedDouble;
	}
}

package civilisationClicker;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class MapCache {
	String mapImageHash;
	String mapXMLHash;
	List<Rectangle> connectionLines = new ArrayList<Rectangle>();
	MapCache() {
		
	}
	void setMapImageHash(String mapImageHash) {
		this.mapImageHash = mapImageHash;
	}
	void setMapXMLHash(String mapXMLHash) {
		this.mapXMLHash = mapXMLHash;
	}
	void addConnectionLine(Rectangle connectionLine) {
		connectionLines.add(connectionLine);
	}
}

package civilisationClicker;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Map {
	List<Province> provinceList = new ArrayList<Province>();
	List<Color> provinceColors = new ArrayList<Color>();
	String map;
	String mapfile;
	@Override
	public boolean equals(Object o) {
		 if (o == this) return true;
	     if (!(o instanceof Map)) {
	    	 return false;
	     }
	     Map map = (Map) o;
	     return map.map.equals(this.map);
	}
	@Override
	public int hashCode() {
		int result = 17;
        result = 31 * result + map.hashCode();
        return result;
	}
	Map(String map) {
		this.map = map;
	}
	Province getProvince(int province) {
		return provinceList.get(province);
	}
	void setProvinceList(ArrayList<Province> provinceList) {
		this.provinceList = new ArrayList<Province>(provinceList);
	}
	void setMapFile(String mapfile) {
		this.mapfile = mapfile;
	}
	void addProvince(Province province) {
		provinceList.add(province);
	}
	void compileColors() {
		for (Province province : provinceList) {
			int red = province.red;
			int green = province.green;
			int blue = province.blue;
			Color color = new Color(red, green, blue);
			provinceColors.add(color);
		}
	}
	void createDevelopementData() {
		for (Province province : provinceList) {
			province.createProvinceDevelopement();
		}
	}
}

package civilisationClicker;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class CivilisationClickerMap {
	List<CivilisationClickerProvince> provinceList = new ArrayList<CivilisationClickerProvince>();
	List<Color> provinceColors = new ArrayList<Color>();
	String map;
	String mapfile;
	@Override
	public boolean equals(Object o) {
		 if (o == this) return true;
	     if (!(o instanceof CivilisationClickerMap)) {
	    	 return false;
	     }
	     CivilisationClickerMap map = (CivilisationClickerMap) o;
	     return map.map.equals(this.map);
	}
	@Override
	public int hashCode() {
		int result = 17;
        result = 31 * result + map.hashCode();
        return result;
	}
	CivilisationClickerMap(String map) {
		this.map = map;
	}
	CivilisationClickerProvince getProvince(int province) {
		return provinceList.get(province);
	}
	void setProvinceList(ArrayList<CivilisationClickerProvince> provinceList) {
		this.provinceList = new ArrayList<CivilisationClickerProvince>(provinceList);
	}
	void setMapFile(String mapfile) {
		this.mapfile = mapfile;
	}
	void addProvince(CivilisationClickerProvince province) {
		provinceList.add(province);
	}
	void compileColors() {
		for (CivilisationClickerProvince province : provinceList) {
			int red = province.red;
			int green = province.green;
			int blue = province.blue;
			Color color = new Color(red, green, blue);
			provinceColors.add(color);
		}
	}
	void createDevelopementData() {
		for (CivilisationClickerProvince province : provinceList) {
			province.createProvinceDevelopement();
		}
	}
}

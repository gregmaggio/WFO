/**
 * 
 */
package ca.datamagic.wfo.dao;

/**
 * @author Greg
 *
 */
public class BaseDAO {
	private static String dataPath = "C:/Dev/Applications/WFO/src/main/resources/data";
	
	public static String getDataPath() {
		return dataPath;
	}
	
	public static void setDataPath(String newVal) {
		dataPath = newVal;
	}
}

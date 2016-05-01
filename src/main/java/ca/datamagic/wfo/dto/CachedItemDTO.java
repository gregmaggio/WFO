/**
 * 
 */
package ca.datamagic.wfo.dto;

/**
 * @author Greg
 *
 */
public class CachedItemDTO {
	private String _key = null;
	private WFODTO _wfo = null;
	
	public CachedItemDTO() {
	}

	public CachedItemDTO(String key, WFODTO wfo) {
		_key = key;
		_wfo = wfo;
	}
	
	public String getKey() {
		return _key;
	}
	
	public void setKey(String newVal) {
		_key = newVal;
	}
	
	public WFODTO getWFO() {
		return _wfo;
	}
	
	public void setWFO(WFODTO newVal) {
		_wfo = newVal;
	}
}

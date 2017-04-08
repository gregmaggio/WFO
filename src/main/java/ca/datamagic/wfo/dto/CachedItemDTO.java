/**
 * 
 */
package ca.datamagic.wfo.dto;

import java.util.List;

/**
 * @author Greg
 *
 */
public class CachedItemDTO {
	private String _key = null;
	private WFODTO _wfo = null;
	private List<WFODTO> _wfos = null;
	
	public CachedItemDTO() {
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
	
	public List<WFODTO> getWFOList() {
		return _wfos;
	}
	
	public void setWFOList(List<WFODTO> newVal) {
		_wfos = newVal;
	}
}

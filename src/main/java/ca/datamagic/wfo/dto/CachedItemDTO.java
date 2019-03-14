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
	private String key = null;
	private WFODTO wfo = null;
	private List<WFODTO> wfos = null;
	
	public CachedItemDTO() {
	}
	
	public String getKey() {
		return this.key;
	}
	
	public void setKey(String newVal) {
		this.key = newVal;
	}
	
	public WFODTO getWFO() {
		return this.wfo;
	}
	
	public void setWFO(WFODTO newVal) {
		this.wfo = newVal;
	}
	
	public List<WFODTO> getWFOList() {
		return this.wfos;
	}
	
	public void setWFOList(List<WFODTO> newVal) {
		this.wfos = newVal;
	}
}

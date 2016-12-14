/**
 * 
 */
package ca.datamagic.wfo.dto;

import org.opengis.feature.simple.SimpleFeature;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Greg
 *
 */
public class StationDTO {
	private String _stationId = null;
	private String _stationName = null;
	private String _state = null;
	private Double _latitude = null;
	private Double _longitude = null;
	private Boolean _hasRadiosonde = null;

	public StationDTO() {
	}

	public StationDTO(SimpleFeature feature) {
		_stationId = (String)feature.getAttribute("station_id");
		_stationName = (String)feature.getAttribute("station_na");
		_state = (String)feature.getAttribute("state");
		_longitude = (Double)feature.getAttribute("latitude");
		_latitude = (Double)feature.getAttribute("longitude");
		_hasRadiosonde = ((String)feature.getAttribute("has_rad")).compareToIgnoreCase("Y") == 0;
	}
	
	public String getStationId() {
		return _stationId;
	}
	
	public String getStationName() {
		return _stationName;
	}
	
	public String getState() {
		return _state;
	}
	
	public Double getLatitude() {
		return _latitude;
	}
	
	public Double getLongitude() {
		return _longitude;
	}
	
	@JsonProperty(value = "hasRadiosonde")
	public Boolean hasRadiosonde() {
		return _hasRadiosonde;
	}
	
	public void setStationId(String newVal) {
		_stationId = newVal;
	}
	
	public void setStationName(String newVal) {
		_stationName = newVal;
	}
	
	public void setState(String newVal) {
		_state = newVal;
	}
	
	public void setLatitude(Double newVal) {
		_latitude = newVal;
	}
	
	public void setLongitude(Double newVal) {
		_longitude = newVal;
	}
	
	@JsonProperty(value = "hasRadiosonde")
	public void setHasRadiosonde(Boolean newVal) {
		_hasRadiosonde = newVal;
	}
}

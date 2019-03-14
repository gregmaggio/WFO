package ca.datamagic.wfo.dto;

import org.opengis.feature.simple.SimpleFeature;

public class WFODTO {
	private String cwa = null;
	private String wfo = null;
	private Double latitude = null;
	private Double longitude = null;
	private String region = null;
	private String fullStationId = null;
	private String cityState = null;
	private String city = null;
	private String state = null;
	private String stateAbbreviation = null;
	private String radar = null;
	
	public WFODTO() {
	}

	public WFODTO(SimpleFeature feature) {
		this.cwa = (String)feature.getAttribute("CWA");
		this.wfo = (String)feature.getAttribute("WFO");
		this.longitude = (Double)feature.getAttribute("LON");
		this.latitude = (Double)feature.getAttribute("LAT");
		this.region = (String)feature.getAttribute("REGION");
		this.fullStationId = (String)feature.getAttribute("FULLSTAID");
		this.cityState = (String)feature.getAttribute("CITYSTATE");
		this.city = (String)feature.getAttribute("CITY");
		this.state = (String)feature.getAttribute("STATE");
		this.stateAbbreviation = (String)feature.getAttribute("ST");
		this.radar = (String)feature.getAttribute("RADAR");
	}
	
	public String getCWA() {
		return this.cwa;
	}
	
	public String getWFO() {
		return this.wfo;
	}
	
	public Double getLatitude() {
		return this.latitude;
	}
	
	public Double getLongitude() {
		return this.longitude;
	}
	
	public String getRegion() {
		return this.region;
	}
	
	public String getFullStationId() {
		return this.fullStationId;
	}
	
	public String getCityState() {
		return this.cityState;
	}
	
	public String getCity() {
		return this.city;
	}
	
	public String getState() {
		return this.state;
	}
	
	public String getStateAbbreviation() {
		return this.stateAbbreviation;
	}
	
	public String getRadar() {
		return this.radar;
	}
	
	public void setCWA(String newVal) {
		this.cwa = newVal;
	}
	
	public void setWFO(String newVal) {
		this.wfo = newVal;
	}
	
	public void setLatitude(Double newVal) {
		this.latitude = newVal;
	}
	
	public void setLongitude(Double newVal) {
		this.longitude = newVal;
	}
	
	public void setRegion(String newVal) {
		this.region = newVal;
	}
	
	public void setFullStationId(String newVal) {
		this.fullStationId = newVal;
	}
	
	public void setCityState(String newVal) {
		this.cityState = newVal;
	}
	
	public void setCity(String newVal) {
		this.city = newVal;
	}
	
	public void setState(String newVal) {
		this.state = newVal;
	}
	
	public void setStateAbbreviation(String newVal) {
		this.stateAbbreviation = newVal;
	}
	
	public void setRadar(String newVal) {
		this.radar = newVal;
	}
}

/**
 * 
 */
package ca.datamagic.wfo.dao;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.referencing.GeodeticCalculator;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Point;

import ca.datamagic.wfo.dto.StationDTO;
import ca.datamagic.wfo.inject.MemoryCache;

/**
 * @author Greg
 *
 */
public class StationDAO extends BaseDAO {
	private static Logger _logger = LogManager.getLogger(StationDAO.class);
	private String _fileName = null;
	private String _typeName = null; 
	private SimpleFeatureSource _featureSource = null;
	
	public StationDAO() throws IOException {
		_fileName = MessageFormat.format("{0}/stations/stations.shp", getDataPath());
		HashMap<Object, Object> connect = new HashMap<Object, Object>();
		connect.put("url", "file://" + _fileName);
		DataStore dataStore = DataStoreFinder.getDataStore(connect);
		String[] typeNames = dataStore.getTypeNames();
		String typeName = typeNames[0];
		SimpleFeatureSource featureSource = dataStore.getFeatureSource(typeName);
		_typeName = typeName;
		_featureSource = featureSource;
	}

	@MemoryCache
	public List<StationDTO> list() throws IOException {		
		SimpleFeatureCollection collection = _featureSource.getFeatures();
		SimpleFeatureIterator iterator = null;
		try {
			List<StationDTO> items = new ArrayList<StationDTO>();
			iterator = collection.features();
			while (iterator.hasNext()) {
				items.add(new StationDTO(iterator.next()));
			}
			return items;
		} finally {
			if (iterator != null) {
				iterator.close();
			}
		}
	}
	
	@MemoryCache
	public List<StationDTO> list(boolean hasRadisonde) throws IOException, CQLException {
		StringBuffer filter = new StringBuffer();
		if (hasRadisonde) {
			if (filter.length() > 0) {
				filter.append(" AND ");
			}
			filter.append("has_rad = 'Y'");
		}
		if (filter.length() < 1)
			return list();
		_logger.debug("filter: " + filter.toString());
		Query query = new Query(_typeName, CQL.toFilter(filter.toString()));
		SimpleFeatureCollection collection = _featureSource.getFeatures(query);
		SimpleFeatureIterator iterator = null;
		try {
			List<StationDTO> items = new ArrayList<StationDTO>();
			iterator = collection.features();
			while (iterator.hasNext()) {
				items.add(new StationDTO(iterator.next()));
			}
			return items;
		} finally {
			if (iterator != null) {
				iterator.close();
			}
		}
	}
	
	@MemoryCache
	public List<StationDTO> list(String city, String state, String zip) throws IOException, CQLException {
		StringBuffer filter = new StringBuffer();
		if ((city != null) && (city.length() > 0)) {
			if (filter.length() > 0) {
				filter.append(" AND ");
			}
			filter.append(MessageFormat.format("city = {0}", "'" + city + "'"));
		}
		if ((state != null) && (state.length() > 0)) {
			if (filter.length() > 0) {
				filter.append(" AND ");
			}
			filter.append(MessageFormat.format("state = {0}", "'" + state + "'"));
		}
		if ((zip != null) && (zip.length() > 0)) {
			if (filter.length() > 0) {
				filter.append(" AND ");
			}
			filter.append(MessageFormat.format("zip = {0}", "'" + zip + "'"));
		}
		if (filter.length() < 1)
			return list();
		_logger.debug("filter: " + filter.toString());
		Query query = new Query(_typeName, CQL.toFilter(filter.toString()));
		SimpleFeatureCollection collection = _featureSource.getFeatures(query);
		SimpleFeatureIterator iterator = null;
		try {
			List<StationDTO> items = new ArrayList<StationDTO>();
			iterator = collection.features();
			while (iterator.hasNext()) {
				items.add(new StationDTO(iterator.next()));
			}
			return items;
		} finally {
			if (iterator != null) {
				iterator.close();
			}
		}
	}
	
	@MemoryCache
	public List<StationDTO> list(String city, String state, String zip, boolean hasRadisonde) throws IOException, CQLException {
		StringBuffer filter = new StringBuffer();
		if ((city != null) && (city.length() > 0)) {
			if (filter.length() > 0) {
				filter.append(" AND ");
			}
			filter.append(MessageFormat.format("city = {0}", "'" + city + "'"));
		}
		if ((state != null) && (state.length() > 0)) {
			if (filter.length() > 0) {
				filter.append(" AND ");
			}
			filter.append(MessageFormat.format("state = {0}", "'" + state + "'"));
		}
		if ((zip != null) && (zip.length() > 0)) {
			if (filter.length() > 0) {
				filter.append(" AND ");
			}
			filter.append(MessageFormat.format("zip = {0}", "'" + zip + "'"));
		}
		if (hasRadisonde) {
			if (filter.length() > 0) {
				filter.append(" AND ");
			}
			filter.append("has_rad = 'Y'");
		}
		if (filter.length() < 1)
			return list();
		_logger.debug("filter: " + filter.toString());
		Query query = new Query(_typeName, CQL.toFilter(filter.toString()));
		SimpleFeatureCollection collection = _featureSource.getFeatures(query);
		SimpleFeatureIterator iterator = null;
		try {
			List<StationDTO> items = new ArrayList<StationDTO>();
			iterator = collection.features();
			while (iterator.hasNext()) {
				items.add(new StationDTO(iterator.next()));
			}
			return items;
		} finally {
			if (iterator != null) {
				iterator.close();
			}
		}
	}
	
	@MemoryCache
	public StationDTO read(String id) throws IOException, CQLException {
		String filter = MessageFormat.format("station_id = {0}", "'" + id + "'");
		_logger.debug("filter: " + filter);
		Query query = new Query(_typeName, CQL.toFilter(filter));
		SimpleFeatureCollection collection = _featureSource.getFeatures(query);
		SimpleFeatureIterator iterator = null;
		try {
			iterator = collection.features();
			if (iterator.hasNext()) {
				return new StationDTO(iterator.next());
			}
			return null;
		} finally {
			if (iterator != null) {
				iterator.close();
			}
		}
	}
	
	@MemoryCache
	public StationDTO readNearest(double latitude, double longitude, double distance, String units) throws IOException, CQLException {
		String filter = MessageFormat.format("DWITHIN(the_geom, POINT({0} {1}), {2}, {3})", Double.toString(longitude), Double.toString(latitude), Double.toString(distance), units);
		_logger.debug("filter: " + filter);
		Query query = new Query(_typeName, CQL.toFilter(filter));
		SimpleFeatureCollection collection = _featureSource.getFeatures(query);
		SimpleFeatureIterator iterator = null;
		GeodeticCalculator calculator = new GeodeticCalculator();
		calculator.setStartingGeographicPoint(longitude, latitude);
		SimpleFeature nearestFeature = null;
		double minDistanceToPoint = 0.0;
		try {
			iterator = collection.features();
			while (iterator.hasNext()) {		
				SimpleFeature feature = iterator.next();
				Point point = (Point)feature.getAttribute(0);
				calculator.setDestinationGeographicPoint(point.getX(), point.getY());
				double distanceToPoint = calculator.getOrthodromicDistance();
				if (nearestFeature == null) {
					nearestFeature = feature;
					minDistanceToPoint = distanceToPoint;
				} else {
					if (distanceToPoint < minDistanceToPoint) {
						nearestFeature = feature;
						minDistanceToPoint = distanceToPoint;
					}
				}
			}
			if (nearestFeature != null) {
				return new StationDTO(nearestFeature);
			}
			return null;
		} finally {
			if (iterator != null) {
				iterator.close();
			}
		}
	}
	
	@MemoryCache
	public StationDTO readNearest(double latitude, double longitude, double distance, String units, boolean hasRadisonde) throws IOException, CQLException {
		String filter = MessageFormat.format("DWITHIN(the_geom, POINT({0} {1}), {2}, {3})", Double.toString(longitude), Double.toString(latitude), Double.toString(distance), units);
		_logger.debug("filter: " + filter);
		Query query = new Query(_typeName, CQL.toFilter(filter));
		SimpleFeatureCollection collection = _featureSource.getFeatures(query);
		SimpleFeatureIterator iterator = null;
		GeodeticCalculator calculator = new GeodeticCalculator();
		calculator.setStartingGeographicPoint(longitude, latitude);
		SimpleFeature nearestFeature = null;
		double minDistanceToPoint = 0.0;
		try {
			iterator = collection.features();
			while (iterator.hasNext()) {		
				SimpleFeature feature = iterator.next();
				if (hasRadisonde) {
					if (feature.getAttribute("has_rad").toString().compareToIgnoreCase("Y") != 0) {
						continue;
					}
				} else {
					if (feature.getAttribute("has_rad").toString().compareToIgnoreCase("N") != 0) {
						continue;
					}
				}
				Point point = (Point)feature.getAttribute(0);
				calculator.setDestinationGeographicPoint(point.getX(), point.getY());
				double distanceToPoint = calculator.getOrthodromicDistance();
				if (nearestFeature == null) {
					nearestFeature = feature;
					minDistanceToPoint = distanceToPoint;
				} else {
					if (distanceToPoint < minDistanceToPoint) {
						nearestFeature = feature;
						minDistanceToPoint = distanceToPoint;
					}
				}
			}
			if (nearestFeature != null) {
				return new StationDTO(nearestFeature);
			}
			return null;
		} finally {
			if (iterator != null) {
				iterator.close();
			}
		}
	}
}

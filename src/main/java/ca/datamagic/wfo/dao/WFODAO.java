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

import ca.datamagic.wfo.dto.WFODTO;
import ca.datamagic.wfo.inject.MemoryCache;

/**
 * @author Greg
 *
 */
public class WFODAO {
	private static Logger _logger = LogManager.getLogger(WFODAO.class);
	private static String _fileName = null;
	private String _typeName = null; 
	private SimpleFeatureSource _featureSource = null;
	
	public WFODAO() throws IOException {
		HashMap<Object, Object> connect = new HashMap<Object, Object>();
		connect.put("url", "file://" + _fileName);
		DataStore dataStore = DataStoreFinder.getDataStore(connect);
		String[] typeNames = dataStore.getTypeNames();
		String typeName = typeNames[0];
		SimpleFeatureSource featureSource = dataStore.getFeatureSource(typeName);
		_typeName = typeName;
		_featureSource = featureSource;
	}
	
	public static synchronized String getFileName() {
		return _fileName;
	}
	
	public static synchronized void setFileName(String newVal) {
		_fileName = newVal;
	}
	
	@MemoryCache
	public List<WFODTO> list() throws IOException {		
		SimpleFeatureCollection collection = _featureSource.getFeatures();
		SimpleFeatureIterator iterator = null;
		try {
			List<WFODTO> items = new ArrayList<WFODTO>();
			iterator = collection.features();
			while (iterator.hasNext()) {
				items.add(new WFODTO(iterator.next()));
			}
			return items;
		} finally {
			if (iterator != null) {
				iterator.close();
			}
		}
	}
	
	@MemoryCache
	public WFODTO read(String id) throws IOException, CQLException {
		String filter = MessageFormat.format("WFO = {0}", "'" + id + "'");
		_logger.debug("filter: " + filter);
		Query query = new Query(_typeName, CQL.toFilter(filter));
		SimpleFeatureCollection collection = _featureSource.getFeatures(query);
		SimpleFeatureIterator iterator = null;
		try {
			iterator = collection.features();
			if (iterator.hasNext()) {
				return new WFODTO(iterator.next());
			}
			return null;
		} finally {
			if (iterator != null) {
				iterator.close();
			}
		}
	}
	
	@MemoryCache
	public List<WFODTO> read(double latitude, double longitude) throws IOException, CQLException {
		String filter = MessageFormat.format("CONTAINS (the_geom, POINT({0} {1}))", Double.toString(longitude), Double.toString(latitude));
		_logger.debug("filter: " + filter);
		Query query = new Query(_typeName, CQL.toFilter(filter));
		SimpleFeatureCollection collection = _featureSource.getFeatures(query);
		SimpleFeatureIterator iterator = null;
		try {
			List<WFODTO> items = new ArrayList<WFODTO>();
			iterator = collection.features();
			while (iterator.hasNext()) {
				items.add(new WFODTO(iterator.next()));
			}
			return items;
		} finally {
			if (iterator != null) {
				iterator.close();
			}
		}
	}
}

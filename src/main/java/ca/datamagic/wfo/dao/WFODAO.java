/**
 * 
 */
package ca.datamagic.wfo.dao;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
public class WFODAO extends BaseDAO {
	private static Logger logger = LogManager.getLogger(WFODAO.class);
	private String fileName = null;
	private String typeName = null; 
	private SimpleFeatureSource featureSource = null;
	
	public WFODAO() throws IOException {
		this.fileName = MessageFormat.format("{0}/w_10nv15/w_10nv15.shp", getDataPath());
		HashMap<Object, Object> connect = new HashMap<Object, Object>();
		connect.put("url", "file://" + this.fileName);
		DataStore dataStore = DataStoreFinder.getDataStore(connect);
		String[] typeNames = dataStore.getTypeNames();
		String typeName = typeNames[0];
		SimpleFeatureSource featureSource = dataStore.getFeatureSource(typeName);
		this.typeName = typeName;
		this.featureSource = featureSource;
	}
	
	@MemoryCache
	public List<WFODTO> list(String state) throws IOException, CQLException {		
		SimpleFeatureCollection collection = null;
		SimpleFeatureIterator iterator = null;
		try {
			List<WFODTO> items = new ArrayList<WFODTO>();
			if ((state == null) || (state.length() < 1)) {
				collection = this.featureSource.getFeatures();				
			} else {
				String filter = MessageFormat.format("ST = {0}", "'" + state.toUpperCase() + "'");
				logger.debug("filter: " + filter);
				Query query = new Query(this.typeName, CQL.toFilter(filter));
				collection = this.featureSource.getFeatures(query);
			}
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
		String filter = MessageFormat.format("WFO = {0}", "'" + id.toUpperCase() + "'");
		logger.debug("filter: " + filter);
		Query query = new Query(this.typeName, CQL.toFilter(filter));
		SimpleFeatureCollection collection = this.featureSource.getFeatures(query);
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
		logger.debug("filter: " + filter);
		Query query = new Query(this.typeName, CQL.toFilter(filter));
		SimpleFeatureCollection collection = this.featureSource.getFeatures(query);
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

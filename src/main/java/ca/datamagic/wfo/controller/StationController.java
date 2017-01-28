/**
 * 
 */
package ca.datamagic.wfo.controller;

import java.net.URI;
import java.net.URLDecoder;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import ca.datamagic.wfo.dao.BaseDAO;
import ca.datamagic.wfo.dao.StationDAO;
import ca.datamagic.wfo.dto.CityDTO;
import ca.datamagic.wfo.dto.StationDTO;
import ca.datamagic.wfo.dto.ZipDTO;
import ca.datamagic.wfo.inject.DAOModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * @author Greg
 *
 */
@Controller
@RequestMapping("/api/station")
public class StationController {
	private static Logger _logger = LogManager.getLogger(StationController.class);
	private static Injector _injector = null;
	private static StationDAO _dao = null;
	
	static {
		try {
			DefaultResourceLoader loader = new DefaultResourceLoader();       
		    Resource dataResource = loader.getResource("classpath:data");
		    String dataPath = dataResource.getFile().getAbsolutePath();
		    _logger.debug("dataPath: " + dataPath);
		    BaseDAO.setDataPath(dataPath);
			_injector = Guice.createInjector(new DAOModule());
			_dao = _injector.getInstance(StationDAO.class);
		} catch (Throwable t) {
			_logger.error("Exception", t);
		}
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/cities", produces="application/json")
	@ResponseBody
    public List<CityDTO> cities(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			return _dao.cities();
		} catch (Throwable t) {
			_logger.error("Exception", t);
			throw new Exception(t);
		}
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/zips", produces="application/json")
	@ResponseBody
    public List<ZipDTO> zips(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			return _dao.zips();
		} catch (Throwable t) {
			_logger.error("Exception", t);
			throw new Exception(t);
		}
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/list", produces="application/json")
	@ResponseBody
    public List<StationDTO> list(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			StringBuffer requestURL = request.getRequestURL();
		    String queryString = request.getQueryString();
		    if ((queryString != null) && (queryString.length() > 0)) {
		        requestURL.append("?");
		        requestURL.append(queryString);
		    }
			String requestURI = requestURL.toString();
			_logger.debug("requestURI: " + requestURI);			
			MultiValueMap<String, String> queryParameters = UriComponentsBuilder.fromUri(new URI(requestURI)).build().getQueryParams();
			String address = null;
			String city = null;
			String state = null;
			String zip = null;
			boolean hasRadisonde = false;
			Set<String> queryKeys = queryParameters.keySet();
			for (String key : queryKeys) {
				_logger.debug("key: " + key);
				String value = null;
				List<String> values = queryParameters.get(key);
				if ((values != null) && (values.size() > 0)) {
					value = URLDecoder.decode(values.get(0), "UTF-8");
				}
				if ((value != null) && (value.length() > 0)) {
					_logger.debug("value: " + value);
					if (key.compareToIgnoreCase("address") == 0) {
						address = value;
					} else if (key.compareToIgnoreCase("city") == 0) {
						city = value;
					} else if (key.compareToIgnoreCase("state") == 0) {
						state = value;
					} else if (key.compareToIgnoreCase("zip") == 0) {
						zip = value;
					} else if (key.compareToIgnoreCase("hasRadisonde") == 0) {
						hasRadisonde = Boolean.parseBoolean(value);
					}
				}
			}
			if ((address != null) && (address.length() > 0)) {
				return _dao.list(address, hasRadisonde);
			}
			return _dao.list(city, state, zip, hasRadisonde);
		} catch (Throwable t) {
			_logger.error("Exception", t);
			throw new Exception(t);
		}
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/{id}", produces="application/json")
	@ResponseBody
    public StationDTO readById(@PathVariable String id, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			return _dao.read(id);
		} catch (Throwable t) {
			_logger.error("Exception", t);
			throw new Exception(t);
		}
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/{latitude}/{longitude}/nearest", produces="application/json")
	@ResponseBody
    public StationDTO readNearest(@PathVariable String latitude, @PathVariable String longitude, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			return _dao.readNearest(Double.parseDouble(latitude), Double.parseDouble(longitude), 25, "statute miles");
		} catch (Throwable t) {
			_logger.error("Exception", t);
			throw new Exception(t);
		}
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/{latitude}/{longitude}/nearestWithRadiosonde", produces="application/json")
	@ResponseBody
    public StationDTO readNearestWithRadiosonde(@PathVariable String latitude, @PathVariable String longitude, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			return _dao.readNearest(Double.parseDouble(latitude), Double.parseDouble(longitude), 25, "statute miles", true);
		} catch (Throwable t) {
			_logger.error("Exception", t);
			throw new Exception(t);
		}
	}
}

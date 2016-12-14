/**
 * 
 */
package ca.datamagic.wfo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.datamagic.wfo.dao.BaseDAO;
import ca.datamagic.wfo.dao.StationDAO;
import ca.datamagic.wfo.dto.StationDTO;
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
	
	@RequestMapping(method=RequestMethod.GET, value="/list", produces="application/json")
	@ResponseBody
    public List<StationDTO> list(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			return _dao.list();
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
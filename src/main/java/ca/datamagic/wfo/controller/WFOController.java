/**
 * 
 */
package ca.datamagic.wfo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mangofactory.swagger.annotations.ApiIgnore;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import ca.datamagic.wfo.dao.WFODAO;
import ca.datamagic.wfo.dto.CachedItemDTO;
import ca.datamagic.wfo.dto.WFODTO;
import ca.datamagic.wfo.inject.DAOModule;
import ca.datamagic.wfo.inject.MemoryCacheInterceptor;

/**
 * @author Greg
 *
 */
@Api(value="wfo", description="WFO operations")
@Controller
@RequestMapping("")
public class WFOController extends BaseController {
	private static Logger _logger = LogManager.getLogger(WFOController.class);
	@Autowired
	private ApplicationContext _context = null;
	private WFODAO _dao = null;
	
	public WFOController() throws IOException {
		Injector injector = Guice.createInjector(new DAOModule());
		_dao = injector.getInstance(WFODAO.class);
	}

	@ApiIgnore
	@RequestMapping(method=RequestMethod.GET, value="")
    public String index(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return "index";
	}
	
	@ApiOperation(value = "List all of the WFO")
	@RequestMapping(method=RequestMethod.GET, value="/list", produces="application/json")
	@ResponseBody
    public List<WFODTO> list(@ApiIgnore Model model, @ApiIgnore HttpServletRequest request, @ApiIgnore HttpServletResponse response) throws Exception {
		try {
			return _dao.list();
		} catch (Throwable t) {
			_logger.error("Exception", t);
			throw new Exception(t);
		}
	}
	
	@ApiOperation(value = "Get a WFO by identifier")
	@RequestMapping(method=RequestMethod.GET, value="/{id}", produces="application/json")
	@ResponseBody
    public WFODTO readById(@ApiParam(name="id", value="The identifier (i.e. LWX for Baltimore MD/Washington DC)", required=true) @PathVariable String id, @ApiIgnore Model model, @ApiIgnore HttpServletRequest request, @ApiIgnore HttpServletResponse response) throws Exception {
		try {
			return _dao.read(id);
		} catch (Throwable t) {
			_logger.error("Exception", t);
			throw new Exception(t);
		}
	}
	
	@ApiOperation(value = "List the WFO by latitude/longitude")
	@RequestMapping(method=RequestMethod.GET, value="/{latitude}/{longitude}/coordinates", produces="application/json")
	@ResponseBody
    public List<WFODTO> readByLocation(@ApiParam(name="latitude", value="The latitude", required=true) @PathVariable String latitude, @ApiParam(name="longitude", value="The longitude", required=true) @PathVariable String longitude, @ApiIgnore Model model, @ApiIgnore HttpServletRequest request, @ApiIgnore HttpServletResponse response) throws Exception {
		try {
			return _dao.read(Double.parseDouble(latitude), Double.parseDouble(longitude));
		} catch (Throwable t) {
			_logger.error("Exception", t);
			throw new Exception(t);
		}
	}
	
	@ApiOperation(value = "Returned the cached items")
	@RequestMapping(method=RequestMethod.GET, value="/cache", produces="application/json")
	@ResponseBody
	public List<CachedItemDTO> getCachedItems() {
		List<CachedItemDTO> items = new ArrayList<CachedItemDTO>();
		Enumeration<String> keys = MemoryCacheInterceptor.getKeys();
		if (keys != null) {
			while (keys.hasMoreElements()) {
				String key = keys.nextElement();
				Object value = MemoryCacheInterceptor.getValue(key);
				if (value instanceof WFODTO) {
					items.add(new CachedItemDTO(key, (WFODTO)value));
				} else if (value instanceof List<?>) {
					try {
						@SuppressWarnings("unchecked")
						List<WFODTO> list = (List<WFODTO>)value;
						for (WFODTO dto : list) {
							items.add(new CachedItemDTO(key, dto));
						}
					} catch (Throwable t) {
						_logger.warn("Exception", t);
					}
				}
			}
		}
		return items;
	}
	
	@ApiOperation(value = "Clear the cache")
	@RequestMapping(method=RequestMethod.DELETE, value="/cache")
	public void clearCachedItems() {
		MemoryCacheInterceptor.clearCache();
	}
}

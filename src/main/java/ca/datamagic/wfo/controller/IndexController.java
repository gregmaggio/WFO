/**
 * 
 */
package ca.datamagic.wfo.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Writer;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.datamagic.wfo.dao.BaseDAO;
import ca.datamagic.wfo.dto.CachedItemDTO;
import ca.datamagic.wfo.dto.SwaggerConfigurationDTO;
import ca.datamagic.wfo.dto.SwaggerResourceDTO;
import ca.datamagic.wfo.dto.WFODTO;
import ca.datamagic.wfo.inject.MemoryCacheInterceptor;

/**
 * @author Greg
 *
 */
@Controller
@RequestMapping("")
public class IndexController {
	private static Logger _logger = LogManager.getLogger(IndexController.class);
	private static SwaggerConfigurationDTO _swaggerConfiguration = null;
	private static SwaggerResourceDTO[] _swaggerResources = null;
	private static String _swagger = null;
	
	static {
		FileInputStream swaggerStream = null;
		try {
			DefaultResourceLoader loader = new DefaultResourceLoader();       
		    Resource dataResource = loader.getResource("classpath:data");
		    Resource metaInfResource = loader.getResource("META-INF");
		    String dataPath = dataResource.getFile().getAbsolutePath();
		    String metaInfPath = metaInfResource.getFile().getAbsolutePath();
		    _logger.debug("dataPath: " + dataPath);
		    _logger.debug("metaInfPath: " + metaInfPath);
		    
		    String swaggerFileName = MessageFormat.format("{0}/swagger.json", metaInfPath);
		    swaggerStream = new FileInputStream(swaggerFileName);
		    _swagger = IOUtils.toString(swaggerStream, "UTF-8");
		    
		    BaseDAO.setDataPath(dataPath);
			_swaggerConfiguration = new SwaggerConfigurationDTO();
			_swaggerResources = new SwaggerResourceDTO[] { new SwaggerResourceDTO() };
		} catch (Throwable t) {
			_logger.error("Exception", t);
		}
		if (swaggerStream != null) {
			IOUtils.closeQuietly(swaggerStream);
		}
	}
	
	
	
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
	
	@RequestMapping(method=RequestMethod.DELETE, value="/cache")
	public void clearCachedItems() {
		MemoryCacheInterceptor.clearCache();
	}
	
	@RequestMapping(value="/swagger-resources/configuration/ui", method=RequestMethod.GET, produces="application/json")
	@ResponseBody
	public SwaggerConfigurationDTO getSwaggerConfigurationUI() {
		return _swaggerConfiguration;
	}
	
	@RequestMapping(value="/swagger-resources", method=RequestMethod.GET, produces="application/json")
	@ResponseBody
	public SwaggerResourceDTO[] getSwaggerResources() {
		return _swaggerResources;
	}
	
	@RequestMapping(value="/v2/api-docs", method=RequestMethod.GET, produces="application/json")
	public void getSwagger(Writer responseWriter) throws IOException {
		responseWriter.write(_swagger);
	}
}

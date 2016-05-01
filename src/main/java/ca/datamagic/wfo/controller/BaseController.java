/**
 * 
 */
package ca.datamagic.wfo.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import ca.datamagic.wfo.dao.WFODAO;

/**
 * @author Greg
 *
 */
public abstract class BaseController {
	private static Logger _logger = LogManager.getLogger(BaseController.class);
	
	static {
		try {
			DefaultResourceLoader loader = new DefaultResourceLoader();       
		    Resource resource = loader.getResource("classpath:META-INF/data/w_10nv15/w_10nv15.shp");
		    String fileName = resource.getFile().getAbsolutePath();
		    _logger.debug("filename: " + fileName);
			WFODAO.setFileName(fileName);
		} catch (Throwable t) {
			_logger.error("Exception", t);
		}
	}

}

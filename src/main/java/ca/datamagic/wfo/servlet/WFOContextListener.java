/**
 * 
 */
package ca.datamagic.wfo.servlet;

import java.text.MessageFormat;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import com.google.inject.Guice;
import com.google.inject.Injector;

import ca.datamagic.wfo.dao.BaseDAO;
import ca.datamagic.wfo.dao.WFODAO;
import ca.datamagic.wfo.inject.DAOModule;

/**
 * @author Greg
 *
 */
public class WFOContextListener implements ServletContextListener {
	private static Logger _logger = LogManager.getLogger(WFOContextListener.class);
	private static Injector _injector = null;
	private static WFODAO _dao = null;
	
	public static WFODAO getDAO() {
		return _dao;
	}
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		String realPath = sce.getServletContext().getRealPath("/");
		String fileName = MessageFormat.format("{0}/WEB-INF/classes/log4j.cfg.xml", realPath);
		String dataPath = MessageFormat.format("{0}/WEB-INF/classes/data", realPath);
		DOMConfigurator.configure(fileName);
		BaseDAO.setDataPath(dataPath);
		_injector = Guice.createInjector(new DAOModule());
		_dao = _injector.getInstance(WFODAO.class);
		_logger.debug("contextInitialized");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		_logger.debug("contextDestroyed");
	}
}

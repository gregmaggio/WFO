/**
 * 
 */
package ca.datamagic.wfo.servlet;

import java.text.MessageFormat;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	private static Logger logger = LogManager.getLogger(WFOContextListener.class);
	private static Injector injector = null;
	private static WFODAO dao = null;
	
	public static WFODAO getDAO() {
		return dao;
	}
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		String realPath = sce.getServletContext().getRealPath("/");
		String dataPath = MessageFormat.format("{0}/WEB-INF/classes/data", realPath);
		BaseDAO.setDataPath(dataPath);
		injector = Guice.createInjector(new DAOModule());
		dao = injector.getInstance(WFODAO.class);
		logger.debug("contextInitialized");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		logger.debug("contextDestroyed");
	}
}

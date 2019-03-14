/**
 * 
 */
package ca.datamagic.wfo.servlet;

import java.io.IOError;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.gson.Gson;

import ca.datamagic.wfo.dto.WFODTO;

/**
 * @author Greg
 *
 */
public class ListWFOServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LogManager.getLogger(ListWFOServlet.class);

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			String state = request.getParameter("state");
			List<WFODTO> wfoList = WFOContextListener.getDAO().list(state);
			String json = (new Gson()).toJson(wfoList);
			response.setContentType("application/json");
			response.getWriter().println(json);
		} catch (Throwable t) {
			logger.error("Exception", t);
			throw new IOError(t);
		}
	}
}

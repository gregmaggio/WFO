/**
 * 
 */
package ca.datamagic.wfo.servlet;

import java.io.IOError;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import ca.datamagic.wfo.dto.WFODTO;

/**
 * @author Greg
 *
 */
public class ReadWFOServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LogManager.getLogger(ReadWFOServlet.class);
	private static final Pattern readNearestPattern = Pattern.compile("/(?<latitude>[+-]?([0-9]*[.])?[0-9]+)/(?<longitude>[+-]?([0-9]*[.])?[0-9]+)/coordinates", Pattern.CASE_INSENSITIVE);
	private static final Pattern readPattern = Pattern.compile("/(?<identifier>\\w+)", Pattern.CASE_INSENSITIVE);

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			String pathInfo = request.getPathInfo();
			logger.debug("pathInfo: " + pathInfo);
			Matcher readNearestMatcher = readNearestPattern.matcher(pathInfo);
			if (readNearestMatcher.find()) {
				logger.debug("readNearest");
				String latitude = readNearestMatcher.group("latitude");
				logger.debug("latitude: " + latitude);
				String longitude = readNearestMatcher.group("longitude");
				logger.debug("longitude: " + longitude);
				double doubleLatitude = Double.parseDouble(latitude);
				double doubleLongitude = Double.parseDouble(longitude);
				List<WFODTO> wfoList = WFOContextListener.getDAO().read(doubleLatitude, doubleLongitude);
				if ((wfoList != null) && (wfoList.size() > 0)) {
					WFODTO wfo = wfoList.get(0);
					String json = (new Gson()).toJson(wfo);
					response.setContentType("application/json");
					response.getWriter().println(json);
					return;
				}
			}
			
			Matcher readMatcher = readPattern.matcher(pathInfo);
			if (readMatcher.find()) {
				logger.debug("read");
				String identifier = readMatcher.group("identifier");
				logger.debug("identifier: " + identifier);
				WFODTO wfo = WFOContextListener.getDAO().read(identifier);
				if (wfo != null) {
					String json = (new Gson()).toJson(wfo);
					response.setContentType("application/json");
					response.getWriter().println(json);
					return;
				}
			}
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} catch (Throwable t) {
			logger.error("Exception", t);
			throw new IOError(t);
		}
	}
}

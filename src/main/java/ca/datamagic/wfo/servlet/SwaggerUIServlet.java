/**
 * 
 */
package ca.datamagic.wfo.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import ca.datamagic.wfo.dto.SwaggerConfigurationDTO;

/**
 * @author Greg
 *
 */
public class SwaggerUIServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String swaggerConfiguration = null;
	
	static {
		swaggerConfiguration = (new Gson()).toJson(new SwaggerConfigurationDTO());
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		response.getWriter().println(swaggerConfiguration);
	}
}

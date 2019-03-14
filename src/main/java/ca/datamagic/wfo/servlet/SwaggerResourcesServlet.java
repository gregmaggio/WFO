/**
 * 
 */
package ca.datamagic.wfo.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import ca.datamagic.wfo.dto.SwaggerResourceDTO;

/**
 * @author Greg
 *
 */
public class SwaggerResourcesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String swaggerResources = null;
	
	static {
		swaggerResources = (new Gson()).toJson(new SwaggerResourceDTO[] { new SwaggerResourceDTO() });
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		response.getWriter().println(swaggerResources);
	}
}

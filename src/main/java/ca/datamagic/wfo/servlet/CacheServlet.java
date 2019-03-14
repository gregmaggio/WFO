/**
 * 
 */
package ca.datamagic.wfo.servlet;

import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.gson.Gson;

import ca.datamagic.wfo.dto.CachedItemDTO;
import ca.datamagic.wfo.dto.WFODTO;
import ca.datamagic.wfo.inject.MemoryCacheInterceptor;

/**
 * @author Greg
 *
 */
public class CacheServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LogManager.getLogger(CacheServlet.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			List<CachedItemDTO> items = new ArrayList<CachedItemDTO>();
			Enumeration<String> keys = MemoryCacheInterceptor.getKeys();
			if (keys != null) {
				while (keys.hasMoreElements()) {
					String key = keys.nextElement();
					Object value = MemoryCacheInterceptor.getValue(key);
					if (value instanceof WFODTO) {
						CachedItemDTO cachedItem = new CachedItemDTO();
						cachedItem.setKey(key);
						cachedItem.setWFO((WFODTO)value);
						items.add(cachedItem);
					} else if (value instanceof List<?>) {
						List<WFODTO> wfos = null; 
						try {
							wfos = (List<WFODTO>)value;
						} catch (Throwable t) {
						}
						CachedItemDTO cachedItem = new CachedItemDTO();
						cachedItem.setKey(key);
						cachedItem.setWFOList(wfos);
						items.add(cachedItem);
					}
				}
			}
			String json = (new Gson()).toJson(items);
			response.setContentType("application/json");
			response.getWriter().println(json);
		} catch (Throwable t) {
			logger.error("Exception", t);
			throw new IOError(t);
		}
	}
	
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			MemoryCacheInterceptor.clearCache();
		} catch (Throwable t) {
			logger.error("Exception", t);
			throw new IOError(t);
		}
	}
}

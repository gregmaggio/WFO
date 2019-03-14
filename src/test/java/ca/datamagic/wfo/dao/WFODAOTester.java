/**
 * 
 */
package ca.datamagic.wfo.dao;

import java.io.File;
import java.util.List;

import org.apache.log4j.xml.DOMConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;

import ca.datamagic.wfo.dto.WFODTO;

/**
 * @author Greg
 *
 */
public class WFODAOTester {
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DOMConfigurator.configure("src/test/resources/log4j.cfg.xml");
		BaseDAO.setDataPath((new File("src/test/resources/data")).getAbsolutePath());
	}

	@Test
	public void test1() throws Exception {
		WFODAO dao = new WFODAO();
		List<WFODTO> items = dao.list(null);
		Gson gson = new Gson();
		for (WFODTO item : items) {
			System.out.println("WFO: " + gson.toJson(item));
		}
	}
	
	@Test
	public void test2() throws Exception {
		WFODAO dao = new WFODAO();
		List<WFODTO> items = dao.list("VA");
		Gson gson = new Gson();
		for (WFODTO item : items) {
			System.out.println("WFO: " + gson.toJson(item));
		}
	}
	
	@Test
	public void test3() throws Exception {
		WFODAO dao = new WFODAO();
		WFODTO item = dao.read("LWX");
		Gson gson = new Gson();
		System.out.println("WFO: " + gson.toJson(item));
	}
	
	@Test
	public void test4() throws Exception {
		WFODAO dao = new WFODAO();
		double latitude = 38.9967;
	    double longitude = -76.9275;	
		List<WFODTO> items = dao.read(latitude, longitude);
		Gson gson = new Gson();
		for (WFODTO item : items) {
			System.out.println("WFO: " + gson.toJson(item));
		}
	}
}

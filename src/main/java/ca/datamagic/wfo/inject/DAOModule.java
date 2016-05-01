/**
 * 
 */
package ca.datamagic.wfo.inject;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

/**
 * @author Greg
 *
 */
public class DAOModule extends AbstractModule {
	public DAOModule() {
	}

	@Override
	protected void configure() {
		bindInterceptor(Matchers.any(), Matchers.annotatedWith(MemoryCache.class), new MemoryCacheInterceptor());
	}
}

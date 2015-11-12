
package com.realhome.common.pattern.mvc;

import com.realhome.common.pattern.proxy.Proxy;

/** The interface definition for a MVC Model.
 * <p>
 * In MVC, <code>Model</code> implementors provide access to <code>Proxy</code> objects by named lookup.
 * </p>
 * <p>
 * An <code>Model</code> assumes these responsibilities:
 * </p>
 * <ul>
 * <li>Maintain a cache of <code>Proxy</code> instances</LI>
 * <li>Provide methods for registering, retrieving, and removing <code>Proxy</code> instances</li>
 * </ul> */
public interface Model {

	/** Register an <code>Proxy</code> instance with the <code>Model</code>.
	 * 
	 * @param proxy an object reference to be held by the <code>Model</code>. */
	void registerProxy (Proxy proxy);

	/** Retrieve an <code>Proxy</code> instance from the Model.
	 * 
	 * @param proxy
	 * @return the <code>Proxy</code> instance previously registered with the given <code>proxyName</code>. */
	<T extends Proxy> T retrieveProxy (String proxy);

	/** Remove an <code>Proxy</code> instance from the Model.
	 * 
	 * @param proxy name of the <code>Proxy</code> instance to be removed. */
	Proxy removeProxy (String proxy);

	/** Check if a Proxy is registered
	 * 
	 * @param proxyName
	 * @return whether a Proxy is currently registered with the given <code>proxyName</code>. */
	boolean hasProxy (String proxyName);
}

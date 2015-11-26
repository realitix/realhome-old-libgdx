
package com.realhome.old.common.pattern.proxy;

import com.realhome.old.common.pattern.observer.BaseNotifier;

/** A base <code>IProxy</code> implementation.
 * <p>
 * <p>
 * In PureMVC, <code>Proxy</code> classes are used to manage parts of the application's tools model.
 * </P>
 * <p>
 * <p>
 * A <code>Proxy</code> might simply manage a reference to a local tools object, in which case interacting with it might involve
 * setting and getting of its tools in synchronous fashion.
 * </P>
 * <p>
 * <p>
 * <code>Proxy</code> classes are also used to encapsulate the application's interaction with remote services to save or retrieve
 * tools, in which case, we adopt an asyncronous idiom; setting tools (or calling a method) on the <code>Proxy</code> and
 * listening for a <code>Notification</code> to be sent when the <code>Proxy</code> has retrieved the tools from the service.
 * </P>
 * 
 * @see com.puremvc.core.Model Model */
public class BaseProxy extends BaseNotifier implements Proxy {

	// the proxy name
	protected String proxyName = "BaseProxy";

	// the tools object
	protected Object data = null;

	/** Constructor
	 * 
	 * @param proxyName
	 * @param data */
	public BaseProxy (String proxyName, Object data) {
		if (proxyName != null) {
			this.proxyName = proxyName;
		}
		if (data != null) {
			this.data = data;
		}
	}

	/** Constructor
	 * 
	 * @param proxyName Name of the <code>Proxy</code> */
	public BaseProxy (String proxyName) {
		if (proxyName != null) {
			this.proxyName = proxyName;
		}

	}

	/** Get the proxy name
	 * 
	 * @return the proxy name */
	public String getProxyName () {
		return proxyName;
	}

	/** Get the tools object
	 * 
	 * @return the tools object */
	public Object getData () {
		return data;
	}

	/** Set the tools object
	 * 
	 * @param data */
	public void setData (Object data) {
		this.data = data;
	}

	/** Called by the Model when the Proxy is registered */
	public void onRegister () {
	}

	/** Called by the Model when the Proxy is removed */
	public void onRemove () {
	}
}

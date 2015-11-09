
package com.realhome;

import com.realhome.commons.patterns.facade.SimpleFacade;
import com.realhome.commons.patterns.observer.BaseNotification;

/** @author realitix */
public class RealHomeFacade extends SimpleFacade {
	public static final String STARTUP = "startup";
	private static RealHomeFacade instance = null;
	private RealHomeApp realHomeApp;

	protected RealHomeFacade () {
		super();
	}

	/** Facade Singleton Factory method
	 *
	 * @return The Singleton instance of the Facade */
	public synchronized static RealHomeFacade getInstance () {
		if (instance == null) {
			instance = new RealHomeFacade();
		}
		return instance;
	}

	public void startup (RealHomeApp realHomeApp) {
		this.realHomeApp = realHomeApp;
		registerProxy(this.realHomeApp);
		notifyObservers(new BaseNotification(STARTUP, null, null));
	}

	@Override
	protected void initializeFacade () {
		super.initializeFacade();
	}

	@Override
	protected void initializeController () {
		super.initializeController();
		// registerCommand(STARTUP, StartupCommand.class);
	}

	@Override
	protected void initializeModel () {
		super.initializeModel();
	}

	@Override
	protected void initializeView () {
		super.initializeView();
	}
}

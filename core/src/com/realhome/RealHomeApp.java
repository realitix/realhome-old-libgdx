
package com.realhome;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.VisUI;
import com.realhome.common.MsgAPI;
import com.realhome.common.pattern.proxy.Proxy;

public class RealHomeApp extends ApplicationAdapter implements Proxy {
	public static final String NAME = "RealHomeApp";

	private RealHomeFacade facade;
	private Object data;

	@Override
	public void create () {
		VisUI.load(Gdx.files.internal("style/uiskin.json"));
		VisUI.setDefaultTitleAlign(Align.center);
		facade = RealHomeFacade.getInstance();
		facade.startup(this);
		sendNotification(MsgAPI.CREATE);
	}

	@Override
	public void render () {
		sendNotification(MsgAPI.RENDER, Gdx.graphics.getDeltaTime());
	}

	@Override
	public void resize (int width, int height) {
		sendNotification(MsgAPI.RESIZE, new int[] {width, height});
	}

	@Override
	public void dispose () {
		sendNotification(MsgAPI.DISPOSE);
		VisUI.dispose();
	}

	@Override
	public void sendNotification (String notificationName, Object body, String type) {
		facade.sendNotification(notificationName, body, type);
	}

	@Override
	public void sendNotification (String notificationName, Object body) {
		facade.sendNotification(notificationName, body);
	}

	@Override
	public void sendNotification (String notificationName) {
		facade.sendNotification(notificationName);
	}

	@Override
	public String getProxyName () {
		return NAME;
	}

	@Override
	public Object getData () {
		return data;
	}

	@Override
	public void setData (Object data) {
		this.data = data;
	}

	@Override
	public void onRegister () {
	}

	@Override
	public void onRemove () {
	}
}

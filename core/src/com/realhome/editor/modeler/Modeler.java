
package com.realhome.editor.modeler;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Disposable;
import com.realhome.editor.model.house.House;

public interface Modeler extends Disposable {
	public void resize (int width, int height);

	public void render ();

	public void reload (House house);

	public void action(String actionName);

	public House getHouse();

	public InputProcessor getInputProcessor();

	public String getName();
}

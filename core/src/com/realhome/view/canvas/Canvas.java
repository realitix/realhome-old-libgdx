
package com.realhome.view.canvas;

public interface Canvas {
	public void create ();

	public void resize (int width, int height);

	public void render ();

	public Canvas setEnabled (boolean enabled);
}

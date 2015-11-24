
package com.realhome.v2.view.menu;

import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuBar;
import com.realhome.v2.common.pattern.mvc.View;

public class MenuBarView extends View {

	private MenuBar menuBar;

	public MenuBarView () {
		menuBar = new MenuBar();
		menuBar.addMenu(new Menu("Toto"));
		menuBar.addMenu(new Menu("Tata"));
		init(menuBar.getTable());
	}
}

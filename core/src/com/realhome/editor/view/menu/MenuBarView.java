
package com.realhome.editor.view.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuBar;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.realhome.editor.common.pattern.mvc.View;

public class MenuBarView extends View {

	private MenuBar menuBar;

	public MenuBarView () {
		menuBar = new MenuBar();
		Menu file = new Menu("File");

		file.addItem(new MenuItem("Test 1"));
		file.addItem(new MenuItem("Test 1"));
		file.addItem(new MenuItem("Test 1"));

		Menu file2 = new Menu("TOTO 2");
		file2.addItem(new MenuItem("mama"));
		file2.addItem(new MenuItem("mama2"));
		file2.addItem(new MenuItem("mama3"));

		Menu file3 = new Menu("TOTO 3");
		file3.addItem(new MenuItem("tama"));
		file3.addItem(new MenuItem("tama2"));
		file3.addItem(new MenuItem("tama3"));

		Menu file4 = new Menu("TOTO 4");
		file4.addItem(new MenuItem("yama"));
		file4.addItem(new MenuItem("yama2"));
		file4.addItem(new MenuItem("yama3"));

		menuBar.addMenu(file);
		menuBar.addMenu(file2);
		menuBar.addMenu(file3);
		menuBar.addMenu(file4);
		init(menuBar.getTable());

		padMenus(20);
	}

	private void padMenus (int pad) {
		Table menuItems = (Table)menuBar.getTable().getCells().get(0).getActor();
		for (Cell<?> c : menuItems.getCells()) {
			c.padLeft(pad);
			c.padRight(pad);
		}
	}
}

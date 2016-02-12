package com.realhome.editor.ui;



import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuBar;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.realhome.editor.util._;

public class MenuBarComposer {
	public final static String NAME = "ui.MenuBar";

	private MenuBar menuBar;
	private MenuItem menuNew;
	private MenuItem menuOpen;

	public MenuBarComposer () {
		menuBar = new MenuBar();

		Menu file = new Menu(_.g("file"));
		file.addItem(menuNew = new MenuItem(_.g("new")));
		file.addItem(menuOpen = new MenuItem(_.g("open")));
		file.addItem(new MenuItem(_.g("save")));
		file.addItem(new MenuItem(_.g("save_as")));
		file.addSeparator();
		file.addItem(new MenuItem(_.g("exit")));

		Menu edit = new Menu(_.g("edit"));
		edit.addItem(new MenuItem(_.g("undo")));
		edit.addItem(new MenuItem(_.g("redo")));
		edit.addItem(new MenuItem(_.g("preference")));

		Menu share = new Menu(_.g("share"));
		share.addItem(new MenuItem(_.g("share_external")));
		share.addItem(new MenuItem(_.g("export")));

		Menu help = new Menu(_.g("help"));
		help.addItem(new MenuItem(_.g("online_help")));
		help.addItem(new MenuItem(_.g("about")));

		menuBar.addMenu(file);
		menuBar.addMenu(edit);
		menuBar.addMenu(share);
		menuBar.addMenu(help);

		padMenus(20);
	}

	public Table getTable() {
		return menuBar.getTable();
	}

	public void addOpenListener (ChangeListener listener) {
		menuOpen.addListener(listener);
	}

	public void addNewListener (ChangeListener listener) {
		menuNew.addListener(listener);
	}

	private void padMenus (int pad) {
		Table menuItems = (Table)menuBar.getTable().getCells().get(0).getActor();
		for (Cell<?> c : menuItems.getCells()) {
			c.padLeft(pad);
			c.padRight(pad);
		}
	}
}

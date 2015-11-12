/*
 * ******************************************************************************
 *  * Copyright 2015 See AUTHORS file.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package com.realhome.view.menu;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.realhome.RealHomeFacade;
import com.realhome.event.MenuItemListener;
import com.realhome.proxy.I18nManager;
import com.realhome.view.ui.widgets.CustomMenu;
import com.realhome.view.ui.widgets.CustomMenuBar;

public class RealHomeMenuBar extends CustomMenuBar {

	public static final String TAG = "com.realhome.view.menu.RealHomeMenuBar";

	/* File Menu */
	public static final String FILE_MENU = TAG + ".FILE_MENU";
	public static final String NEW_PROJECT = TAG + ".NEW_PROJECT";
	public static final String OPEN_PROJECT = TAG + ".OPEN_PROJECT";
	public static final String SAVE_PROJECT = TAG + ".SAVE_PROJECT";
	public static final String SAVE_AS_PROJECT = TAG + ".SAVE_AS_PROJECT";
	public static final String EXIT = TAG + ".EXIT";

	/* Edit Menu */
	public static final String EDIT_MENU = TAG + ".EDIT_MENU";
	public static final String UNDO = TAG + ".UNDO";
	public static final String REDO = TAG + ".REDO";
	public static final String PREFERENCE = TAG + ".PREFERENCE";

	/* Share Menu */
	public static final String SHARE_MENU = TAG + ".SHARE_MENU";
	public static final String SHARE_EXTERNAL = TAG + ".SHARE_EXTERNAL";
	public static final String EXPORT = TAG + ".EXPORT";

	/* Help Menu */
	public static final String HELP_MENU = TAG + ".HELP_MENU";
	public static final String ONLINE_HELP = TAG + ".ONLINE_HELP";
	public static final String ABOUT = TAG + ".ABOUT";

	private final String maskKey;
	private final RealHomeFacade facade;

	public RealHomeMenuBar () {
		facade = RealHomeFacade.getInstance();

		maskKey = getMaskKey();

		addMenu(new FileMenu());
		addMenu(new EditMenu());
		addMenu(new ShareMenu());
		addMenu(new HelpMenu());
	}

	private String getMaskKey () {
		if (Gdx.app.getType() == ApplicationType.Desktop && System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0)
			return "Cmd";
		return "Ctrl";
	}

	private class FileMenu extends RealHomeMenu {

		public FileMenu () {
			I18nManager i18n = facade.retrieveProxy(I18nManager.NAME);
			load(i18n.get("file"));

			addItem(new MenuItem(i18n.get("new"), new MenuItemListener(NEW_PROJECT, null, FILE_MENU)));
			addItem(new MenuItem(i18n.get("open"), new MenuItemListener(OPEN_PROJECT, null, FILE_MENU)));
			addItem(new MenuItem(i18n.get("save"), new MenuItemListener(SAVE_PROJECT, null, FILE_MENU)));
			addItem(new MenuItem(i18n.get("save_as"), new MenuItemListener(SAVE_AS_PROJECT, null, FILE_MENU)));
			addSeparator();
			addItem(new MenuItem(i18n.get("exit"), new MenuItemListener(EXIT, null, FILE_MENU)));
		}
	}

	private class EditMenu extends RealHomeMenu {

		public EditMenu () {
			I18nManager i18n = facade.retrieveProxy(I18nManager.NAME);
			load(i18n.get("edit"));

			addItem(new MenuItem(i18n.get("undo"), new MenuItemListener(UNDO, null, EDIT_MENU)).setShortcut(maskKey + " + Z"));
			addItem(new MenuItem(i18n.get("redo"), new MenuItemListener(REDO, null, EDIT_MENU))
				.setShortcut(maskKey + " + Shift + Z"));
			addItem(new MenuItem(i18n.get("preference"), new MenuItemListener(PREFERENCE, null, EDIT_MENU)));
		}
	}

	private class ShareMenu extends RealHomeMenu {

		public ShareMenu () {
			I18nManager i18n = facade.retrieveProxy(I18nManager.NAME);
			load(i18n.get("share"));

			addItem(new MenuItem(i18n.get("share_external"), new MenuItemListener(SHARE_EXTERNAL, null, SHARE_MENU)));
			addItem(new MenuItem(i18n.get("export"), new MenuItemListener(EXPORT, null, SHARE_MENU)));
		}
	}

	private class HelpMenu extends RealHomeMenu {

		public HelpMenu () {
			I18nManager i18n = facade.retrieveProxy(I18nManager.NAME);
			load(i18n.get("help"));

			addItem(new MenuItem(i18n.get("online_help"), new MenuItemListener(ONLINE_HELP, null, HELP_MENU)));
			addItem(new MenuItem(i18n.get("about"), new MenuItemListener(ABOUT, null, HELP_MENU)));
		}
	}

	class RealHomeMenu extends CustomMenu {
		@Override
		public void load (String title) {
			super.load(title);
			Cell<?> labelCell = openButton.getLabelCell();
			labelCell.width(openButton.getWidth() + 14);
		}
	}
}

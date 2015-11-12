
package com.realhome.views.menus;

import com.realhome.RealHomeFacade;
import com.realhome.commons.patterns.mediator.SimpleMediator;
import com.realhome.commons.patterns.observer.Notification;

/** @author realitix */
public class RealHomeMenuBarMediator extends SimpleMediator<RealHomeMenuBar> {
	private static final String TAG = "com.realhome.views.menus.RealHomeMenuBarMediator";
	public static final String NAME = TAG;

	// private ProjectManager projectManager;

	public RealHomeMenuBarMediator () {
		super(NAME, new RealHomeMenuBar());
	}

	@Override
	public void onRegister () {
		super.onRegister();
		facade = RealHomeFacade.getInstance();
		// projectManager = facade.retrieveProxy(ProjectManager.NAME);
	}

	@Override
	public String[] listNotificationInterests () {
		return new String[] {};
		// FILE
		/*
		 * RealHomeMenuBar.NEW_PROJECT, Overlap2DMenuBar.OPEN_PROJECT, Overlap2DMenuBar.SAVE_PROJECT,
		 * RealHomeMenuBar.IMPORT_TO_LIBRARY, Overlap2DMenuBar.EXPORT, Overlap2DMenuBar.EXPORT_SETTINGS,
		 * RealHomeMenuBar.RECENT_PROJECTS, Overlap2DMenuBar.CLEAR_RECENTS, Overlap2DMenuBar.EXIT, Overlap2DMenuBar.NEW_SCENE,
		 * RealHomeMenuBar.SELECT_SCENE, Overlap2DMenuBar.DELETE_CURRENT_SCENE,
		 */
		// EDIT
		// Overlap2DMenuBar.CUT, Overlap2DMenuBar.COPY, Overlap2DMenuBar.PASTE, Overlap2DMenuBar.UNDO, Overlap2DMenuBar.REDO,
		// General
		// ProjectManager.PROJECT_OPENED, Overlap2DMenuBar.RECENT_LIST_MODIFIED};
	}

	@Override
	public void handleNotification (Notification notification) {
		super.handleNotification(notification);
		String type = notification.getType();

		/*
		 * if (notification.getName().equals(Overlap2DMenuBar.RECENT_LIST_MODIFIED)) { PreferencesManager prefs =
		 * PreferencesManager.getInstance(); viewComponent.reInitRecent(prefs.getRecentHistory()); }
		 */

		if (type == null) {
			// handleGeneralNotification(notification);
			return;
		}
		switch (type) {
		case RealHomeMenuBar.FILE_MENU:
			// handleFileMenuNotification(notification);
			break;
		case RealHomeMenuBar.EDIT_MENU:
			// handleEditMenuNotification(notification);
			break;
		default:
			break;
		}
	}

	/*
	 * private void handleGeneralNotification (Notification notification) { switch (notification.getName()) { case
	 * ProjectManager.PROJECT_OPENED: onProjectOpened(); break; } }
	 *
	 * private void handleEditMenuNotification (Notification notification) { Sandbox sandbox = Sandbox.getInstance(); switch
	 * (notification.getName()) { case Overlap2DMenuBar.CUT: facade.sendNotification(MsgAPI.ACTION_CUT); break; case
	 * Overlap2DMenuBar.COPY: facade.sendNotification(MsgAPI.ACTION_COPY); break; case Overlap2DMenuBar.PASTE:
	 * facade.sendNotification(MsgAPI.ACTION_PASTE); break; case Overlap2DMenuBar.UNDO: CommandManager commandManager =
	 * facade.retrieveProxy(CommandManager.NAME); commandManager.undoCommand(); break; case Overlap2DMenuBar.REDO: commandManager =
	 * facade.retrieveProxy(CommandManager.NAME); commandManager.redoCommand(); break; } }
	 *
	 * private void handleFileMenuNotification(Notification notification) { Sandbox sandbox = Sandbox.getInstance(); switch
	 * (notification.getName()) { case Overlap2DMenuBar.NEW_PROJECT: break; case Overlap2DMenuBar.OPEN_PROJECT: showOpenProject();
	 * break; case Overlap2DMenuBar.SAVE_PROJECT: SceneVO vo = sandbox.sceneVoFromItems(); projectManager.saveCurrentProject(vo);
	 * break; case Overlap2DMenuBar.IMPORT_TO_LIBRARY: //showDialog("showImportDialog"); break; case
	 * Overlap2DMenuBar.RECENT_PROJECTS: recentProjectItemClicked(notification.getBody()); //showDialog("showImportDialog"); break;
	 * case Overlap2DMenuBar.CLEAR_RECENTS: clearRecents(); break; case Overlap2DMenuBar.EXPORT:
	 * facade.sendNotification(MsgAPI.ACTION_EXPORT_PROJECT); break; case Overlap2DMenuBar.EXPORT_SETTINGS:
	 * //showDialog("showExportDialog"); break; case Overlap2DMenuBar.EXIT: Gdx.app.exit(); break; case Overlap2DMenuBar.NEW_SCENE:
	 * DialogUtils.showInputDialog(sandbox.getUIStage(), "Create New Scene", "Scene Name : ", new InputDialogListener() {
	 *
	 * @Override public void finished(String input) { if (input == null || input.equals("")) { return; } SceneDataManager
	 * sceneDataManager = facade.retrieveProxy(SceneDataManager.NAME); sceneDataManager.createNewScene(input);
	 * sandbox.loadScene(input); onScenesChanged(); }
	 *
	 * @Override public void canceled() {
	 *
	 * } }); break; case Overlap2DMenuBar.SELECT_SCENE: sceneMenuItemClicked(notification.getBody()); break; case
	 * Overlap2DMenuBar.DELETE_CURRENT_SCENE: DialogUtils.showConfirmDialog(sandbox.getUIStage(), "Delete Scene",
	 * "Do you realy want to delete '" + projectManager.currentProjectVO.lastOpenScene + "' scene?", new String[]{"Delete",
	 * "Cancel"}, new Integer[]{0, 1}, result -> { if (result == 0) { SceneDataManager sceneDataManager =
	 * facade.retrieveProxy(SceneDataManager.NAME); sceneDataManager.deleteCurrentScene(); sandbox.loadScene("MainScene");
	 * onScenesChanged(); } }); break; } }
	 *
	 * private void onScenesChanged () { viewComponent.reInitScenes(projectManager.currentProjectInfoVO.scenes); }
	 *
	 * private void onProjectOpened () { viewComponent.reInitScenes(projectManager.currentProjectInfoVO.scenes);
	 * viewComponent.setProjectOpen(true); }
	 *
	 * public void showOpenProject () { Sandbox sandbox = Sandbox.getInstance(); // chooser creation FileChooser fileChooser = new
	 * FileChooser(FileChooser.Mode.OPEN);
	 *
	 * // TODO: does not show folders on Windows // fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES); //
	 * fileChooser.setFileFilter(new SuffixFileFilter(".pit"));
	 *
	 * fileChooser.setMultiselectionEnabled(false); fileChooser.setDirectory(projectManager.getWorkspacePath());
	 * fileChooser.setListener(new FileChooserAdapter() {
	 *
	 * @Override public void selected (FileHandle file) { String path = file.file().getAbsolutePath(); if (path.length() > 0) {
	 * projectManager.openProjectFromPath(path); } } }); sandbox.getUIStage().addActor(fileChooser.fadeIn()); }
	 *
	 * public void recentProjectItemClicked (String path) { PreferencesManager prefs = PreferencesManager.getInstance();
	 * prefs.buildRecentHistory(); prefs.pushHistory(path); Sandbox sandbox = Sandbox.getInstance();
	 * projectManager.openProjectFromPath(path); }
	 *
	 * public void clearRecents () { PreferencesManager prefs = PreferencesManager.getInstance(); prefs.clearHistory();
	 * viewComponent.reInitRecent(prefs.getRecentHistory()); }
	 *
	 * public void sceneMenuItemClicked (String sceneName) { Sandbox sandbox = Sandbox.getInstance(); sandbox.loadScene(sceneName);
	 * }
	 */

	/*
	 * public void addMenuItem (String menu, String subMenuName, String notificationName) { viewComponent.addMenuItem(menu,
	 * subMenuName, notificationName); }
	 */
}

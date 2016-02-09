public class ThreedView extends BaseView<ThreedWidget> {

	public final static String NAME = "view.ThreedView";

	private boolean updated;

	public ThreedView () {
		ThreedWidget widget = new ThreedWidget();
		widget.init();
		init(widget, NAME);
	}

	public void reloadHouse (House house) {
		actor.reloadHouse(house);
	}

	public ThreedView enable () {
		actor.setVisible(true);
		updated = true;
		return this;
	}

	public ThreedView disable () {
		actor.setVisible(false);
		updated = true;
		return this;
	}

	@Override
	public boolean isUpdated () {
		boolean result = updated;
		updated = false;
		return result;
	}

	public void addListener (EventListener listener) {
		actor.addListener(listener);
	}

	public ThreedModeler getModeler() {
		return actor.getModeler();
	}
}
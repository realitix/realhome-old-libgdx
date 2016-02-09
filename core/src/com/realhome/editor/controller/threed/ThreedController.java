public class ThreedController extends BaseController<PlanView> {

	public ThreedController (ThreedView view) {
		super(view);
		view.addListener(new ThreedListener());
	}

	@Override
	public void receiveNotification (Notification notification) {
		switch (notification.getName()) {
		case Message.DISPLAY_THREED:
			view.reloadHouse(appModel.getHouse());
			view.enable();
			break;
		}
	}
}

public class Interactor {
	private final D3Modeler modeler;
	private final House house;
	private final Model houseModel;
	private final HouseModelBuilder builder;

	public InteractorD3Modeler(D3Modeler modeler, House house, Model houseModel) {
		this.modeler = modeler;
		this.house = house;
		this.houseModel = houseModel;
		builder = new HouseModelBuilder(house, houseModel);
	}

	public void update() {
		builder.sync();
	}
}
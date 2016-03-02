package com.realhome.editor.modeler.d3.interactor;

import com.badlogic.gdx.graphics.g3d.Model;
import com.realhome.editor.model.house.House;
import com.realhome.editor.modeler.d3.D3Modeler;
import com.realhome.editor.modeler.d3.builder.HouseModelBuilder;

public class Interactor {
	private final D3Modeler modeler;
	private final House house;
	private final Model houseModel;
	private final HouseModelBuilder builder;

	public Interactor(D3Modeler modeler, House house, Model houseModel) {
		this.modeler = modeler;
		this.house = house;
		this.houseModel = houseModel;
		builder = new HouseModelBuilder(house, houseModel);
	}

	public void update() {
		builder.sync();
	}
}
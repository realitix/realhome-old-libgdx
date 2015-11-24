
package com.realhome.v2.view.presenter;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.realhome.v2.common.pattern.mvc.View;

public interface Presenter {
	public void present (Stage stage, Array<View> views);
}

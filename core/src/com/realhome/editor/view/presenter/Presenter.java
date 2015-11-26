
package com.realhome.editor.view.presenter;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.realhome.editor.common.pattern.mvc.View;

public interface Presenter {
	public void present (Stage stage, Array<View> views);
}

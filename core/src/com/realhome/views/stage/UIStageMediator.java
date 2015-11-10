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

package com.realhome.views.stage;

import com.realhome.RealHomeFacade;
import com.realhome.commons.MsgAPI;
import com.realhome.commons.patterns.mediator.SimpleMediator;
import com.realhome.commons.patterns.observer.Notification;

/** Created by sargis on 4/20/15. */
public class UIStageMediator extends SimpleMediator<UIStage> {
	private static final String TAG = UIStageMediator.class.getCanonicalName();
	public static final String NAME = TAG;

	public UIStageMediator () {
		super(NAME, new UIStage());
	}

	@Override
	public void onRegister () {
		super.onRegister();
		facade = RealHomeFacade.getInstance();
	}

	@Override
	public String[] listNotificationInterests () {
		return new String[] {MsgAPI.SHOW_ADD_LIBRARY_DIALOG};
	}

	@Override
	public void handleNotification (Notification notification) {
		switch (notification.getName()) {
		case MsgAPI.SHOW_ADD_LIBRARY_DIALOG:
			// Sandbox sandbox = Sandbox.getInstance();

			// Entity item = notification.getBody();

			/*
			 * DialogUtils.showInputDialog(sandbox.getUIStage(), "New Library Item ", "Unique Name", new InputDialogListener() {
			 * 
			 * @Override public void finished (String input) { Object[] payload = new Object[2]; //payload[0] = item; payload[1] =
			 * input; facade.sendNotification(MsgAPI.ACTION_ADD_TO_LIBRARY, payload); }
			 * 
			 * @Override public void canceled () {
			 * 
			 * } });
			 */
			break;
		}
	}

}

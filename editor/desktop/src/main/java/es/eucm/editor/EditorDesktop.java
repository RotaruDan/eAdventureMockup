/**
 * eAdventure is a research project of the
 *    e-UCM research group.
 *
 *    Copyright 2005-2013 e-UCM research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    e-UCM is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.eucm.editor;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglFrame;
import es.eucm.editor.io.Platform;

import javax.swing.*;

public class EditorDesktop extends Editor {

	private LwjglFrame frame;

	public EditorDesktop(Platform platform) {
		super(platform);
	}

	@Override
	public void create() {
		super.create();
		frame = ((DesktopPlatform) platform).getFrame();
		if (preferences.getBoolean(Prefs.WINDOW_MAXIMIZED)) {
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		} else {
			int width = preferences.getInteger(Prefs.WINDOW_WIDTH);
			int height = preferences.getInteger(Prefs.WINDOW_HEIGHT);
			frame.setSize(width, height);
		}
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		preferences.putInteger(Prefs.WINDOW_WIDTH, width);
		preferences.putInteger(Prefs.WINDOW_HEIGHT, height);
		preferences.putBoolean(Prefs.WINDOW_MAXIMIZED,
				frame.getExtendedState() == JFrame.MAXIMIZED_BOTH);
		preferences.flush();
	}

	public static void main(String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.useGL20 = true;
		config.forceExit = true;
		DesktopPlatform platform = new DesktopPlatform();
		LwjglFrame frame = new LwjglFrame(new EditorDesktop(platform), config);
		platform.setFrame(frame);
		frame.setVisible(true);
	}
}

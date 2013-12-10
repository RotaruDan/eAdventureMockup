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
package es.eucm.ead.editor;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import es.eucm.ead.editor.control.Controller;
import es.eucm.ead.editor.conversors.EditorConversor;
import es.eucm.ead.editor.factories.EditorFactory;
import es.eucm.ead.editor.io.EditorIO;
import es.eucm.ead.editor.io.Platform;
import es.eucm.ead.editor.listeners.EditorEventListener;
import es.eucm.ead.editor.scene.EditorSceneManager;
import es.eucm.ead.engine.Assets;
import es.eucm.ead.engine.EAdEngine;
import es.eucm.ead.engine.EngineStage;
import es.eucm.ead.engine.Factory;
import es.eucm.ead.engine.FileResolver;
import es.eucm.ead.engine.io.JsonIO;
import es.eucm.ead.engine.scene.SceneManager;

public class Editor extends EAdEngine {

	public static boolean debug = false;

	public static EditorConversor conversor;
	public static Platform platform;
	public static Controller controller;

	private static final String nameOfPreferences = "eadventure_editor";

	public Editor(String path, Platform platform) {
		super(path);
		Editor.platform = platform;
	}

	@Override
	public void create() {
		if (debug) {
			Gdx.app.setLogLevel(Application.LOG_DEBUG);
		}
		Editor.conversor = new EditorConversor();
		Editor.controller = new Controller(nameOfPreferences);
		super.create();

		Preferences p = controller.getPrefs();

		I18N.setLang(p.getString(Prefs.lang, Prefs.defaultLang));
		if (!p.contains(Prefs.lang)) {
			p.putString(Prefs.lang, Prefs.defaultLang);
		}
		platform.setTitle(I18N.m("editor.title"));
		platform.setSize(p.getInteger(Prefs.editorWidth,
				Prefs.defaultEditorWidth), p.getInteger(Prefs.editorHeight,
				Prefs.defaultEditorHeight));
	}

	@Override
	public void render() {
		super.render();
		if (debug) {
			Table.drawDebug(stage);
			stage.getSpriteBatch().begin();
			assets.defaultFont().draw(stage.getSpriteBatch(),
					"FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);
			stage.getSpriteBatch().end();
		}
	}

	@Override
	protected EngineStage createStage() {
		return new EditorStage(Gdx.graphics.getWidth(), Gdx.graphics
				.getHeight(), false);
	}

	@Override
	protected EventListener createEventListener() {
		return new EditorEventListener((EditorStage) Editor.stage);
	}

	@Override
	protected Factory createFactory() {
		return new EditorFactory();
	}

	@Override
	protected JsonIO createJsonIO() {
		return new EditorIO();
	}

	@Override
	protected SceneManager createSceneManager(Assets assetManager) {
		return new EditorSceneManager(assetManager);
	}
}
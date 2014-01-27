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
package es.eucm.ead.mockup.core.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IdentityMap;

import es.eucm.ead.editor.control.Controller;
import es.eucm.ead.mockup.core.control.listeners.EventListener;
import es.eucm.ead.mockup.core.control.listeners.FocusListener;
import es.eucm.ead.mockup.core.control.screens.AbstractScreen;
import es.eucm.ead.mockup.core.control.screens.Loading;
import es.eucm.ead.mockup.core.control.screens.Screens;
import es.eucm.ead.mockup.core.control.screens.camara.Picture;
import es.eucm.ead.mockup.core.control.screens.camara.Recording;
import es.eucm.ead.mockup.core.control.screens.edition.ElementEdition;
import es.eucm.ead.mockup.core.control.screens.edition.SceneEdition;
import es.eucm.ead.mockup.core.control.screens.gallery.ElementGallery;
import es.eucm.ead.mockup.core.control.screens.gallery.Gallery;
import es.eucm.ead.mockup.core.control.screens.gallery.ProjectGallery;
import es.eucm.ead.mockup.core.control.screens.gallery.SceneGallery;
import es.eucm.ead.mockup.core.control.screens.menu.MainMenu;
import es.eucm.ead.mockup.core.control.screens.menu.ProjectMenu;

/**
 * The main controller for Mockup Editor. Controlls the different views or
 * screens and stores a reference to Editor's Controller providing access to
 * <ul>
 * <li>persistent editor preferences</li>
 * <li>internationalized messages (i18n)</li>
 * <li>currently-edited game</li>
 * <li>project controller (in charge of creating and managing games)</li>
 * <li>command-manager (for undo/redo)</li>
 * <li>actions (reusable editor calls)</li>
 * </ul>
 */
public class MockupController implements EventListener, Disposable {

	private IdentityMap<Screens, AbstractScreen> states;
	private Controller controller;
	private ScreenController screenCtr;

	/**
	 * Is the screen that we came from. Used if we want to go to he previous
	 * screen.
	 */
	private Screens previousScreen, actualScreen;

	/**
	 * The loading Screen.
	 */
	private Loading loading;

	public MockupController() {
		AbstractScreen.mockupController = this;
		AbstractScreen.am = new AssetManager();
		Gdx.input.setCatchBackKey(true);

		loading = new Loading();
		loading.create();
		// { loading.create(); }
		// Must be done before we create the other screens so if we
		// use variables like stagew, stageh... in their constructor (or final)
		// we make sure those attributes are properly setted.

		this.states = new IdentityMap<Screens, AbstractScreen>();
		/* MENUS */
		this.states.put(Screens.MAIN_MENU, new MainMenu());
		this.states.put(Screens.PROJECT_MENU, new ProjectMenu());
		/* GALLERIES */
		this.states.put(Screens.GALLERY, new Gallery());
		this.states.put(Screens.PROJECT_GALLERY, new ProjectGallery());
		this.states.put(Screens.SCENE_GALLERY, new SceneGallery());
		this.states.put(Screens.ELEMENT_GALLERY, new ElementGallery());
		/* CAMARA */
		this.states.put(Screens.RECORDING, new Recording());
		this.states.put(Screens.PICTURE, new Picture());
		/* EDITION */
		this.states.put(Screens.SCENE_EDITION, new SceneEdition());
		this.states.put(Screens.ELEMENT_EDITION, new ElementEdition());

		this.screenCtr = new ScreenController();

		actualScreen = Screens.LOADING;
		this.screenCtr.setCurrentScreen(loading);
	}

	public void create() {
		for (AbstractScreen _screen : this.states.values()) {
			_screen.create();
		}
		screenCtr.create();
	}

	/**
	 * Updates the handlers through EventController.
	 * 
	 * @param delta
	 *            Elapsed time since the game last updated.
	 */
	public void act(float delta) {
		this.screenCtr.act(delta);
	}

	/**
	 * Draws the renderers through RendererController.
	 */
	public void draw() {
		this.screenCtr.draw();
	}

	public void changeTo(Screens next) {
		AbstractScreen _screen = this.states.get(next);
		previousScreen = actualScreen;
		actualScreen = next;
		this.screenCtr.changeTo(_screen);
	}

	public ScreenController getScreenController() {
		return screenCtr;
	}

	public Controller getController() {
		return this.controller;
	}

	public void resize(int width, int height) {
	}

	public void pause() {
		this.screenCtr.pause();
	}

	public void resume() {
		this.screenCtr.resume();
	}

	public void dispose() {
		for (AbstractScreen _screen : this.states.values()) {
			_screen.dispose();
		}
		
		loading.dispose();

		AbstractScreen.stage.dispose();
		AbstractScreen.stage = null;

		AbstractScreen.am.dispose();
		AbstractScreen.am = null;

		/* TODO Dispose Engine here */

		System.exit(0);
	}

	/**
	 * Is the screen that we came from. Used if we want to go to he previous
	 * screen.
	 */
	public Screens getPreviousScreen() {
		return this.previousScreen;
	}

	public void show(FocusListener focusListener) {
		focusListener.show();
	}

	public void hide(FocusListener focusListener) {
		focusListener.hide();
	}
}

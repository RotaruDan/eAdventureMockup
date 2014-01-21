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
package es.eucm.ead.mockup.core.control.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

import es.eucm.ead.mockup.core.view.UIAssets;
import es.eucm.ead.mockup.core.view.ui.CircularGroup;
import es.eucm.ead.mockup.core.view.ui.buttons.MenuButton;

public class MainMenu extends AbstractScreen {
	
	private final float MAIN_MENU_BUTTON_WIDTH_HEIGHT = stageh * .23f;

	private Group optionsGroup;
	private Dialog exitDialog;

	@Override
	public void create() {
		this.optionsGroup = UIAssets.getOptionsGroup();

		super.root = new Group();
		root.setVisible(false);

		final Button newProject, projectGallery;		
		newProject = new MenuButton("Nuevo\nproyecto", skin, "ic_newproject",
		MAIN_MENU_BUTTON_WIDTH_HEIGHT, MAIN_MENU_BUTTON_WIDTH_HEIGHT);//TODO use i18n in this class
		projectGallery = new MenuButton("Galería de\nproyectos", skin, "ic_gallery",
				MAIN_MENU_BUTTON_WIDTH_HEIGHT, MAIN_MENU_BUTTON_WIDTH_HEIGHT);//TODO use i18n in this class

		CircularGroup cg = new CircularGroup(halfstageh, 135, 180, true, newProject,
				projectGallery);
		cg.setX(halfstagew);
		cg.setY(halfstageh);
		
		//Scan for aviable projects here...
		Table projectsTable = new Table();
		//projectsTable.debug();
		projectsTable.defaults().space(10);
		ScrollPane sp = new ScrollPane(projectsTable);
		sp.setBounds(stagew * .1f, 10, stagew * .8f, stageh * .2f);
		sp.setScrollingDisabled(false, true);
		Texture t = new Texture(Gdx.files.internal("mockup/temp/proyecto.png"));
		t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		final int PROJECTS = 8;
		final Array<Actor> mProjects = new Array<Actor>(false, PROJECTS);
		
		// Creating the transition listener
		ClickListener mTransitionListener = new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				final Screens next = getNextScreen(event.getListenerActor());
				if (next == null) {
					return;
				}
				exitAnimation(next);
			}

			private Screens getNextScreen(Actor target) {
				Screens next = null;
				if (target == newProject) {
					next = Screens.PROJECT_MENU;
				} else if (target == projectGallery) {
					next = Screens.PROJECT_GALLERY;
				} else {
					for (Actor project : mProjects) {
						if (target == project) {
							next = Screens.PROJECT_MENU;
						}
					}
				}
				return next;
			}
		};

		for (int i = 0; i < PROJECTS; ++i) {
			Image im = new Image(t);
			im.addListener(mTransitionListener);

			projectsTable.add(im);

			mProjects.add(im);
		}		
		
		newProject.addListener(mTransitionListener);
		projectGallery.addListener(mTransitionListener);	

		Image bg = new Image(skin.getRegion("bg"));
		bg.setTouchable(Touchable.disabled);
		bg.setBounds(0, 0, stagew, stageh);

		root.addActor(bg);
		root.addActor(sp);
		root.addActor(cg);
		stage.addActor(root);

		exitDialog = new Dialog("¿Salir?", skin, "exit-dialog") {
			protected void result(Object object) {
				if ((Boolean) object) {
					Gdx.app.exit();
				}
			}
		}.text("¿Estás seguro?").button("Salir", true).button("¡Todavía no!",
				false).key(Keys.BACK, false).key(Keys.ENTER, true); // TODO use i18n
		exitDialog.setMovable(false);
	}

	@Override
	public void show() {
		super.show();
		root.setVisible(true);
		optionsGroup.setVisible(true);
	}

	@Override
	public void act(float delta) {
		stage.act(delta);
	}

	@Override
	public void draw() {
		stage.draw();
		//Table.drawDebug(stage);
	}

	@Override
	public void hide() {
		root.setVisible(false);
		optionsGroup.setVisible(false);
	}

	@Override
	public void onBackKeyPressed() {
		exitDialog.show(stage);

	}
}

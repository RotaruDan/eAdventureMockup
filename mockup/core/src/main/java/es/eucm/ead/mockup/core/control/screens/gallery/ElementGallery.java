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
package es.eucm.ead.mockup.core.control.screens.gallery;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import es.eucm.ead.mockup.core.control.screens.AbstractScreen;
import es.eucm.ead.mockup.core.control.screens.Screens;
import es.eucm.ead.mockup.core.view.UIAssets;
import es.eucm.ead.mockup.core.view.ui.GridPanel;
import es.eucm.ead.mockup.core.view.ui.Panel;
import es.eucm.ead.mockup.core.view.ui.ToolBar;

public class ElementGallery extends AbstractScreen {

	private Group navigationGroup;
	private ToolBar toolBar;

	@Override
	public void create() {
		setPreviousScreen(Screens.PROJECT_MENU);
		navigationGroup = UIAssets.getNavigationGroup();

		super.root = new Group();
		root.setVisible(false);

		toolBar = new ToolBar(skin);
		//toolBar.setVisible(false);
		toolBar.right();

		String search = "Buscar por ...";//TODO use i18n!
		TextField searchtf = new TextField("", skin);
		searchtf.setMessageText(search);
		searchtf.setMaxLength(search.length());
		String[] orders = new String[] { "Ordenar por ...", "Ordenar por 2..." };//TODO use i18n!
		SelectBox order = new SelectBox(orders, skin);

		/*filter panel*/
		Button applyFilter = new TextButton("Filtrar", skin);

		CheckBox[] tags = new CheckBox[] { new CheckBox("Hospital", skin),
				new CheckBox("Quirófano", skin),
				new CheckBox("Enfermera", skin), new CheckBox("Camilla", skin),
				new CheckBox("Almohada", skin),
				new CheckBox("Habitación", skin),
				new CheckBox("Vehículo", skin), new CheckBox("Doctor", skin),
				new CheckBox("Paciente", skin), new CheckBox("Guantes", skin),
				new CheckBox("Medicamentos", skin),
				new CheckBox("Médico", skin) };
		Table tagList = new Table(skin);
		tagList.left();
		tagList.defaults().left();
		for (int i = 0; i < tags.length; ++i) {
			tagList.add(tags[i]);
			if (i < tags.length - 1)
				tagList.row();
		}
		ScrollPane tagScroll = new ScrollPane(tagList, skin, "opaque");

		final Panel filterPanel = new Panel(skin);
		filterPanel.setVisible(false);
		final float panelw = stagew * .26f, panelx = stagew - panelw;
		filterPanel.add(tagScroll).fill().colspan(3).left();
		filterPanel.row();
		filterPanel.add(applyFilter).colspan(3).expandX();
		filterPanel.setBounds(panelx, toolBar.getHeight(), panelw, stageh
				- toolBar.getHeight() * 2f);

		Button filterButton = new TextButton("Filtrar por tags", skin);
		ClickListener closeFilterListenerTmp = new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (filterPanel.isVisible()) {
					mockupController.hide(filterPanel);
				} else {
					mockupController.show(filterPanel);
				}
			}
		};
		applyFilter.addListener(closeFilterListenerTmp);
		filterButton.addListener(closeFilterListenerTmp);

		
		Label nombre = new Label("Galería de elementos", skin);

		toolBar.add(nombre).expandX().left().padLeft(
				UIAssets.NAVIGATION_BUTTON_WIDTH_HEIGHT*1.1f);
		toolBar.add(order);
		toolBar.add(filterButton);
		toolBar.add(searchtf).width(
				skin.getFont("default-font").getBounds(search).width + 50); //FIXME hardcoded fixed value
		/***/
		Texture t = new Texture(Gdx.files.internal("mockup/temp/proyecto.png"));//TODO change for scene
		t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		final int COLS = 3, ROWS = 6;
		GridPanel<Actor> gridPanel = new GridPanel<Actor>(skin, ROWS, COLS,
				UIAssets.GALLERY_PROJECT_HEIGHT * .2f);
		gridPanel.defaults().fill().uniform();
		boolean first = true;
		for (int i = 0; i < ROWS; ++i) {
			for (int j = 0; j < COLS; ++j) {
				if (first) {
					first = false;
					gridPanel.addItem(new TextButton("NUEVO", skin), 0, 0)
							.fill();
				} else {
					gridPanel.addItem(new Image(t), i, j);
				}
			}
		}
		gridPanel.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (event.getListenerActor() instanceof GridPanel) {
					exitAnimation(Screens.SCENE_EDITION);
				}
			}
		});
		ScrollPane scrollPane = new ScrollPane(gridPanel);
		scrollPane.setScrollingDisabled(true, false);
		scrollPane.setBounds(0, toolBar.getHeight(), stagew, stageh - 2
				* toolBar.getHeight());
		final float DEFAULT_ICON_LABEL_SPACE = 10f;
		final Button picButton = new Button(skin);
		picButton.defaults().space(DEFAULT_ICON_LABEL_SPACE);
		Label picLabel = new Label("Nuevo desde cámara", skin);
		Image picImage = new Image(skin.getDrawable("ic_photocamera"));
		picButton.add(picImage);
		picButton.add(picLabel);

		ClickListener mTransitionLIstener = new ClickListener() {

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
				if (target == picButton) {
					next = Screens.PICTURE;
				} 
				return next;
			}
		};
		picButton.addListener(mTransitionLIstener);

		ToolBar toolBar2 = new ToolBar(skin);
		toolBar2.setY(0);
		toolBar2.add(picButton).expandX().left();

		root.addActor(toolBar);
		root.addActor(toolBar2);
		root.addActor(scrollPane);
		root.addActor(filterPanel);

		stage.addActor(root);
	}

	@Override
	public void show() {
		super.show();
		root.setVisible(true);
		navigationGroup.setVisible(true);
	}

	@Override
	public void act(float delta) {
		stage.act(delta);
	}

	@Override
	public void draw() {
		stage.draw();
	}

	@Override
	public void hide() {
		root.setVisible(false);
		navigationGroup.setVisible(false);
	}
}

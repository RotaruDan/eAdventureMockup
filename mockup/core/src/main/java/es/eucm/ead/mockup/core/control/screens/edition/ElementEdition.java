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
/**
 in * eAdventure is a research project of the
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
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.eucm.ead.mockup.core.control.screens.edition;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;

import es.eucm.ead.mockup.core.control.screens.AbstractScreen;
import es.eucm.ead.mockup.core.control.screens.Loading;
import es.eucm.ead.mockup.core.control.screens.Screens;
import es.eucm.ead.mockup.core.view.UIAssets;
import es.eucm.ead.mockup.core.view.ui.ToolBar;
import es.eucm.ead.mockup.core.view.ui.buttons.ToolbarButton;
import es.eucm.ead.mockup.core.view.ui.components.edition.DeletingComponent;
import es.eucm.ead.mockup.core.view.ui.components.edition.DrawComponent;
import es.eucm.ead.mockup.core.view.ui.components.edition.DrawComponent.Type;
import es.eucm.ead.mockup.core.view.ui.components.edition.EffectsComponent;
import es.eucm.ead.mockup.core.view.ui.components.edition.InteractiveComponent;
import es.eucm.ead.mockup.core.view.ui.components.edition.OtherComponent;
import es.eucm.ead.mockup.core.view.ui.components.edition.OtherComponent.TypeOther;
import es.eucm.ead.mockup.core.view.ui.components.edition.PaintingComponent;
import es.eucm.ead.mockup.core.view.ui.components.edition.RectangleSelector;

public class ElementEdition extends AbstractScreen {

	private ToolBar toolBar;
	private DrawComponent paint;
	private DrawComponent delete;
	private DrawComponent text;
	private InteractiveComponent interac;
	private EffectsComponent effect;
	private OtherComponent more;

	private float TOOLBAR_ICON_HEIGHT;
	private float TOOLBAR_ICON_WIDTH;

	/**
	 * Fast implementation to know what element we shall display (edit).
	 */
	private static Integer ELEMENT_INDEX;

	/**
	 * Shows the element that we're editing.
	 */
	private Image editingElement;

	/**
	 * Used to tdaw the rectangle selection.
	 */
	private RectangleSelector rectSel;

	/**
	 * Used to draw lines.
	 */
	private PaintingComponent paintingComponent;
	private ButtonGroup buttonGroup;
	/**
	 * Used to delete lines.
	 */
	private DeletingComponent deletingComponent;

	@Override
	public void create() {
		this.TOOLBAR_ICON_HEIGHT = UIAssets.TOOLBAR_HEIGHT;
		this.TOOLBAR_ICON_WIDTH = TOOLBAR_ICON_HEIGHT * 1.35f;
		super.root = new Group();
		root.setVisible(false);

		toolBar = new ToolBar(skin);
		toolBar.right();

		Button move = new ToolbarButton(skin.getDrawable("ic_move"), "Mover",
				skin);

		paintingComponent = new PaintingComponent();
		paintingComponent.setVisible(false);
		paint = new DrawComponent(null, paintingComponent, "ic_pencil",
				"Pintar", skin, "Herramienta de pincel", Type.BRUSH, 350, 550);
		deletingComponent = new DeletingComponent(paintingComponent);
		delete = new DrawComponent(deletingComponent, null, "ic_eraser",
				"Borrar", skin, "Herramienta de goma", Type.RUBBER, 350, 250);
		text = new DrawComponent(null, null, "ic_text", "Texto", skin,
				"Herramienta de escribir", Type.TEXT, 350, 550);

		rectSel = new RectangleSelector();
		rectSel.setVisible(false);
		interac = new InteractiveComponent(rectSel, "ic_select", "Selección",
				skin, "Seleccionar parte a recortar usando", 300, 390);
		effect = new EffectsComponent("ic_effects", "Efectos", skin,
				"Añadir efectos de imagen", 300, 600);
		more = new OtherComponent("ic_more", "Otros", skin,
				TypeOther.OTHER_ELEMENT, 300, 600);

		Button frames = new ImageButton(skin, "ic_hidescenes");
		frames.setX(AbstractScreen.stagew - frames.getWidth());

		/* Undo & Redo buttons */
		Button undo = new ToolbarButton(skin.getDrawable("ic_undo"),
				"Deshacer", skin);
		TextureRegion redoRegion = new TextureRegion(skin.getRegion("ic_undo"));
		redoRegion.flip(true, false);
		TextureRegionDrawable redoDrawable = new TextureRegionDrawable(
				redoRegion);
		Button redo = new ToolbarButton(redoDrawable, "Rehacer", skin);

		// Radio-button functionality
		buttonGroup = new ButtonGroup(move, paint.getButton(),
				delete.getButton(), text.getButton(), interac.getButton(),
				effect.getButton(), more.getButton(), undo, redo);

		// toolBar.debug();
		Label name = new Label("Edición de elemento", skin);
		toolBar.add(name)
				.padLeft(UIAssets.NAVIGATION_BUTTON_WIDTH_HEIGHT * 1.1f)
				.expandX().left();
		toolBar.defaults().size(TOOLBAR_ICON_HEIGHT).width(TOOLBAR_ICON_WIDTH);
		toolBar.add(undo);
		toolBar.add(redo);
		toolBar.add(move);
		toolBar.add(paint.getButton());
		toolBar.add(delete.getButton());
		toolBar.add(text.getButton());
		toolBar.add(interac.getButton());
		toolBar.add(effect.getButton());
		toolBar.add(more.getButton());
		/* Make the actors in toolBar update their coordinates */
		toolBar.invalidate();
		toolBar.validate();

		editingElement = new Image();
		editingElement.setScaling(Scaling.fit);
		editingElement
				.setBounds(0, 0, stagew, stageh - UIAssets.TOOLBAR_HEIGHT);

		root.addActor(editingElement);
		root.addActor(paintingComponent);
		root.addActor(deletingComponent);
		root.addActor(rectSel);
		root.addActor(frames);

		root.addActor(paint.getPanel());
		delete.actCoordinates();
		root.addActor(delete.getPanel());
		paint.actCoordinates();
		root.addActor(text.getPanel());
		text.actCoordinates();
		root.addActor(interac.getPanel());
		interac.actCoordinates();
		root.addActor(effect.getPanel());
		root.addActor(effect.getOpt());
		effect.actCoordinates();
		root.addActor(more.getPanel());
		more.actCoordinates();
		root.addActor(toolBar);
		root.addActor(more.getActions());

		stage.addActor(root);
	}

	@Override
	public void show() {
		super.show();
		Screens previousScreen = mockupController.getPreviousScreen();
		setPreviousScreen(previousScreen);
		root.setVisible(true);
		float x, y, w, h;
		x = 0;
		y = 0;
		w = stagew;
		h = stageh - UIAssets.TOOLBAR_HEIGHT;
		rectSel.setBounds(x, y, w, h);
		paintingComponent.setBounds(x, y, w, h);
		deletingComponent.setBounds(x, y, w, h);
		if (ELEMENT_INDEX != null) {
			editingElement.setDrawable(new TextureRegionDrawable(
					new TextureRegion(Loading.demoElements[ELEMENT_INDEX])));
		} else {
			editingElement.setDrawable(null);
		}
		UIAssets.getNavigationGroup().setVisible(true);
	}

	@Override
	public void act(float delta) {
		stage.act(delta);
	}

	@Override
	public void draw() {
		stage.draw();
		// Table.drawDebug(stage);
	}

	@Override
	public void hide() {
		root.setVisible(false);
		rectSel.setVisible(false);
		paintingComponent.setVisible(false);
		deletingComponent.setVisible(false);
		UIAssets.getNavigationGroup().setVisible(false);
		buttonGroup.uncheckAll();
	}

	@Override
	public void dispose() {
		paintingComponent.dispose();
	}

	/**
	 * Fast implementation to know what element we shall display (edit). if
	 * ELEMENT_IDX == null no image will be shown.
	 */
	public static void setELEMENT_INDEX(Integer ELEMENT_IDX) {
		ELEMENT_INDEX = ELEMENT_IDX;
	}
}

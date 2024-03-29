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
package es.eucm.ead.mockup.core.view.ui.components;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.tablelayout.Cell;

import es.eucm.ead.mockup.core.view.UIAssets;
import es.eucm.ead.mockup.core.view.ui.GridPanel;
import es.eucm.ead.mockup.core.view.ui.ToolBar;
import es.eucm.ead.mockup.core.view.ui.buttons.ToolbarButton;

/**
 * A GridPanel that has multiple selection functionality after long press.
 * T must be instance of GalleryEntity in order to be able to select.
 */
public abstract class GalleryGrid<T extends Actor> extends GridPanel<T> {

	private static final float DEFAULT_DIALOG_PADDING_BOTTON_TOP = 20f;
	private static final float DEFAULT_ICON_SPACE = 10f;

	/**
	 * A collection storing the entities we've selected.
	 */
	protected Array<GalleryEntity> selectedEntities;

	/**
	 * If it's true we're in "selection mode"
	 */
	private boolean selecting;

	/**
	 * Auxiliar attribute that automatically hides it's contents when necesary.
	 */
	private Actor[] actorsToHide;
	/**
	 * The top toolbar that will be shown when we're in selection "mode".
	 */
	private ToolBar topToolbar;

	/**
	 * Just a label showing how many entities we've selected in the topToolBar.
	 */
	private Label numSelectedEntities;

	/**
	 * The button that will allow us to delete our selected entities.
	 */
	private ToolbarButton deleteButton;

	/**
	 * We need to know if this panel it's visible so we leave left padding or not.
	 */
	private Actor navigationPanelGroup;

	/**
	 * The cell that will have leftPadding or not.
	 */
	private Cell<?> backButtonCell;

	public GalleryGrid(Skin skin, int rows, int cols, Group root,
			Actor... actorsToHide) {
		super(skin, rows, cols, UIAssets.GALLERY_PROJECT_HEIGHT * .2f);
		if (actorsToHide == null) {
			throw new IllegalArgumentException("actorsToHide can't be null.");
		}
		this.actorsToHide = actorsToHide;
		this.navigationPanelGroup = UIAssets.getNavigationGroup();
		defaults().expand().fill().uniform();
		selectedEntities = new Array<GalleryEntity>();
		selecting = false;
		addListener(new ActorGestureListener() {

			/**
			 * 	Auxiliar attribute used to know if the target
			 * of our event it's indeed instance of GalleryEntity (which implements SelectListener)
			 */
			private GalleryEntity target;

			@Override
			public void touchDown(InputEvent event, float x, float y,
					int pointer, int button) {

				Actor targ = event.getTarget();
				if (targ instanceof GalleryEntity) {

					target = (GalleryEntity) targ;
					if (selecting) {
						if (target.isSelected()) {
							target.deselect();
							removeSelectedEntry(target);
						} else {
							target.select();
							addSelectedEntry(target);
						}
					}
				}
				super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (selecting)
					return;

				GalleryGrid.this.entityClicked(event);

			}

			@Override
			public boolean longPress(Actor actor, float x, float y) {
				if (selecting)
					return true;
				if (target instanceof GalleryEntity) {
					startSelecting();
				}
				return true;
			}

			private void addSelectedEntry(GalleryEntity entity) {
				if (selectedEntities.size == 0) {
					deleteButton.setVisible(true);
				}
				selectedEntities.add(entity);
				numSelectedEntities.setText(String
						.valueOf(selectedEntities.size));

			}

			private void removeSelectedEntry(GalleryEntity entity) {
				selectedEntities.removeValue(entity, true);
				int entitiesCount = selectedEntities.size;
				numSelectedEntities.setText(String.valueOf(entitiesCount));
				if (entitiesCount == 0) {
					deleteButton.setVisible(false);
				}
			}

			private void startSelecting() {
				selecting = true;
				target.select();
				addSelectedEntry(target);
				changeActorsVisibility(false);
			}

		});

		initializeTopToolBar(skin, root);
	}

	private void initializeTopToolBar(Skin skin, Group root) {
		final Dialog confirmDialog = new Dialog("Eliminar elementos", skin,
				"exit-dialog") {
			protected void result(Object object) {
				onHide();
			}
		}.button("Cancelar", false).button("Aceptar", true).key(Keys.BACK,
				false).key(Keys.ENTER, true); // TODO use i18n
		confirmDialog.padLeft(DEFAULT_DIALOG_PADDING_BOTTON_TOP);
		confirmDialog.padRight(DEFAULT_DIALOG_PADDING_BOTTON_TOP);

		confirmDialog.setMovable(false);
		topToolbar = new ToolBar(skin);
		topToolbar.setVisible(false);

		deleteButton = new ToolbarButton(skin.getDrawable("ic_delete"),
				"Borrar", skin, false); //TODO i18n
		final ToolbarButton backButton = new ToolbarButton(skin
				.getDrawable("ic_goback"), "Atrás", skin, false); //TODO i18n
		ClickListener mListener = new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Actor target = event.getListenerActor();
				if (target == deleteButton) {
					confirmDialog.getContentTable().clearChildren();
					String message;
					if (selectedEntities.size == 1) {
						message = selectedEntities.size
								+ " entrada se eliminará";
					} else {
						message = selectedEntities.size
								+ " entradas se eliminarán";
					}
					confirmDialog.text(message).show(getStage());
				} else if (target == backButton) {
					onHide();
				}
			}
		};
		deleteButton.addListener(mListener);
		backButton.addListener(mListener);

		numSelectedEntities = new Label("", skin);
		topToolbar.defaults().size(topToolbar.getHeight()).space(
				DEFAULT_ICON_SPACE);
		backButtonCell = topToolbar.add(backButton);
		topToolbar.add(numSelectedEntities).left().expandX();
		topToolbar.add(deleteButton);

		root.addActor(topToolbar);
	}

	private void changeActorsVisibility(boolean visible) {
		int i = 0, length = actorsToHide.length;
		for (; i < length; ++i) {
			actorsToHide[i].setVisible(visible);
		}
		topToolbar.setVisible(!visible);
		backButtonCell
				.padLeft((navigationPanelGroup.isVisible() ? UIAssets.NAVIGATION_BUTTON_WIDTH_HEIGHT
						: 0)
						+ DEFAULT_ICON_SPACE);
	}

	/**
	 * Called when this Actor was clicked if we're not in Selecting Mode.
	 */
	protected abstract void entityClicked(InputEvent event);

	/**
	 * Resets previous visibility changes to actors.
	 */
	public void onHide() {
		changeActorsVisibility(true);
		for (GalleryEntity select : selectedEntities) {
			select.deselect();
		}
		selectedEntities.clear();
		selecting = false;
	}

	/**
	 * True if we're in "selection mode"
	 */
	public boolean isSelecting() {
		return selecting;
	}
}

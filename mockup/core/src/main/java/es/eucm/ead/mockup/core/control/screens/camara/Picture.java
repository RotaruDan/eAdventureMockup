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
package es.eucm.ead.mockup.core.control.screens.camara;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import es.eucm.ead.mockup.core.control.ScreenController;
import es.eucm.ead.mockup.core.control.screens.AbstractScreen;
import es.eucm.ead.mockup.core.control.screens.Screens;
import es.eucm.ead.mockup.core.control.screens.edition.ElementEdition;
import es.eucm.ead.mockup.core.control.screens.edition.SceneEdition;
import es.eucm.ead.mockup.core.control.screens.menu.ProjectMenu;
import es.eucm.ead.mockup.core.view.UIAssets;
import es.eucm.ead.mockup.core.view.ui.Panel;

public class Picture extends AbstractScreen {

	/**
	 * Fast implementation to know where we go after
	 * the choice dialog depending on which button we clicked.
	 * 
	 */
	private static Screens nextScreen;

	private Group navigationGroup;
	private Table rootTable;
	private Button takePicButton;
	private ScreenController screenController;
	private Color previousClearColor = new Color(), clearColor = new Color(0f,
			0f, 0f, 0f);

	/**
	 *  Asks if we must the user want an element or 
	 *  an scene after he takes the picture.
	 */
	private Panel mDialogPanel;
	private final float DEFAULT_ICON_LABEL_SPACE = 10f;

	@Override
	public void create() {
		this.navigationGroup = UIAssets.getNavigationGroup();
		this.screenController = mockupController.getScreenController();

		takePicButton = new ImageButton(skin, "ic_photocamera");
		takePicButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				takePic();
			}
		});
		String[] res = { "800x600", "1280x720", "1920x1080", "4000x3000" };
		SelectBox resolution = new SelectBox(res, skin);
		resolution.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// TODO change resolution here
				super.clicked(event, x, y);
			}
		});

		rootTable = new Table();
		rootTable.setVisible(false);
		rootTable.setFillParent(true);
		rootTable.pad(10f);

		rootTable.add(resolution).right().top();
		rootTable.row();
		rootTable.add(takePicButton).size(takePicButton.getWidth() * 1.3f)
		.bottom().expand().padBottom(5f);

		//Coice dialog panel
		mDialogPanel = new Panel(skin, "dialog");
		mDialogPanel.setVisible(false);
		mDialogPanel.setModal(true);
		mDialogPanel.setHideOnOutterTouch(false);
		mDialogPanel.pad(DEFAULT_ICON_LABEL_SPACE);
		mDialogPanel.defaults().space(DEFAULT_ICON_LABEL_SPACE).uniform()
		.expand().fill();
		final float PANEL_W = stagew * .3f, PANEL_H = UIAssets.NAVIGATION_BUTTON_WIDTH_HEIGHT * 3f, PANEL_X = halfstagew
				- PANEL_W * .5F, PANEL_Y = halfstageh - PANEL_H * .5f;
		mDialogPanel.setBounds(PANEL_X, PANEL_Y, PANEL_W, PANEL_H);
		final Button newElement = createButton("Nuevo elemento",
				"ic_editelement", DEFAULT_ICON_LABEL_SPACE, false);
		final Button newScene = createButton("Escena nueva", "ic_editstage",
				DEFAULT_ICON_LABEL_SPACE, false);
		mDialogPanel.add(newScene);
		mDialogPanel.row();
		mDialogPanel.add(newElement);
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
				if (target == newElement) {
					next = Screens.ELEMENT_EDITION;
					ElementEdition.setELEMENT_INDEX(null);
				} else if (target == newScene) {
					next = Screens.SCENE_EDITION;
					SceneEdition.setSCENE_INDEX(null);
				}
				return next;
			}
		};
		newElement.addListener(mTransitionLIstener);
		newScene.addListener(mTransitionLIstener);

		stage.addActor(rootTable);
		stage.addActor(mDialogPanel);
	}

	private Button createButton(String text, String image, float defaultSpace,
			boolean left) {
		Button mButton = new Button(skin);
		mButton.defaults().space(defaultSpace);
		Label vidLabel = new Label(text, skin);
		Image vidImage = new Image(skin.getDrawable(image));
		if (left) {
			mButton.add(vidLabel);
			mButton.add(vidImage);
		} else {
			mButton.add(vidImage);
			mButton.add(vidLabel);
		}
		return mButton;
	}

	private void takePic() {
		//TODO take picture here

		onPictureTaken();
	}

	private void onPictureTaken() {
		if (nextScreen == null) {
			if (mockupController.getPreviousScreen() == Screens.GALLERY) {
				//  We've came from Gallery 
				// so we must ask if he want an element or a scene
				mockupController.show(mDialogPanel);
			} else {
				//We've came from Project menu so we go back
				onBackKeyPressed();
			}
		} else {
			exitAnimation(nextScreen);
		}
	}

	@Override
	public void show() {
		super.show();
		previousClearColor.set(this.screenController.getClearColor());
		this.screenController.changeClearColor(clearColor);
		Screens previousScreen = mockupController.getPreviousScreen();
		if(previousScreen != Screens.ELEMENT_EDITION && previousScreen != Screens.SCENE_EDITION){
			setPreviousScreen(previousScreen);
		}

		//	We implement distincg behaviours 
		// depending on where we came from
		Screens nextScr = null;
		if (ProjectMenu.getFROM_INITIAL_SCENE()) {
			nextScr = Screens.PROJECT_MENU;
		} else {
			if (previousScreen == Screens.ELEMENT_GALLERY) {
				nextScr = Screens.ELEMENT_EDITION;
				ElementEdition.setELEMENT_INDEX(null);
			} else if (previousScreen == Screens.SCENE_GALLERY) {
				nextScr = Screens.SCENE_EDITION;
				SceneEdition.setSCENE_INDEX(null);
			} else if (previousScreen == Screens.PROJECT_MENU) {
				// If it's null we go to the previous screen.
				nextScr = previousScreen;
			}
		}
		setNextScreen(nextScr);
		rootTable.setVisible(true);
		navigationGroup.setVisible(true);
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
	public void pause() {
	}

	@Override
	public void hide() {
		if (mDialogPanel.isVisible()) {
			mDialogPanel.setVisible(false);
		}
		this.screenController.changeClearColor(previousClearColor);
		rootTable.setVisible(false);
		navigationGroup.setVisible(false);
	}

	/**
	 * Sets the nextScreen that will be shown after we take the picture.
	 * Fast implementation to know where we go after
	 * the choice dialog depending on which button we clicked.
	 * 
	 * If nextScreen in null we will go to the previous Screen.
	 */
	public static void setNextScreen(Screens nextScreen) {
		Picture.nextScreen = nextScreen;
	}
}

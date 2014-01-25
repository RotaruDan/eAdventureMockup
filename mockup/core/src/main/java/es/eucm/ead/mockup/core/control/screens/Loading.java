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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import es.eucm.ead.mockup.core.utils.Constants;
import es.eucm.ead.mockup.core.view.UIAssets;
import es.eucm.ead.mockup.core.view.ui.buttons.ToolbarButton;

public class Loading extends AbstractScreen {

	public static NinePatch loadingSel;
	private NinePatch loadingBar, loadingProgress;
	private TextureAtlas atlas;
	private float xBar, yBar, wBar, hBar;
	private Batch sb;

	/**
	 * Fast implementation to cache some demo Textures of elements and scenes...
	 */
	public static Texture[] demoElements, demoScenes;

	/**
	 * Fast implementation to cache some demo Thumbnail Textures of elements and
	 * scenes...
	 */
	public static Texture[] demoElementsThumbnail, demoScenesThumbnail;

	@Override
	public void create() {

		/* DEMO */
		int i;
		for (i = 0; i < Constants.demoElements.length; ++i) {
			am.load(Constants.demoElements[i], Texture.class);
		}
		demoElements = new Texture[Constants.demoElements.length];
		for (i = 0; i < Constants.demoScenes.length; ++i) {
			am.load(Constants.demoScenes[i], Texture.class);
		}
		demoScenes = new Texture[Constants.demoScenes.length];

		for (i = 0; i < Constants.demoElementsThumbnail.length; ++i) {
			am.load(Constants.demoElementsThumbnail[i], Texture.class);
		}
		demoElementsThumbnail = new Texture[Constants.demoElementsThumbnail.length];
		for (i = 0; i < Constants.demoScenesThumbnail.length; ++i) {
			am.load(Constants.demoScenesThumbnail[i], Texture.class);
		}
		demoScenesThumbnail = new Texture[Constants.demoScenesThumbnail.length];

		am.load("mockup/temp/proyecto.png", Texture.class);
		/*--*/
		am.load(Constants.SKIN_SRC, Skin.class);

		float hh = Gdx.graphics.getHeight() / 2f, hw = Gdx.graphics.getWidth() / 2f;
		this.wBar = hw * 1.5f;
		this.hBar = hw / 7f;
		this.xBar = hw - this.wBar / 2f;
		this.yBar = hh / 2f - hBar / 2f;
		this.atlas = new TextureAtlas("mockup/ninepatch/ninepatch.atlas");
		loadingBar = new NinePatch(atlas.findRegion("2"), 4, 4, 4, 4);
		loadingProgress = new NinePatch(atlas.findRegion("3"), 4, 4, 4, 4);
		loadingSel = new NinePatch(atlas.findRegion("1"), 39, 39, 39, 39);
		loadingSel.setBottomHeight(5f);
		loadingSel.setRightWidth(5f);
		loadingSel.setTopHeight(5f);
		loadingSel.setLeftWidth(5f);

		// We must create Engine here if we want to pass
		// his SpriteBatch to ours (very important performancewise)

		stage = new Stage(Constants.SCREENW, Constants.SCREENH, true);
		initStage();

		this.sb = stage.getSpriteBatch();
	}

	@Override
	public void act(float delta) {
		if (am.update()) {
			/* DEMO */
			int i;
			Texture auxTex = null;
			for (i = 0; i < Constants.demoElements.length; ++i) {
				auxTex = am.get(Constants.demoElements[i], Texture.class);
				auxTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
				demoElements[i] = auxTex;
			}
			for (i = 0; i < Constants.demoScenes.length; ++i) {
				auxTex = am.get(Constants.demoScenes[i], Texture.class);
				auxTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
				demoScenes[i] = auxTex;
			}

			for (i = 0; i < Constants.demoElementsThumbnail.length; ++i) {
				auxTex = am.get(Constants.demoElementsThumbnail[i],
						Texture.class);
				auxTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
				demoElementsThumbnail[i] = auxTex;
			}
			for (i = 0; i < Constants.demoScenesThumbnail.length; ++i) {
				auxTex = am
						.get(Constants.demoScenesThumbnail[i], Texture.class);
				auxTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
				demoScenesThumbnail[i] = auxTex;
			}

			/*--*/

			initStatics();

			mockupController.create();
			mockupController.changeTo(Screens.MAIN_MENU);

			UIAssets.addActors();
		}
	}

	@Override
	public void draw() {
		sb.begin();
		loadingBar.draw(sb, xBar, yBar, wBar, hBar);
		loadingProgress.draw(sb, xBar, yBar, wBar * am.getProgress(), hBar);
		sb.end();
	}

	public void dispose() {
		atlas.dispose();
	}

	private void initStatics() {
		if (skin == null) {
			skin = am.get(Constants.SKIN_SRC, Skin.class);
			skin.getFont("default-font").setScale(.7f);
			ToolbarButton.loadStyle(skin);
		}

		if (!UIAssets.isCreated()) {
			UIAssets.create();
		}
	}

	private void initStage() {
		stage.getRoot().addCaptureListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				if (!(event.getTarget() instanceof TextField)) {
					stage.setKeyboardFocus(null);
					Gdx.input.setOnscreenKeyboardVisible(false);
				}
				return false;
			}
		});
		stageh = stage.getHeight();
		stagew = stage.getWidth();
		halfstageh = stageh / 2f;
		halfstagew = stagew / 2f;
	}
}

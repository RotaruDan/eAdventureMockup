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

import es.eucm.ead.engine.Engine;
import es.eucm.ead.mockup.core.engine.MockupEngine;
import es.eucm.ead.mockup.core.engine.MockupEventListener;
import es.eucm.ead.mockup.core.utils.Constants;
import es.eucm.ead.mockup.core.view.UIAssets;
import es.eucm.ead.mockup.core.view.ui.buttons.ToolbarButton;

public class Loading extends AbstractScreen {

	private NinePatch loadingBar, loadingProgress;
	private TextureAtlas atlas;
	private float xBar, yBar, wBar, hBar;
	private Batch sb;

	public static final String[] demoElements = {
		"mockup/temp/elements/cama-quirofano.jpg",
		"mockup/temp/elements/Cama2.jpg",
		"mockup/temp/elements/enfermera.jpg",
		"mockup/temp/elements/enfermera2.jpg",
		"mockup/temp/elements/estetoscopio-recortado.png",
		"mockup/temp/elements/estetoscopio.jpg",
		"mockup/temp/elements/lampara.jpg",
		"mockup/temp/elements/medico-de-ambulancia.jpg",
		"mockup/temp/elements/Elemento1.jpg"
	};

	public static final String[] demoScenes = {
		"mockup/temp/scenes/sala-de-estar.jpg", //Initial scene
		"mockup/temp/scenes/ambulancia.jpg",
		"mockup/temp/scenes/pacientes.jpg",
		"mockup/temp/scenes/quirofano.jpg"
	};
	@Override
	public void create() {
		
		int i;
		for(i = 0; i < demoElements.length; ++i){
			am.load(demoElements[i], Texture.class);			
		}
		for(i = 0; i < demoScenes.length; ++i){
			am.load(demoScenes[i], Texture.class);			
		}
		am.load(Constants.SKIN_SRC, Skin.class);
		am.load("mockup/temp/proyecto.png", Texture.class);

		float hh = Gdx.graphics.getHeight() / 2f, hw = Gdx.graphics.getWidth() / 2f;
		this.wBar = hw * 1.5f;
		this.hBar = hw / 7f;
		this.xBar = hw - this.wBar / 2f;
		this.yBar = hh / 2f - hBar / 2f;
		this.atlas = new TextureAtlas("mockup/ninepatch/ninepatch.atlas");
		loadingBar = new NinePatch(atlas.findRegion("2"), 4, 4, 4, 4);
		loadingProgress = new NinePatch(atlas.findRegion("3"), 4, 4, 4, 4);

		//We must create Engine here if we want to pass 
		//his SpriteBatch to ours (very important performancewise)
		MockupEngine engine = new MockupEngine();
		engine.setMockupEventListener(new MockupEventListener());
		engine.create();

		stage = new Stage(Constants.SCREENW, Constants.SCREENH, true,
				Engine.stage.getSpriteBatch());
		initStage();

		this.sb = stage.getSpriteBatch();
	}

	@Override
	public void act(float delta) {
		if (am.update()) {
			initStatics();
			int i;
			for(i = 0; i < demoElements.length; ++i){
				am.get(demoElements[i], Texture.class).setFilter(TextureFilter.Linear, TextureFilter.Linear);		
			}
			for(i = 0; i < demoScenes.length; ++i){
				am.get(demoScenes[i], Texture.class).setFilter(TextureFilter.Linear, TextureFilter.Linear);			
			}

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

	@Override
	public void hide() {
		this.atlas.dispose();
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
				if (!(event.getTarget() instanceof TextField)){
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

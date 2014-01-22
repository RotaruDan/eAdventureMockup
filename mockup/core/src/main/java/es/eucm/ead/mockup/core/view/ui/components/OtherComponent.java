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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import es.eucm.ead.mockup.core.control.screens.AbstractScreen;
import es.eucm.ead.mockup.core.view.UIAssets;
import es.eucm.ead.mockup.core.view.ui.Panel;
import es.eucm.ead.mockup.core.view.ui.buttons.ToolbarButton;

public class OtherComponent {

	private OtherPanel panel;
	private Button button;

	public OtherComponent( String imageUp,  String name, Skin skin, float width, float height) {
		this.button = new ToolbarButton(skin.getDrawable(imageUp), name, skin);
		this.panel = new OtherPanel(skin, "opaque", width, height);
		this.button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				event.cancel();
				if (!panel.isVisible()) {
					AbstractScreen.mockupController.show(panel);
				} else {
					AbstractScreen.mockupController.hide(panel);
				}
			}
		});
	}

	private class OtherPanel extends Panel {

		private float width;
		private float height;

		public OtherPanel(Skin skin, String styleName, float width, float height) {
			super(skin, styleName);

			this.height = height;
			this.width = width;
			
			setHeight(height);
			setWidth(width);
			
			setVisible(false);
			setModal(false);
			setColor(Color.DARK_GRAY);
			
			Label label = new Label("default-thin-opaque", skin);
			label.setWrap(true);
			label.setAlignment(Align.center);
			label.setFontScale(0.7f);
			
			TextField nameField = new TextField("Nombre", skin);
		
			//Change for TextArea
			TextField tags = new TextField("TAGs separados por ','", skin);
			TextField infoField = new TextField("Información", skin);
		
			Label auxLabel1 = new Label("Duplicar escena", skin);
			auxLabel1.setFontScale(0.7f);			
			Image backImg1 = new Image(skin.getRegion("icon-blitz")); //edit element img
			final Button clone = new Button(skin, "default");
			clone.add(backImg1).left().expand();
			clone.add(auxLabel1).left().expand();
			clone.scale(0.5f);
			
			Label auxLabel2 = new Label("Eliminar escena", skin);
			auxLabel2.setFontScale(0.7f);			
			Image backImg2 = new Image(skin.getRegion("icon-blitz")); //edit element img
			final Button erase = new Button(skin, "default");
			erase.add(backImg2).left().expand();
			erase.add(auxLabel2).left().expand();
			erase.scale(0.5f);
			
			
			//defaults().fill().expand();
			
			add(nameField).fill().expand();
			row();
			add(tags).fill().expand();
			row();
			add(infoField).fill().expand();
			row();
			add(clone).expandX().fill();
			row();
			add(erase).expandX().fill();
			
		}

		/**
		 * Set the panel'coordinates according to the button's coordinates
		 */
		public void actCoordinates() {
			if((button.getX() + (button.getWidth() / 2) - (width / 2) + width)<AbstractScreen.stagew){
				setX(button.getX() + (button.getWidth() / 2) - (width / 2));
			} else {
				setX(AbstractScreen.stagew-width-5);
			}
				setY(AbstractScreen.stageh - UIAssets.TOOLBAR_HEIGHT - height - 10);
		}

		@Override
		public void show() {
			super.show();
		}

		@Override
		public void hide() {
			super.hide();
		}
		
	}

	public OtherPanel getPanel() {
		return panel;
	}

	public Button getButton() {
		return button;
	}

	public void actCoordinates() {
		panel.actCoordinates();
	}
}

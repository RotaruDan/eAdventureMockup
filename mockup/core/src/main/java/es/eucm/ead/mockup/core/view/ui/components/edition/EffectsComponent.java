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
package es.eucm.ead.mockup.core.view.ui.components.edition;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import es.eucm.ead.mockup.core.control.screens.AbstractScreen;
import es.eucm.ead.mockup.core.view.UIAssets;
import es.eucm.ead.mockup.core.view.ui.Panel;
import es.eucm.ead.mockup.core.view.ui.buttons.ToolbarButton;

public class EffectsComponent {

	private EffectsPanel panel;
	private Button button;
	private EffectOption effectsOpt;

	public EffectsComponent(String imageUp, String name, Skin skin,
			String description, float width, float height) {
		this.button = new ToolbarButton(skin.getDrawable(imageUp), name, skin);
		this.panel = new EffectsPanel(skin, "opaque", description, width,
				height);
		this.effectsOpt = panel.getOptions();
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

	private class EffectsPanel extends Panel {

		private float width;
		private float height;
		private EffectOption effectOpt;

		public EffectsPanel(Skin skin, String styleName, String description,
				float width, float height) {
			super(skin, styleName);

			this.height = height;
			this.width = width;

			setHeight(height);
			setWidth(width);

			setVisible(false);
			setModal(false);
			setColor(Color.DARK_GRAY);

			effectOpt = new EffectOption(skin, "variante1", "variante2",
					"variante3");

			//FIXME *repeated code*
			Label label = new Label(description, skin, "default-thin-opaque");
			label.setWrap(true);
			label.setAlignment(Align.center);
			label.setFontScale(0.7f);

			Button prop1 = new ToolbarButton(skin.getDrawable("ic_settings"),
					skin);
			Button prop2 = new ToolbarButton(skin.getDrawable("ic_settings"),
					skin);
			Button prop3 = new ToolbarButton(skin.getDrawable("ic_settings"),
					skin);
			Button prop4 = new ToolbarButton(skin.getDrawable("ic_settings"),
					skin);
			Button prop5 = new ToolbarButton(skin.getDrawable("ic_settings"),
					skin);
			Button prop6 = new ToolbarButton(skin.getDrawable("ic_settings"),
					skin);
			Button prop7 = new ToolbarButton(skin.getDrawable("ic_settings"),
					skin);
			prop1.addListener(optionListener());
			prop2.addListener(optionListener());
			prop3.addListener(optionListener());
			prop4.addListener(optionListener());
			prop5.addListener(optionListener());
			prop6.addListener(optionListener());
			prop7.addListener(optionListener());

			new ButtonGroup(prop1, prop2, prop3, prop4, prop5, prop6, prop7);
			//END FIXME

			Table table = new Table(skin);

			CheckBox cb1 = new CheckBox("Efecto 1", skin);
			CheckBox cb2 = new CheckBox("Efecto 2", skin);
			CheckBox cb3 = new CheckBox("Efecto 3", skin);
			CheckBox cb4 = new CheckBox("Efecto 4", skin);
			CheckBox cb5 = new CheckBox("Efecto 5", skin);
			CheckBox cb6 = new CheckBox("Efecto 6", skin);
			CheckBox cb7 = new CheckBox("Efecto 7", skin);

			ScrollPane sp = new ScrollPane(table, skin);
			sp.setupFadeScrollBars(0f, 0f);
			sp.setScrollingDisabled(true, false);

			table.add(cb1).left().expandX();
			table.add(prop1).right().padRight(30);
			table.row();
			table.add(cb2).left();
			table.add(prop2).right().padRight(30);
			table.row();
			table.add(cb3).left();
			table.add(prop3).right().padRight(30);
			table.row();
			table.add(cb4).left();
			table.add(prop4).right().padRight(30);
			table.row();
			table.add(cb5).left();
			table.add(prop5).right().padRight(30);
			table.row();
			table.add(cb6).left();
			table.add(prop6).right().padRight(30);
			table.row();
			table.add(cb7).left();
			table.add(prop7).right().padRight(30);

			//table.debug();

			defaults().fill().expand();
			add(label);
			row();
			add(sp);
			//add(table);
		}

		public EffectOption getOptions() {
			return effectOpt;
		}

		/**
		 * Set the panel'coordinates according to the button's coordinates
		 */
		public void actCoordinates() {
			if ((button.getX() + (button.getWidth() / 2) - (width / 2) + width) < AbstractScreen.stagew) {
				setX(button.getX() + (button.getWidth() / 2) - (width / 2));
			} else {
				setX(AbstractScreen.stagew - width - 5);
			}
			setY(AbstractScreen.stageh - UIAssets.TOOLBAR_HEIGHT - height - 10);

			effectOpt.setCoordinates(this.getX(), this.getY());
		}

		@Override
		public void show() {
			super.show();
		}

		@Override
		public void hide() {
			super.hide();
		}

		public ClickListener optionListener() {
			return new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					event.cancel();
					if (!effectOpt.isVisible()) {
						AbstractScreen.mockupController.show(effectOpt);
					} else {
						AbstractScreen.mockupController.hide(effectOpt);
					}
				}
			};
		}
	}

	public EffectsPanel getPanel() {
		return panel;
	}

	public Button getButton() {
		return button;
	}

	public void actCoordinates() {
		panel.actCoordinates();
	}

	public Actor getOpt() {
		return effectsOpt;
	}
}

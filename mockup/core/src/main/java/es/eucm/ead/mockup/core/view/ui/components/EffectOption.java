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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

import es.eucm.ead.mockup.core.view.ui.Panel;

public class EffectOption extends Panel {

	private float width = 220;
	private float height = 300;

	public EffectOption(Skin skin, String... effects) {
		super(skin);

		setHeight(height);
		setWidth(width);

		setVisible(false);
		setModal(false);
		setColor(Color.DARK_GRAY);

		Label label = new Label("Opciones de filtro", skin,
				"default-thin-opaque");
		label.setWrap(true);
		label.setAlignment(Align.center);
		label.setFontScale(0.7f);

		Slider slider = new Slider(1, 60, 0.5f, false, skin, "left-horizontal");
		slider.setValue(30);

		SelectBox eff = new SelectBox(effects, skin);

		defaults().expand().fill();

		add(label);
		row();
		add(eff);
		row();
		add("Intensidad");
		row();
		add(slider);
		//debug();
	}

	public void setCoordinates(float x, float y) {
		setX(x - width);
		setY(y);
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

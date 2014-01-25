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
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import es.eucm.ead.mockup.core.view.ui.Panel;

public class FlagPanel extends Panel {

	public FlagPanel(Skin skin) {
		super(skin);

		setHeight(600);
		setWidth(600);

		setVisible(false);
		setModal(false);
		setColor(Color.DARK_GRAY);

		Table top = new Table(skin);
		Table bot = new Table(skin);

		top.defaults().pad(20);

		top.add("Panel de flags, selecciona uno").expandX().fill().center()
				.top();
		top.row();
		top.add(new TextButton("Flag1", skin)).expandX().fill();
		top.add(new TextButton("LuzSala1", skin)).expandX().fill();
		top.add(new TextButton("LuzSala2", skin)).expandX().fill();

		top.row();
		top.add(new TextButton("Flag2", skin)).expandX().fill();
		top.add(new TextButton("Puerta1", skin)).expandX().fill();

		ScrollPane sp = new ScrollPane(top, skin);
		sp.setupFadeScrollBars(0f, 0f);
		sp.setScrollingDisabled(true, false);

		TextButton accept = new TextButton("Aceptar", skin);
		TextButton newAct = new TextButton("Nuevo FLAG", skin);

		bot.row();
		bot.add(accept).expandX().fill().left();
		bot.add(newAct).expandX().fill().right();

		this.add(top).expandY();
		this.row();
		this.add(bot);
	}

}

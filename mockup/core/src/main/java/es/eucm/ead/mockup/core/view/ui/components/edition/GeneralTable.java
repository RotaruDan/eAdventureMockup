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

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import es.eucm.ead.mockup.core.control.screens.AbstractScreen;
import es.eucm.ead.mockup.core.view.ui.Panel;

public class GeneralTable extends Table {

	private Table top;
	private Table bot;
	private Skin skin;
	private FlagPanel fPanel;

	public GeneralTable(Skin s, final Panel parent, FlagPanel f) {
		super(s);

		fPanel = f;
		top = new Table(s);
		bot = new Table(s);
		this.skin = s;

		top.defaults().pad(10);

		top.add("Elemento solo visible si:").expandX().left().padLeft(50);
		top.row();

		ScrollPane sp = new ScrollPane(top);
		sp.setScrollingDisabled(true, false);

		TextButton accept = new TextButton("Aceptar", skin);
		TextButton newAct = new TextButton("Nueva condición", skin);

		bot.row();
		bot.add(accept).expandX().left();
		bot.add(newAct).expandX().right();

		accept.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				event.cancel();
				AbstractScreen.mockupController.hide(parent);
			}
		});

		newAct.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				event.cancel();
				top.add(new FragTableVisible(skin, fPanel)).expandX().fill();
			}
		});

		this.add(sp).expand().fill();
		this.row();
		this.add(bot).expandX().fill().bottom();

		top.debug();
		this.debug();

	}
}

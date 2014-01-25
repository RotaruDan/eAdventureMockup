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

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

import es.eucm.ead.mockup.core.view.ui.TabPane;

public class ActionsComponent extends TabPane<Button, Table> {

	private final float height = 600;
	private final float width = 900;
	private final float x = 100;
	private final float y = 100;
	private FlagPanel flagPanel;

	public ActionsComponent(Skin skin) {
		super(skin);

		setBounds(x, y, width, height);

		setVisible(false);
		setModal(true);

		flagPanel = new FlagPanel(skin);

		Label generalLabel = new Label("General", skin);
		generalLabel.setAlignment(Align.center);
		final Button general = new Button(skin, "toggle");
		general.add(generalLabel).expandX();

		Label actionsLabel = new Label("Acciones", skin);
		actionsLabel.setAlignment(Align.center);
		final Button actions = new Button(skin, "toggle");
		actions.add(actionsLabel).expandX();

		Label conversationLabel = new Label("Conversacion", skin);
		conversationLabel.setAlignment(Align.center);
		final Button conversation = new Button(skin, "toggle");
		conversation.add(conversationLabel).expandX();

		final Table botGeneral = new GeneralTable(skin, this, flagPanel);
		final Table botActions = new ActionsTable(skin, this, flagPanel);

		getTabTable().defaults().expandX().fill();
		addBinding(general, botGeneral);
		addBinding(actions, botActions);
		addBinding(conversation, botActions);
		setCurrentTab(general);

		//getParent().addActor(flagPanel); 
		//flagPanel.toFront();
	}
}

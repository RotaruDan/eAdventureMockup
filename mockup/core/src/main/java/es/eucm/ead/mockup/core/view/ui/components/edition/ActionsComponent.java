package es.eucm.ead.mockup.core.view.ui.components.edition;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

import es.eucm.ead.mockup.core.view.ui.TabPane;

public class ActionsComponent extends TabPane<Button, Table>{

	private final float height=600;
	private final float width=900;
	private final float x=100;
	private final float y=100;
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

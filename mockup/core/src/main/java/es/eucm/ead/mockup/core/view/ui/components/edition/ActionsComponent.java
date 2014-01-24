package es.eucm.ead.mockup.core.view.ui.components.edition;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import es.eucm.ead.mockup.core.control.screens.AbstractScreen;
import es.eucm.ead.mockup.core.view.ui.Panel;

public class ActionsComponent extends Panel{

	private final float height=600;
	private final float width=900;
	private final float X=100;
	private final float Y=100;
	private FlagPanel flagPanel;
	
	public ActionsComponent(Skin skin) {
		super(skin);
		
		setHeight(height);
		setWidth(width);
		setPosition(X, Y);
		
		setVisible(false);
		setModal(true);
		//setColor(Color.DARK_GRAY);
		flagPanel = new FlagPanel(skin);
		
		Table top = new Table(skin);
		
		Label generalLabel = new Label("General", skin);
		generalLabel.setAlignment(Align.center);
		final Button general = new Button(skin, "navigationPanelRest");
		general.add(generalLabel).expandX();
		
		Label actionsLabel = new Label("Acciones", skin);
		actionsLabel.setAlignment(Align.center);
		final Button actions = new Button(skin, "navigationPanelRest");
		actions.add(actionsLabel).expandX();
		
		Label conversationLabel = new Label("Conversacion", skin);
		conversationLabel.setAlignment(Align.center);
		final Button conversation = new Button(skin, "navigationPanelRest");
		conversation.add(conversationLabel).expandX();
		
		new ButtonGroup(general, actions, conversation);

		top.defaults().expandX().fill();
		
		top.add(general).pad(20);
		top.add(actions).pad(20);
		top.add(conversation).pad(20);
		
		final Table botGeneral = new GeneralTable(skin, this, flagPanel);
		final Table botActions = new ActionsTable(skin, this, flagPanel);
		
		add(top).expandX().fill().top();
		row();
		add(botGeneral).expand().fill().center();
		
		general.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				event.cancel();
				removeActor(botActions);
				add(botGeneral).expand().fill().center();
			}
		});
		actions.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					event.cancel();
					removeActor(botGeneral);
					add(botActions).expand().fill().center();
				}
			});
		
		AbstractScreen.stage.getRoot().addActor(flagPanel); ///////
		
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

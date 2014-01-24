package es.eucm.ead.mockup.core.view.ui.components.edition;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import es.eucm.ead.mockup.core.control.screens.AbstractScreen;
import es.eucm.ead.mockup.core.view.ui.Panel;

public class ActionsTable extends Table{
	
	private Table top;
	private Table bot;
	private Skin skin;
	private FlagPanel fPanel;
	
	public ActionsTable(Skin s, final Panel parent, FlagPanel f){
		super(s);
		
		fPanel=f;
		top = new Table(s);
		bot = new Table(s);
		this.skin = s;
		
		final String[] pos = {"Activo", "Inactivo"};
		
		top.defaults().pad(10);
		FlagButton flag = new FlagButton(skin, fPanel);

		top.add(new CheckBox("DAR", s));
		top.add(" a ");
		top.add(new TextButton("Elemento", skin));
		top.add(" cambia si ");
		top.add(flag.getFlag());
		top.add(new SelectBox(pos, skin));
		
		ScrollPane sp = new ScrollPane(top, skin);
		sp.setupFadeScrollBars(0f, 0f);
		sp.setScrollingDisabled(true, false);
		
		TextButton accept = new TextButton("Aceptar", skin);
		TextButton newAct = new TextButton("Nueva acción", skin);
		
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
				//add new action
			}
		});
		
		
		this.add(sp).expand().fill();
		this.row();
		this.add(bot).expandX().fill().bottom();
		
		top.debug();
		this.debug();
		
	}
}

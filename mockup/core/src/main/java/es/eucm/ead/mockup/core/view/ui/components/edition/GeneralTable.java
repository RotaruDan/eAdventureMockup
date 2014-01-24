package es.eucm.ead.mockup.core.view.ui.components.edition;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import es.eucm.ead.mockup.core.control.screens.AbstractScreen;
import es.eucm.ead.mockup.core.view.ui.Panel;

public class GeneralTable extends Table{
	
	private Table top;
	private Table bot;
	private Skin skin;
	private FlagPanel fPanel;
	
	public GeneralTable(Skin s, final Panel parent, FlagPanel f){
		super(s);
		
		fPanel=f;
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

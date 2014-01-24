package es.eucm.ead.mockup.core.view.ui.components.edition;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import es.eucm.ead.mockup.core.view.ui.Panel;

public class FlagPanel extends Panel{

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

		top.add("Panel de flags, selecciona uno").expandX().fill().center().top();
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

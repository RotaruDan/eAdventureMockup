package es.eucm.ead.mockup.core.view.ui.components.edition;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import es.eucm.ead.mockup.core.view.ui.buttons.ToolbarButton;

public class FragTableVisible extends Table{
	
	private Table me;
	
	public FragTableVisible(Skin skin, FlagPanel fPanel){
		
		super(skin);
		me=this;
		Button delete = new ToolbarButton(skin.getDrawable("ic_delete"), skin);
		delete.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				event.cancel();
				me.remove();
			}
		}); 
		
		final String[] pos = {"Activo", "Inactivo"};
		
		FlagButton flag = new FlagButton(skin, fPanel);
		
		this.add(flag.getFlag()).center().expandX().fill();
		this.add(new SelectBox(pos, skin)).expandX().fill();
		this.add(delete).expandX();
		row();
	}
}

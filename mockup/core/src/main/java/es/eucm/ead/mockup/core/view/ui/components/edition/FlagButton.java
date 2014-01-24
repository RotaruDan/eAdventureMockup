package es.eucm.ead.mockup.core.view.ui.components.edition;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import es.eucm.ead.mockup.core.view.ui.Panel;

public class FlagButton {
	
	private Button flag;
	private FlagPanel panel;
	
	public FlagButton(Skin skin, FlagPanel fPanel){
		panel = fPanel;
		flag = new TextButton("FLAG", skin);
		flag.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				event.cancel();
				panel.show();
			}
		});
		
	}
	
	public Button getFlag(){
		return flag;
	}
	
	public Panel getFlagPanel(){
		return panel;
	}
	
}

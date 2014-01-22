package es.eucm.ead.mockup.core.view.ui.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

import es.eucm.ead.mockup.core.view.ui.Panel;

public class EffectOption extends Panel{

	private float width=220;
	private float height=300;
	
	public EffectOption(Skin skin, String... effects) {
		super(skin);
		
		setHeight(height);
		setWidth(width);
		
		setVisible(false);
		setModal(false);
		setColor(Color.DARK_GRAY);
		
		Label label = new Label("Opciones de filtro", skin, "default-thin-opaque");
		label.setWrap(true);
		label.setAlignment(Align.center);
		label.setFontScale(0.7f);
		
		Slider slider = new Slider(1, 60, 0.5f, false, skin, "left-horizontal");
		slider.setValue(30);
		
		SelectBox eff = new SelectBox(effects, skin);
		
		defaults().expand().fill();
		
		add(label);
		row();
		add(eff);
		row();
		add("Intensidad");
		row();
		add(slider);
		debug();
	}
	
	public void setCoordinates(float x, float y){
		setX(x-width);
		setY(y);
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

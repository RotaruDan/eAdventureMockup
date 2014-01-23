package es.eucm.ead.mockup.core.view.ui.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;

import es.eucm.ead.mockup.core.control.listeners.SelectListener;

/**
 * Represents a selectable entry for the GalleryGrid by implementing SelectListener interface.
 */
public class GalleryEntity extends Image implements SelectListener{

	private boolean selected;

	public GalleryEntity(Texture texture) {
		super(texture);
		setScaling(Scaling.fit);
	}

	@Override
	public void select() {
		changeAlpha(.5f);
		selected = true;
	}

	@Override
	public void deselect() {
		selected = false;
		changeAlpha(1f);
	}
	
	private void changeAlpha(float to){
		Color col = getColor();
		col.a = to;
		setColor(col);
	}
	
	public boolean isSelected() {
		return selected;
	}

}

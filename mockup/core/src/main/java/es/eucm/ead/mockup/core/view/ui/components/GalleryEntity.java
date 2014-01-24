package es.eucm.ead.mockup.core.view.ui.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;

import es.eucm.ead.mockup.core.control.listeners.SelectListener;

/**
 * Represents a selectable entry for the GalleryGrid by implementing SelectListener interface.
 */
public class GalleryEntity extends Image implements SelectListener{
	private static final float animationDuration = .4f;
	private boolean selected;

	public GalleryEntity(Texture texture) {
		super(texture);
		setScaling(Scaling.fit);
		setOrigin(getPrefWidth()*.5f, getPrefHeight()*.5f);
	}

	@Override
	public void select() {
		changeAlpha(.9f);
		selected = true;
		addAction(Actions.scaleTo(.9f, .9f, animationDuration, Interpolation.swingOut));
	}

	@Override
	public void deselect() {
		selected = false;
		changeAlpha(1f);
		addAction(Actions.scaleTo(1f, 1f, animationDuration, Interpolation.swingOut));
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

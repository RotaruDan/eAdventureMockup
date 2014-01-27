/**
 * eAdventure is a research project of the
 *    e-UCM research group.
 *
 *    Copyright 2005-2013 e-UCM research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    e-UCM is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.eucm.ead.mockup.core.view.ui.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;

import es.eucm.ead.mockup.core.control.listeners.SelectListener;
import es.eucm.ead.mockup.core.control.screens.AbstractScreen;

/**
 * Represents a selectable entry for the GalleryGrid by implementing
 * SelectListener interface.
 */
public class GalleryEntity extends Image implements SelectListener {
	private static final float animationDuration = .4f;
	private boolean selected, originUpdated = false;
	private static NinePatch selectedview;

	public GalleryEntity(Texture texture) {
		super(texture);
		setScaling(Scaling.fit);
		if (selectedview == null) {
			selectedview = AbstractScreen.skin.getPatch("text_focused");
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		if (selected)
			selectedview.draw(batch, getX(), getY(), getWidth(), getHeight());
	}

	@Override
	public void select() {
		changeAlpha(.9f);
		selected = true;
		if (!originUpdated) {
			originUpdated = true;
			setOrigin(getWidth() * .5f, getHeight() * .5f);
		}
		addAction(Actions.scaleTo(.9f, .9f, animationDuration,
				Interpolation.swingOut));
	}

	@Override
	public void deselect() {
		selected = false;
		changeAlpha(1f);
		addAction(Actions.scaleTo(1f, 1f, animationDuration,
				Interpolation.swingOut));
	}

	private void changeAlpha(float to) {
		Color col = getColor();
		col.a = to;
	}

	public boolean isSelected() {
		return selected;
	}
}

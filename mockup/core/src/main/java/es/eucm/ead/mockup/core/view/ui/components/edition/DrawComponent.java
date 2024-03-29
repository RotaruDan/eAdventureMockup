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
package es.eucm.ead.mockup.core.view.ui.components.edition;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import es.eucm.ead.mockup.core.control.screens.AbstractScreen;
import es.eucm.ead.mockup.core.view.UIAssets;
import es.eucm.ead.mockup.core.view.ui.GridPanel;
import es.eucm.ead.mockup.core.view.ui.Panel;
import es.eucm.ead.mockup.core.view.ui.buttons.ToolbarButton;

public class DrawComponent {

	private PaintPanel panel;
	private ToolbarButton button;
	private Color color;

	/**
	 * This will draw lines.
	 */
	private PaintingComponent paintingComponent;

	/**
	 * This will delete lines.
	 */
	private DeletingComponent deletingComponent;

	public enum Type {
		BRUSH, RUBBER, TEXT
	}

	public DrawComponent(final DeletingComponent deletingComponent,
			final PaintingComponent paintingComponent, String imageUp,
			String name, Skin skin, String description, Type type, float width,
			float height) {
		if (type.equals(Type.BRUSH)) {
			this.color = Color.YELLOW;
		} else {
			this.color = Color.WHITE;
		}

		this.paintingComponent = paintingComponent;
		this.deletingComponent = deletingComponent;
		this.button = new ToolbarButton(skin.getDrawable(imageUp), name, skin) {
			@Override
			public void setChecked(boolean isChecked) {
				if (paintingComponent != null) {
					paintingComponent
							.setTouchable(isChecked ? Touchable.enabled
									: Touchable.disabled);
				}
				if (deletingComponent != null) {
					deletingComponent
							.setTouchable(isChecked ? Touchable.enabled
									: Touchable.disabled);
				}
				super.setChecked(isChecked);
			}
		};
		this.panel = new PaintPanel(skin, "opaque", description, type, width,
				height);
		button.setFocusListener(panel);
		this.button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				event.cancel();
				if (!panel.isVisible()) {
					AbstractScreen.mockupController.show(panel);
				} else {
					AbstractScreen.mockupController.hide(panel);
				}
				button.setChecked(true);
			}
		});
	}

	private class PaintPanel extends Panel {

		private float width;
		private float height;
		private Type type;

		private Slider slider;
		private GridPanel<Actor> gridPanel;

		private Label textSample;
		private Image cir;
		private Texture pixTex;
		private Pixmap circleSample;

		private String tSize = "Tamaño de ";

		private final float maxPixRadius = 50f;
		private final int pixmapWidthHeight = 100,
				center = pixmapWidthHeight / 2;

		public PaintPanel(Skin skin, String styleName, String description,
				Type t, float width, float height) {

			super(skin, styleName);

			this.height = height;
			this.width = width;

			this.type = t;

			if (type == Type.TEXT) {
				tSize += "texto";
				this.textSample = new Label("Ejemplo...", skin);
			} else {
				tSize += "pincel";
			}

			setHeight(height);
			setWidth(width);

			setVisible(false);
			setModal(false);
			setColor(Color.DARK_GRAY);

			defaults().fill().expand();

			Label label = new Label(description, skin, "default-thin-opaque");
			label.setWrap(true);
			label.setAlignment(Align.center);
			label.setFontScale(0.7f);

			slider = new Slider(15, 60, 0.5f, false, skin, "left-horizontal");
			slider.setValue(30);
			slider.addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {

					updateDemoColor();

					return true;
				}

				@Override
				public void touchDragged(InputEvent event, float x, float y,
						int pointer) {

					updateDemoColor();

				}

				@Override
				public void touchUp(InputEvent event, float x, float y,
						int pointer, int button) {

					updateDemoColor();

				}
			});

			createPalette(skin);
			circleSample = new Pixmap(pixmapWidthHeight, pixmapWidthHeight,
					Format.RGBA8888);

			Blending b = Pixmap.getBlending();
			Pixmap.setBlending(Blending.None);
			circleSample.fill();
			Pixmap.setBlending(b);
			if (type == Type.BRUSH) {
				paintingComponent.setRadius(getCurrentRadius());
			} else if (type.equals(Type.RUBBER)) {
				deletingComponent.setRadius(getCurrentRadius());
			}
			circleSample.setColor(color);
			int radius = (int) getCurrentRadius();
			circleSample.fillCircle(center, center, radius);
			pixTex = new Texture(circleSample); // FIXME unmanaged upenGL
			// textures, TODO reload
			// onResume (after pause)
			pixTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);

			add(label);
			row();
			add(tSize).padLeft(8f);
			row();
			add(slider);
			row();
			add(textSample);
			row();

			if (type != Type.TEXT) {
				cir = new Image(pixTex);
				add(cir).align(Align.center).expand(false, false).fill(false)
						.size(60, 60);
			} else {
				textSample.setColor(color);
				add(textSample).align(Align.left).size(60, 60).padLeft(8f);
			}
			if (type != Type.RUBBER) {
				row();
				add("Colores:").padLeft(8f);
				row();
				add(gridPanel);
			}

			// debug();

		}

		/**
		 * Set the panel'coordinates according to the button's coordinates
		 */
		public void actCoordinates() {
			setX(button.getX() + (button.getWidth() / 2) - (width / 2));
			setY(AbstractScreen.stageh - UIAssets.TOOLBAR_HEIGHT - height - 10);
		}

		@Override
		public void show() {
			super.show();
		}

		@Override
		public void hide() {
			super.hide();
			if (paintingComponent != null)
				paintingComponent.setVisible(button.isChecked());
			if (deletingComponent != null)
				deletingComponent.setVisible(button.isChecked());
		}

		public float getSize() {
			return slider.getValue();
		}

		/**
		 * Updates the texture that displays the visual representation of our
		 * draw component.
		 */
		private void updateDemoColor() {
			if (type == Type.TEXT) {
				updateTextSample();
			} else {
				updateCircleSample();
			}
		}

		private void updateCircleSample() {
			Blending b = Pixmap.getBlending();
			Pixmap.setBlending(Blending.None);
			circleSample.setColor(0f, 0f, 0f, 0f);
			circleSample.fill();
			Pixmap.setBlending(b);

			circleSample.setColor(color);
			float radius = getCurrentRadius();
			circleSample.fillCircle(center, center, (int) radius);
			if (paintingComponent != null) {
				paintingComponent.setRadius(radius);
				paintingComponent.setMeshColor(color);
			}
			if (deletingComponent != null)
				deletingComponent.setRadius(radius);
			pixTex.draw(circleSample, 0, 0);
		}

		private float getCurrentRadius() {
			return maxPixRadius * slider.getValue() / slider.getMaxValue();
		}

		private void updateTextSample() {
			textSample.setColor(color);
			textSample.setFontScale((slider.getValue() + 1)
					/ slider.getMaxValue());
		}

		private void createPalette(Skin skin) {
			Pixmap auxPixmap = new Pixmap(50, 50, Format.RGB888);
			final int COLS = 4, ROWS = 3;
			final Color[][] colrs = {
					{ Color.BLACK, Color.BLUE, Color.CYAN,
							new Color(.5f, .75f, .32f, 1f) },
					{ Color.GREEN, Color.MAGENTA, Color.ORANGE, Color.PINK },
					{ Color.RED, Color.LIGHT_GRAY, Color.YELLOW, Color.WHITE } };
			gridPanel = new GridPanel<Actor>(skin, ROWS, COLS, 20);
			ClickListener colorListener = new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					event.cancel();
					Image list = (Image) event.getListenerActor();
					color = list.getColor();
					updateDemoColor();
				}
			};
			for (int i = 0; i < ROWS; i++) {
				for (int j = 0; j < COLS; j++) {
					Color c = colrs[i][j];
					auxPixmap.setColor(c);
					auxPixmap.fill();
					final Image colorB = new Image(new Texture(auxPixmap)); // FIXME
					// unmanaged
					// upenGL
					// textures,
					// TODO
					// reload
					// onResume
					// (after
					// pause)
					colorB.setColor(c);
					colorB.addListener(colorListener);
					gridPanel.addItem(colorB, i, j).expand().fill();
				}
			}
			auxPixmap.dispose();
		}
	}

	public PaintPanel getPanel() {
		return panel;
	}

	public Button getButton() {
		return button;
	}

	public Color getColor() {
		return color;
	}

	public float getPincelSize() {
		return panel.getSize();
	}

	public void actCoordinates() {
		panel.actCoordinates();
	}
}

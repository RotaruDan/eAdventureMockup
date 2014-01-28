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

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ScreenUtils;

import es.eucm.ead.mockup.core.control.screens.AbstractScreen;

/**
 * Fast implementation of a PaintingActor.
 */
public class PaintingComponent extends Actor implements Disposable {

	private MeshHelper mesh;
	private float radius;

	public PaintingComponent() {
		mesh = new MeshHelper();
		addListener(new InputListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}

			public void touchDragged(InputEvent event, float screenX,
					float screenY, int pointer) {
				if(pointer == 0){
					mesh.input(screenX, screenY);
				}
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if(pointer == 0){
					mesh.restart(getStage().getSpriteBatch());
				}
			}
		});
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		mesh.draw(batch, parentAlpha);
	}
	
	public void delete(int x, int y, int radius){
		mesh.delete(x, y, radius);
	}

	@Override
	public void dispose() {
		mesh.dispose();
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (!visible) {
			mesh.reset();
		}
	}

	private class MeshHelper implements Disposable {
		private Mesh mesh;
		private ShaderProgram meshShader;
		private static final int MAX_LINES = 1000;
		private float[] lineVertices;
		private int vertexIndex = 0;
		private Vector3 unprojectedVertex = new Vector3();
		private float lastX, lastY;
		private Image showingImage;
		private Texture showingTexture;
		private Pixmap showingPixmap;
		private FrameBuffer mBuf;
		private final int WIDTH, HEIGHT;

		public MeshHelper() {
			lineVertices = new float[MAX_LINES * 2 * 2];
			createShader();
			createMesh();
			Stage stage = AbstractScreen.stage;
			WIDTH = (int)stage.getWidth();
			HEIGHT = (int)stage.getHeight();
			showingPixmap = new Pixmap(WIDTH, HEIGHT, Format.RGBA8888);
			showingTexture = new Texture(WIDTH, HEIGHT, Format.RGBA8888);
			TextureRegion texRegion = new TextureRegion(showingTexture);
			texRegion.flip(false, true);
			showingImage = new Image(texRegion);
			showingImage.setBounds(0, 0, WIDTH, HEIGHT);
			showingImage.setOrigin(WIDTH*.5f, stage.getHeight()*.5f);
			mBuf = new FrameBuffer(Format.RGBA8888, WIDTH, HEIGHT, false);
		}

		public void delete(int x, int y, int radius) {
			Blending oldBlending = Pixmap.getBlending();
			Pixmap.setBlending(Blending.None);
			showingPixmap.fillCircle(x, y, radius);
			showingTexture.draw(showingPixmap, 0, 0);	
			Pixmap.setBlending(oldBlending);
		}

		public void restart(Batch batch) {
			mBuf.begin();
			drawMesh(batch);
			Pixmap pix = ScreenUtils.getFrameBufferPixmap(0, 0, WIDTH, HEIGHT);
			mBuf.end();
			showingPixmap.drawPixmap(pix, 0, 0);
			showingTexture.draw(showingPixmap, 0, 0);		
			pix.dispose();
			reset();
		}

		private void reset() {
			vertexIndex = 0;
			mesh.setVertices(lineVertices, 0, vertexIndex);
		}

		public void createMesh() {
			mesh = new Mesh(true, MAX_LINES * 2, 0, new VertexAttribute(
					Usage.Position, 2, "a_position"));
		}

		private void createShader() {
			// this shader tells opengl where to put things
			String vertexShader = "attribute vec4 a_position;    					\n"
					+ "uniform mat4 u_worldView;					\n"
					+ "void main()                  				\n"
					+ "{                            				\n"
					+ "   gl_Position =  u_worldView * a_position;	\n" + "}";

			// this one tells it what goes in between the points (i.e
			// color/texture)
			String fragmentShader = "#ifdef GL_ES                			\n"
					+ "precision mediump float;    			\n"
					+ "#endif                      			\n"
					+ "void main()                 			\n"
					+ "{                           			\n"
					+ "  gl_FragColor = vec4(1, 1, 0, 1);   \n" + "}";

			// make an actual shader from our strings
			ShaderProgram.pedantic = false;
			meshShader = new ShaderProgram(vertexShader, fragmentShader);

			// check there's no shader compile errors
			if (meshShader.isCompiled() == false)
				throw new IllegalStateException(meshShader.getLog());
		}

		public void draw(Batch sb, float parentAlpha) {
			showingImage.draw(sb, parentAlpha);
			sb.end();
			drawMesh(sb);
			sb.begin();
		}
		
		private void drawMesh(Batch sb){
			meshShader.begin();
			meshShader
			.setUniformMatrix("u_worldView", sb.getProjectionMatrix());
			mesh.render(meshShader, GL20.GL_TRIANGLE_STRIP);
			meshShader.end();
		}

		public void dispose() {
			mesh.dispose();
			meshShader.dispose();
			mBuf.dispose();
			showingTexture.dispose();
			showingPixmap.dispose();
		}

		public void input(float x, float y) {
			if (vertexIndex == lineVertices.length)
				return;
			unprojectedVertex.set(x, y, 0);
			x = unprojectedVertex.x;
			y = unprojectedVertex.y;

			if (vertexIndex == 0 || unprojectedVertex.dst(lastX, lastY, 0) > 5) {

				unprojectedVertex.set(x, y, 0).sub(lastX, lastY, 0).nor();
				unprojectedVertex.set(-unprojectedVertex.y,
						unprojectedVertex.x, 0);
				unprojectedVertex.scl(radius);

				lineVertices[vertexIndex++] = x + unprojectedVertex.x;
				lineVertices[vertexIndex++] = y + unprojectedVertex.y;

				lineVertices[vertexIndex++] = x - unprojectedVertex.x;
				lineVertices[vertexIndex++] = y - unprojectedVertex.y;

				mesh.setVertices(lineVertices, 0, vertexIndex);

				lastX = x;
				lastY = y;
			}
		}
	}

	public void setRadius(float radius) {
		this.radius = radius * .5f;
	}
}

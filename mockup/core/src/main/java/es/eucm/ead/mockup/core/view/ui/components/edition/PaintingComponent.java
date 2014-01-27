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
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

/**
 * Fast implementation of a PaintingActor.
 */
public class PaintingComponent extends Actor {

	private MeshHelper mesh;

	public PaintingComponent() {	
		mesh = new MeshHelper();
		addListener(new InputListener() {			
			public void touchDragged(InputEvent event, float screenX,
					float screenY, int pointer) {
				mesh.input(screenX, screenY);
			}
		});
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		mesh.draw(batch);
	}
	
	private class MeshHelper {
		private Mesh mesh;
		private ShaderProgram meshShader;
		static final int MAX_LINES = 1000; 
		float[] lineVertices;
		int vertexIndex = 0;
		private Vector3 unprojectedVertex = new Vector3();
		private float lastX, lastY;
		
		public MeshHelper() {
			lineVertices = new float[MAX_LINES * 2 * 2];		
			createShader();
			createMesh();
		}

		public void createMesh() {
			mesh = new Mesh(true, MAX_LINES * 2, 0, new VertexAttribute(Usage.Position, 2, "a_position"));		
		}

		private void createShader() {
			// this shader tells opengl where to put things
			String vertexShader = "";
			/*vertexShader =  "attribute vec4 a_position;\n"
					+  "attribute vec4 a_color;\n"
					+  "attribute vec2 a_texCoord0;\n"

	      + " uniform mat4 u_projTrans;\n"

	      +  "varying vec4 v_color;\n"
	      +  "varying vec2 v_texCoords;\n"

	      +  "void main() {\n"
	      +     " v_color = a_color;\n"
	      +    " v_texCoords = a_texCoord0;\n"
	      +     " gl_Position = u_projTrans * a_position;\n"
	      + " }\n";*/
			// this one tells it what goes in between the points (i.e
			// colour/texture)
			vertexShader = "attribute vec4 a_position;    \n" + 
					//"attribute vec4 a_color;\n" +
					//"attribute vec2 a_texCoord0;\n" + 
					"uniform mat4 u_worldView;\n" + 
					//"varying vec4 v_color;" + 
					//"varying vec2 v_texCoords;" + 
					"void main()                  \n" + 
					"{                            \n" + 
					//"   v_color = vec4(1, 1, 1, 1); \n" + 
					//"   v_texCoords = a_texCoord0; \n" + 
					"   gl_Position =  u_worldView * a_position;  \n"      + 
					"}" ;
			String fragmentShader = "#ifdef GL_ES                \n"
					+ "precision mediump float;    \n"
					+ "#endif                      \n"
					+ "void main()                 \n"
					+ "{                           \n"
					+ "  gl_FragColor = vec4(1.0,1.0,0.0,1.0);    \n"
					+ "}";

			/*fragmentShader = "#ifdef GL_ES\n"
					+"precision mediump float;\n"
					+"#endif\n"

			+"varying vec4 v_color;\n"
			+"varying vec2 v_texCoords;\n"
			+"uniform sampler2D u_texture;\n"

			+"void main() {\n"
			+"	gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n"
			+"}\n";


			vertexShader = "attribute vec4 a_position;    \n" + 
					"attribute vec4 a_color;\n" +
					"attribute vec2 a_texCoord0;\n" + 
					"uniform mat4 u_worldView;\n" + 
					"varying vec4 v_color;" + 
					"varying vec2 v_texCoords;" + 
					"void main()                  \n" + 
					"{                            \n" + 
					"   v_color = vec4(1, 1, 1, 1); \n" + 
					"   v_texCoords = a_texCoord0; \n" + 
					"   gl_Position =  u_worldView * a_position;  \n"      + 
					"}                            \n" ;
			fragmentShader = "#ifdef GL_ES\n" +
					"precision mediump float;\n" + 
					"#endif\n" + 
					"varying vec4 v_color;\n" + 
					"varying vec2 v_texCoords;\n" + 
					"uniform sampler2D u_texture;\n" + 
					"void main()                                  \n" + 
					"{                                            \n" + 
					"  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n"+
					"}";*/
			// make an actual shader from our strings
			ShaderProgram.pedantic = false;
			meshShader = new ShaderProgram(vertexShader, fragmentShader);

			// check there's no shader compile errors
			if (meshShader.isCompiled() == false)
				throw new IllegalStateException(meshShader.getLog());
		}

		public void draw(Batch sb) {
			sb.end();
			meshShader.begin();
			meshShader.setUniformMatrix("u_worldView", sb.getProjectionMatrix());
			mesh.render(meshShader, GL20.GL_TRIANGLE_STRIP);
			meshShader.end();
			sb.begin();
		}
		
		public void dispose() {
			mesh.dispose();
			meshShader.dispose();
		}

		@SuppressWarnings("deprecation")
		public void input(float x, float y) {
			System.out.println(x + " " + y);
			unprojectedVertex.set(x,y, 0);
			x =  unprojectedVertex.x;
			y =  unprojectedVertex.y;

			if (vertexIndex == 0 || 
					unprojectedVertex.dst(lastX, lastY, 0) > 5) {

				unprojectedVertex.set(x, y, 0).sub(lastX, lastY, 0).nor();
				unprojectedVertex.set(-unprojectedVertex.y, unprojectedVertex.x, 0);
				unprojectedVertex.mul(5);		

				lineVertices[vertexIndex++] = x + unprojectedVertex.x;
				lineVertices[vertexIndex++] = y + unprojectedVertex.y;


				lineVertices[vertexIndex++] = x - unprojectedVertex.x;
				lineVertices[vertexIndex++] = y - unprojectedVertex.y;

				mesh.setVertices(lineVertices, 0, vertexIndex);

				lastX = x; lastY = y;
			}
		}

	}
}

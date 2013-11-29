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
package es.eucm.ead.core.scene;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import es.eucm.ead.core.EAdEngine;
import es.eucm.ead.core.actors.SceneActor;
import es.eucm.ead.core.actors.SceneElementActor;
import es.eucm.ead.core.scene.tasks.AddSceneElementTask;
import es.eucm.ead.core.scene.tasks.SetSceneTask;
import es.eucm.ead.schema.actors.Scene;
import es.eucm.ead.schema.actors.SceneElement;
import es.eucm.ead.schema.game.Game;

public class SceneManager {

	private static final int LOAD_TIME = 1000 / 30;

	private String currentSceneName;

	private Scene currentScene;

	private SceneActor currentSceneActor;

	private Array<SceneTask> tasks;

	private AssetManager assetManager;

	private Game game;

	public SceneManager(AssetManager assetManager) {
		tasks = new Array<SceneTask>();
		this.assetManager = assetManager;
	}

	/**
	 * Loads the scene with the given name
	 * 
	 * @param name
	 *            the scene's name
	 */
	public void loadScene(String name) {
		if (!name.endsWith(".json")) {
			name += ".json";
		}

		currentSceneName = "scenes/" + name;
		assetManager.load(currentSceneName, Scene.class);
		SetSceneTask st = Pools.obtain(SetSceneTask.class);
		st.setScene(currentSceneName);
		addTask(st);
	}

	/**
	 * Sets a scene. All the assets required by the scene must be already
	 * loaded. Consider using {@link SceneManager#loadScene(String)}
	 * 
	 * @param name
	 *            the scene fil name (with.json extension)
	 */
	public void setScene(String name) {
		currentScene = assetManager.get(name);
		if (currentSceneActor != null) {
			currentSceneActor.free();
		}
		currentSceneActor = EAdEngine.factory.getElement(currentScene);
		EAdEngine.stage.setScene(currentSceneActor);
	}

	/**
	 * Adds a task to be performed once the scene manager is done loading
	 * 
	 * @param task
	 *            the task
	 */
	protected void addTask(SceneTask task) {
		tasks.add(task);
	}

	/**
	 * @return if the scene manager is still loading assets
	 */
	public boolean isLoading() {
		return EAdEngine.assetManager.getQueuedAssets() > 0;
	}

	/**
	 * Update the scene manager
	 */
	public void act() {
		if (isLoading()) {
			boolean done = EAdEngine.assetManager.update(LOAD_TIME);
			if (done) {
				executeTasks();
			}
		} else if (tasks.size > 0) {
			executeTasks();
		}
	}

	private void executeTasks() {
		for (SceneTask t : tasks) {
			t.execute(this);
			Pools.free(t);
		}
		tasks.clear();
	}

	/**
	 * 
	 * @return the current scene
	 */
	public Scene getScene() {
		return currentScene;
	}

	public String getCurrentSceneName() {
		return currentSceneName;
	}

	public void loadSceneElement(SceneElement sceneElement) {
		AddSceneElementTask t = Pools.obtain(AddSceneElementTask.class);
		t.setSceneElement(sceneElement);
		addTask(t);
	}

	public void addSceneElement(SceneElement sceneElement) {
		currentScene.getChildren().add(sceneElement);
		currentSceneActor.addActor(sceneElement);
	}

	public void removeSceneElement(SceneElementActor actor) {
		actor.remove();
		currentScene.getChildren().remove(actor.getElement());
	}

	public void loadGame() {
		assetManager.load("game.json", String.class);
		assetManager.finishLoading();
		game = EAdEngine.jsonIO.fromJson(Game.class, (String) assetManager
				.get("game.json"));
		setGame(game);
	}

	public void setGame(Game game) {
		this.game = game;
		EAdEngine.stage.setGameSize(game.getWidth(), game.getHeight());
		loadScene(game.getInitialScene());
	}

	public Game getGame() {
		return game;
	}

	/**
	 * Interface for tasks that are executed once the scene manager is done
	 * loading
	 */
	public interface SceneTask {
		/**
		 * Executes the task
		 * 
		 * @param sceneManager
		 *            the scene manager
		 */
		void execute(SceneManager sceneManager);
	}

}
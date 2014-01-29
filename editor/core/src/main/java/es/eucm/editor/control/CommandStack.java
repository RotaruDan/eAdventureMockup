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
package es.eucm.editor.control;

import com.badlogic.gdx.Gdx;
import es.eucm.ead.editor.control.commands.Command;
import es.eucm.editor.model.ModelEvent;
import java.util.Stack;

import es.eucm.editor.model.EditorModel;

/**
 * Stacks of performed and undone actions
 */
public class CommandStack extends Command {

	public static final String commandName = "CommandStack";

	/**
	 * Stack of performed actions
	 */
	private Stack<Command> performed;

	/**
	 * Stack of undone actions
	 */
	private Stack<Command> undone;

	/**
	 * The number of actions performed successfully on the model. Might differ
	 * from performed.size() if there are actions that cannot be undone.
	 */
	private int actionHistory;

	/**
	 * Default constructor
	 */
	public CommandStack() {
		performed = new Stack<Command>();
		undone = new Stack<Command>();
		actionHistory = 0;
	}

	@Override
	public ModelEvent doCommand(EditorModel em) {
		throw new UnsupportedOperationException("Cannot 'perform' whole stacks");
	}

	@Override
	public boolean canUndo() {
		return actionHistory == performed.size();
	}

	@Override
	public ModelEvent undoCommand(EditorModel em) {
		undone.clear();
		ModelEvent mmc = new ModelEvent(this);
		while (!performed.isEmpty()) {
			Command action = performed.peek();
			ModelEvent me = action.undoCommand(em);
			if (me != null) {
				mmc.merge(me);
			} else {
				Gdx.app.error("CommandStack", "Error undoing command-stack at "
						+ action);
			}
			undone.push(performed.pop());
		}
		return mmc;
	}

	@Override
	public boolean canRedo() {
		for (Command a : undone) {
			if (!a.canRedo()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ModelEvent redoCommand(EditorModel em) {
		performed.clear();
		ModelEvent mmc = new ModelEvent(this);
		while (!undone.isEmpty()) {
			Command action = undone.peek();
			ModelEvent me = action.redoCommand(em);
			if (me != null) {
				mmc.merge(me);
			} else {
				Gdx.app.error("CommandStack", "Error redoing command-stack at "
						+ action);
			}
			performed.push(undone.pop());
		}
		return mmc;
	}

	@Override
	public boolean combine(Command other) {
		return false;
	}

	/**
	 * @return The stack of performed actions
	 */
	public Stack<Command> getPerformed() {
		return performed;
	}

	/**
	 * @return The stack of undone actions
	 */
	public Stack<Command> getUndone() {
		return undone;
	}

	/**
	 * Increase the action history
	 */
	public void increaseActionHistory() {
		this.actionHistory++;
	}

	/**
	 * Decrease the action history
	 */
	public void decreaseActionHistory() {
		this.actionHistory--;
	}

	/**
	 * @return The action history
	 */
	public int getActionHistory() {
		return actionHistory;
	}

	/**
	 * Clear stacks of performed and undone actions
	 */
	public void clear() {
		performed.clear();
		undone.clear();
	}
}

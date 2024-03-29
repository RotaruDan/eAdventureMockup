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
package es.eucm.ead.engine;

import com.badlogic.gdx.Gdx;
import es.eucm.ead.schema.components.VariableDef;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Holds all game variables
 */
public class VarsContext {

	/**
	 * Prefix for variables are created and managed by the engine. This is a
	 * naming convention, intended to avoid name clashes with user-defined
	 * variables.
	 */
	public static final String RESERVED_VAR_PREFIX = "_";

	/**
	 * Prefix for variables that are always copied to (and from) subroutines.
	 * This makes then "global" in the traditional programming sense. Note that
	 * global variables are all reserved.
	 */
	public static final String GLOBAL_VAR_PREFIX = RESERVED_VAR_PREFIX + "g_";

	/**
	 * Current game language. See @see es.eucm.ead.engine.I18N for details on
	 * possible values
	 */
	public static final String LANGUAGE_VAR = GLOBAL_VAR_PREFIX + "lang";

	private Map<String, Variable> variables;

	public VarsContext() {
		variables = new HashMap<String, Variable>();
	}

	/**
	 * Registers the given list variables
	 * 
	 * @param vars
	 *            a list with variables
	 */
	public void registerVariables(List<VariableDef> vars) {
		for (VariableDef v : vars) {
			registerVariable(v);
		}
	}

	/**
	 * Register the given variable and sets its value to its initial value. The
	 * initial value of the variable determines the variable type, and this
	 * won't be able to change during execution
	 * 
	 * @param v
	 *            the variable
	 */
	public void registerVariable(VariableDef v) {
		variables.put(v.getName(),
				new Variable(v.getType(), v.getInitialValue()));
	}

	/**
	 * Copies global variables to another VarsContext.
	 * 
	 * @param target
	 *            VarsContext
	 */
	public void copyGlobalsTo(VarsContext target) {
		for (String key : variables.keySet()) {
			if (key.startsWith(GLOBAL_VAR_PREFIX)) {
				// copies both value and definition
				target.variables.put(key, variables.get(key));
			}
		}
	}

	/**
	 * Sets the value for the variable with given name
	 * 
	 * @param name
	 *            variable name
	 * @param value
	 *            variable value
	 */
	public void setValue(String name, Object value) {
		Variable var = getVariable(name);
		if (var != null) {
			if (!var.setValue(value)) {
				Gdx.app.error("VarsContext", "Cannot set value of " + name
						+ " to " + value + ": bad value");
			}
		} else {
			Gdx.app.error("VarsContext", "Cannot set value of " + name + " to "
					+ value + ": no such variable found");
		}
	}

	/**
	 * @param name
	 *            variable name
	 * @return Returns the variable with the given name
	 */
	public Variable getVariable(String name) {
		try {
			Variable value = variables.get(name);
			if (value == null) {
				Gdx.app.error("VarsContext", "No variable with name " + name
						+ ": returning 'null'.");
			}
			return value;
		} catch (ClassCastException e) {
			Gdx.app.error("VarsContext", "Invalid return type for variable "
					+ name + ": returning 'null'", e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	/**
	 * @param name variable name
	 * @return the value for the variable
	 */
	public <T> T getValue(String name) {
		Variable variable = getVariable(name);
		return variable == null ? null : (T) variable.value;
	}

	/**
	 * Represents a variable during execution time
	 */
	public static class Variable {
		/**
		 * Type of the variable
		 */
		private final Class<?> type;

		/**
		 * Current value of the variable
		 */
		private Object value;

		public Variable(VariableDef.Type type, String initialValue) {
			switch (type) {
			case BOOLEAN:
				this.type = Boolean.class;
				this.value = Boolean.parseBoolean(initialValue);
				break;
			case FLOAT:
				this.type = Float.class;
				this.value = Float.parseFloat(initialValue);
				break;
			case INTEGER:
				this.type = Integer.class;
				this.value = Integer.parseInt(initialValue);
				break;
			case STRING:
				this.type = String.class;
				this.value = initialValue;
				break;
			default:
				throw new IllegalArgumentException("Unknown VariableDef type: "
						+ type);
			}
		}

		/**
		 * 
		 * @return Type of the variable
		 */
		public Class<?> getType() {
			return type;
		}

		/**
		 * 
		 * @return the value of the variable
		 */
		public Object getValue() {
			return value;
		}

		/**
		 * 
		 * @param value
		 *            the value
		 * @return if the value was setValue. (It returns false if type of the
		 *         variable it is different from the type of the value)
		 */
		public boolean setValue(Object value) {
			if (value == null || value.getClass() == type) {
				this.value = value;
				return true;
			} else {
				Gdx.app.error("VarsContext", value + " is not assignable to "
						+ type);
				return false;
			}
		}
	}
}

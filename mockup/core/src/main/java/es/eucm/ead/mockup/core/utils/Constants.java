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
package es.eucm.ead.mockup.core.utils;

/**
 * Constants used for the editor.
 */
public class Constants {

	/**
	 * Screen's width used for UI's Stage.
	 */
	public static final float SCREENW = 1100f;

	/**
	 * Screen's height used for UI's Stage.
	 */
	public static final float SCREENH = 720f;

	public static final String LOG_TAG = "MOCKUP_LOG";

	public static final String SKIN_SRC = "mockup/skin/holo-dark-xhdpi.json";

	public static final String DEFAULT_LANG = "es_ES";

	/**
	 * Fast implementation to display some
	 * elements...
	 */
	public static final String[] demoElements = {
			"mockup/temp/elements/cama-quirofano.jpg",
			"mockup/temp/elements/Cama2.jpg",
			"mockup/temp/elements/enfermera.jpg",
			"mockup/temp/elements/enfermera2.jpg",
			"mockup/temp/elements/estetoscopio.jpg",
			"mockup/temp/elements/lampara.jpg",
			"mockup/temp/elements/medico-de-ambulancia.jpg" };

	/**
	 * Fast implementation to display some
	 * scenes...
	 */
	public static final String[] demoScenes = {
			"mockup/temp/scenes/sala-de-estar.jpg", //Initial scene
			"mockup/temp/scenes/ambulancia.jpg",
			"mockup/temp/scenes/pacientes.jpg",
			"mockup/temp/scenes/quirofano.jpg" };

	/**
	 * Fast implementation to display some thumbnail
	 * elements...
	 */
	public static final String[] demoElementsThumbnail = {
			"mockup/temp/elements/thumbnail/cama-quirofano_thumb.jpg",
			"mockup/temp/elements/thumbnail/Cama2_thumb.jpg",
			"mockup/temp/elements/thumbnail/enfermera_thumb.jpg",
			"mockup/temp/elements/thumbnail/enfermera2_thumb.jpg",
			"mockup/temp/elements/thumbnail/estetoscopio_thumb.jpg",
			"mockup/temp/elements/thumbnail/lampara_thumb.jpg",
			"mockup/temp/elements/thumbnail/medico-de-ambulancia_thumb.jpg"

	};

	/**
	 * Fast implementation to display some thumbnail
	 * scenes...
	 */
	public static final String[] demoScenesThumbnail = {
			"mockup/temp/scenes/thumbnail/sala-de-estar_thumb.jpg", //Initial scene
			"mockup/temp/scenes/thumbnail/ambulancia_thumb.jpg",
			"mockup/temp/scenes/thumbnail/pacientes_thumb.jpg",
			"mockup/temp/scenes/thumbnail/quirofano_thumb.jpg" };
}

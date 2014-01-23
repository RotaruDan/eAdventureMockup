package es.eucm.ead.mockup.core.view.ui.components;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.tablelayout.Cell;

import es.eucm.ead.mockup.core.view.UIAssets;
import es.eucm.ead.mockup.core.view.ui.GridPanel;
import es.eucm.ead.mockup.core.view.ui.ToolBar;
import es.eucm.ead.mockup.core.view.ui.buttons.ToolbarButton;

/**
 * A GridPanel that has multiple selection functionality after long press.
 * T must be instance of GalleryEntity in order to be able to select.
 */
public abstract class GalleryGrid<T extends Actor> extends GridPanel<T> {

	private static final float DEFAULT_DIALOG_PADDING_BOTTON_TOP = 20f;
	private static final float DEFAULT_ICON_SPACE = 10f;
	
	/**
	 * A collection storing the entities we've selected.
	 */
	protected Array<GalleryEntity> selectedEntities;
	
	/**
	 * If it's true we're in "selection mode"
	 */
	private boolean selecting;
	
	/**
	 * Auxiliar attribute that automatically hides it's contents when necesary.
	 */
	private Actor[] actorsToHide;
	/**
	 * The top toolbar that will be shown when we're in selection "mode".
	 */
	private ToolBar topToolbar;
	
	/**
	 * Just a label showing how many entities we've selected in the topToolBar.
	 */
	private Label numSelectedEntities;

	public GalleryGrid(Skin skin, int rows, int cols, Group root, Actor ...actorsToHide) {
		super(skin, rows, cols, UIAssets.GALLERY_PROJECT_HEIGHT * .2f);
		if(actorsToHide == null){
			throw new IllegalArgumentException("actorsToHide can't be null.");
		}
		this.actorsToHide = actorsToHide;
		defaults().expand().fill().uniform();
		selectedEntities = new Array<GalleryEntity>();
		selecting = false;		
		addListener(new ActorGestureListener(){
			
			/**
			 * 	Auxiliar attribute used to know if the target
			 * of our event it's indeed instance of GalleryEntity (which implements SelectListener)
			 */
			private GalleryEntity target;
			@Override
			public void touchDown(InputEvent event, float x, float y,
					int pointer, int button) {

				super.touchDown(event, x, y, pointer, button);
				Actor targ = event.getTarget();
				if(targ instanceof GalleryEntity){

					target = (GalleryEntity) targ;
					if(selecting) {
						if(target.isSelected()){
							target.deselect();
							removeSelectedEntry(target);
						} else {
							target.select();
							addSelectedEntry(target);
						}
					}
				} 
			}
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if(selecting) return;

				GalleryGrid.this.entityClicked(event);

			}
			@Override
			public boolean longPress(Actor actor, float x, float y) {
				if(selecting) return true;
				if(target instanceof GalleryEntity){
					startSelecting();
				}
				return true;
			}

			private void addSelectedEntry(GalleryEntity entity){
				selectedEntities.add(entity);
				numSelectedEntities.setText(String.valueOf(selectedEntities.size));
			}
			
			private void removeSelectedEntry(GalleryEntity entity){
				selectedEntities.removeValue(entity, true);
				numSelectedEntities.setText(String.valueOf(selectedEntities.size));
			}
			
			private void startSelecting(){
				selecting = true;
				target.select();
				addSelectedEntry(target);
				changeActorsVisibility(false);
			}

		});

		final Dialog confirmDialog = new Dialog("Eliminar elementos", skin, "exit-dialog") {
			protected void result(Object object) {
				onHide();
			}			 
		}.button("Cancelar", false)
		.button("Aceptar", true).key(Keys.BACK, false).key(Keys.ENTER, true); // TODO use i18n
		confirmDialog.padLeft(DEFAULT_DIALOG_PADDING_BOTTON_TOP);
		confirmDialog.padRight(DEFAULT_DIALOG_PADDING_BOTTON_TOP);

		confirmDialog.setMovable(false);
		topToolbar = new ToolBar(skin);
		topToolbar.setVisible(false);
		final ToolbarButton 
		deleteButton = new ToolbarButton(skin.getDrawable("ic_delete"), "Borrar", skin, false), //TODO i18n
		backButton = new ToolbarButton(skin.getDrawable("ic_goback"), "Atrás", skin, false); //TODO i18n
		ClickListener mListener = new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Actor target = event.getListenerActor();
				if(target == deleteButton){
					confirmDialog.getContentTable().clearChildren();
					String message;
					if(selectedEntities.size == 1){
						message = selectedEntities.size + " entrada se eliminará";
					}else {
						message = selectedEntities.size + " entradas se eliminarán";
					}
					confirmDialog.text(message).show(getStage());
				} else if(target == backButton){
					onHide();
				}
			}
		};
		deleteButton.addListener(mListener);
		backButton.addListener(mListener);		

		numSelectedEntities = new Label("1", skin);
		topToolbar.defaults().height(topToolbar.getHeight()).space(DEFAULT_ICON_SPACE);
		topToolbar.add(backButton).padLeft(UIAssets.NAVIGATION_BUTTON_WIDTH_HEIGHT + DEFAULT_ICON_SPACE);
		topToolbar.add(numSelectedEntities).left().expandX();
		topToolbar.add(deleteButton).padRight(DEFAULT_ICON_SPACE);
		
		root.addActor(topToolbar);
	}

	private void changeActorsVisibility(boolean visible){
		int i = 0, length = actorsToHide.length;
		for(; i < length; ++i){
			actorsToHide[i].setVisible(visible);
		}
		topToolbar.setVisible(!visible);
	}

	/**
	 * Called when this Actor was clicked if we're not in Selecting Mode.
	 */
	protected abstract void entityClicked(InputEvent event);

	@Override
	public Cell<?> addItem(T t, int row, int col) {
		return super.addItem(t, row, col);
	}

	/**
	 * Resets previous visibility changes to actors.
	 */
	public void onHide(){
		changeActorsVisibility(true);
		for(GalleryEntity select : selectedEntities){
			select.deselect();
		}
		selectedEntities.clear();
		selecting = false;
	}

	/**
	 * True if we're in "selection mode"
	 */
	public boolean isSelecting() {
		return selecting;
	}
}

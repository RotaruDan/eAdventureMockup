package es.eucm.ead.mockup.core.view.ui.components.edition;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

/**
 * Fast implementation of a component that deletes what we've painted.
 *
 */
public class DeletingComponent extends Actor{

	private int radius;

	public DeletingComponent(final PaintingComponent paintingComp) {
		addListener(new InputListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}

			public void touchDragged(InputEvent event, float screenX,
					float screenY, int pointer) {
				if(pointer == 0){
					paintingComp.delete((int)screenX, (int)screenY, radius);
				}
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if(pointer == 0){
					paintingComp.delete((int)x, (int)y, radius);
				}
			}
		});
	}

	public void setRadius(float currentRadius) {
		this.radius = (int) (currentRadius*.5f);
		
	}
}

package es.eucm.ead.mockup.core.view.ui.components.edition;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import es.eucm.ead.mockup.core.control.screens.Loading;

public class RectangleSelector extends Actor {

	/**TODO FAST implementation for rectangle selection**/

	private final float rRESIZEWH = 50;

	private enum Quadrant{
		ONE, TWO, THREE, FOUR
	}

	private float x, y;
	private boolean drawingNP, toDrag, dragging;
	private Rectangle rHitbox, rResize;
	private Quadrant np2q;
	private Vector3 touch;

	/**
	 * Used for the bounding limits...
	 */
	float screenw, screenh;


	public RectangleSelector() {

		touch= new Vector3();
		rHitbox = new Rectangle();
		rResize = new Rectangle();
		rResize.setWidth(rRESIZEWH);
		rResize.setHeight(rRESIZEWH);

		addListener(new InputListener(){
			private void limitHitBox(){
				if(rHitbox.x < 0){
					rHitbox.x = 0;
				} else if(rHitbox.x + rHitbox.width > screenw){
					rHitbox.x = screenw - rHitbox.width;
				}

				if(rHitbox.y < 0){
					rHitbox.y = 0;
				} else if(rHitbox.y + rHitbox.height > screenh){
					rHitbox.y = screenh - rHitbox.height;
				}
			}

			private void onDragging(){
				x=touch.x - rHitbox.width/2;
				y=touch.y - rHitbox.height/2;

				rHitbox.setX(x);
				rHitbox.setY(y);
				limitHitBox();

				float x2, y2;
				if(np2q == Quadrant.ONE){
					x2 = rHitbox.x + rHitbox.width - rResize.width;
					y2 = rHitbox.y + rHitbox.height - rResize.height;
				} else if(np2q == Quadrant.TWO){
					x2 = rHitbox.x;
					y2 = rHitbox.y + rHitbox.height - rResize.height;
				} else if(np2q == Quadrant.THREE){
					x2 = rHitbox.x;
					y2 = rHitbox.y;
				} else { //FOUR
					x2 = rHitbox.x + rHitbox.width - rResize.width;
					y2 = rHitbox.y;
				}
				rResize.setX(x2);
				rResize.setY(y2);
			}

			public boolean touchDown(InputEvent event, float screenX, float screenY,
					int pointer, int button) {
				if(pointer == 0){
					touch.set(screenX, screenY, 0);
					if(toDrag){
						if(rResize.contains(touch.x, touch.y)){
							toDrag = false;
							if(rHitbox.x<rResize.x){
								x = rHitbox.x;
							} else {
								x = rHitbox.x + rHitbox.width;
							}
							if(rHitbox.y<rResize.y){
								y = rHitbox.y;
							} else {
								y = rHitbox.y + rHitbox.height;
							} 

						} else if(rHitbox.contains(touch.x,  touch.y)){
							if(drawingNP){
								dragging = true;
							}						
						}
					}else{
						if(!drawingNP){
							drawingNP=true;
							x=touch.x;
							y=touch.y;
							rHitbox.set(touch.x, touch.y, 0, 0);
						}				
					}
				}
				return true;
			}

			public void touchDragged(InputEvent event, float screenX, float screenY,
					int pointer){
				if(pointer == 0){
					touch.set(screenX, screenY, 0);
					if(toDrag){
						if(dragging){
							if(drawingNP){
								onDragging();
							}	
						}

					} else {
						if(drawingNP){

							float auxX, auxY;
							float w = Math.abs(touch.x-x);
							float h = Math.abs(touch.y-y);
							if(x<touch.x){
								auxX = x;
							} else {
								auxX = touch.x;
							}
							if(y<touch.y){
								auxY=y;
							} else {
								auxY= touch.y;
							}
							rHitbox.set(auxX, auxY, w, h);
						}
					}
				}
			}

			public void touchUp(InputEvent event, float screenX, float screenY,
					int pointer, int button) {
				if(pointer == 0){
					touch.set(screenX, screenY, 0);

					if(toDrag){
						if(dragging){
							if(drawingNP){
								onDragging();
							}	
							dragging = false;
						}
					}else{

						if(drawingNP){

							float auxX, x2, auxY, y2;
							float w = Math.abs(touch.x-x);
							float h = Math.abs(touch.y-y);

							if(x<touch.x){
								if(y<touch.y){
									np2q = Quadrant.ONE;

									auxX = x;
									x2 = auxX + w - rRESIZEWH;
									auxY=y;
									y2 = auxY + h - rRESIZEWH;
								} else {
									np2q = Quadrant.FOUR;

									auxX = x;
									x2 = auxX + w - rRESIZEWH;
									auxY= touch.y;
									y2 = auxY;
								}
							} else {
								if(y<touch.y){
									np2q = Quadrant.TWO;

									auxX = touch.x;
									x2 = auxX;
									auxY=y;
									y2 = auxY + h - rRESIZEWH;
								} else {
									np2q = Quadrant.THREE;

									auxX = touch.x;
									x2 = auxX;
									auxY= touch.y;
									y2 = auxY;
								}
							}

							rHitbox.set(auxX, auxY, w, h);
							rResize.setX(x2);
							rResize.setY(y2);
							toDrag = true;
						}
					}
				}
			}
		});
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		rHitbox.set(-1, -1, 0, 0);
		drawingNP = toDrag = dragging = false;
	}

	@Override
	public void setBounds(float x, float y, float width, float height) {
		super.setBounds(x, y, width, height);
		this.screenh = height;
		this.screenw = width;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		if(drawingNP){
			Loading.loadingSel.draw(batch, rHitbox.x, rHitbox.y, rHitbox.width, rHitbox.height);
			if(toDrag) {
				Loading.loadingSel.draw(batch, rResize.x, rResize.y, rResize.width, rResize.height);
			}
		}
	}
}

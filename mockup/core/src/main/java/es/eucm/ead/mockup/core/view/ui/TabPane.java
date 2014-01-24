package es.eucm.ead.mockup.core.view.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ArrayMap;
import com.esotericsoftware.tablelayout.Cell;

/**
 * This contains the bindings between Tabs and Containers.
 */
public class TabPane<T extends Button, C extends Table> extends Panel
{

	private Table tabs;
	private Stack body;

	/**
	 * Used to keep one tab at any time.
	 */
	private ButtonGroup buttonGroup;

	private ArrayMap<T, C> tabBind;

	private T currentTab;
	private C currentContainer;

	/**
	 * Create a {@link TabPane} with default style.
	 * 
	 * @param skin the skin to use for style
	 */
	public TabPane(Skin skin)
	{
		this(skin, "default");
	}

	/**
	 * Create a {@link TabPane} with the specified style.
	 * 
	 * @param skin the skin to use for style
	 * @param styleName the name of the style to use
	 */
	public TabPane(Skin skin, String styleName)
	{
		super(skin, styleName);

		tabBind = new ArrayMap<T, C>();
		buttonGroup = new ButtonGroup();
		tabs = new Table();

		body = new Stack();
		this.left().top();

		super.add(tabs).left().expandX().fillX();
		this.row();
		super.add(body).expand().fill();
	}

	@Deprecated
	@Override
	public Cell<?> add() { throw new UnsupportedOperationException("Use addBinding(T, C) instead."); };

	@Deprecated
	@Override
	public Cell<?> add(Actor actor) { throw new UnsupportedOperationException("Use addBinding(T, C) instead."); }

	@Deprecated
	@Override
	public Cell<?> add(String text) { throw new UnsupportedOperationException("Use addBinding(T, C) instead."); }

	@Deprecated
	@Override
	public Cell<?> add(String text, String fontName, Color color) { throw new UnsupportedOperationException("Use addBinding(T, C) instead."); }

	@Deprecated
	@Override
	public Cell<?> add(String text, String fontName, String colorName) { throw new UnsupportedOperationException("Use addBinding(T, C) instead."); }

	@Deprecated
	@Override
	public Cell<?> add(String text, String labelStyleName) { throw new UnsupportedOperationException("Use addBinding(T, C) instead."); }

	@Deprecated
	@Override
	public void addActor(Actor actor)
	{
		if(actor instanceof HorizontalGroup || actor instanceof Stack)
			super.addActor(actor);
		else
			throw new UnsupportedOperationException("Use addBinding(T, C) instead.");
	}

	@Deprecated
	@Override
	public void addActorAfter(Actor actorAfter, Actor actor) { throw new UnsupportedOperationException("Use addBinding(T, C) instead."); }

	@Deprecated
	@Override
	public void addActorAt(int index, Actor actor) { throw new UnsupportedOperationException("Use addBinding(T, C) instead."); }

	@Deprecated
	@Override
	public void addActorBefore(Actor actorBefore, Actor actor) { throw new UnsupportedOperationException("Use addBinding(T, C) instead."); }

	/**
	 * Add the specified {@link T tab} bound to the specified {@link C container}.
	 * 
	 * @param tab the {@link T tab} to add
	 * @param container the {@link C container} to add
	 * 
	 * @return Returns the Tab's cell in the upper table.
	 */
	public Cell<?> addBinding(T tab, C container)
	{
		tabBind.put(tab, container);
		buttonGroup.add(tab);

		body.add(container);

		setCurrentTab(tab);
		return tabs.add(tab);
	}

	/**
	 * Remove the specified {@link T tab}.
	 * 
	 * @param tab the {@link T tab} to remove
	 */
	public void removeTab(T tab)
	{
		T _tab = null;
		int _index = tabBind.indexOfKey(tab);
		if(_index + 1 < tabBind.size)
			_tab = tabBind.getKeyAt(_index + 1);
		else if(_index - 1 >= 0)
			_tab = tabBind.getKeyAt(_index - 1);

		buttonGroup.remove(tab);
		tabBind.getValueAt(_index).remove();
		tabBind.removeIndex(_index);
		tab.remove();

		setCurrentTab(_tab);
	}

	@Deprecated
	@Override
	public boolean removeActor(Actor actor) { throw new UnsupportedOperationException("Use removeTab(T) instead."); }

	private void hideAllContainer()
	{
		for (C _cont : tabBind.values())
			_cont.setVisible(false);
	}

	/**
	 * Get all {@link T tabs}s of this {@link TabPane}.
	 * 
	 * @return all {@link T tabs}
	 */
	public ArrayMap<T, C> getTabs()
	{
		return tabBind;
	}
	
	/**
	 * Get the selected {@link C container}.
	 * 
	 * @return the selected {@link C container}
	 */
	public C getCurrentContainer() {
		return currentContainer;
	}

	/**
	 * Get the selected {@link T tab}.
	 * 
	 * @return the selected {@link T tab}
	 */
	public T getCurrentTab()
	{
		return currentTab;
	}

	/**
	 * Set the selected {@link T tab}.
	 * 
	 * @param currentTab the {@link T tab} to set selected
	 */
	public void setCurrentTab(T currentTab)
	{
		this.currentTab = currentTab;

		if(currentTab == null)
			return;

		currentContainer = tabBind.get(currentTab);

		hideAllContainer();
		tabBind.get(currentTab).setVisible(true);
		currentTab.setChecked(true);
	}

	/**
	 * Set the selected {@ink T tab}.
	 * 
	 * @param index the index of the {@link T tab}
	 */
	public void setCurrentTab(int index)
	{
		setCurrentTab(tabBind.getKeyAt(index));
	}

	/**
	 * Get the {@link C container} at the specified index.
	 * Start from 0.
	 * 
	 * @param index position of the {@link T tab} of the {@link C container}
	 * @return the {@link C container} at the specified index
	 */
	public C getTab(int index)
	{
		return tabBind.getValueAt(index);
	}

	/**
	 * Get the first {@link C container} of the specified type.
	 * 
	 * @param clazz the type of the searched {@link C container}
	 * @return the first found {@link C container} of the searched type or null if nothing is found
	 */
	public <A extends C> A getTab(Class<A> clazz)
	{
		for(C _container : tabBind.values())
		{
			try {
				A _cast = clazz.cast(_container);

				return _cast;
			} catch(ClassCastException e)
			{

			}
		}
		return null;
	}
}

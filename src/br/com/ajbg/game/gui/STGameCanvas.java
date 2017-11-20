package br.com.ajbg.game.gui;

import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.lcdui.Graphics;

import br.com.ajbg.game.model.GameObject;
import br.com.ajbg.screentrace.STElement;
import br.com.ajbg.screentrace.lcdui.STCanvas;

public abstract class STGameCanvas extends STCanvas implements GameObject {
	// Hierarquia de objetos
	private Vector childs;

	// Tempo e espera antes de redesenhar
	private int deltaTime;
	// Cor de fundo dessa tela
	private int red, green, blue;

	public STGameCanvas (STElement parent) {
		super (parent);
		childs = new Vector ();
		deltaTime = 33;
		red = green = blue = 255;
	}

	public final void setFrameRate (int frameRate) {
		deltaTime = 1000 / frameRate;
	}

	public final void setBackgroundColor (int r, int g, int b) {
		red = r;
		green = g;
		blue = b;
	}

	public final void onUpdate (int deltaTime) {
		for (Enumeration e=childs.elements (); e.hasMoreElements (); ) {
			GameObject go = (GameObject) e.nextElement ();
			go.onUpdate (deltaTime);
		}
	}

	public final void onPaint (Graphics g) {
		g.setColor (red, green, blue);
		g.fillRect (0, 0, getWidth (), getHeight ());
		for (Enumeration e=childs.elements (); e.hasMoreElements (); ) {
			GameObject go = (GameObject) e.nextElement ();
			go.onPaint (g);
		}
	}

	public final void paint (Graphics g) {
		onUpdate (deltaTime);
		onPaint (g);

		try {
			Thread.sleep (deltaTime);
		} catch (InterruptedException e) { }
		repaint ();
	}

	public final void onAction (int action) {
		for (Enumeration e=childs.elements (); e.hasMoreElements (); ) {
			GameObject go = (GameObject) e.nextElement ();
			go.onAction (action);
		}
	}

	protected final void keyPressed (int key) {
		onAction (getGameAction (key));
	}

	public final boolean onPointerAction (int x, int y) {
		boolean ret = false;
		for (Enumeration e=childs.elements (); e.hasMoreElements (); ) {
			GameObject go = (GameObject) e.nextElement ();
			if (ret = go.onPointerAction (x, y))
				break;
		}
		return ret;
	}

	protected final void pointerReleased(int x, int y) {
		onPointerAction(x, y);
	}

	protected abstract void gameEvent (int event, int value);

	public final void onGameEvent(int event, int value) {
		gameEvent (event, value);
		for (Enumeration e=childs.elements (); e.hasMoreElements (); ) {
			GameObject go = (GameObject) e.nextElement ();
			go.onGameEvent (event, value);
		}
	}

	public final void notificate (int event, int value) {
		onGameEvent (event, value);
	}

	public final GameObject getParent() {
		return null;
	}

	public final void setParent(GameObject parent) { }

	public Enumeration listChilds() {
		return childs.elements();
	}

	public final void addChild(GameObject child, int position) {
		if (position < 0)
			childs.addElement(child);
		else
			childs.insertElementAt(child, position);

		child.setParent(this);
	}

	public final void removeChild(GameObject child) {
		childs.removeElement(child);
	}

	public final void removeChild(int position) {
		if (position < 0)
			childs.removeElement(childs.lastElement());
		else
			childs.removeElementAt(position);
	}
}

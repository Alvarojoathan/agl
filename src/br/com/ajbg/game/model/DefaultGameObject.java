package br.com.ajbg.game.model;

import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.lcdui.Graphics;

// Falta implementacao de deteccao de colisao, animacao com frames e
// transformacao (rotacao)
public abstract class DefaultGameObject implements GameObject {
	// Hierarquia de objetos
	private GameObject parent;
	private Vector childs;

	// Posisao desse objeto
	private int x, y;
	// Tamanho desse objeto
	private int width, height;

	// Constroi o objeto com o tamanho especificado, na posicao (0, 0)
	public DefaultGameObject (int width, int height) {
		this (0, 0, width, height);
	}

	// Constroi o objeto com o tamanho especificado, na posicao especificada
	public DefaultGameObject (int x, int y, int width, int height) {
		parent = null;
		childs = new Vector ();

		this.x = x;
		this.y = y;

		this.width = width;
		this.height = height;
	}

	public final int getX () {
		return x;
	}

	public final int getY () {
		return y;
	}

	public final int getWidth () {
		return width;
	}

	public final int getHeight () {
		return height;
	}

 	public final void setPosition (int x, int y) {
		this.x = x;
		this.y = y;
	}

	public final void move (int x, int y) {
		this.x += x;
		this.y += y;
	}

	// Metodo chamado antes de paint para que o objeto atualize sua posicao
	protected abstract void update (int deltaTime);

	public final void onUpdate (int deltaTime) {
		update (deltaTime);
		for (Enumeration e=childs.elements (); e.hasMoreElements (); ) {
			GameObject go = (GameObject) e.nextElement ();
			go.onUpdate (deltaTime);
		}
	}

	// Metodo chamado para redesenhar o objeto
	protected abstract void paint (Graphics g);

	public final void onPaint (Graphics g) {
		g.translate (x, y);
		paint (g);
		for (Enumeration e=childs.elements (); e.hasMoreElements (); ) {
			GameObject go = (GameObject) e.nextElement ();
			go.onPaint (g);
		}
		g.translate (-x, -y);
	}

	// Metodo chamado quando o jogador realiza alguma acao nas teclas
	protected abstract void gameAction (int action);

	public final void onAction (int action) {
		gameAction (action);
		for (Enumeration e=childs.elements (); e.hasMoreElements (); ) {
			GameObject go = (GameObject) e.nextElement ();
			go.onAction (action);
		}
	}

	// Metodo chamado quando o jogador realiza alguma acao na tela
	protected abstract void pointerAction (int x, int y);

	public final boolean onPointerAction (int x, int y) {
		boolean ret = false;
		for (Enumeration e=childs.elements (); e.hasMoreElements (); ) {
			GameObject go = (GameObject) e.nextElement ();
			if (ret = go.onPointerAction (x-getX (), y-getY ()))
				break;
		}
		if (!ret && x > getX () && x < getWidth ()
				&& y > getY () && y < getHeight ()) {
			pointerAction (x, y);
			ret = true;
		}
		return ret;
	}

	// Metodo chamado quando algum outro objeto notifica um evento
	protected abstract void gameEvent (int event, int value);

	public final void onGameEvent (int event, int value) {
		gameEvent (event, value);
		for (Enumeration e=childs.elements (); e.hasMoreElements (); ) {
			GameObject go = (GameObject) e.nextElement ();
			go.onGameEvent (event, value);
		}
	}

	public final void notificate (int event, int value) {
		parent.notificate (event, value);
	}

	public final GameObject getParent() {
		return parent;
	}

	public final void setParent (GameObject parent) {
		this.parent = parent;
	}

	public Enumeration listChilds() {
		return childs.elements();
	}

	public final void addChild (GameObject child, int position) {
		if (position < 0)
			childs.addElement (child);
		else
			childs.insertElementAt (child, position);

		child.setParent (this);
	}

	public final void removeChild (GameObject child) {
		if (!childs.removeElement (child))
			throw new IllegalArgumentException ("Not a child of this GameObject");
	}

	public final void removeChild (int position) {
		if (position < 0)
			childs.removeElement (childs.lastElement ());
		else
			childs.removeElementAt (position);
	}
}

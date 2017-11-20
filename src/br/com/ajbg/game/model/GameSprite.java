package br.com.ajbg.game.model;

import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

// Usa as implementacoes da classe Sprite para complementar DefaultGameObject
public abstract class GameSprite extends Sprite implements GameObject {
	// Hierarquia de objetos
	private GameObject parent;
	private Vector childs;

	// Constroi um GameSprite com a imagem na posicao (0, 0)
	public GameSprite (Image i) {
		super(i);

		childs = new Vector ();
		setPosition (0, 0);
	}

	// Constroi um GameSprite animado com os frames da imagem
	public GameSprite (Image i, int width, int height) {
		super (i, width, height);

		childs = new Vector ();
		setPosition (0, 0);
	}

	public GameSprite (GameSprite gs) {
		super (gs);
		childs = new Vector ();
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

	public final void onPaint (Graphics g) {
		paint (g);
		g.translate (getX (), getY ());
		for (Enumeration e=childs.elements (); e.hasMoreElements (); ) {
			GameObject go = (GameObject) e.nextElement ();
			go.onPaint (g);
		}
		g.translate (-getX (), -getY ());
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
		childs.removeElement (child);
	}

	public final void removeChild (int position) {
		if (position < 0)
			childs.removeElement (childs.lastElement ());
		else
			childs.removeElementAt (position);
	}
}

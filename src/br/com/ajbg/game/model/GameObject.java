package br.com.ajbg.game.model;

import java.util.Enumeration;

import javax.microedition.lcdui.Graphics;

public interface GameObject {
	// Metodo utilizado pela biblioteca para propagar, sobre toda a hierarquia
	// de objetos, a acao de atualizaca (nao sobreescreva)
	void onUpdate (int deltaTime);

	// Metodo utilizado pela biblioteca para propagar, sobre toda a hierarquia
	// de objetos, a acao de desenho (nao sobreescreva)
	void onPaint (Graphics g);

	// Metodo utilizado pela biblioteca para propagar, sobre toda a hierarquia
	// de objetos, a acao do jogador (nao sobreescreva)
	void onAction (int action);

	// Metodo utilizado pela biblioteca para propagar, sobre toda a hierarquia
	// de objetos, a acao do jogador (nao sobreescreva)
	boolean onPointerAction (int x, int y);

	// Metodo utilizado pela biblioteca para propagar, sobre toda a hierarquia
	// de objetos, um evento de outro objeto (nao sobreescreva)
	void onGameEvent (int event, int value);

	// Chamado pelos objetos para notificar todos os outros sobre algum evento
	// (nao sobreescreva)
	void notificate (int event, int value);

	/**
	 * @return O GameObject pai desse
	 */
	GameObject getParent ();

	/**
	 * @param parent O GameObject que passara a ser o pai desse
	 */
	void setParent (GameObject parent);

	/**
	 * Lista todos os GameObject que sao filhos do atual
	 * 
	 * @return Um Enumeration com todos os filhos do objeto atual
	 */
	Enumeration listChilds ();

	/**
	 * Adiciona um GameObject como filho desse na hierarquia de
	 * objetos
	 * 
	 * @param child GameObject que sera adicionado
	 * @param position posicao no vetor de filhos em que o novo filho
	 * sera adicionado, caso < 0 sera adicionado no final e caso == 0
	 * insere no inicio
	 * 
	 * @throws ArrayIndexOutOfBoundsException se position maior que
	 * tamanho do vetor de filhos
	 */
	void addChild (GameObject child, int position);

	/**
	 * Remove o filho especificado do vetor de filhos desse
	 * GameObject, removendo-o da hierarquia de objetos
	 * 
	 * @param child filho a ser removido
	 * 
	 * @throws IllegalArgumentException se o filho especificado nao
	 * estiver no vetor de filhos
	 */
	void removeChild (GameObject child);

	/**
	 * Remove o GameObject do vetor de filhos que estiver na poscao
	 * indicada
	 * 
	 * @param position a posicao de onde remover o filho, caso menor
	 * que zero remove o ultimo elemento do vetor de fihos
	 * 
	 * @throwsArrayIndexOutOfBoundsException se position maior que
	 * tamanho do vetor de filhos
	 */
	void removeChild (int position);
}

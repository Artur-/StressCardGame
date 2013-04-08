package org.vaadin.artur.stresscardgame.engine;

/**
 * Exception which indicates the play cannot be done right now. Most likely
 * caused by another player doing a move right before which invalidates this
 * move.
 */
public class IllegalPlayException extends Exception {

}

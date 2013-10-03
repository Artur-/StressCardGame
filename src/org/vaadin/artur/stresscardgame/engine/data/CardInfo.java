package org.vaadin.artur.stresscardgame.engine.data;

import java.io.Serializable;

import org.vaadin.artur.playingcards.client.ui.Suite;

public class CardInfo implements Serializable {
    Suite suite;
    int rank;

    public CardInfo(Suite suite, int rank) {
        setSuite(suite);
        setRank(rank);
    }

    public Suite getSuite() {
        return suite;
    }

    public void setSuite(Suite suite) {
        this.suite = suite;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CardInfo)) {
            return false;
        }

        CardInfo other = (CardInfo) obj;
        return other.getSuite() == getSuite() && other.getRank() == getRank();
    }

    @Override
    public int hashCode() {
        return (getSuite() + "," + getRank()).hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [suite=" + getSuite() + ", rank="
                + getRank() + "]";
    }
}

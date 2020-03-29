package com.bergdavi.onlab.gameservice.service.delegate.chess.dto;

/**
 * ChessTurn
 */
public class ChessTurn {
    Integer fromX;
    Integer fromY;
    Integer toX;
    Integer toY;
    Boolean offerDraw = false;
    Boolean forfeit = false;

    public ChessTurn() {
    }

    public ChessTurn(Integer fromX, Integer fromY, Integer toX, Integer toY) {
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
    }

    public Integer getFromX() {
        return this.fromX;
    }

    public void setFromX(Integer fromX) {
        this.fromX = fromX;
    }

    public Integer getFromY() {
        return this.fromY;
    }

    public void setFromY(Integer fromY) {
        this.fromY = fromY;
    }

    public Integer getToX() {
        return this.toX;
    }

    public void setToX(Integer toX) {
        this.toX = toX;
    }

    public Integer getToY() {
        return this.toY;
    }

    public void setToY(Integer toY) {
        this.toY = toY;
    }


    public Boolean isOfferDraw() {
        return this.offerDraw;
    }

    public Boolean getOfferDraw() {
        return this.offerDraw;
    }

    public void setOfferDraw(Boolean offerDraw) {
        this.offerDraw = offerDraw;
    }

    public Boolean isForfeit() {
        return this.forfeit;
    }

    public Boolean getForfeit() {
        return this.forfeit;
    }

    public void setForfeit(Boolean forfeit) {
        this.forfeit = forfeit;
    }

}

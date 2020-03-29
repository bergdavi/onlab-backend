package com.bergdavi.onlab.gameservice.service.delegate.chess.figures;

/**
 * Figure
 */
public class Figure {    
    private Color color;
    private FigureType type;

    public Figure() {
    }

    public Figure(Color color, FigureType type) {
        this.color = color;
        this.type = type;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public FigureType getType() {
        return this.type;
    }

    public void setType(FigureType type) {
        this.type = type;
    }
}

package com.bergdavi.onlab.gameservice.service.delegate.chess.figures;

/**
 * Color
 */
public enum Color {
    BLACK, WHITE;

    public static Color invert(Color color) {
        return color == WHITE ? BLACK : WHITE;
    }
}
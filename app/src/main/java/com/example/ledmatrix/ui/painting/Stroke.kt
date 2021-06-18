package com.example.ledmatrix.ui.painting
import android.graphics.Color
import android.graphics.Path


class Stroke(//color of the stroke
    var color: Int, //width of the stroke
    var strokeWidth: Int, path: Path
) {
    //a Path object to represent the path drawn
    var path: Path

    //constructor to initialise the attributes
    init {
        color=color
        strokeWidth = strokeWidth
        this.path = path
    }
}
package com.javafx.addressconverter.util;

import javafx.scene.control.Label;

import java.io.File;

public class Util {

    public void resetFileSelection(File file, Label label) {
        file = null;
        label.setText("No file selected");
    }
}

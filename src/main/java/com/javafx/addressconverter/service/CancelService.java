package com.javafx.addressconverter.service;

import com.javafx.addressconverter.util.Util;
import javafx.scene.control.Label;

import java.io.File;

public class CancelService {

    private final Util util = new Util();

    public void cancel(File file, Label label) {
        util.resetFileSelection(file, label);
    }
}

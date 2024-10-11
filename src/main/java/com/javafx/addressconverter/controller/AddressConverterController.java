package com.javafx.addressconverter.controller;

import com.javafx.addressconverter.service.CancelService;
import com.javafx.addressconverter.service.ConvertService;
import com.javafx.addressconverter.service.FileUploadService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.File;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class AddressConverterController {

    private static File fileData = null;

    private static final Logger log = Logger.getLogger(AddressConverterController.class.getName());

    private final CancelService cancelService = new CancelService();
    private final FileUploadService fileUploadService = new FileUploadService();
    private final ConvertService convertService = new ConvertService();

    @FXML
    private Label fileNameLabel;

    @FXML
    protected void onCancelClick() {
        handleAction("Cancel", event -> cancelService.cancel(fileData, fileNameLabel), null);
    }

    @FXML
    protected void onFileUploadClick(ActionEvent event) {
        handleAction("FileUpload", e -> {
            fileData = fileUploadService.getFile(event, fileData, fileNameLabel);
        }, event);
    }

    @FXML
    protected void onConvertClick() {
        handleAction("Convert", e -> convertService.convertAddress(fileData, fileNameLabel),null);
    }



    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void handleAction(String action, Consumer<ActionEvent> task, ActionEvent event) {
        log.info(action + " clicked.");
        task.accept(event);
        log.info(action + " completed.");
    }
}
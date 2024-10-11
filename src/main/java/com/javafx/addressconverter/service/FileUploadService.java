package com.javafx.addressconverter.service;

import com.javafx.addressconverter.util.Util;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

public class FileUploadService {

    private static final Logger log = Logger.getLogger(FileUploadService.class.getName());
    private final Util util = new Util();

    private static File fileData;

    public File getFile(ActionEvent event, File file, Label label) {
        File selectedFile = showFileChooser((Button) event.getSource());
        validateSelectedFile(selectedFile, label);
        handleFileSelection(selectedFile, label);
        return selectedFile;
    }



    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private File showFileChooser(Button button) {
        FileChooser fileChooser = createFileChooser();
        Stage stage = (Stage) button.getScene().getWindow();
        return fileChooser.showOpenDialog(stage);
    }

    private FileChooser createFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");

        List<String> excelFileExtensions = List.of("*.xlsx", "*.xls", "*.xlsm", "*.xlsb", "*.xltx");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Excel Files", excelFileExtensions)
        );

        return fileChooser;
    }

    private void handleFileSelection(File selectedFile, Label label) {
        fileData = selectedFile;
        label.setText(fileData.getName());
    }

    private void validateSelectedFile(File selectedFile, Label label) {
        if (selectedFile == null) {
            util.resetFileSelection(fileData, label);
            log.warning("No file selected.");
            throw new IllegalArgumentException("No file selected for ");
        }
    }
}

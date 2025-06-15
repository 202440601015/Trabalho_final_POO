package ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import domain.*;

public class FileInfoViewer extends Application {

    private TextArea infoTextArea;
    private TextField filePathField;
    private Archive doc1;

    @Override
    public void start(Stage primaryStage) {
        Document documentWorker = new Document();

        primaryStage.setTitle("Visualizador de Informações de Arquivo");

        // Componentes da interface
        Button selectFileButton = new Button("Selecionar Arquivo");
        filePathField = new TextField();
        filePathField.setEditable(false);
        
        Label infoLabel = new Label("Informações do Arquivo:");
        infoTextArea = new TextArea();
        infoTextArea.setEditable(false);
        infoTextArea.setWrapText(true);
        
        Button btnChangeMetadata = new Button("Alterar MetaDados");
        Button btnChangeType = new Button("Funcionalidade 2");
        Button btnFeature3 = new Button("Funcionalidade 3");
        
        btnFeature3.setDisable(true);

        // Layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15, 15, 15, 15));
        grid.setVgap(10);
        grid.setHgap(10);

        grid.add(selectFileButton, 0, 0);
        grid.add(filePathField, 1, 0);
        grid.add(infoLabel, 0, 1, 2, 1);
        grid.add(infoTextArea, 0, 2, 2, 1);
        
        HBox buttonBox = new HBox(10, btnChangeMetadata, btnChangeType, btnFeature3);
        grid.add(buttonBox, 0, 3, 2, 1);

        selectFileButton.setOnAction(e -> selectFile(primaryStage));
        
        // Adicionando ação para o botão de alterar metadados
        btnChangeMetadata.setOnAction(e -> showMetadataDialog(doc1));

        btnChangeType.setOnAction(e -> changeTypeDialog(doc1));

        Scene scene = new Scene(grid, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void changeTypeDialog( Archive doc1){
        Picture pictureWorker = new Picture();
        System.out.println(doc1.getType());

         // Criar nova janela de diálogo
        Stage dialog = new Stage();
        dialog.setTitle("Alterar formato1");

        // Criar campos de texto
        TextField param1Field = new TextField();
        param1Field.setPromptText("Tag a ser alterada");

        // Botão de ação
        Button applyButton = new Button("Converter");
        applyButton.setOnAction(e -> {
            String param1 = param1Field.getText();
    
            
            // Aqui você chamaria o método que processa os parâmetros
            System.out.println(param1);
            pictureWorker.changeType(doc1, param1);
        
            
            // Fecha o diálogo
            dialog.close();
        });

        // Layout do diálogo
        VBox dialogVBox = new VBox(10);
        dialogVBox.setPadding(new Insets(15));
        dialogVBox.getChildren().addAll(
            new Label("Digite o novo formato"),
            param1Field,
            applyButton
        );

        Scene dialogScene = new Scene(dialogVBox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();
        

    }

    private void showMetadataDialog(Archive doc1) {
        Document documentWorker = new Document();
        if (doc1 == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aviso");
            alert.setHeaderText("Nenhum arquivo selecionado");
            alert.setContentText("Por favor, selecione um arquivo primeiro.");
            alert.showAndWait();
            return;
        }

        // Criar nova janela de diálogo
        Stage dialog = new Stage();
        dialog.setTitle("Alterar Metadados");

        // Criar campos de texto
        TextField param1Field = new TextField();
        param1Field.setPromptText("Tag a ser alterada");
        TextField param2Field = new TextField();
        param2Field.setPromptText("Novo Valor da Tag");

        // Botão de ação
        Button applyButton = new Button("Aplicar");
        applyButton.setOnAction(e -> {
            String param1 = param1Field.getText();
            String param2 = param2Field.getText();
            
            // Aqui você chamaria o método que processa os parâmetros
            System.out.println(param1);
            System.out.println(param2);
            documentWorker.changeMetaData(doc1, param1, param2);
            
            // Atualiza as informações exibidas
            displayFileInfo();
            
            // Fecha o diálogo
            dialog.close();
        });

        // Layout do diálogo
        VBox dialogVBox = new VBox(10);
        dialogVBox.setPadding(new Insets(15));
        dialogVBox.getChildren().addAll(
            new Label("Digite os novos parâmetros:"),
            param1Field,
            param2Field,
            applyButton
        );

        Scene dialogScene = new Scene(dialogVBox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    private void processMetadataParameters(String param1, String param2) {
        // Aqui você implementaria a lógica para processar os parâmetros
        // Por exemplo, alterar os metadados do arquivo
        
        // Exemplo simples apenas para demonstração
        System.out.println("Parâmetros recebidos:");
        System.out.println("Parâmetro 1: " + param1);
        System.out.println("Parâmetro 2: " + param2);
        
        // Você poderia chamar algum método do seu Document ou Archive aqui
        // documentWorker.changeMetadata(doc1, param1, param2);
        
        // Mostrar mensagem de sucesso
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText("Metadados alterados");
        alert.setContentText("Os parâmetros foram processados com sucesso.");
        alert.showAndWait();
    }

    private void selectFile(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecionar Arquivo");
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        doc1 = new Archive(selectedFile.getAbsolutePath());
        
        if (selectedFile != null) {
            filePathField.setText(selectedFile.getAbsolutePath());
            displayFileInfo();
        }
    }

    private void displayFileInfo() {
        if (doc1 != null) {
            try {
                Document documentWorker = new Document();
                doc1.setMetaData(documentWorker.pickMetaData(doc1));
                doc1.setType(documentWorker.pickType(doc1));
                
                infoTextArea.setText(doc1.getMetaData());
            } catch (Exception e) {
                infoTextArea.setText("Erro ao ler informações do arquivo: " + e.getMessage());
            }
        }
    }

    private String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " bytes";
        if (bytes < 1024 * 1024) return String.format("%.2f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.2f MB", bytes / (1024.0 * 1024));
        return String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024));
    }

    private String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "Nenhuma" : filename.substring(dotIndex + 1);
    }

    public static void LauchFileSlecetor() {
        launch();
    }
}
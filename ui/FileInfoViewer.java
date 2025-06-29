package ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import domain.*;
import technicalservices.*;
import java.util.List;
import javafx.scene.control.cell.PropertyValueFactory;

public class FileInfoViewer extends Application {

    private TextArea infoTextArea;
    private TextField filePathField;
    private Archive doc1;
    private ArchiveController controller = new ArchiveController();

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Visualizador de Informações de Arquivo");

        Button selectFileButton = new Button("Selecionar Arquivo");
        filePathField = new TextField();
        filePathField.setEditable(false);
        
        Label infoLabel = new Label("Informações do Arquivo:");
        infoTextArea = new TextArea();
        infoTextArea.setEditable(false);
        infoTextArea.setWrapText(true);
        
        Button btnChangeMetadata = new Button("Alterar MetaDados");
        Button btnChangeType = new Button("Converter formato");
        Button btnFeature3 = new Button("Banco de Dados");
        
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
        
        btnChangeMetadata.setOnAction(e -> showMetadataDialog(doc1));
        btnFeature3.setOnAction(e -> showDatabaseOperationsDialog(doc1));
        btnChangeType.setOnAction(e -> changeTypeDialog(doc1));

        Scene scene = new Scene(grid, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void changeTypeDialog( Archive doc1){
        Picture pictureWorker = new Picture();
        System.out.println(doc1.getType());

        Stage dialog = new Stage();
        dialog.setTitle("Alterar formato1");

        TextField param1Field = new TextField();
        param1Field.setPromptText("Tag a ser alterada");

        Button applyButton = new Button("Converter");
        applyButton.setOnAction(e -> {
            
            String param1 = param1Field.getText();
    
            System.out.println(param1);
            pictureWorker.changeType(doc1, param1);
    
            dialog.close();
        });

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

        Stage dialog = new Stage();
        dialog.setTitle("Alterar Metadados");

        TextField param1Field = new TextField();
        param1Field.setPromptText("Tag a ser alterada");
        TextField param2Field = new TextField();
        param2Field.setPromptText("Novo Valor da Tag");

        Button applyButton = new Button("Aplicar");
        applyButton.setOnAction(e -> {
            String param1 = param1Field.getText();
            String param2 = param2Field.getText();
            
            System.out.println(param1);
            System.out.println(param2);
            documentWorker.changeMetaData(doc1, param1, param2);
            
            displayFileInfo();
            
            dialog.close();
        });

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

    public static void LauchFileSlecetor() {
        launch();
    }

    private void showDatabaseOperationsDialog(Archive doc1) {
    if (doc1 == null) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText("Nenhum arquivo selecionado");
        alert.setContentText("Por favor, selecione um arquivo primeiro.");
        alert.showAndWait();
        return;
    }

    Stage dialog = new Stage();
    dialog.setTitle("Operações com Banco de Dados");

    Button saveButton = new Button("Salvar no Banco");
    Button queryButton = new Button("Consultar Banco");
    Button deleteButton = new Button("Apagar do Banco");
    Button updateButton = new Button("Atualizar no Banco");

    saveButton.setOnAction(e -> saveToDatabase(doc1, dialog));
    queryButton.setOnAction(e -> queryDatabase(dialog));
    deleteButton.setOnAction(e -> deleteFromDatabase(doc1, dialog));
    updateButton.setOnAction(e -> updateFromDatabase(doc1));

    VBox dialogVBox = new VBox(10);
    dialogVBox.setPadding(new Insets(15));
    dialogVBox.getChildren().addAll(
        new Label("Selecione a operação desejada:"),
        saveButton,
        queryButton,
        deleteButton
    );

    Scene dialogScene = new Scene(dialogVBox, 300, 200);
    dialog.setScene(dialogScene);
    dialog.show();
}

private void updateFromDatabase( Archive doc1){
    try{
        controller.update(doc1);
    } catch(Exception e){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText("Falha ao salvar no banco");
        alert.setContentText("Erro: " + e.getMessage());
        alert.showAndWait();
    }
}

private void saveToDatabase(Archive doc1, Stage dialog) {
    try {
        controller.save(doc1);
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText("Arquivo salvo no banco");
        alert.setContentText("As informações do arquivo foram salvas com sucesso.");
        alert.showAndWait();
        
    } catch (Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText("Falha ao salvar no banco");
        alert.setContentText("Erro: " + e.getMessage());
        alert.showAndWait();
    }
}

private void queryDatabase(Stage dialog) {
    try {

        ArchiveDAO dbMN = new ArchiveDAO();
        
        List<Archive> archives = dbMN.listAll();
        
        Stage resultStage = new Stage();
        resultStage.setTitle("Arquivos no Banco de Dados");
        
        TableView<Archive> table = new TableView<>();
        
        TableColumn<Archive, String> pathCol = new TableColumn<>("Caminho");
        pathCol.setCellValueFactory(new PropertyValueFactory<>("path"));
        
        TableColumn<Archive, String> typeCol = new TableColumn<>("Tipo");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        
        table.getColumns().addAll(pathCol, typeCol);
        table.getItems().addAll(archives);
        
        VBox vbox = new VBox(table);
        Scene scene = new Scene(vbox, 400, 400);
        resultStage.setScene(scene);
        resultStage.show();
        
    } catch (Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText("Falha ao consultar banco");
        alert.setContentText("Erro: " + e.getMessage());
        alert.showAndWait();
    }
}

private void deleteFromDatabase(Archive doc1, Stage dialog) {
    try {
        
        boolean deleted = controller.delete(doc1);
        
        if (deleted) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucesso");
            alert.setHeaderText("Arquivo removido");
            alert.setContentText("O arquivo foi removido do banco de dados.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aviso");
            alert.setHeaderText("Arquivo não encontrado");
            alert.setContentText("O arquivo não foi encontrado no banco de dados.");
            alert.showAndWait();
        }
        
    } catch (Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText("Falha ao remover do banco");
        alert.setContentText("Erro: " + e.getMessage());
        alert.showAndWait();
    }
}

}
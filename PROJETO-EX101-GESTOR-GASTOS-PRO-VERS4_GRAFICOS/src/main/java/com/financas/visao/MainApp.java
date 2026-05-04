package com.financas.visao;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/financas/visao/MainLayout.fxml"));      
        Scene scene = new Scene(root);
        String css = getClass().getResource("/com/financas/estilo/estilo-moderno.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setTitle("Gestor de Gastos PRO - Versão 4.0");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
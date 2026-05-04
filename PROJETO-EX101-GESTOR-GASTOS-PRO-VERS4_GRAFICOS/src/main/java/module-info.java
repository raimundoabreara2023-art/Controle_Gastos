module com.financas {
    // Necessário para o JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    
    // Necessário para conexão com Banco de Dados
    requires java.sql;

    // Abre os pacotes para o FXML e Reflexão
    opens com.financas.visao to javafx.fxml;
    
    // ADICIONE ESTA LINHA ABAIXO:
    opens com.financas.visao.graficos to javafx.fxml; 
    
    opens com.financas.modelo to javafx.base; // Permite que TableView leia os DTOs
    
    // Exporta os pacotes para serem usados pelo sistema
    exports com.financas.visao;
    exports com.financas.visao.graficos; // ADICIONE ESTA LINHA TAMBÉM
    exports com.financas.modelo;
    exports com.financas.dados;
}
package com.financas.dados;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDAO {

    // Configurações do Banco de Dados
    private static final String URL = "jdbc:postgresql://localhost:5432/controle_gasto";
    private static final String USUARIO = "postgres";
    private static final String SENHA = "Rama2022@";

    /**
     * Estabelece uma conexão com o banco de dados PostgreSQL.
     * @return Connection objeto de conexão ativa.
     * @throws SQLException caso ocorra erro na conexão.
     */
    public static Connection conectar() throws SQLException {
        try {
            // No Java moderno o Driver é carregado automaticamente, 
            // mas é boa prática garantir que a classe existe.
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver do PostgreSQL não encontrado: " + e.getMessage());
        } catch (SQLException e) {
            throw new SQLException("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }
}
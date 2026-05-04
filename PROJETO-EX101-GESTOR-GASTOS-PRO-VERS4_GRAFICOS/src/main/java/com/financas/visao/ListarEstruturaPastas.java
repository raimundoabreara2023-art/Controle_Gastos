package com.financas.visao;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Stream;
import java.nio.file.Files;

public class ListarEstruturaPastas {

    public static void main(String[] args) {
        File rootDir = new File("C:/Users/raimu/Faculdade/3_semestre/programacao_orientada_objeto/PROJETO-EX101-GESTOR-GASTOS-PRO-VERS4_GRAFICOS");

        File saida = new File("estrutura_projeto.txt");

        try (PrintWriter writer = new PrintWriter(new FileWriter(saida))) {
            // --- Versão enxuta ---
            writer.println("=== VERSÃO ENXUTA (somente pastas e arquivos) ===\n");
            imprimirEstruturaEnxuta(rootDir, "", writer);

            // --- Versão detalhada ---
            writer.println("\n=== VERSÃO DETALHADA (com código) ===\n");
            imprimirEstruturaDetalhada(rootDir, "", writer);

            System.out.println("Estrutura gerada em: " + saida.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Apenas pastas e nomes de arquivos
    private static void imprimirEstruturaEnxuta(File dir, String indentacao, PrintWriter writer) {
        if (dir.isDirectory() && dir.getName().equals("target")) return;

        if (dir.isDirectory()) {
            writer.println(indentacao + "[DIR] " + dir.getName());
            File[] arquivos = dir.listFiles();
            if (arquivos != null) {
                for (File arquivo : arquivos) {
                    imprimirEstruturaEnxuta(arquivo, indentacao + "   ", writer);
                }
            }
        } else {
            if (dir.getName().equals("ListarEstruturaPastas.java") && dir.getParentFile().getName().equals("visao")) return;
            writer.println(indentacao + dir.getName());
        }
    }

    // Pastas, arquivos e conteúdo
    private static void imprimirEstruturaDetalhada(File dir, String indentacao, PrintWriter writer) {
        if (dir.isDirectory() && dir.getName().equals("target")) return;

        if (dir.isDirectory()) {
            writer.println(indentacao + "[DIR] " + dir.getName());
            File[] arquivos = dir.listFiles();
            if (arquivos != null) {
                for (File arquivo : arquivos) {
                    imprimirEstruturaDetalhada(arquivo, indentacao + "   ", writer);
                }
            }
        } else {
            if (dir.getName().equals("ListarEstruturaPastas.java") && dir.getParentFile().getName().equals("visao")) return;

            writer.println(indentacao + dir.getName());

            // Inclui conteúdo de arquivos relevantes
            if (dir.getName().endsWith(".java") ||
                dir.getName().endsWith(".xml") ||
                dir.getName().endsWith(".fxml") ||
                dir.getName().endsWith(".txt") ||
                dir.getName().endsWith(".css")) {

                try (Stream<String> linhas = Files.lines(dir.toPath())) {
                    linhas.forEach(linha -> writer.println(indentacao + "      " + linha));
                } catch (IOException e) {
                    writer.println(indentacao + "      [ERRO ao ler arquivo]");
                }
            }
        }
    }
}
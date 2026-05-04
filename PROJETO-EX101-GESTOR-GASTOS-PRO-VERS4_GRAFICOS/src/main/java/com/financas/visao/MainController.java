package com.financas.visao;

import com.financas.dados.CompraDAO;
import com.financas.modelo.*;
import com.financas.servico.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.fxml.Initializable;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    // =========================================================================
    // ATRIBUTOS FXML
    // =========================================================================
    @FXML private ComboBox<EstabelecimentoDTO> comboEstabelecimento;
    @FXML private ComboBox<String> comboPagamento;
    @FXML private DatePicker dpData;
    @FXML private TextField txtValorNota; 
    @FXML private Label lblTotalCompra;

    @FXML private ComboBox<MembroDTO> comboMembroItem; 
    @FXML private ComboBox<CategoriaDTO> comboCategoria;
    @FXML private ComboBox<SubcategoriaDTO> comboSubcategoria;
    @FXML private ComboBox<ProdutoDTO> comboProduto;
    @FXML private TextField txtQuantidade;
    @FXML private TextField txtValorUnitario;
    
    @FXML private TableView<TransacaoDetalheDTO> tabelaItens;
    @FXML private TableColumn<TransacaoDetalheDTO, String> colMembro;
    @FXML private TableColumn<TransacaoDetalheDTO, String> colProduto;
    @FXML private TableColumn<TransacaoDetalheDTO, Double> colQuantidade;
    @FXML private TableColumn<TransacaoDetalheDTO, Double> colValorUnitario;
    @FXML private TableColumn<TransacaoDetalheDTO, Void> colAcoes;

    // =========================================================================
    // SERVIÇOS E ESTADO
    // =========================================================================
    private MembroService membroService = new MembroService();
    private EstabelecimentoService estabService = new EstabelecimentoService();
    private CategoriaService categoriaService = new CategoriaService();
    private SubcategoriaService subcategoriaService = new SubcategoriaService();
    private ProdutoService produtoService = new ProdutoService();

    private ObservableList<TransacaoDetalheDTO> itensDaCompra = FXCollections.observableArrayList();
    private double totalGeral = 0.0;

    // =========================================================================
    // INICIALIZAÇÃO E CONFIGURAÇÃO
    // =========================================================================
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarTabela();
        carregarCombos();
        configurarListeners();

        // Estados iniciais
        comboCategoria.setDisable(true);
        comboSubcategoria.setDisable(true);
        comboPagamento.getItems().addAll("Dinheiro", "Cartão de Crédito", "Cartão de Débito", "Pix");
        lblTotalCompra.setText("R$ 0,00");
    }

    private void configurarTabela() {
        colMembro.setCellValueFactory(new PropertyValueFactory<>("nomeMembro"));
        colProduto.setCellValueFactory(new PropertyValueFactory<>("nomeProduto"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colValorUnitario.setCellValueFactory(new PropertyValueFactory<>("valorUnitario"));

        colAcoes.setCellFactory(param -> new TableCell<>() {
            private final Button btnExcluir = new Button("Excluir/Alterar");
            private final StackPane container = new StackPane(btnExcluir);

            {
                btnExcluir.getStyleClass().add("botao-excluir-tabela");
                btnExcluir.setOnAction(event -> {
                    TransacaoDetalheDTO item = getTableView().getItems().get(getIndex());
                    editarItemSelecionado(item);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(container);
                    container.setAlignment(javafx.geometry.Pos.CENTER);
                }
            }
        });

        tabelaItens.setItems(itensDaCompra);
    }

    private void carregarCombos() {
        try {
            comboMembroItem.setItems(FXCollections.observableArrayList(membroService.listarTodos()));
            comboEstabelecimento.setItems(FXCollections.observableArrayList(estabService.listarTodos()));
            comboCategoria.setItems(FXCollections.observableArrayList(categoriaService.listarTodos()));
            comboProduto.setItems(FXCollections.observableArrayList(produtoService.listarTodos()));
        } catch (Exception e) {
            exibirAlerta("Erro ao carregar listas: " + e.getMessage());
        }
    }

    private void configurarListeners() {
        // Filtro Categoria -> Subcategoria
        comboCategoria.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            if (novo != null) {
                try {
                    comboSubcategoria.setItems(FXCollections.observableArrayList(
                        subcategoriaService.listarPorCategoria(novo.getIdCategoria())
                    ));
                } catch (Exception e) {
                    exibirAlerta("Erro ao carregar subcategorias: " + e.getMessage());
                }
            } else {
                comboSubcategoria.getItems().clear();
            }
        });

        // Automação Produto -> Sub -> Categoria
        comboProduto.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            if (novo != null && novo.getSubcategoria() != null) {
                comboSubcategoria.setValue(novo.getSubcategoria());
                if (novo.getSubcategoria().getCategoria() != null) {
                    comboCategoria.setValue(novo.getSubcategoria().getCategoria());
                }
            }
        });

        // Listener para atualizar cores do total em tempo real
        txtValorNota.textProperty().addListener((obs, antigo, novo) -> atualizarTotal());
    }

    // =========================================================================
    // AÇÕES DE INTERFACE (BOTÕES)
    // =========================================================================
    @FXML
    void adicionarItem(ActionEvent event) {
        try {
            if (!validarCabecalho()) return;

            if (comboProduto.getValue() == null || comboMembroItem.getValue() == null || 
                txtQuantidade.getText().isEmpty() || txtValorUnitario.getText().isEmpty()) {
                exibirAlerta("Preencha todos os campos do item!");
                return;
            }

            TransacaoDetalheDTO item = new TransacaoDetalheDTO();
            item.setIdProduto(comboProduto.getValue().getIdProduto());
            item.setIdMembro(comboMembroItem.getValue().getIdMembro());
            item.setNomeMembro(comboMembroItem.getValue().getNome()); 
            item.setNomeProduto(comboProduto.getValue().getNome()); 
            item.setNomeCategoria(comboCategoria.getValue().getNome()); 
            item.setNomeSubcategoria(comboSubcategoria.getValue().getNome()); 
            item.setQuantidade(Double.parseDouble(txtQuantidade.getText().replace(",", ".")));
            item.setValorUnitario(Double.parseDouble(txtValorUnitario.getText().replace(",", "."))); 
            
            itensDaCompra.add(item);
            atualizarTotal();
            limparCamposItem();

        } catch (NumberFormatException e) {
            exibirAlerta("Quantidade e Valor devem ser números válidos.");
        }
    }

    @FXML
    void salvarCompra(ActionEvent event) {
        try {
            // 1. Validação do Valor da Nota
            String valorNotaStr = txtValorNota.getText().replace(",", ".");
            if (valorNotaStr.isEmpty()) {
                exibirAlerta("Por favor, informe o valor total da nota antes de finalizar.");
                return;
            }
            double valorNota = Double.parseDouble(valorNotaStr);

            // 2. Validação da Diferença (Soma dos itens vs Nota)
            if (Math.abs(totalGeral - valorNota) > 2.00) {
                exibirAlerta(String.format("Diferença entre Nota e Soma dos itens é superior a R$ 2,00."));
                return;
            }

            // 3. Montar o Objeto Mestre (Cabeçalho)
            CompraMestreDTO mestre = new CompraMestreDTO();
            
            if (comboEstabelecimento.getValue() != null) {
                mestre.setIdEstabelecimento(comboEstabelecimento.getValue().getIdEstabelecimento());
            } else {
                exibirAlerta("Selecione um estabelecimento!");
                return;
            }

            mestre.setValorTotal(totalGeral);
            
            // Usando o seu DatePicker 'dpData'
            LocalDate dataDoc = dpData.getValue() != null ? dpData.getValue() : LocalDate.now();
            mestre.setDataEmissao(dataDoc);
            
            // Usando o seu ComboBox 'comboPagamento'
            mestre.setFormaPagamento(comboPagamento.getValue() != null ? comboPagamento.getValue() : "DINHEIRO");

            // 4. Calcular o ID do Mês de Referência baseado na data
            int idMesCalculado = dataDoc.getMonthValue();

            // 5. PERSISTÊNCIA NO BANCO (A parte que estava faltando)
            // Certifique-se que o objeto compraDAO está instanciado na classe
            int idGerado = new CompraDAO().salvar(mestre); 

            if (idGerado > 0) {
                // 6. Salvar os Itens (Usando a sua lista 'itensDaCompra')
                CompraDAO daoItem = new CompraDAO();
                for (TransacaoDetalheDTO item : itensDaCompra) {
                    item.setIdCompra(idGerado);
                    item.setIdMesRef(idMesCalculado); // Vincula ao mês da data do cupom
                    
                    daoItem.salvarItem(item);
                }

                exibirInformativo("Compra salva com sucesso no PostgreSQL!");
                limparTudo(); 
            }

        } catch (NumberFormatException e) {
            exibirAlerta("Valor da nota inválido!");
        } catch (Exception e) {
            e.printStackTrace();
            exibirAlerta("Erro ao salvar no banco: " + e.getMessage());
        }
    }

    // =========================================================================
    // LÓGICA INTERNA E SUPORTE
    // =========================================================================
    private void editarItemSelecionado(TransacaoDetalheDTO item) {
        txtQuantidade.setText(String.valueOf(item.getQuantidade()).replace(".", ","));
        txtValorUnitario.setText(String.valueOf(item.getValorUnitario()).replace(".", ","));

        comboMembroItem.getItems().stream()
            .filter(m -> m.getIdMembro() == item.getIdMembro())
            .findFirst().ifPresent(m -> comboMembroItem.setValue(m));

        comboProduto.getItems().stream()
            .filter(p -> p.getIdProduto() == item.getIdProduto())
            .findFirst().ifPresent(p -> comboProduto.setValue(p));

        itensDaCompra.remove(item);
        atualizarTotal();
        comboMembroItem.requestFocus();
    }

    private void atualizarTotal() {
        totalGeral = itensDaCompra.stream().mapToDouble(TransacaoDetalheDTO::getValorUnitario).sum();
        lblTotalCompra.setText(String.format("R$ %.2f", totalGeral));

        try {
            String valorNotaStr = txtValorNota.getText().replace(",", ".");
            if (!valorNotaStr.isEmpty()) {
                double valorNota = Double.parseDouble(valorNotaStr);
                if (Math.abs(totalGeral - valorNota) > 2.00) { 
                    lblTotalCompra.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                } else {
                    lblTotalCompra.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                }
            }
        } catch (Exception e) {
            lblTotalCompra.setStyle("-fx-text-fill: #2c3e50;");
        }
    }

    private boolean validarCabecalho() {
        if (comboEstabelecimento.getValue() == null || dpData.getValue() == null || 
            comboPagamento.getValue() == null || txtValorNota.getText().trim().isEmpty()) {
            
            exibirAlerta("Preencha os dados da nota antes de lançar produtos!");
            if (comboEstabelecimento.getValue() == null) comboEstabelecimento.requestFocus();
            return false;
        }
        return true;
    }

    private void limparCamposItem() {
        txtQuantidade.clear();
        txtValorUnitario.clear();
        comboMembroItem.getSelectionModel().clearSelection();
        comboProduto.getSelectionModel().clearSelection();
        comboSubcategoria.setValue(null);
        comboCategoria.setValue(null);
        comboMembroItem.requestFocus(); 
    }

    private void limparTudo() {
        itensDaCompra.clear();
        txtValorNota.clear();
        limparCamposItem();
        comboEstabelecimento.getSelectionModel().clearSelection();
        dpData.setValue(null);
        comboPagamento.getSelectionModel().clearSelection();
        totalGeral = 0.0;
        lblTotalCompra.setText("R$ 0,00");
    }

    private void exibirAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void exibirInformativo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
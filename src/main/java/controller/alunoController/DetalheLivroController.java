package com.biblioqueue.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DetalheLivroController implements Initializable {

    @FXML private Label lblNomeUsuario;
    @FXML private Label lblTitulo;
    @FXML private Label lblAutor;
    @FXML private Label lblAno;
    @FXML private Label lblCategoria;
    @FXML private Label lblIsbn;
    @FXML private Label lblIdExemplar;
    @FXML private Label lblAnoPublicacao;
    @FXML private Label lblDisponibilidade;
    @FXML private Label lblDescricao;
    @FXML private Label lblEmprestimosAtivos;

    @FXML private HBox alertaBloqueado;
    @FXML private Button btnEmprestimo;
    @FXML private Button btnReserva;

    // Estado do usuário (injetar via sessão/singleton em produção)
    private boolean acessoBloqueado = true;
    private int emprestimosAtivos  = 2;
    private int limiteEmprestimos  = 3;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblNomeUsuario.setText("Ana Silva"); // vem da sessão
        lblEmprestimosAtivos.setText(
                "Empréstimos ativos: " + emprestimosAtivos + "/" + limiteEmprestimos);
    }

    /**
     * Chamado pelo CatalogoController antes de exibir a tela.
     */
    public void carregarLivro(String titulo, String autor, String ano,
                               String categoria, int disponivel) {
        lblTitulo.setText(titulo);
        lblAutor.setText(autor);
        lblAno.setText(ano);
        lblAnoPublicacao.setText(ano);
        lblCategoria.setText(categoria);

        // ISBN e ID fictícios — em produção viriam do banco
        lblIsbn.setText("978-0262033848");
        lblIdExemplar.setText("#COMP-001");
        lblDescricao.setText("Referência completa sobre " + titulo.toLowerCase() + ".");

        // Disponibilidade
        if (disponivel > 0) {
            lblDisponibilidade.setText(disponivel + " exemplar(es)");
            lblDisponibilidade.getStyleClass().setAll("detail-field-value-green");
        } else {
            lblDisponibilidade.setText("Indisponível");
            lblDisponibilidade.getStyleClass().setAll("loan-value-atrasado");
        }

        atualizarBotaoAcao(disponivel);
    }

    private void atualizarBotaoAcao(int disponivel) {
        if (acessoBloqueado) {
            // Mostra apenas o alerta vermelho
            alertaBloqueado.setVisible(true);
            alertaBloqueado.setManaged(true);
            btnEmprestimo.setVisible(false);
            btnEmprestimo.setManaged(false);
            btnReserva.setVisible(false);
            btnReserva.setManaged(false);
        } else if (disponivel > 0 && emprestimosAtivos < limiteEmprestimos) {
            // Pode emprestar
            alertaBloqueado.setVisible(false);
            alertaBloqueado.setManaged(false);
            btnEmprestimo.setVisible(true);
            btnEmprestimo.setManaged(true);
            btnReserva.setVisible(false);
            btnReserva.setManaged(false);
        } else {
            // Pode reservar
            alertaBloqueado.setVisible(false);
            alertaBloqueado.setManaged(false);
            btnEmprestimo.setVisible(false);
            btnEmprestimo.setManaged(false);
            btnReserva.setVisible(true);
            btnReserva.setManaged(true);
        }
    }

    @FXML
    private void onVoltar() {
        navegarPara("/fxml/Catalogo.fxml");
    }

    @FXML
    private void onEmprestimo() {
        System.out.println("Empréstimo solicitado para: " + lblTitulo.getText());
        // Aqui chama o serviço de empréstimo
    }

    @FXML
    private void onReserva() {
        System.out.println("Reserva solicitada para: " + lblTitulo.getText());
        // Aqui chama o serviço de reserva
    }

    @FXML private void onLogout() { navegarPara("/fxml/Login.fxml"); }

    @FXML private void onNavCatalogo()    { navegarPara("/fxml/Catalogo.fxml"); }
    @FXML private void onNavEmprestimos() { navegarPara("/fxml/Emprestimos.fxml"); }
    @FXML private void onNavReservas()    { navegarPara("/fxml/Reservas.fxml"); }

    private void navegarPara(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) lblTitulo.getScene().getWindow();
            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

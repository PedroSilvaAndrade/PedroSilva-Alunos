package com.biblioqueue.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ReservasController implements Initializable {

    @FXML private Label lblNomeUsuario;
    @FXML private Label lblContadorReservas;
    @FXML private VBox  emptyStateReservas;
    @FXML private VBox  listaReservasContainer;

    private static final int LIMITE_RESERVAS = 3;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Dados de exemplo (substituir por consulta ao banco)
    private final List<ReservaItem> reservas = new ArrayList<>(List.of(
        // new ReservaItem("Código Limpo", "#COMP-002", 1, LocalDate.of(2026, 4, 20)),
        // new ReservaItem("Algoritmos: Teoria e Prática", "#COMP-005", 3, LocalDate.of(2026, 4, 22))
    ));

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblNomeUsuario.setText("Ana Silva");
        atualizarContador();

        if (reservas.isEmpty()) {
            mostrarEstadoVazio();
        } else {
            mostrarListaReservas();
        }
    }

    private void atualizarContador() {
        lblContadorReservas.setText(reservas.size() + "/" + LIMITE_RESERVAS);
    }

    private void mostrarEstadoVazio() {
        emptyStateReservas.setVisible(true);
        emptyStateReservas.setManaged(true);
        listaReservasContainer.setVisible(false);
        listaReservasContainer.setManaged(false);
    }

    private void mostrarListaReservas() {
        emptyStateReservas.setVisible(false);
        emptyStateReservas.setManaged(false);
        listaReservasContainer.setVisible(true);
        listaReservasContainer.setManaged(true);

        listaReservasContainer.getChildren().clear();
        for (ReservaItem reserva : reservas) {
            listaReservasContainer.getChildren().add(criarCardReserva(reserva));
        }
    }

    private VBox criarCardReserva(ReservaItem item) {
        VBox card = new VBox(12);
        card.getStyleClass().add("loan-card");

        // Cabeçalho
        HBox header = new HBox(12);

        VBox iconBox = new VBox();
        iconBox.setStyle("-fx-background-color: #EFF6FF; -fx-background-radius: 8; " +
                         "-fx-padding: 10; -fx-min-width: 42px; -fx-min-height: 42px; " +
                         "-fx-max-width: 42px; -fx-max-height: 42px; -fx-alignment: CENTER;");
        Label icon = new Label("🕐");
        icon.setStyle("-fx-font-size: 18px;");
        iconBox.getChildren().add(icon);

        VBox info = new VBox(4);
        HBox.setHgrow(info, javafx.scene.layout.Priority.ALWAYS);

        Label titulo = new Label(item.titulo);
        titulo.getStyleClass().add("loan-book-title");

        Label idExemplar = new Label(item.idExemplar);
        idExemplar.getStyleClass().add("loan-book-id");

        HBox badgeRow = new HBox();
        Label badge = new Label("Na fila");
        badge.setStyle("-fx-background-color: #FEF3C7; -fx-background-radius: 20; " +
                       "-fx-padding: 3 12 3 12; -fx-font-size: 11px; " +
                       "-fx-text-fill: #92400E; -fx-font-weight: bold;");
        badgeRow.getChildren().add(badge);

        info.getChildren().addAll(titulo, idExemplar, badgeRow);
        header.getChildren().addAll(iconBox, info);

        // Separador
        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #E2E8F0;");

        // Infos da reserva
        VBox detalhes = new VBox(8);

        HBox rowPosicao = new HBox();
        Label lblPosicao = new Label("Posição na fila");
        lblPosicao.getStyleClass().add("loan-label");
        HBox.setHgrow(lblPosicao, javafx.scene.layout.Priority.ALWAYS);
        Label valPosicao = new Label(item.posicaoFila + "º");
        valPosicao.getStyleClass().add("loan-value");
        rowPosicao.getChildren().addAll(lblPosicao, valPosicao);

        HBox rowData = new HBox();
        Label lblData = new Label("Data da Reserva");
        lblData.getStyleClass().add("loan-label");
        HBox.setHgrow(lblData, javafx.scene.layout.Priority.ALWAYS);
        Label valData = new Label(item.dataReserva.format(FMT));
        valData.getStyleClass().add("loan-value");
        rowData.getChildren().addAll(lblData, valData);

        detalhes.getChildren().addAll(rowPosicao, rowData);

        // Botão cancelar
        HBox btnRow = new HBox();
        btnRow.setStyle("-fx-alignment: CENTER;");
        Button btnCancelar = new Button("Cancelar Reserva");
        btnCancelar.setStyle("-fx-background-color: transparent; -fx-text-fill: #DC2626; " +
                             "-fx-font-size: 12px; -fx-border-color: #FECACA; " +
                             "-fx-border-radius: 8; -fx-background-radius: 8; " +
                             "-fx-cursor: hand; -fx-padding: 6 16 6 16;");
        btnCancelar.setOnAction(e -> cancelarReserva(item, card));
        btnRow.getChildren().add(btnCancelar);

        card.getChildren().addAll(header, sep, detalhes, btnRow);
        return card;
    }

    private void cancelarReserva(ReservaItem item, VBox card) {
        reservas.remove(item);
        atualizarContador();

        if (reservas.isEmpty()) {
            mostrarEstadoVazio();
        } else {
            listaReservasContainer.getChildren().remove(card);
        }
    }

    @FXML private void onLogout()         { navegarPara("/fxml/Login.fxml"); }
    @FXML private void onNavCatalogo()    { navegarPara("/fxml/Catalogo.fxml"); }
    @FXML private void onNavEmprestimos() { navegarPara("/fxml/Emprestimos.fxml"); }
    @FXML private void onNavReservas()    { /* já está nesta tela */ }

    private void navegarPara(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) lblNomeUsuario.getScene().getWindow();
            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ──────────────── Inner record ────────────────
    public record ReservaItem(
            String titulo,
            String idExemplar,
            int posicaoFila,
            LocalDate dataReserva
    ) {}
}

package com.biblioqueue.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CatalogoController implements Initializable {

    @FXML private ComboBox<String> comboCategorias;
    @FXML private VBox listaLivrosContainer;

    // Dados de exemplo (substituir por chamada ao banco/serviço)
    private final List<LivroItem> todosOsLivros = List.of(
        new LivroItem("Estruturas de Dados e Algoritmos", "Thomas H. Cormen", "2009", "Algoritmos", 2),
        new LivroItem("Código Limpo",                    "Robert C. Martin",  "2008", "Engenharia de Software", 0),
        new LivroItem("Engenharia de Software",          "Ian Sommerville",   "2016", "Engenharia de Software", 3),
        new LivroItem("Padrões de Projeto",              "Erich Gamma",       "1995", "Design Patterns", 1),
        new LivroItem("Algoritmos: Teoria e Prática",    "Thomas H. Cormen",  "2012", "Algoritmos", 0)
    );

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Popula ComboBox de categorias
        comboCategorias.getItems().add("Todas as categorias");
        comboCategorias.getItems().addAll("Algoritmos", "Engenharia de Software", "Design Patterns");
        comboCategorias.getSelectionModel().selectFirst();

        renderizarLivros(todosOsLivros);
    }

    @FXML
    private void onCategoriaChanged() {
        String selecionada = comboCategorias.getValue();
        if (selecionada == null || selecionada.equals("Todas as categorias")) {
            renderizarLivros(todosOsLivros);
        } else {
            List<LivroItem> filtrados = todosOsLivros.stream()
                    .filter(l -> l.categoria.equals(selecionada))
                    .toList();
            renderizarLivros(filtrados);
        }
    }

    /** Monta os cards de livro dinamicamente */
    private void renderizarLivros(List<LivroItem> livros) {
        listaLivrosContainer.getChildren().clear();

        for (LivroItem livro : livros) {
            VBox card = criarCardLivro(livro);
            listaLivrosContainer.getChildren().add(card);
        }
    }

    private VBox criarCardLivro(LivroItem livro) {
        VBox card = new VBox(6);
        card.getStyleClass().add("book-card");

        // Título
        Label titulo = new Label(livro.titulo);
        titulo.getStyleClass().add("book-title");

        // Autor
        Label autor = new Label(livro.autor);
        autor.getStyleClass().add("book-author");

        // Ano
        Label ano = new Label(livro.ano);
        ano.getStyleClass().add("book-year");

        // Linha de tags
        HBox tagRow = new HBox(8);
        tagRow.setPadding(new Insets(4, 0, 0, 0));

        Label tagCategoria = new Label(livro.categoria);
        tagCategoria.getStyleClass().add("tag-category");

        Label tagStatus;
        if (livro.disponivel > 0) {
            tagStatus = new Label(livro.disponivel + " disponível");
            tagStatus.getStyleClass().add("tag-disponivel");
        } else {
            tagStatus = new Label("Fila de espera");
            tagStatus.getStyleClass().add("tag-fila");
        }

        tagRow.getChildren().addAll(tagCategoria, tagStatus);
        card.getChildren().addAll(titulo, autor, ano, tagRow);

        // Clique abre detalhes
        card.setOnMouseClicked(e -> abrirDetalhe(livro));

        return card;
    }

    private void abrirDetalhe(LivroItem livro) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DetalheLivro.fxml"));
            Parent root = loader.load();

            DetalheLivroController ctrl = loader.getController();
            ctrl.carregarLivro(livro.titulo, livro.autor, livro.ano,
                               livro.categoria, livro.disponivel);

            Stage stage = (Stage) listaLivrosContainer.getScene().getWindow();
            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML private void onNavCatalogo()    { /* já está no catálogo */ }
    @FXML private void onNavEmprestimos() { navegarPara("/fxml/Emprestimos.fxml"); }
    @FXML private void onNavReservas()    { navegarPara("/fxml/Reservas.fxml"); }

    private void navegarPara(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) listaLivrosContainer.getScene().getWindow();
            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ──────────────── Inner record ────────────────
    public record LivroItem(
            String titulo,
            String autor,
            String ano,
            String categoria,
            int disponivel
    ) {}
}

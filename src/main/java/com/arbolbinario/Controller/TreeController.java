package com.arbolbinario.Controller;

import com.arbolbinario.Model.ArbolBinario;
import com.arbolbinario.Model.ArbolBinario.Node;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import com.arbolbinario.Model.Queue;

public class TreeController {
    @FXML private TextField tfValue;
    @FXML private Pane treePane;
    @FXML private TextArea outputArea;
    @FXML private ScrollPane scrollPane;

    private ArbolBinario tree = new ArbolBinario();
    private Map<Node, Point2D> nodePositions = new HashMap<>();
    private double nextX;                      
    private final double horizontalSpacing = 50;
    private final double verticalSpacing = 80; 
    private final double MAX_WINDOW_WIDTH = 1200;
    private final double MAX_WINDOW_HEIGHT = 900;
    private final double LEFT_PANEL_WIDTH = 250;
    private final double BOTTOM_AREA_HEIGHT = 150;

    @FXML
    public void initialize() {
        scrollPane.setPannable(true);
        scrollPane.setFitToWidth(false);
        scrollPane.setFitToHeight(false);
        scrollPane.viewportBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            if (newBounds.getWidth() > 0 && newBounds.getHeight() > 0) {
                treePane.setMinWidth(newBounds.getWidth());
                treePane.setMinHeight(newBounds.getHeight());
                if (!nodePositions.isEmpty()) {
                    redraw();
                }
            }
        });
    }

    @FXML
    private void onInsert() {
        if(tfValue.getText().equals("")){
            redraw();
        }
        else{
            int v = Integer.parseInt(tfValue.getText().trim());
            tree.insert(v);
            outputArea.setText("Insertado: " + v);
            redraw();
            tfValue.setText("");
        }
    }

    @FXML
    private void onDelete() {
        if(tfValue.getText().equals("")){
            redraw();
        }
        else{
            int v = Integer.parseInt(tfValue.getText().trim());
            boolean removed = tree.delete(v);
            outputArea.setText(removed ? "Eliminado: " + v : "No encontrado: " + v);
            redraw();
        }
    }

    @FXML
    private void onClear() {
        tree.clear();
        redraw();
        outputArea.setText("");
        tfValue.setText("");

    }

    @FXML
    private void onSearch() {
        if(tfValue.getText().equals("")){
            redraw();
        }
        else{
            int v = Integer.parseInt(tfValue.getText().trim());
            boolean found = tree.search(v);
            outputArea.setText(found ? "Encontrado: " + v : "No encontrado: " + v);
            redraw();
            if (found) {
                nextX = 0;
                nodePositions.clear();
                computePositions(tree.getRoot(), 0);
                highlightSearchPath(v);
            }
        }
    }

    @FXML
    private void onPreorder() {
        Queue<Integer> list = tree.preOrderList();
        outputArea.setText("Preorden: " + list);
    }

    @FXML
    private void onInorder() {
        Queue<Integer> list = tree.inOrderList();
        outputArea.setText("Inorden: " + list);
    }

    @FXML
    private void onPostorder() {
        Queue<Integer> list = tree.postOrderList();
        outputArea.setText("Postorden: " + list);
    }

    @FXML
    private void onWeight() {
        int w = tree.weight();
        outputArea.setText("Peso del árbol: " + w);
    }
    @FXML
    private void onHeight() {
        int h = tree.height();
        outputArea.setText("Altura del árbol: " + h);
    }

    @FXML
    private void onMaxValue() {
        int max = tree.maxValue();
        outputArea.setText("El valor maximo del arbol: " + max);
    }
    @FXML
    private void onMinValue() {
        int min = tree.minValue();
        outputArea.setText("El valor minimo del arbol: " + min);
    }

    @FXML
    private void onLeafCount() {
        int leaf = tree.leafCount();
        outputArea.setText("Cantidad de hojas: " + leaf);
    }
    
    @FXML
    private void onLevel() {
        if(tfValue.getText().equals("")){
            outputArea.setText("Ingrese el nodo");
        }
        else{
            int v = Integer.parseInt(tfValue.getText().trim());
            int level = tree.getLevel(v);
            if (level == 0) {
                outputArea.setText("Nodo no encontrado: " + v);
            } else {
                outputArea.setText("Nivel del nodo " + v + ": " + level);
            }
        }
    }

    @FXML
    private void onLevelOrden() {
        if(tfValue.getText().equals("")){
            Queue<Integer> list = tree.printLevelOrder();
            outputArea.setText("Level orden: " + list.toString());
        }
        else{
            int v = Integer.parseInt(tfValue.getText().trim());
            if(v < 1 || v > tree.height()) {
                outputArea.setText("Nivel no valido: " + v);
                return;
            }else{
                Queue<Integer> list = tree.getLevelList(v);
                outputArea.setText("Lista del nivel " +v+ ": " + list.toString());
            }
        }
    }
    @FXML
    private void onWidth() {
        int w = tree.width();
        outputArea.setText("Amplitud del árbol: " + w);
    }

    private void redraw() {
        treePane.getChildren().clear();
        nextX = 0;
        nodePositions.clear();
        computePositions(tree.getRoot(), 0);

        double totalWidth = nextX * horizontalSpacing;
        double totalHeight = (tree.height() + 1) * verticalSpacing;
        
        treePane.setPrefWidth(Math.max(totalWidth + 100, scrollPane.getViewportBounds().getWidth()));
        treePane.setPrefHeight(Math.max(totalHeight + 50, scrollPane.getViewportBounds().getHeight()));
        
        Stage stage = (Stage) treePane.getScene().getWindow();
        if (stage != null) {
            boolean needsResize = false;
            
            double newWidth = stage.getWidth();
            if (totalWidth > stage.getWidth() - LEFT_PANEL_WIDTH) {
                newWidth = Math.min(totalWidth + LEFT_PANEL_WIDTH + 50, MAX_WINDOW_WIDTH);
                needsResize = true;
            }
            
            double newHeight = stage.getHeight();
            if (totalHeight > stage.getHeight() - BOTTOM_AREA_HEIGHT) {
                newHeight = Math.min(totalHeight + BOTTOM_AREA_HEIGHT + 30, MAX_WINDOW_HEIGHT);
                needsResize = true;
            }
            
            if (needsResize) {
                stage.setWidth(newWidth);
                stage.setHeight(newHeight);
            }
        }

        double offsetX = (treePane.getPrefWidth() - totalWidth) / 2;

        for (Map.Entry<Node, Point2D> entry : nodePositions.entrySet()) {
            Node node = entry.getKey();
            Point2D pos = entry.getValue();
            double adjustedX = pos.getX() + offsetX;

            if (node.getLeft() != null) {
                Point2D leftPos = nodePositions.get(node.getLeft());
                double adjustedLeftX = leftPos.getX() + offsetX;
                treePane.getChildren().add(new Line(adjustedX, pos.getY(), adjustedLeftX, leftPos.getY()));
            }
            if (node.getRight() != null) {
                Point2D rightPos = nodePositions.get(node.getRight());
                double adjustedRightX = rightPos.getX() + offsetX;
                treePane.getChildren().add(new Line(adjustedX, pos.getY(), adjustedRightX, rightPos.getY()));
            }
        }

        for (Map.Entry<Node, Point2D> entry : nodePositions.entrySet()) {
            Point2D pos = entry.getValue();
            double adjustedX = pos.getX() + offsetX; // Ajustar posición X
            Circle circle = new Circle(15);
            circle.setFill(Color.web("#2a2a2a"));
            Text text = new Text(String.valueOf(entry.getKey().getData()));
            text.setBoundsType(TextBoundsType.VISUAL);
            text.setStyle("-fx-font-size: 14px; -fx-fill: white;");
            StackPane stack = new StackPane(circle, text);
            stack.setLayoutX(adjustedX - 15);
            stack.setLayoutY(pos.getY() - 15);
            treePane.getChildren().add(stack);
        }
    }

    private void computePositions(Node node, int depth) {
        if (node == null) return;
        computePositions(node.getLeft(), depth + 1);
        double x = nextX * horizontalSpacing + horizontalSpacing / 2;
        double y = depth * verticalSpacing + verticalSpacing;
        nodePositions.put(node, new Point2D(x, y));
        nextX++;
        computePositions(node.getRight(), depth + 1);
    }

    private void highlightSearchPath(int value) {
        Node current = tree.getRoot();
        double totalWidth = nextX * horizontalSpacing;
        double offsetX = (treePane.getPrefWidth() - totalWidth) / 2;
        while (current != null) {
            Point2D from = nodePositions.get(current);
            if (value == current.getData()) break;
            Node next = (value < current.getData()) ? current.getLeft() : current.getRight();
            Point2D to = nodePositions.get(next);
            Line hl = new Line(from.getX() + offsetX, from.getY(), to.getX() + offsetX, to.getY());
            hl.setStroke(Color.RED);
            hl.setStrokeWidth(3);
            treePane.getChildren().add(hl);
            current = next;
        }
    }
}


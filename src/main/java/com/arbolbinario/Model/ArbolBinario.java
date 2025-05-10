package com.arbolbinario.Model;


public class ArbolBinario {
    /**
     * Nodo interno del árbol con datos y referencias a subárboles.
     */
    public static class Node {
        private int data;
        private Node leftNode;
        private Node rightNode;

        public Node(int data) {
            this.data = data;
            this.leftNode = null;
            this.rightNode = null;
        }

        public int getData() {
            return data;
        }

        public Node getLeft() {
            return leftNode;
        }

        public Node getRight() {
            return rightNode;
        }
    }

    private Node root;

    public ArbolBinario() {
        root = null;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public void insert(int data) {
        Node newNode = new Node(data);
        if (isEmpty()) {
            root = newNode;
        } else {
            insertRec(root, newNode);
        }
    }

    private void insertRec(Node current, Node newNode) {
        if(newNode.data == current.data) {
            return; // No se permiten duplicados
        }
        else if (newNode.data < current.data) {
            if (current.leftNode == null) {
                current.leftNode = newNode;
            } else {
                insertRec(current.leftNode, newNode);
            }
        } else {
            if (current.rightNode == null) {
                current.rightNode = newNode;
            } else {
                insertRec(current.rightNode, newNode);
            }
        }
    }

    public Queue<Integer> inOrderList() {
        Queue<Integer> result = new Queue<>();
        inOrderRec(root, result);
        return result;
    }

    private void inOrderRec(Node current, Queue<Integer> list) {
        if (current != null) {
            inOrderRec(current.leftNode, list);
            list.offer(current.data);
            inOrderRec(current.rightNode, list);
        }
    }

    public Queue<Integer> preOrderList() {
        Queue<Integer> result = new Queue<>();
        preOrderRec(root, result);
        return result;
    }

    private void preOrderRec(Node current, Queue<Integer> list) {
        if (current != null) {
            list.offer(current.data);
            preOrderRec(current.leftNode, list);
            preOrderRec(current.rightNode, list);
        }
    }

    public Queue<Integer> postOrderList() {
        Queue<Integer> result = new Queue<>();
        postOrderRec(root, result);
        return result;
    }

    private void postOrderRec(Node current, Queue<Integer> list) {
        if (current != null) {
            postOrderRec(current.leftNode, list);
            postOrderRec(current.rightNode, list);
            list.offer(current.data);
        }
    }

    public int weight() {
        return weightRec(root);
    }

    private int weightRec(Node current) {
        if (current == null) {
            return 0;
        }
        return 1 + weightRec(current.leftNode) + weightRec(current.rightNode);
    }

    public Node getRoot() {
        return root;
    }
    //Metodo para eliminar un nodo del arbol binario por la izquierda
    public boolean delete(int data) {
        if (search(data)) { root = deleteRec(root, data); return true; }
        return false;
    }
    private Node deleteRec(Node current, int data) {
        if (current == null) return null;
        if (data < current.data) {
            current.leftNode = deleteRec(current.leftNode, data);
        } else if (data > current.data) {
            current.rightNode = deleteRec(current.rightNode, data);
        } else {
            // Nodo a eliminar encontrado
            if (current.leftNode == null && current.rightNode == null) {
                return null;
            }
            if (current.leftNode == null) {
                return current.rightNode;
            }
            if (current.rightNode == null) {
                return current.leftNode;
            }
            // Dos hijos: usar predecesor inorden (mayor del subárbol izquierdo)
            int predecessorValue = maxValue(current.leftNode);
            current.data = predecessorValue;
            current.leftNode = deleteRec(current.leftNode, predecessorValue);
        }
        return current;
    }
    
    // Método para buscar un nodo en el árbol binario
    public boolean search(int data) {
        return searchRec(root, data);
    }
    // Método recursivo para buscar un nodo en el árbol binario
    private boolean searchRec(Node current, int data) {
        if (current == null) {
            return false;
        }
        if (current.data == data) {
            return true;
        } else if (data < current.data) {
            return searchRec(current.leftNode, data);
        } else {
            return searchRec(current.rightNode, data);
        }
    }
    // Método para encontrar el valor máximo en un subárbol
    public int maxValue(){
        return maxValue(root);
    }
    private int maxValue(Node node) {
        while (node.rightNode != null) {
            node = node.rightNode;
        }
        return node.data;
    }
    // Método para la altura del árbol
    public int height() {
        return heightRec(root);
    }
    // Método recursivo para calcular la altura del árbol
    private int heightRec(Node current) {
        if (current == null) {
            return 0;
        }
        int leftHeight = heightRec(current.leftNode);
        int rightHeight = heightRec(current.rightNode);
        if (leftHeight > rightHeight) {
            return leftHeight + 1;
        } else {
            return rightHeight + 1;
        }
    }

    public int minValue(){
        return minValue(root);
    }
    private int minValue(Node node) {
        return node.leftNode == null ? node.data : minValue(node.leftNode);
    }
    // Método para obtener el numero de nodos hoja
    public int leafCount() {
        return leafCountRec(root);
    }
    private int leafCountRec(Node current) {
        if (current == null) {
            return 0;
        }
        if (current.leftNode == null && current.rightNode == null) {
            return 1; // Nodo hoja
        }
        return leafCountRec(current.leftNode) + leafCountRec(current.rightNode);
    }

    public void clear() {
        root = null;
    }

    // Obtener el nivel de un nodo
    public int getLevel(int data) {
        return getLevelRec(root, data, 1);
    }
    private int getLevelRec(Node current, int data, int level) {
        if (current == null) {
            return 0; // Nodo no encontrado
        }
        if (current.data == data) {
            return level; // Nodo encontrado
        }
        int downLevel = getLevelRec(current.leftNode, data, level + 1);
        if (downLevel != 0) {
            return downLevel; // Nodo encontrado en el subárbol izquierdo
        }
        return getLevelRec(current.rightNode, data, level + 1); // Buscar en el subárbol derecho
    }

    // Metodo para imprimir el orden de nivel del arbol
    public Queue<Integer> printLevelOrder() {
        Queue<Integer> queue = new Queue<>();
        int height = height();
        for (int i = 1; i <= height; i++) {
            printGivenLevel(root, i, queue);
        }
        return queue;
    }

    private void printGivenLevel(Node current, int level, Queue<Integer> queue) {
        if (current == null) {
            return;
        }
        if (level == 1) {
            queue.offer(current.data);
        } else if (level > 1) {
            printGivenLevel(current.leftNode, level - 1, queue);
            printGivenLevel(current.rightNode, level - 1, queue);
        }
    }

    // Metodo para  la amplitud del arbol
    public int width() {
        if (root == null) {
            return 0; // El árbol está vacío
        }

        Queue<Node> queue = new Queue<>();
        queue.offer(root);
        int maxWidth = 0;

        while (!queue.isEmpty()) {
            int levelSize = queue.size();

            // Comparar el tamaño de la fila actual con el máximo
            if (levelSize > maxWidth) {
                maxWidth = levelSize;
            }

            for (int i = 0; i < levelSize; i++) {
                Node current = queue.poll();
                if (current.leftNode != null) queue.offer(current.leftNode);
                if (current.rightNode != null) queue.offer(current.rightNode);
            }
        }

        return maxWidth;
    }

    // Método para obtener la lista de nivel dado
    public Queue<Integer> getLevelList(int level) {
        Queue<Integer> queue = new Queue<>();
        getLevelListRec(root, level, 1, queue);
        return queue;
    }
    private void getLevelListRec(Node current, int targetLevel, int currentLevel, Queue<Integer> queue) {
        if (current == null) {
            return;
        }
        if (currentLevel == targetLevel) {
            queue.offer(current.data);
        } else {
            getLevelListRec(current.leftNode, targetLevel, currentLevel + 1, queue);
            getLevelListRec(current.rightNode, targetLevel, currentLevel + 1, queue);
        }
    }

}
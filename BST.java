import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

public class BST<K extends Comparable<? super K>, V> implements DefaultMap<K, V> {
    String ILLEGAL_ARG_NULL_KEY = "Keys must be non-null";
    private int size = 0; // total number of entries in the tree
    private Node<K, V> root;

    public BST() {
        this.root = null;
    }

    @Override
    public boolean put(K key, V value) throws IllegalArgumentException {
        if (key == null)
            throw new IllegalArgumentException(ILLEGAL_ARG_NULL_KEY);
        if (this.root == null) {
            this.root = new Node<K, V>(key, value);
            this.size += 1;
            return true;
        }

        Node<K, V> current = this.root;

        while (current != null) {
            if (key.compareTo(current.key) == 0)
                return false;

            if (key.compareTo(current.key) < 0) {
                if (current.left == null) {
                    current.left = new Node<K, V>(key, value);
                    this.size += 1;
                    return true;
                } else
                    current = current.left;
            } else if (key.compareTo(current.key) > 0) {
                if (current.right == null) {
                    current.right = new Node<K, V>(key, value);
                    this.size += 1;
                    return true;
                } else
                    current = current.right;
            }
        }

        return false;
    }

    @Override
    public boolean replace(K key, V newValue) throws IllegalArgumentException {
        if (key == null)
            throw new IllegalArgumentException(ILLEGAL_ARG_NULL_KEY);

        if (this.root == null)
            return false;

        Node<K, V> current = this.root;

        while (current != null) {
            if (key.compareTo(current.key) == 0) {
                current.value = newValue;
                return true;
            }

            if (key.compareTo(current.key) < 0)
                current = current.left;
            else if (key.compareTo(current.key) > 0)
                current = current.right;
        }

        return true;
    }

    @Override
    public boolean remove(K key) throws IllegalArgumentException {
        if (key == null)
            throw new IllegalArgumentException(ILLEGAL_ARG_NULL_KEY);

        Node<K, V> result = remove(this.root, key);

        if (result != null) {
            this.root = result;
            size -= 1;
            return true;
        } else return false;
    }

    // Helper remove method, enables recursive algorithmic approach
    private Node<K, V> remove(Node<K, V> current, K key) throws IllegalArgumentException {
        if (key == null)
            throw new IllegalArgumentException(ILLEGAL_ARG_NULL_KEY);
        if (current == null)
            return null;

        if (key.compareTo(current.key) < 0)
            current.left = remove(current.left, key);
        else if (key.compareTo(current.key) > 0)
            current.right = remove(current.right, key);
        else {
            if (current.left == null && current.right == null) {
                return null;
            }
            if (current.right == null) {
                return current.left;
            }
            if (current.left == null) {
                return current.right;
            }

            Node<K, V> temp = findReplacement(current.left);
            current.left = remove(current.left, temp.key);
            current.key = temp.key;
            current.value = temp.value;
        }
        return current;
    }

    @Override
    public void set(K key, V value) throws IllegalArgumentException {
        if (key == null)
            throw new IllegalArgumentException(ILLEGAL_ARG_NULL_KEY);
        if (this.root == null) {
            this.root = new Node<K, V>(key, value);
            this.size += 1;
            return;
        }

        Node<K, V> current = this.root;

        while (current != null) {
            if (key.compareTo(current.key) == 0) {
                current.value = value;
                return;
            } else if (key.compareTo(current.key) < 0) {
                if (current.left == null) {
                    current.left = new Node<K, V>(key, value);
                    this.size += 1;
                } else
                    current = current.left;
            } else if (key.compareTo(current.key) > 0) {
                if (current.right == null) {
                    current.right = new Node<K, V>(key, value);
                    this.size += 1;
                } else
                    current = current.right;
            }
        }
    }

    @Override
    public V get(K key) throws IllegalArgumentException {
        if (key == null)
            throw new IllegalArgumentException(ILLEGAL_ARG_NULL_KEY);

        if (this.root == null)
            return null;

        Node<K, V> current = this.root;

        while (current != null) {
            if (key.compareTo(current.key) == 0)
                return current.value;

            if (key.compareTo(current.key) < 0) {
                current = current.left;
            } else if (key.compareTo(current.key) > 0)
                current = current.right;
        }
        return null;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public boolean containsKey(K key) throws IllegalArgumentException {
        if (key == null)
            throw new IllegalArgumentException(ILLEGAL_ARG_NULL_KEY);

        if (this.root == null)
            return false;

        Node<K, V> current = this.root;

        while (current != null) {
            if (key.compareTo(current.key) == 0)
                return true;

            if (key.compareTo(current.key) < 0) {
                current = current.left;
            } else if (key.compareTo(current.key) > 0)
                current = current.right;
        }
        return false;
    }

    // Keys must be in ascending sorted order
    @Override
    public List<K> keys() {
        List<K> toReturn = new ArrayList<>();

        keys(toReturn, this.root);

        return toReturn;
    }

    private void keys(List<K> toReturn, Node<K, V> node) {
        if (node == null)
            return;

        keys(toReturn, node.left);
        toReturn.add(node.key);
        keys(toReturn, node.right);
    }

    // finds the largest node in the given branch and returns it,
    // intended for use searching the left branch of parent node
    private Node<K, V> findReplacement(Node<K, V> current) {
        if (current == null)
            return null;

        if (current.right != null)
            return findReplacement(current.right);
        else
            return current;
    }

    private static class Node<K extends Comparable<? super K>, V> implements DefaultMap.Entry<K, V> {
        private K key;
        private V value;
        private Node<K, V> left;
        private Node<K, V> right;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "key=" + key +
                    '}';
        }

        @Override
        public K getKey() {
            return this.key;
        }

        @Override
        public V getValue() {
            return this.value;
        }

        @Override
        public void setValue(V value) {
            this.value = value;
        }
    }
}
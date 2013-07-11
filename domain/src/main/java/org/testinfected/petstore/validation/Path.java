package org.testinfected.petstore.validation;

public abstract class Path {

    public static final String ROOT = ".";

    public static Path root(Object target) {
        return new Root(target);
    }

    public static class Root extends Path {
        private final Object target;

        public Root(Object target) {
            this.target = target;
        }

        public String name() {
            return ROOT;
        }

        public String value() {
            return name();
        }

        public Object target() {
            return target;
        }

        protected String valueOf(String nodePath) {
            return nodePath;
        }
    }

    public static class Node extends Path {
        private final Path parent;
        private final String name;

        public Node(Path parent, String name) {
            this.parent = parent;
            this.name = name;
        }

        public String name() {
            return name;
        }

        public String value() {
            return parent.valueOf(name);
        }

        public Object target() {
            return parent.target();
        }

        protected String valueOf(String nodePath) {
            return value() + "." + nodePath;
        }
    }

    public abstract String name();

    public abstract String value();

    public abstract Object target();

    public Path node(String name) {
        return new Node(this, name);
    }

    protected abstract String valueOf(String nodePath);
}

package com.pyxis.petstore;

public abstract class Maybe<T> {
    public abstract boolean exists();
    public abstract T bare();

    public static <T> Maybe<T> nothing() {
        return new Maybe<T>() {
            @Override
            public boolean exists() {
                return false;
            }

            @Override
            public T bare() {
                throw new Error("You have nothing where you expected something");
            }

            @Override
            public String toString() {
                return "nothing";
            }

            @Override
            public boolean equals(Object o) {
                return this == o || o != null && getClass() == o.getClass();

            }

            @Override
            public int hashCode() {
                return 0;
            }
        };
    }

    public static <T> Maybe<T> some(final T value) {
        return new Something<T>(value);
    }

    public static <T> Maybe<T> possibly(final T value) {
        return value != null ? some(value) : Maybe.<T>nothing();
    }

    private static class Something<T> extends Maybe<T> {
        private final T value;

        public Something(T value) {
            assertNotNull(value);
            this.value = value;
        }

        private void assertNotNull(T value) {
            if (value == null) throw new Error("You have nothing");
        }

        @Override
        public boolean exists() {
            return true;
        }

        @Override
        public T bare() {
            return this.value;
        }

        @Override
        public String toString() {
            return "some " + value.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Something that = (Something) o;

            return value.equals(that.value);

        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }
    }
}
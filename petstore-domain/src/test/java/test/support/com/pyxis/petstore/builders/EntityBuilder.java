package test.support.com.pyxis.petstore.builders;

//todo move to domain module to use in domain unit tests
public interface EntityBuilder<T> {
    T build();
}

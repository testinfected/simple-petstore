package test.com.pyxis.petstore.domain.product;

import com.pyxis.petstore.domain.product.Product;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

public class ProductTest {

    @Test public void
    hasADefaultPhoto() {
        assertThat("default photo", aProductWithoutAPhoto(), productWithPhoto("missing.png"));
    }

    @Test public void
    productIsUniquelyIdentifiedByItsNumber() {
        Product product = aProduct().withNumber("AAA-123").build();
        Product shouldMatch = aProduct().withNumber("AAA-123").build();
        Product shouldNotMatch = aProduct().withNumber("BBB-456").build();
        assertThat("product", product, equalTo(shouldMatch));
        assertThat("hash code", product.hashCode(), equalTo(shouldMatch.hashCode()));
        assertThat("product", product, not(equalTo(shouldNotMatch)));
    }

    private Product aProductWithoutAPhoto() {
        return aProduct().withoutAPhoto().build();
    }

    private Matcher<? super Product> productWithPhoto(String fileName) {
        return new FeatureMatcher<Product, String>(equalTo(fileName), "a product with photo", "photo") {
            protected String featureValueOf(Product actual) {
                return actual.getPhotoFileName();
            }
        };
    }
}

package test.integration.com.pyxis.petstore.persistence.support;

import com.pyxis.petstore.domain.Item;

public class ItemBuilder implements EntityBuilder<Item> {

    private String name;

    public static ItemBuilder anItem() {
        return new ItemBuilder();
    }

    public ItemBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public Item build() {
        return new Item(name);
    }
}

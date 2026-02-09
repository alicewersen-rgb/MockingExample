package com.example;

import com.example.shop.Item;
import com.example.shop.ShoppingCart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class ShoppingCartTest {

    private ShoppingCart cart;
    private Item item;

    @BeforeEach
    void setUp() {
        cart = new ShoppingCart();
        item = new Item("Apple", 10.0);
    }

    @Test
    void shouldAddItemToCart() {
        cart.addItem(item);
        assertThat(cart.getItems()).contains(item);
    }
    @Test
    void shouldRemoveItemFromCart() {
        cart.addItem(item);
        cart.removeItem(item);

        assertThat(cart.getItems()).doesNotContain(item);
    }
    @Test
    void shouldCalculateTotalPrice() {
        cart.addItem(item);
        cart.addItem(new Item("Banana", 5.0));

        assertThat(cart.getTotalPrice()).isEqualTo(15.0);
    }
    @Test
    void shouldUpdateQuantityWhenAddingSameItem() {
        cart.addItem(item);
        cart.addItem(new Item("Apple", 10.0));

        assertThat(cart.getItems().size()).isEqualTo(1);
        assertThat(cart.getQuantity(item)).isEqualTo(2);
    }
    @Test
    void shouldNotFailWhenRemovingNonExistentItem() {
        cart.addItem(item);
        Item orange = new Item("Orange", 5.0);

        cart.removeItem(orange);

        assertThat(cart.getItems()).contains(item);
    }
    @Test
    void shouldReturnZeroForEmptyCart() {
        assertThat(cart.getTotalPrice()).isEqualTo(0.0);
    }

    @ParameterizedTest
    @CsvSource({
            "Apple, 10.0",
            "Banana, 5.5",
            "Orange, 3.75"
    })
    void shouldAddMultipleItemsCorrectly(String name, double price) {
        Item newItem = new Item(name, price);
        cart.addItem(newItem);

        assertThat(cart.getItems()).contains(newItem);
    }
}


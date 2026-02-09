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
    private Item apple;

    @BeforeEach
    void setUp() {
        cart = new ShoppingCart();
        apple = new Item("Apple", 10.0);
    }

    // Test: Lägga till ett item.
    @Test
    void shouldAddItemToCart() {
        cart.addItem(apple);
        assertThat(cart.getItems()).contains(apple);
    }

    // Test: Ta bort ett item.
    @Test
    void shouldRemoveItemFromCart() {
        cart.addItem(apple);
        cart.removeItem(apple);
        assertThat(cart.getItems()).doesNotContain(apple);
    }

    // Test: Totalpris för flera items.
    @Test
    void shouldCalculateTotalPrice() {
        cart.addItem(apple);
        cart.addItem(new Item("Banana", 5.0));
        assertThat(cart.getTotalPrice()).isEqualTo(15.0);
    }

    // Test: Uppdatera kvantitet när samma item läggs till flera gånger.
    @Test
    void shouldUpdateQuantityWhenAddingSameItem() {
        cart.addItem(apple);
        cart.addItem(new Item("Apple", 10.0));
        assertThat(cart.getItems().size()).isEqualTo(1);
        assertThat(cart.getQuantity(apple)).isEqualTo(2);
    }

    // Test: Ta bort ett item som inte finns, påverkar inte varukorgen.
    @Test
    void shouldNotFailWhenRemovingNonExistentItem() {
        cart.addItem(apple);
        Item orange = new Item("Orange", 5.0);
        cart.removeItem(orange);
        assertThat(cart.getItems()).contains(apple);
    }

    // Test: Totalpriset är 0 när varukorgen är tom.
    @Test
    void shouldReturnZeroForEmptyCart() {
        assertThat(cart.getTotalPrice()).isEqualTo(0.0);
    }

    // Parametriserat test: Lägga till olika items.
    @ParameterizedTest
    @CsvSource({
            "Apple, 10.0",
            "Banana, 5.5",
            "Orange, 3.75"
    })
    void shouldAddMultipleItemsCorrectly(String name, double price) {
        Item item = new Item(name, price);
        cart.addItem(item);
        assertThat(cart.getItems()).contains(item);
    }

    // Kantfall: Item med nollpris.
    @Test
    void shouldHandleItemWithZeroPrice() {
        Item freeItem = new Item("FreeCandy", 0.0);
        cart.addItem(freeItem);
        assertThat(cart.getTotalPrice()).isEqualTo(0.0);
        assertThat(cart.getItems()).contains(freeItem);
    }

    // Kantfall: Item med negativt pris.
    @Test
    void shouldHandleItemWithNegativePrice() {
        Item badItem = new Item("FaultyItem", -5.0);
        cart.addItem(badItem);
        assertThat(cart.getTotalPrice()).isEqualTo(-5.0);
        assertThat(cart.getItems()).contains(badItem);
    }

    // Parametriserat test: Lägg till flera av samma item och kontrollera kvantitet.
    @ParameterizedTest
    @CsvSource({
            "Apple, 10.0, 1",
            "Apple, 10.0, 2",
            "Apple, 10.0, 5"
    })
    void shouldCorrectlyHandleMultipleQuantities(String name, double price, int quantity) {
        Item item = new Item(name, price);
        for (int i = 0; i < quantity; i++) {
            cart.addItem(item);
        }
        assertThat(cart.getQuantity(item)).isEqualTo(quantity);
    }
}
package com.example.shop;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {

    private final List<Item> items = new ArrayList<>();
    private final List<Integer> quantities = new ArrayList<>();

    public void addItem(Item item) {
        int index = items.indexOf(item);
        if (index >= 0) {

            quantities.set(index, quantities.get(index) + 1);
        } else {
            items.add(item);
            quantities.add(1);
        }
    }

    public void removeItem(Item item) {
        int index = items.indexOf(item);
        if (index >= 0) {
            quantities.set(index, quantities.get(index) - 1);
            if (quantities.get(index) <= 0) {
                items.remove(index);
                quantities.remove(index);
            }
        }
    }

    public List<Item> getItems() {
        return new ArrayList<>(items);
    }

    public int getQuantity(Item item) {
        int index = items.indexOf(item);
        return index >= 0 ? quantities.get(index) : 0;
    }

    public double getTotalPrice() {
        double total = 0;
        for (int i = 0; i < items.size(); i++) {
            total += items.get(i).getPrice() * quantities.get(i);
        }
        return total;
    }
}

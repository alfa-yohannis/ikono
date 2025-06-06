package edu.pradita.p14.strategy;

import edu.pradita.p14.javafx.ShippingMerek;
import java.util.List;

public interface SearchStrategy {
    List<ShippingMerek> search(String keyword);
}
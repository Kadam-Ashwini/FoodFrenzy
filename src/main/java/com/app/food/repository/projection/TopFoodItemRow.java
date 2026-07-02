package com.app.food.repository.projection;

import java.math.BigDecimal;

public interface TopFoodItemRow {
    String getFoodName();
    String getCategory();
    Long getTotalQty();
    BigDecimal getRevenue();
}
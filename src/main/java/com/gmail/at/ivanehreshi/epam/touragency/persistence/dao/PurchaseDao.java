package com.gmail.at.ivanehreshi.epam.touragency.persistence.dao;

import com.gmail.at.ivanehreshi.epam.touragency.domain.Purchase;

public interface PurchaseDao extends Dao<Purchase,Long> {
    Purchase deepen(Purchase purchase);
}

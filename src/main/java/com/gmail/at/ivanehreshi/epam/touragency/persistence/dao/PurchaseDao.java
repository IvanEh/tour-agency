package com.gmail.at.ivanehreshi.epam.touragency.persistence.dao;

import com.gmail.at.ivanehreshi.epam.touragency.domain.Purchase;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.Dao;

import java.util.List;

public interface PurchaseDao extends Dao<Purchase,Long> {
    Purchase deepen(Purchase purchase);
    List<Purchase> findByUser(Long userId);
}

package com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.inmemory;

import com.gmail.at.ivanehreshi.epam.touragency.domain.Purchase;

public class PurchaseInMemoryDao extends AbstractInMemoryDao<Purchase> {
    public PurchaseInMemoryDao() {
        super(Purchase.class);
    }

    @Override
    public Long create(Purchase purchase) {
        Long key = super.create(purchase);
        purchase.setId(key);
        return key;
    }

    @Override
    public void update(Purchase purchase) {
        getEntities().replace(purchase.getId(), purchase);
    }
}

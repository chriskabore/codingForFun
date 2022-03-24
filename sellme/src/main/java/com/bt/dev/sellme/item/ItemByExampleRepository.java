package com.bt.dev.sellme.item;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface ItemByExampleRepository extends CrudRepository<Item, Integer>, QueryByExampleExecutor<Item> {

}

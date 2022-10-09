package com.courseori.server.item.service;

import com.courseori.server.exception.BusinessLogicException;
import com.courseori.server.exception.ExceptionCode;
import com.courseori.server.item.entity.Item;
import com.courseori.server.item.repository.ItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item createItem(Item item){
        Item savedItem = itemRepository.save(item);
        return savedItem;
    }

    public Item updateItem(Item item){
        Item findItem = findVerifiedItem(item.getItemId());

        Optional.ofNullable(item.getTitle())
                .ifPresent(title -> findItem.setTitle(title));
        Optional.ofNullable(item.getBody())
                .ifPresent(body -> findItem.setBody(body));

        return itemRepository.save(findItem);

    }

    public Item findItem(long itemId){
        Item findItem = findVerifiedItem(itemId);
        return findItem;
    }

    public Page<Item> findItems(int page, int size){
        return itemRepository.findAll(PageRequest.of(page, size,
                Sort.by("itemId").descending()));
    }

    public void deleteItem(long itemId){
        Item findItem = findVerifiedItem(itemId);
        itemRepository.delete(findItem);
    }

    public Item findVerifiedItem(long itemId){
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        Item findItem = optionalItem.orElseThrow(()->new BusinessLogicException(ExceptionCode.ITEM_NOT_FOUND));
        return findItem;
    }
}

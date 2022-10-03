package com.courseori.server.item.controller;

import com.courseori.server.item.dto.ItemDto;
import com.courseori.server.item.entity.Item;
import com.courseori.server.item.mapper.ItemMapper;
import com.courseori.server.item.service.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;


@RestController
@RequestMapping("/v1/items")
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;

    public ItemController(ItemService itemService, ItemMapper mapper) {
        this.itemService = itemService;
        this.itemMapper = mapper;
    }

    @PostMapping
    public ResponseEntity postItem(@RequestBody ItemDto.Post requestBody) {
        Item item = itemMapper.itemPostToItem(requestBody);


        Item createdItem = itemService.createItem(item);
        ItemDto.Response response = itemMapper.itemToItemResponse(createdItem);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{item-id}")
    public ResponseEntity patchItem(@PathVariable("item-id") @Positive long itemId,
                                    @RequestBody ItemDto.Patch requestBody) {
        requestBody.setItemId(itemId);
        Item item = itemService.updateItem(itemMapper.itemPatchToItem(requestBody));
        ItemDto.Response response = itemMapper.itemToItemResponse(item);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{item-id}")
    public ResponseEntity getItem(@PathVariable("item-id") @Positive long itemId) {

        Item item = itemService.findItem(itemId);

        ItemDto.Response response = itemMapper.itemToItemResponse(item);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getItems(@Positive @RequestParam int page,
                                   @Positive @RequestParam int size) {

        Page<Item> itemsPage = itemService.findItems(page, size);
        List<Item> items = itemsPage.getContent();

        List<ItemDto.Response> responses = itemMapper.itemsToItemResponses(items);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @DeleteMapping("/{item-id}")
    public ResponseEntity deleteItem(@PathVariable("item-id") @Positive long itemId) {
        itemService.deleteItem(itemId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}

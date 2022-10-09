package com.courseori.server.item.controller;

import com.courseori.server.dto.MultiResponseDto;
import com.courseori.server.item.dto.ItemDto;
import com.courseori.server.item.entity.Item;
import com.courseori.server.item.mapper.ItemMapper;
import com.courseori.server.item.service.ItemService;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/v1/items")
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper mapper;

    public ItemController(ItemService itemService, ItemMapper mapper) {
        this.itemService = itemService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity postItem(@RequestBody ItemDto.Post requestBody){
        System.out.println(Arrays.asList(requestBody));
        Item item = mapper.itemPostToItem(requestBody);
        Item createdItem = itemService.createItem(item);
        ItemDto.Response response = mapper.itemToItemResponse(createdItem);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{item-id}")
    public ResponseEntity patchItem(@PathVariable("item-id") long itemId,
                                    @RequestBody ItemDto.Patch requestBody){
        requestBody.setItemId(itemId);
        Item item = itemService.updateItem(mapper.itemPatchToItem(requestBody));
        ItemDto.Response response = mapper.itemToItemResponse(item);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/{item-id}")
    public ResponseEntity getItem(@PathVariable("item-id") long itemId){
        Item item = itemService.findItem(itemId);

        ItemDto.Response response = mapper.itemToItemResponse(item);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getItems(@RequestParam int size,
                                   @RequestParam int page){
        Page<Item> pageItems = itemService.findItems(page-1,size);
        List<Item> items = pageItems.getContent();
        return new ResponseEntity<>(new MultiResponseDto<>(mapper.itemsToItemResponses(items),pageItems),HttpStatus.OK);
    }

    @DeleteMapping("/{item-id}")
    public ResponseEntity deleteItem(@PathVariable("item-id") long itemId){
        itemService.deleteItem(itemId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

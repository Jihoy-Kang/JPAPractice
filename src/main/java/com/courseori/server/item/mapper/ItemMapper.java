package com.courseori.server.item.mapper;


import com.courseori.server.item.dto.ItemDto;
import com.courseori.server.item.entity.Item;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ItemMapper {
    Item itemPostToItem(ItemDto.Post requestBody);
    Item itemPatchToItem(ItemDto.Patch requestBody);

    List<ItemDto.Response> itemsToItemResponses(List<Item> itemList);
    ItemDto.Response itemToItemResponse(Item item);

}

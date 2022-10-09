package com.courseori.server.item.mapper;

import com.courseori.server.item.dto.ItemDto;
import com.courseori.server.item.entity.Item;
import com.courseori.server.member.dto.MemberDto;
import com.courseori.server.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ItemMapper {

    Item itemPostToItem(ItemDto.Post requestBody);

    Item itemPatchToItem(ItemDto.Patch requestBody);

    ItemDto.Response itemToItemResponse(Item item);

    List<ItemDto.Response> itemsToItemResponses(List<Item> items);

}

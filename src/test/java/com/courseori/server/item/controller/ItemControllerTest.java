package com.courseori.server.item.controller;

import com.courseori.server.item.dto.ItemDto;
import com.courseori.server.item.entity.Item;
import com.courseori.server.item.mapper.ItemMapper;
import com.courseori.server.item.service.ItemService;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.BDDMockito.given;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
@AutoConfigureMockMvc
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemMapper mapper;


    @Test
    void postItem() throws Exception{
    //given
        ItemDto.Post post= new ItemDto.Post("Title","Body");
        ItemDto.Response response = new ItemDto.Response(1L,"Title","Body");
        String content = gson.toJson(post);

        given(mapper.itemPostToItem(Mockito.any(ItemDto.Post.class))).willReturn(new Item());

        given(itemService.createItem(Mockito.any(Item.class))).willReturn(new Item());
        given(mapper.itemToItemResponse(Mockito.any(Item.class))).willReturn(response);



    //when
        ResultActions actions =
                mockMvc.perform(
                        post("/v1/items")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );
    //then
        MvcResult result = actions
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    void patchItem() throws Exception{
        //given
        Long itemId = 1L;
        ItemDto.Patch patch = new ItemDto.Patch(1L,"Title2","Body2");

        ItemDto.Response response = new ItemDto.Response(1L,"Title2","Body2");


        given(mapper.itemPatchToItem(Mockito.any(ItemDto.Patch.class))).willReturn(new Item());

        given(itemService.updateItem(Mockito.any(Item.class))).willReturn(new Item());

        given(mapper.itemToItemResponse(Mockito.any(Item.class))).willReturn(response);

        String content = gson.toJson(patch);



        //when
        ResultActions actions =
                mockMvc.perform(
                        patch("/v1/items/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );
        //then
        MvcResult result = actions
                .andExpect(status().isOk())
                .andReturn();
    }
//
//    @Test
//    void getItem() {
//        //given
//        //when
//        //then
//    }
//
//    @Test
//    void getItems() {
//        //given
//        //when
//        //then
//    }
//
//    @Test
//    void deleteItem() {
//        //given
//        //when
//        //then
//    }
}
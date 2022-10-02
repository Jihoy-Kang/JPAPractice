package com.courseori.server.item.controller;

import com.courseori.server.item.dto.ItemDto;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;


    @Test
    void postItem() throws Exception{
    //given
        ItemDto.Post post= new ItemDto.Post("Titel","Body");

        String content = gson.toJson(post);

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

//    @Test
//    void patchItem() {
//        //given
//        //when
//        //then
//    }
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
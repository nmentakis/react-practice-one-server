package com.galvanize.fullstack;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
public class ItemControllerTests {


    @Autowired
    MockMvc mvc;

    @Autowired
    ItemRepository repository;

//    ObjectMapper mapper = new ObjectMapper();

    @Test
    @Transactional
    @Rollback
    public void getAllRecordsReturnsCorrectIterable() throws Exception {

        // setup
        Item itemOne = new Item("Learn React");
        Item itemTwo = new Item("Learn Spring");

        this.repository.save(itemOne);
        this.repository.save(itemTwo);
        //execute

        this.mvc.perform(get("/api/items"))
        //assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content", is("Learn React")))
                .andExpect(jsonPath("$[0].completed", is(false)))
                .andExpect(jsonPath("$[1].content", is("Learn Spring")))
                .andExpect(jsonPath("$[1].completed", is(false)));
    }



    @Test
    @Transactional
    @Rollback
    public void updateItemCorrectlySavesToDatabase() throws Exception {
        Item item = new Item("You did it!");

        this.repository.save(item);


        String body = "{\"content\": \"You did great!\", \"completed\": true}";

//        Item changedItem = new Item("You did great!");
//        changedItem.setCompleted(true);
//        ObjectMapper mapper = new ObjectMapper();
//        String json = mapper.writeValueAsString(changedItem);
//

        this.mvc.perform(patch("/api/items/" + item.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", is("You did great!")))
                .andExpect(jsonPath("$.completed", is(true)));

    }




}

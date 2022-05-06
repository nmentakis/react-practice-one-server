package com.galvanize.fullstack;


import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.Map;

@RestController
@CrossOrigin("http://localhost:3000")
//@RequestMapping("/api/items")
public class ItemController {
    private final ItemRepository repository;

    public ItemController(ItemRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/api/items")
    public Iterable<Item> getAllItems() {
        return repository.findAll();
    }

    @PostMapping("/api/items")
    public Item addItemToDatabase(@RequestBody Item item) {
        item.setCompleted(false);
        return repository.save(item);
    }

    @DeleteMapping("/api/items/{id}")
    public void deleteItemById(@PathVariable Long id)    {
        repository.deleteById(id);
    }


    @PatchMapping("/api/items/{id}")
    public Item updateItem(@PathVariable Long id, @RequestBody Map<String, Object> body) {

        Item oldItem = repository.findById(id).get();

        body.forEach( (k,v) -> {

//            if (k.equals("content")) {
//                oldItem.setContent( (String) v);
//            } else if (k.equals("completed")) {
//                oldItem.setCompleted( (boolean) v);
//            }

            Field field = ReflectionUtils.findField(Item.class, k);
            field.setAccessible(true);
            ReflectionUtils.setField(field, oldItem, v);

        });

        return repository.save(oldItem);


    }


}

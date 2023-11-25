package com.booking.api.controller;

import com.booking.api.domain.Block;
import com.booking.api.exceptions.BlockNotFoundException;
import com.booking.api.exceptions.InvalidDateRangeException;
import com.booking.api.exceptions.InvalidPropertyException;
import com.booking.api.service.BlockService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@RestController
@RequestMapping("/blocks")
public class BlockController {

    private BlockService blockService;

    public BlockController(BlockService blockService) {
        this.blockService = blockService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity create(@RequestBody Block block) throws InvalidPropertyException, URISyntaxException, InvalidDateRangeException {
        Block savedBlock = blockService.create(block);
        URI location = new URI("/blocks/" + savedBlock.getId());
        return ResponseEntity.created(location).body(savedBlock);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable UUID id, @RequestBody Block block) throws BlockNotFoundException, InvalidDateRangeException {
        return ResponseEntity.ok(blockService.update(id, block.getFromDate(), block.getToDate()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable UUID id) throws BlockNotFoundException {
        blockService.delete(id);
        return ResponseEntity.ok().build();
    }
}

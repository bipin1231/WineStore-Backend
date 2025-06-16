package com.winestore.winestore.controller;

import com.winestore.winestore.DTO.*;
import com.winestore.winestore.entity.Category;
import com.winestore.winestore.repository.CategoryRepo;
import com.winestore.winestore.service.CategoryService;
import com.winestore.winestore.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/size")

public class SizeController {


    @Autowired
    private SizeService sizeService;

    @PostMapping
    public String createSize(@RequestBody SizeRequestDTO sizeRequestDTO) {
        try {
            sizeService.createSize(sizeRequestDTO);
            return "Size added Successfully";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }


    @GetMapping
    public List<SizeResponseDTO> getAllSize() {
        return sizeService.getAllSize();
    }


}
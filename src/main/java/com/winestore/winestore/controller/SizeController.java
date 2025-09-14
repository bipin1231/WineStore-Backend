package com.winestore.winestore.controller;

import com.winestore.winestore.ApiResponse.ApiResponse;
import com.winestore.winestore.DTO.*;
import com.winestore.winestore.entity.Category;
import com.winestore.winestore.repository.CategoryRepo;
import com.winestore.winestore.service.CategoryService;
import com.winestore.winestore.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/size")

public class SizeController {


    @Autowired
    private SizeService sizeService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<?>> createSize(@RequestBody SizeRequestDTO sizeRequestDTO) {

            sizeService.createSize(sizeRequestDTO);
            return ResponseEntity.ok(
                    new ApiResponse<>(true,"Size Added Successfully",null)
            );


    }
    @PutMapping
    public ResponseEntity<ApiResponse<?>> updateSize(@RequestBody List<SizeUpdateDTO> sizeRequestDTO) {

        sizeService.updateSize(sizeRequestDTO);
        return ResponseEntity.ok(
                new ApiResponse<>(true,"Size Updated Successfully",null)
        );


    }


    @GetMapping
    public List<SizeResponseDTO> getAllSize() {
        return sizeService.getAllSize();
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id){
        sizeService.deleteSize(id);
    }

}
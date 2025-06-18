package com.winestore.winestore.service;

import com.winestore.winestore.DTO.*;
import com.winestore.winestore.entity.Category;
import com.winestore.winestore.entity.Size;
import com.winestore.winestore.repository.CategoryRepo;
import com.winestore.winestore.repository.SizeRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class SizeService {
    @Autowired
    private SizeRepo sizeRepo;

    public void createSize(SizeRequestDTO sizeRequestDTO){

        Optional<Size> sizeOpt=sizeRepo.findBySize(sizeRequestDTO.getSize());
        if(sizeOpt.isPresent()){
            throw new IllegalArgumentException("size already exits");
        }
        Size size=new Size();

        size.setSize(sizeRequestDTO.getSize());
        size.setBottleInCartoon(sizeRequestDTO.getBottleInCartoon());
        sizeRepo.save(size);

    }
    public void updateSize(SizeResponseDTO dto){

        Optional<Size> sizeOpt=sizeRepo.findBySize(dto.getSize());
        if(!sizeOpt.isPresent()){
            throw new IllegalArgumentException("size doesnt exits");
        }
        Size size=new Size();

        size.setSize(dto.getSize());
        size.setBottleInCartoon(dto.getBottleInCartoon());
        sizeRepo.save(size);

    }

    public List<SizeResponseDTO> getAllSize(){
        return sizeRepo.findAll().stream().map(SizeResponseDTO::new).toList();
    }

    public void deleteSize(Long id){
        sizeRepo.deleteById(id);
    }

}

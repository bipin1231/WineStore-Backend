package com.winestore.winestore.DTO;

import com.winestore.winestore.entity.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SizeResponseDTO {
    Long id;
    private String size;
    private Integer bottleInCartoon;
    public SizeResponseDTO(Size size){
        this.id= size.getId();
        this.size=size.getSize();
        this.bottleInCartoon=size.getBottleInCartoon();

    }
}

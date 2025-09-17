package com.winestore.winestore.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SizeUpdateDTO {
    private Long id;
    private String size;
    private Integer bottleInCartoon;
}

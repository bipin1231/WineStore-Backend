package com.winestore.winestore.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class SizeRequestDTO {
    private String size;
    private Integer bottleInCartoon;
}

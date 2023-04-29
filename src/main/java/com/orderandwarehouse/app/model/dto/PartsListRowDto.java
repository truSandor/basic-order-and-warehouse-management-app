package com.orderandwarehouse.app.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartsListRowDto {
    private Long id;
    private Long productId;
    private Long componentId;
    private String componentName;
    private Double quantity;
    private String unit = "pcs";
    private String Comment;
    private LocalDateTime dateAdded;
    private LocalDateTime dateModified;
}

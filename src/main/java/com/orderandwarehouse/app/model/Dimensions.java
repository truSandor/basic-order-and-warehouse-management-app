package com.orderandwarehouse.app.model;


import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Dimensions {
    private int length;
    private int width;
    private int height;

    public Dimensions(String dimensions) {
        try {
            String[] lwh = dimensions.split("x");
            length = Integer.parseInt(lwh[0]);
            width = Integer.parseInt(lwh[1]);
            height = Integer.parseInt(lwh[2]);
        } catch (NumberFormatException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return String.format("%dx%dx%d", length, width, height);
    }
}

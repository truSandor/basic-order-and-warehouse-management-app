package com.orderandwarehouse.app.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dimensions {
    private Integer length;
    private Integer width;
    private Integer height;

    public void setDimensionsFromString(String dimensions) {
        if (dimensions != null) {
            String[] lwh = dimensions.split("x");
            if (lwh.length == 3) {
                length = lwh[0].equals("null") ? null : Integer.valueOf(lwh[0]);
                width = lwh[1].equals("null") ? null : Integer.valueOf(lwh[1]);
                height = lwh[2].equals("null") ? null : Integer.valueOf(lwh[2]);
            }
        }
    }

    @Override
    public String toString() {
        return String.format("%dx%dx%d", length, width, height);
    }
}

package com.medkit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Medicine {
    private int id;

    private String name;
    private String description;
    private String manufacturer;

    private float price;

    private Date startDate;
}

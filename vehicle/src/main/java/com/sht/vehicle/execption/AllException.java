package com.sht.vehicle.execption;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Aaron
 */
@Setter
@Getter
@AllArgsConstructor
public class AllException extends RuntimeException {

    private Integer code;
    private String message;
}

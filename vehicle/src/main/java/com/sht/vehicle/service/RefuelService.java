package com.sht.vehicle.service;

import com.sht.vehicle.repository.RefuelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Aaron
 * @date 2021/2/17 16:49
 */
@Service
public class RefuelService {

    @Autowired
    private RefuelRepository refuelRepository;

}

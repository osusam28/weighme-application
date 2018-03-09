package com.weighme.weighmedata.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.weighme.weighmedata.service.WeighmeDataService;

@RestController
public class WeighmeDataStartController {
	
	@Autowired
	private WeighmeDataService dataService;
	
	public void start() {
		
	}
}

package com.flinktests.testcases;

import org.testng.annotations.Test;

import com.flinktests.basetest.basetest;

import org.testng.AssertJUnit;
import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.qameta.allure.Description;

public class homepagetestcases extends basetest{


@Test
public void endToEndProductSelectionTest() {
	
	if(hp.getCurrentTemperature()<18) {
		pg = hp.selectMositurizerType();
	    cp = pg.selectProductsAndClickCart("aloe", "almond");
	}
	else if(hp.getCurrentTemperature()>34) {
		pg = hp.selectSunscreenType();
		cp = pg.selectProductsAndClickCart("spf-50", "spf-30");
	}
		
	else System.out.println("Temperature not in limits");
		
	
	Assert.assertTrue(cp.verifyProductTotal());
	try {
		cm = cp.makePayment();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	Assert.assertEquals(cm.getSuccessMessage(), "PAYMENT SUCCESS" );
	//System.out.println(cm.getSuccessMessage());
}







}

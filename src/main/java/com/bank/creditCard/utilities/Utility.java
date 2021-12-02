package com.bank.creditCard.utilities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.bank.creditCard.io.entities.ServiceResponse;

public class Utility {

	private Utility() {
		
	}
	
	public static String getUUID() {
		return UUID.randomUUID().toString();
	}
	
	public static ResponseEntity<ServiceResponse> getResponseEntity(Object obj, HttpStatus responseStatus, String responseMessage) {
		ServiceResponse serviceResponse = new ServiceResponse(obj, responseStatus.name(),
				responseStatus.value(),
				responseMessage,
				ZonedDateTime.now(ZoneId.of("Z")) );
		return new ResponseEntity<>(serviceResponse, responseStatus);
	}
	
	
	public static String randomThreeDigitNumber() {
		Random rand = new Random();
		return String.format("%03d", rand.nextInt(1000));
	}
	
	public static String randomFourDigitNumber() {
		Random rand = new Random();
		return String.format("%04d", rand.nextInt(10000));
	}
	
	public static String randomSixteenDigitNumber() {
		Random rand = new Random();
		StringBuilder randNumber = new StringBuilder();
		for(int i=0; i<4; i++) {
			randNumber.append(String.format("%04d", rand.nextInt(10000))); 
		}
		return randNumber.toString(); 
	}
	
	public static Date getNewCardValidToDate(Optional<Short> validityInMonths) {
			
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, validityInMonths.orElse(Short.valueOf("0")));
		return (new Date(cal.getTimeInMillis()));
	}
	
	public static BigDecimal calculateCardLimitBy(BigDecimal salaryPerYear) {
		
		BigDecimal hundred = new BigDecimal("100");
		BigDecimal percentageValue = new BigDecimal("10");
		return (salaryPerYear.divide(hundred, 2, RoundingMode.CEILING))
				.multiply(percentageValue);
	}

	public static boolean compareBothValuesMonthAndYearsAreEqual(Date tCardValidTo, Date cCardValidTo) {

		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(tCardValidTo);
		int tMonth = cal1.get(Calendar.MONTH);
		int tYear = cal1.get(Calendar.YEAR);

		
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(cCardValidTo);
		int cMonth = cal2.get(Calendar.MONTH);
		int cYear = cal2.get(Calendar.YEAR);

		
		if((tMonth == cMonth) 
			&& (tYear == cYear)) {
			return true;
		}
		
		return false;
	}

	public static boolean compareBothValues(String tToken, String cToken) {
		
		if(cToken.equals(tToken)) {
			return true;
		}
		
		return false;
	}
}

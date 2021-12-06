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

/**
 * This is an Utility class contains methods commonly used in code
 *
 */
public class Utility {
 private Utility() {
 }

 /**
  * This method will generate a unique UUID status.
  * @return A random UUID string
  */
 public static String getUUID() {
  return UUID.randomUUID().toString();
 }

 /**
  * This method will generate a ResponseEntity with actual response output
  * object
  * @param obj             Actual response based on request
  * @param responseStatus  HttpStatus value
  * @param responseMessage Request status message
  * @return {@link ResponseEntity} with actual object binded into it.
  */
 public static ResponseEntity<ServiceResponse> getResponseEntity(Object obj,
   HttpStatus responseStatus, String responseMessage) {
  ServiceResponse serviceResponse = new ServiceResponse(obj,
    responseStatus.name(), responseStatus.value(), responseMessage,
    ZonedDateTime.now(ZoneId.of("Z")));
  return new ResponseEntity<>(serviceResponse, responseStatus);
 }

 /**
  * This method is used to generate a Random 3 digit number
  * @return A 3 digit number in String format
  */
 public static String randomThreeDigitNumber() {
  Random rand = new Random();
  return String.format("%03d", rand.nextInt(1000));
 }

 /**
  * This method is used to generate a Random 4 digit number
  * @return A 4 digit number in String format
  */
 public static String randomFourDigitNumber() {
  Random rand = new Random();
  return String.format("%04d", rand.nextInt(10000));
 }

 /**
  * This method is used to generate a Random 16 digit number
  * @return A 16 digit number for a Crad Number
  */
 public static String randomSixteenDigitNumber() {
  Random rand = new Random();
  StringBuilder randNumber = new StringBuilder();

  for (int i = 0; i < 4; i++) {
   randNumber.append(String.format("%04d", rand.nextInt(10000)));
  }

  return randNumber.toString();
 }

 /**
  * This method will add n number of months based on @param
  * @param validityInMonths Number of months
  * @return new Date added which is curr date + number of months valid
  */
 public static Date getNewCardValidToDate(Optional<Short> validityInMonths) {
  Calendar cal = Calendar.getInstance();
  cal.add(Calendar.MONTH, validityInMonths.orElse(Short.valueOf("0")));
  return (new Date(cal.getTimeInMillis()));
 }

 /**
  * This method will calculate Credit Card Limit by input salary per year
  * @param salaryPerYear Customer salary amount per an year
  * @return Credit Card Limit value based on input param
  */
 public static BigDecimal calculateCardLimitBy(BigDecimal salaryPerYear) {
  BigDecimal hundred = new BigDecimal("100");
  BigDecimal percentageValue = new BigDecimal("10");
  return (salaryPerYear.divide(hundred, 2, RoundingMode.CEILING))
    .multiply(percentageValue);
 }

 /**
  * This method is used to compare month and year for two diff dates.
  * @param tCardValidTo This is an input CardValidTo value for a card
  *                     transaction
  * @param cCardValidTo This is actual CardValidTo date for a Credit Card
  * @return Return True if month and year of both dates are equal
  */
 public static boolean compareBothValuesMonthAndYearsAreEqual(Date tCardValidTo,
   Date cCardValidTo) {
  Calendar cal1 = Calendar.getInstance();
  cal1.setTime(tCardValidTo);
  int tMonth = cal1.get(Calendar.MONTH);
  int tYear = cal1.get(Calendar.YEAR);
  Calendar cal2 = Calendar.getInstance();
  cal2.setTime(cCardValidTo);
  int cMonth = cal2.get(Calendar.MONTH);
  int cYear = cal2.get(Calendar.YEAR);

  if ((tMonth == cMonth) && (tYear == cYear)) {
   return true;
  }

  return false;
 }

 /**
  * This method will compare two strings
  * @param tToken input string
  * @param cToken actual string
  * @return Return True when both are same.
  */
 public static boolean compareBothValues(String tToken, String cToken) {

  if (cToken.equals(tToken)) {
   return true;
  }

  return false;
 }
}

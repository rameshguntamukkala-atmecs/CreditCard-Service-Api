package com.bank.creditCard.configuration;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * This class contains Credit Card Reward Points percentage
 *
 */
@Getter
@Setter
@ToString
@Component
@ConfigurationProperties(prefix = "rewards")
public class RewardPointsProperties {
 private Map<String, Double> businessGold;
 private Map<String, Double> moneyback;
 private Map<String, Double> platinumTimes;
 private Map<String, Double> worldMaster;
}

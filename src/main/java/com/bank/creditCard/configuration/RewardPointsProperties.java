package com.bank.creditCard.configuration;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Component
@ConfigurationProperties (prefix = "rewards")
public class RewardPointsProperties {

	
//	@Value ("${rewards.businessGold.groceries}")
//	private Double groceriesPercentBusiness;
//	
//	@Value ("${rewards.businessGold.fuel}")
//	private Double fuelPercentBusiness;
//	
//	@Value ("${rewards.businessGold.travel}")
//	private Double travelPercentBusiness;
//	
//	@Value ("${rewards.businessGold.shopping}")
//	private Double shoppingPercentBusiness;
//	
//	@Value ("${rewards.businessGold.other}")
//	private Double otherPercentBusiness;
//	
//	
//	@Value ("${rewards.moneyback.groceries}")
//	private Double groceriesPercentMoneyback;
//	
//	@Value ("${rewards.moneyback.fuel}")
//	private Double fuelPercentMoneyback;
//	
//	@Value ("${rewards.moneyback.travel}")
//	private Double travelPercentMoneyback;
//	
//	@Value ("${rewards.moneyback.shopping}")
//	private Double shoppingPercentMoneyback;
//	
//	@Value ("${rewards.moneyback.other}")
//	private Double otherPercentMoneyback;
//	
//	
//	@Value ("${rewards.worldMaster.groceries}")
//	private Double groceriesPercentWorldMaster;
//	
//	@Value ("${rewards.worldMaster.fuel}")
//	private Double fuelPercentWorldMaster;
//	
//	@Value ("${rewards.worldMaster.travel}")
//	private Double travelPercentWorldMaster;
//	
//	@Value ("${rewards.worldMaster.shopping}")
//	private Double shoppingPercentWorldMaster;
//	
//	@Value ("${rewards.worldMaster.other}")
//	private Double otherPercentWorldMaster;
//	
//	
//	
//	@Value ("${rewards.platinumTimes.groceries}")
//	private Double groceriesPercentPlatinum;
//	
//	@Value ("${rewards.platinumTimes.fuel}")
//	private Double fuelPercentPlatinum;
//	
//	@Value ("${rewards.platinumTimes.travel}")
//	private Double travelPercentPlatinum;
//	
//	@Value ("${rewards.platinumTimes.shopping}")
//	private Double shoppingPercentPlatinum;
//	
//	@Value ("${rewards.platinumTimes.other}")
//	private Double otherPercentPlatinum;
	
	private Map<String, Double> businessGold;
	private Map<String, Double> moneyback;
	private Map<String, Double> platinumTimes;
	private Map<String, Double> worldMaster;
}

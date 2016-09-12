/*
 * Copyright 2015-2016 Dapinder Singh, Inc. All Rights Reserved.
 *
*/
package com.dapinder.data.feed.test.vo.others;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder(value = { "orgn", "dstn", "airtcraftType", "equipment", "fltMonth", "fltRoute", "firstLeg", "legs",
		"rotations", "landings", "blockHours", "distance" })
public class RecordJSON {

	@JsonProperty("ORGN")
	private String orgn;

	@JsonProperty("DSTN")
	private String dstn;

	@JsonProperty("AIRCRAFT_TYPE")
	private String airtcraftType;

	@JsonProperty("EQUIPMENT")
	private String equipment;

	@JsonProperty("FLT_MONTH")
	private String fltMonth;

	@JsonProperty("FLT_ROUTE")
	private String fltRoute;

	@JsonProperty("FIRST_LEG")
	private String firstLeg;

	@JsonProperty("LEGS")
	private String legs;

	@JsonProperty("ROTATIONS")
	private String rotations;

	@JsonProperty("LANDINGS")
	private String landings;

	@JsonProperty("BLOCK_HOUR")
	private String blockHours;

	@JsonProperty("DISTANCE")
	private String distance;


	public String getOrgn() {
		return orgn;
	}


	public void setOrgn(String orgn) {
		this.orgn = orgn;
	}

	public String getDstn() {
		return dstn;
	}


	public void setDstn(String dstn) {
		this.dstn = dstn;
	}



	public void setAirtcraftType(String airtcraftType) {
		this.airtcraftType = airtcraftType;
	}


	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}


	public void setFltMonth(String fltMonth) {
		this.fltMonth = fltMonth;
	}


	public void setFltRoute(String fltRoute) {
		this.fltRoute = fltRoute;
	}


	public void setFirstLeg(String firstLeg) {
		this.firstLeg = firstLeg;
	}


	public void setLegs(String legs) {
		this.legs = legs;
	}


	public void setRotations(String rotations) {
		this.rotations = rotations;
	}


	public void setLandings(String landings) {
		this.landings = landings;
	}


	public void setBlockHours(String blockHours) {
		this.blockHours = blockHours;
	}


	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getAirtcraftType() {
		return airtcraftType;
	}

	public String getEquipment() {
		return equipment;
	}

	public String getFltMonth() {
		return fltMonth;
	}

	public String getFltRoute() {
		return fltRoute;
	}

	public String getFirstLeg() {
		return firstLeg;
	}

	public String getLegs() {
		return legs;
	}

	public String getRotations() {
		return rotations;
	}

	public String getLandings() {
		return landings;
	}

	public String getBlockHours() {
		return blockHours;
	}

	public String getDistance() {
		return distance;
	}

	@Override
	public String toString() {
		return "Record [orgn=" + orgn + ", dstn=" + dstn + ", airtcraftType=" + airtcraftType + ", equipment="
				+ equipment + ", fltMonth=" + fltMonth + ", fltRoute=" + fltRoute + ", firstLeg=" + firstLeg + ", legs="
				+ legs + ", rotations=" + rotations + ", landings=" + landings + ", blockHours=" + blockHours
				+ ", distance=" + distance + "]";
	}

}

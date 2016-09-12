/*
 * Copyright 2015-2016 Dapinder Singh, Inc. All Rights Reserved.
 *
*/
package com.dapinder.data.feed.test.vo.xls;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.dapinder.data.feed.annotations.PropertyLabel;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder(value = { "orgn", "dstn", "airtcraftType", "equipment", "fltMonth", "fltRoute", "firstLeg", "legs",
		"rotations", "landings", "blockHours", "distance" })
public class RecordXLS {


	@JsonProperty("ORGN")
	@NotNull
	@Size(min = 1)
	@PropertyLabel(value="Origin")
	private String orgn;

	@JsonProperty("DSTN")
	private String dstn;

	@JsonProperty("AIRCRAFT_TYPE")
	@PropertyLabel(value="Aircraft Type")
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
	private int legs;

	@JsonProperty("ROTATIONS")
	private double rotations;

	@JsonProperty("LANDINGS")
	private int landings;

	@JsonProperty("BLOCK_HOUR")
	private double blockHours;

	@JsonProperty("DISTANCE")
	private double distance;


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



	public int getLegs() {
		return legs;
	}


	public void setLegs(int legs) {
		this.legs = legs;
	}


	public double getRotations() {
		return rotations;
	}


	public void setRotations(double rotations) {
		this.rotations = rotations;
	}


	public int getLandings() {
		return landings;
	}


	public void setLandings(int landings) {
		this.landings = landings;
	}


	public double getBlockHours() {
		return blockHours;
	}


	public void setBlockHours(double blockHours) {
		this.blockHours = blockHours;
	}


	public double getDistance() {
		return distance;
	}


	public void setDistance(double distance) {
		this.distance = distance;
	}


	@Override
	public String toString() {
		return "Record [orgn=" + orgn + ", dstn=" + dstn + ", airtcraftType=" + airtcraftType + ", equipment="
				+ equipment + ", fltMonth=" + fltMonth + ", fltRoute=" + fltRoute + ", firstLeg=" + firstLeg + ", legs="
				+ legs + ", rotations=" + rotations + ", landings=" + landings + ", blockHours=" + blockHours
				+ ", distance=" + distance + "]";
	}

}

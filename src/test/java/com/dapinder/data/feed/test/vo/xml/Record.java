/*
 * Copyright 2015-2016 Dapinder Singh, Inc. All Rights Reserved.
 *
*/
package com.dapinder.data.feed.test.vo.xml;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.dapinder.data.feed.annotations.PropertyLabel;

@XmlRootElement(name = "record")
public class Record {

	@NotNull
	@Size(max = 3)
	@PropertyLabel(value="Origin")
	private String orgn;
	private String dstn;
	private String airtcraftType;
	private String equipment;
	private String fltMonth;
	private String fltRoute;
	private String firstLeg;
	private String legs;
	private String rotations;
	private String landings;
	private String blockHours;
	private String distance;


	public String getOrgn() {
		return orgn;
	}

	@XmlElement(name = "ORGN")
	public void setOrgn(String orgn) {
		this.orgn = orgn;
	}

	public String getDstn() {
		return dstn;
	}

	@XmlElement(name = "DSTN")
	public void setDstn(String dstn) {
		this.dstn = dstn;
	}


	@XmlElement(name = "AIRCRAFT_TYPE")
	public void setAirtcraftType(String airtcraftType) {
		this.airtcraftType = airtcraftType;
	}

	@XmlElement(name = "EQUIPMENT")
	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}

	@XmlElement(name = "FLT_MONTH")
	public void setFltMonth(String fltMonth) {
		this.fltMonth = fltMonth;
	}

	@XmlElement(name = "FLT_ROUTE")
	public void setFltRoute(String fltRoute) {
		this.fltRoute = fltRoute;
	}

	@XmlElement(name = "FIRST_LEG")
	public void setFirstLeg(String firstLeg) {
		this.firstLeg = firstLeg;
	}

	@XmlElement(name = "LEGS")
	public void setLegs(String legs) {
		this.legs = legs;
	}

	@XmlElement(name = "ROTATIONS")
	public void setRotations(String rotations) {
		this.rotations = rotations;
	}

	@XmlElement(name = "LANDINGS")
	public void setLandings(String landings) {
		this.landings = landings;
	}

	@XmlElement(name = "BLOCK_HOUR")
	public void setBlockHours(String blockHours) {
		this.blockHours = blockHours;
	}

	@XmlElement(name = "DISTANCE")
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

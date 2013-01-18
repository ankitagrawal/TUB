package com.hk.constants.inventory;

import java.util.List;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Jan 12, 2013
 * Time: 9:43:15 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumCycleCountStatus {

	Start(10L, "Start"),
	InProgress(20L, "InProgress"),
	RequestForApproval(30L, "PendingForApproval"),
	Approved(99L, "Approved"),
	Closed(999L, "Closed");

	private String name;
	private Long id;

	EnumCycleCountStatus(Long id, String name) {
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public Long getId() {
		return id;
	}

	public static List<EnumCycleCountStatus> getAllList() {
		return Arrays.asList(
				EnumCycleCountStatus.Start,
				EnumCycleCountStatus.InProgress,
				EnumCycleCountStatus.RequestForApproval,
				EnumCycleCountStatus.Approved,
				EnumCycleCountStatus.Closed);

	}
}

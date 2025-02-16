package com.devsu.hackerearth.backend.account.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Transaction extends Base {

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	private String type;
	private double amount;
	private double balance;

	@ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
}

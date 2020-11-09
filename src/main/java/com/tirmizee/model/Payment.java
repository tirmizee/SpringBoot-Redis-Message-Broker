package com.tirmizee.model;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment implements Serializable {

	private static final long serialVersionUID = 1L;

	private String no;
	private String investerNo;
	private BigDecimal amount;
	
}

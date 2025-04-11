package com.linq.website.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "company_page_metadata")
public class CompanyPageMetaData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String companyName;
	private String joinCompanyDesc;
	private String participateNowText;
	private String buttonURL;
}
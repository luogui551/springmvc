package com.aires.base.bean;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;
/**
 * 公共实体类
 * @author User
 *
 */
@MappedSuperclass
public class Bean {
	
	private String id;

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}	
}

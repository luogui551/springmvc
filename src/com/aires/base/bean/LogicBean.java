/**
 * 
 */
package com.aires.base.bean;

import javax.persistence.MappedSuperclass;

/**
 * @author aires
 * 2017-2-28 下午5:17:18
 * 描述：继承该类的实体使用逻辑删除
 */
@MappedSuperclass
public class LogicBean extends Bean{
	/**
	 * 是否已删除
	 */
	private boolean deleted;

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		if(deleted != null)
			this.deleted = deleted;
	}
	
}

package com.aires.service;

import com.aires.bean.Coin;

/**
 * @author aires
 * 2017年9月21日 下午3:53:11
 * 描述：
 */
public interface CoinService {
	/**
	 * 获取指定用户的货币信息
	 * @param userId
	 * @return
	 */
	Coin get(String userId);
	/**
	 * 为指定用户添加指定数量的货币
	 * @param userId
	 * @param count
	 * @return
	 */
	Coin add(String userId, int count);
	/**
	 * 扣除指定用户指定数据的货币
	 * @param userId
	 * @param count
	 * @return
	 */
	Coin subtract(String userId, int count);
	/**
	 * 付款
	 * @param fromUserId 付款方
	 * @param toUserId 收款方
	 * @param count 金额
	 */
	void pay(String fromUserId, String toUserId, int count);
}

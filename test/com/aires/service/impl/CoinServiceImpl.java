package com.aires.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.aires.bean.Coin;
import com.aires.dao.CoinDao;
import com.aires.hibernate.dao.SpringBasedHibernateDAO;
import com.aires.hibernate.service.HibernateService;
import com.aires.service.CoinService;

/**
 * @author aires
 * 2017年9月21日 下午3:53:29
 * 描述：
 */
@Service
public class CoinServiceImpl extends HibernateService<Coin> implements CoinService{

	@Resource
	private CoinDao dao;
	
	@Override
	public Coin get(String userId) {
		return sql().sqlOne(Coin.class, "select * from P_USER_COIN where userid=?", userId);
	}

	@Override
	public Coin add(String userId, int count) {
		int row = dao.sqlUpdate("update P_USER_COIN set count=count+? where userid=?", userId);
		if(row == 0){
			dao.sqlUpdate("insert into P_USER_COIN(id,userid,count) values('2', ?, ?)", userId, count);
		}
		return null;
	}

	@Override
	public Coin subtract(String userId, int count) {
		
		return null;
	}

	@Override
	protected SpringBasedHibernateDAO getDao() {
		return dao;
	}

	@Override
	public void pay(String fromUserId, String toUserId, int count) {
		// TODO Auto-generated method stub
		
	}


}

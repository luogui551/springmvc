package com.aires.ctrl;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aires.bean.Coin;
import com.aires.service.CoinService;

/**
 * @author aires
 * 2017年9月21日 下午4:10:08
 * 描述：
 */
@Controller
@RequestMapping("/coin")
public class CoinController {
	@Resource
	private CoinService coinService;
	
	@RequestMapping("/get")
	@ResponseBody
	public Coin get(String userId){
		Coin c = coinService.get(userId);
		c.setCount(100);
		return c;
	}
	@RequestMapping("/add")
	@ResponseBody
	public Coin add(String userId, int count){
		Coin coin = coinService.save(new Coin(userId, count));
		System.out.println("ctrl: " + coin.getId());
		return coin;
//		return coinService.add(userId, count);
	}
	@RequestMapping("/subtract")
	@ResponseBody
	public Coin subtract(String userId, int count){
		return coinService.subtract(userId, count);
	}
	@RequestMapping("/pay")
	@ResponseBody
	public void pay(String fromUserId, String toUserId, int count){
		coinService.pay(fromUserId, toUserId, count);
	}
}

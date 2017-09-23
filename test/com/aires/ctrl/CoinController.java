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
		return coinService.get(userId);
	}
}

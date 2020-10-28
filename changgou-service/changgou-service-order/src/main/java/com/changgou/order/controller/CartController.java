package com.changgou.order.controller;

import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import entity.Result;
import entity.StatusCode;
import entity.TokenDecode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private TokenDecode tokenDecode;

    /***
     * 加入购物车
     * @param num:购买的数量
     * @param id：购买的商品(SKU)ID
     * @return
     */
    @PreAuthorize("hasAnyAuthority('user')")
    @RequestMapping(value = "/add")
    public Result add(Integer num, Long id) {
        //用户名
        String username = tokenDecode.getUserInfo().get("username");
        //将商品加入购物车
        cartService.add(num, id, username);
        return new Result(true, StatusCode.OK, "加入购物车成功！");
    }

    /***
     * 查询用户购物车列表
     * @return
     */
    @PreAuthorize("hasAnyAuthority('user')")
    @GetMapping(value = "/list")
    public Result list() {
        //用户名
        String username = tokenDecode.getUserInfo().get("username");
        List<OrderItem> orderItems = cartService.list(username);
        return new Result(true, StatusCode.OK, "购物车列表查询成功！", orderItems);
    }

}


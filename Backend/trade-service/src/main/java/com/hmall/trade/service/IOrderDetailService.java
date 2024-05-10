package com.hmall.trade.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hmall.trade.domain.po.OrderDetail;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 订单详情表 服务类
 * </p>
 *
 * @author 虎哥
 * @since 2023-05-05
 */
public interface IOrderDetailService extends IService<OrderDetail> {

    List<OrderDetail> getByOrderId(Long orderId);
}

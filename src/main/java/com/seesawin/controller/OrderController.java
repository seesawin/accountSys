package com.seesawin.controller;

import com.seesawin.models.Orderdetail;
import com.seesawin.models.Ordermain;
import com.seesawin.payload.request.GetOrdeByUserNamerRequest;
import com.seesawin.payload.request.OrderRequest;
import com.seesawin.payload.response.CommonResponse;
import com.seesawin.repository.OrderdetailMapper;
import com.seesawin.repository.OrdermainMapper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrdermainMapper ordermainMapper;

    @Autowired
    private OrderdetailMapper orderdetailMapper;

    @PostMapping("/save")
    @ApiOperation(
            value = "新增訂單",
            notes = "資料新增、刪除、修改時，需要使用POST",
            response = CommonResponse.class)
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> save(@Valid @RequestBody OrderRequest orderRequest) {
        log.info("orderRquest: ", orderRequest);

        // calculate the total price
        Integer totalPrice = orderRequest.getOrderDetialRequestList().stream()
                .map(od -> od.getPrice())
                .reduce(0, Integer::sum);

        // add order
        Ordermain order = new Ordermain();
        order.setUsername(orderRequest.getUsername());
        order.setTotalprice(totalPrice.intValue());
        int row = ordermainMapper.insert(order);
        log.info("row: ", row);
        int orderNo = order.getOrderno();
        log.info("orderNo: ", orderNo);

        // add order details
        orderRequest.getOrderDetialRequestList().forEach(od -> {
            Orderdetail detail = new Orderdetail();
            detail.setCount(od.getCount());
            detail.setName(od.getName());
            detail.setPrice(od.getPrice());
            detail.setOrderno(orderNo);
            orderdetailMapper.insert(detail);
        });

        Map<String, Object> resultParam = new HashMap<>();
        resultParam.put("orderNo", orderNo);

        CommonResponse response = new CommonResponse();
        response.setCode("00");
        response.setMsg("sucess");
        response.setData(resultParam);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/orderNo/{orderNo}")
    @ApiOperation(
            value = "根據username查詢訂單(restful)",
            notes = "資料查詢時使用GET",
            response = CommonResponse.class)
    public ResponseEntity<?> getOrder(@Valid @PathVariable int orderNo) {
        Ordermain ordermain = ordermainMapper.selectByPrimaryKey(orderNo);
        List<Orderdetail> orderdetailList = orderdetailMapper.selectByOrderNo(orderNo);

        Map<String, Object> resultParam = new HashMap<>();
        resultParam.put("ordermain", ordermain);
        resultParam.put("orderdetailList", orderdetailList);

        CommonResponse response = new CommonResponse();
        response.setCode("00");
        response.setMsg("sucess");
        response.setData(resultParam);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/username/{username}")
    @ApiOperation(
            value = "根據username查詢訂單(restful)",
            notes = "資料查詢時使用GET",
            response = CommonResponse.class)
    public ResponseEntity<?> getOrder(@Valid @PathVariable String username) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        List<Ordermain> ordermainList = ordermainMapper.selectByUsername(username);
        ordermainList.forEach(odMain -> {
            Map<String, Object> orderMap = new HashMap<>();
            List<Orderdetail> orderdetailList = orderdetailMapper.selectByOrderNo(odMain.getOrderno());
            orderMap.put("ordermain", odMain);
            orderMap.put("orderdetailList", orderdetailList);
            resultList.add(orderMap);
        });

        Map<String, Object> resultParam = new HashMap<>();
        resultParam.put("orderList", resultList);

        CommonResponse response = new CommonResponse();
        response.setCode("00");
        response.setMsg("sucess");
        response.setData(resultParam);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/username")
    @ApiOperation(
            value = "根據username查詢訂單(queryString)",
            notes = "資料查詢時使用GET",
            response = CommonResponse.class)
    public ResponseEntity<?> getOrderByQueryString(@Valid @RequestParam String username) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        List<Ordermain> ordermainList = ordermainMapper.selectByUsername(username);
        ordermainList.forEach(odMain -> {
            Map<String, Object> orderMap = new HashMap<>();
            List<Orderdetail> orderdetailList = orderdetailMapper.selectByOrderNo(odMain.getOrderno());
            orderMap.put("ordermain", odMain);
            orderMap.put("orderdetailList", orderdetailList);
            resultList.add(orderMap);
        });

        Map<String, Object> resultParam = new HashMap<>();
        resultParam.put("orderList", resultList);

        CommonResponse response = new CommonResponse();
        response.setCode("00");
        response.setMsg("sucess");
        response.setData(resultParam);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/username")
    @ApiOperation(
            value = "根據username查詢訂單(data in the post's body)",
            notes = "資料查詢時使用GET",
            response = CommonResponse.class)
    public ResponseEntity<?> getOrderByPost(@Valid @RequestBody GetOrdeByUserNamerRequest getOrdeByUserNamerRequest) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        List<Ordermain> ordermainList = ordermainMapper.selectByUsername(getOrdeByUserNamerRequest.getUsername());
        ordermainList.forEach(odMain -> {
            Map<String, Object> orderMap = new HashMap<>();
            List<Orderdetail> orderdetailList = orderdetailMapper.selectByOrderNo(odMain.getOrderno());
            orderMap.put("ordermain", odMain);
            orderMap.put("orderdetailList", orderdetailList);
            resultList.add(orderMap);
        });

        Map<String, Object> resultParam = new HashMap<>();
        resultParam.put("orderList", resultList);

        CommonResponse response = new CommonResponse();
        response.setCode("00");
        response.setMsg("sucess");
        response.setData(resultParam);
        return ResponseEntity.ok(response);
    }
}




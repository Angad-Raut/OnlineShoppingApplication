package com.projectx.microservice.paymentservice.service;

import com.projectx.microservice.exceptions.AlreadyExistException;
import com.projectx.microservice.exceptions.ResourceNotFoundException;
import com.projectx.microservice.payloads.EntityIdDto;
import com.projectx.microservice.payloads.PaymentAmountDto;
import com.projectx.microservice.payloads.ResponseDto;
import com.projectx.microservice.paymentservice.entity.PaymentDetails;
import com.projectx.microservice.paymentservice.feignconfig.OrderFeignClient;
import com.projectx.microservice.paymentservice.payloads.PaymentDto;
import com.projectx.microservice.paymentservice.repository.PaymentRepository;
import com.razorpay.Order;
import com.razorpay.OrderClient;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderFeignClient orderClient;

    private static final String RZP_KEY_ID="";
    private static final String RZP_KEY_SECRET="";
    private static final String RZP_CURRENCY="INR";
    private static final String RZP_COMPANY_NAME="Swarali Online Shop";
    private static final String UNPAID_STATUS="Unpaid";
    private static final String PAID_STATUS="Paid";
    private static final String ONLINE_TYPE="Online";
    private static final String OFFLINE_TYPE="Offline";

    @Override
    public Long savePaymentDetails(PaymentAmountDto dto) {
        try {
             PaymentDetails paymentDetails = paymentRepository.save(PaymentDetails.builder()
                     .paymentAmount(dto.getPaymentAmount())
                     .paymentDate(new Date())
                     .paymentStatus(UNPAID_STATUS)
                     .build());
             return paymentDetails.getId();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Boolean doOfflinePayment(PaymentDto dto) throws ResourceNotFoundException{
        PaymentDetails paymentDetails = paymentRepository.findPaymentDetailsById(dto.getPaymentId());
        if (paymentDetails==null) {
            throw new ResourceNotFoundException("Payment details not found");
        }else if (paymentDetails.getPaymentStatus().equals(PAID_STATUS)) {
            throw new AlreadyExistException("Payment already done");
        } else {
            paymentDetails.setPaymentAmount(dto.getPaymentAmount());
            paymentDetails.setPaymentDate(new Date());
            paymentDetails.setPaymentStatus(PAID_STATUS);
            paymentDetails.setPaymentType(OFFLINE_TYPE);
            PaymentDetails payments = paymentRepository.save(paymentDetails);
            return paymentDetails!=null?updateOrderStatus(dto.getOrderId()):false;
        }
    }

    @Override
    public String doOnlinePayment(PaymentDto dto){
        String orderId=null;
        PaymentDetails paymentDetails = paymentRepository.findPaymentDetailsById(dto.getPaymentId());
        if (paymentDetails.getPaymentStatus().equals(PAID_STATUS)) {
            throw new AlreadyExistException("Payment already done");
        } else {
            try {
                RazorpayClient razorpay = new RazorpayClient(RZP_KEY_ID, RZP_KEY_SECRET);
                JSONObject orderRequest = new JSONObject();
                orderRequest.put("amount", dto.getPaymentAmount());
                orderRequest.put("currency", RZP_CURRENCY);
                orderRequest.put("receipt", "order_rcptid_11");

                Order order = razorpay.orders.create(orderRequest);
                orderId = order.get("id");

                paymentDetails.setPaymentAmount(dto.getPaymentAmount());
                paymentDetails.setPaymentDate(new Date());
                paymentDetails.setPaymentStatus(PAID_STATUS);
                paymentDetails.setPaymentType(ONLINE_TYPE);
                paymentDetails.setRazorpayPaymentId(orderId);
                PaymentDetails payments = paymentRepository.save(paymentDetails);
                updateOrderStatus(dto.getOrderId());
            } catch (RazorpayException e) {
                System.out.println(e.getMessage());
            }
        }
        return orderId;
    }
    private Boolean updateOrderStatus(Long orderId) {
        ResponseEntity<ResponseDto<Boolean>> response = orderClient.updateOrderStatus(new EntityIdDto(orderId));
        return response.getBody().getResult()!=null && response.getBody().getResult()?true:false;
    }
}

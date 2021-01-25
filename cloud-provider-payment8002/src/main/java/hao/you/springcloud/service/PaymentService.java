package hao.you.springcloud.service;

import hao.you.springcloud.entities.Payment;

public interface PaymentService {
    int create(Payment payment); //写

    Payment getPaymentById(Long id);  //读取
}

package kr.co.mapspring.payment.service;

import java.util.List;
import kr.co.mapspring.payment.dto.AdminPaymentDto;

public interface AdminPaymentService {
    List<AdminPaymentDto.ResponseList> getPaymentList();
    AdminPaymentDto.ResponseStats getPaymentStats();
}
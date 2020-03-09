package aqashop.data;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class PaymentEntity {
    private String id;
    private int amount;
    private LocalDateTime created;
    private String status;
    private String transaction_id;
}

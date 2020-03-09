package aqashop.data;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class OrderEntity {
    private String id;
    private LocalDateTime created;
    private String credit_id;
    private String payment_id;
}

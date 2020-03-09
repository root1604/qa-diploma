package aqashop.data;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class CreditEntity {
    private String id;
    private String bank_id;
    private LocalDateTime created;
    private String status;

}

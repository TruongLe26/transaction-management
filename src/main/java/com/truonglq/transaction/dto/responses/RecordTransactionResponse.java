package com.truonglq.transaction.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecordTransactionResponse {
    @JsonProperty("transaction_id")
    private String transactionId;
    @JsonProperty("user_id")
    private String userId;
    private String role;
    private BigDecimal amount;
    @JsonProperty("sender_old_balance")
    private BigDecimal senderOldBalance;
    @JsonProperty("receiver_old_balance")
    private BigDecimal receiverOldBalance;
    @JsonProperty("sender_new_balance")
    private BigDecimal senderNewBalance;
    @JsonProperty("receiver_new_balance")
    private BigDecimal receiverNewBalance;
}

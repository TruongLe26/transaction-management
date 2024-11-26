package com.truonglq.transaction.dto.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.truonglq.transaction.model.enums.TransactionType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionRequest {
    @JsonProperty("sender_account_id")
    String senderAccountId;
    @JsonProperty("receiver_account_id")
    String receiverAccountId;
    BigDecimal amount;
    TransactionType type;
}

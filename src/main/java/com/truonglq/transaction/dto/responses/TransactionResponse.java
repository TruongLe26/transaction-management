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
public class TransactionResponse {
    @JsonProperty("sender_account_number")
    String senderAccountNumber;
    @JsonProperty("receiver_account_number")
    String receiverAccountNumber;
    @JsonProperty("transaction_id")
    String transactionId;
    @JsonProperty("transaction_type")
    String transactionType;
    String status;
    @JsonProperty("created_at")
    String createdAt;
    @JsonProperty("sender_old_balance")
    BigDecimal senderOldBalance;
    @JsonProperty("receiver_old_balance")
    BigDecimal receiverOldBalance;
    @JsonProperty("sender_new_balance")
    BigDecimal senderNewBalance;
    @JsonProperty("receiver_new_balance")
    BigDecimal receiverNewBalance;
}

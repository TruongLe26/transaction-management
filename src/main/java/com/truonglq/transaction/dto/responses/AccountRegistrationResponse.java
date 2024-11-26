package com.truonglq.transaction.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountRegistrationResponse {
    String name;
    @JsonProperty("account_number")
    String accountNumber;
}

package edu.kollozsolt.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(
        name = "Accounts",
        description = "Schema to hold Account information"
)
public class AccountsDto {

    @Schema(
            description = "Account number of Easy Bank account",
            example = "0123456789"
    )
    @NotEmpty(message = "Account number cannot be null or empty!")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Account number must be 10 digits!")
    private Long accountNumber;

    @Schema(
            description = "Account type of Easy Bank account",
            example = "Saving"
    )
    @NotEmpty(message = "Account type cannot be null or empty!")
    private String accountType;

    @Schema(
            description = "Easy Bank account",
            example = "123 New York"
    )
    @NotEmpty(message = "Account address cannot be null or empty!")
    private String branchAddress;
}

package edu.kollozsolt.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
@Schema(
        name = "Loans",
        description = "Schema to hold Loan information"
)
public class LoansDto {

    @Schema(
            description = "Mobile number of the customer",
            example = "0712345678"
    )
    @NotEmpty(message = "Mobile number cannot be null or empty")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits!")
    private String mobileNumber;

    @Schema(
            description = "Loan number of the customer",
            example = "012345678901"
    )
    @NotEmpty(message = "Loan number cannot be a null or empty")
    @Pattern(regexp="(^$|[0-9]{12})", message = "LoanNumber must be 12 digits")
    private String loanNumber;

    @Schema(
            description = "Type of the loan",
            example = "Home Loan"
    )
    @NotEmpty(message = "Loan type can not be a null or empty")
    private String loanType;

    @Schema(
            description = "Total loan ammount",
            example = "100000"
    )
    @Positive(message = "Total loan should be greater than zero")
    private int totalLoan;

    @Schema(
            description = "Total loan amount paid",
            example = "1000"
    )
    @PositiveOrZero(message = "Total loan amount paid should be equal or greater than zero")
    private int amountPaid;

    @PositiveOrZero(message = "Total outstanding amount should be equal or greater than zero")
    @Schema(
            description = "Total outstanding amount against a loan", example = "99000"
    )
    private int outstandingAmount;
}

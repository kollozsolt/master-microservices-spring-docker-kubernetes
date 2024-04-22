package edu.kollozsolt.cards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
@Schema(
        name = "Cards",
        description = "Schema to hold Card information"
)
public class CardsDto {

    @Schema(
            description = "Mobile number of the customer",
            example = "0712345678"
    )
    @NotEmpty(message = "Mobile number cannot be null or empty")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits!")
    private String mobileNumber;

    @Schema(
            description = "Card number of the customer",
            example = "012345678901"
    )
    @NotEmpty(message = "Card number cannot be a null or empty")
    @Pattern(regexp="(^$|[0-9]{12})", message = "CardNumber must be 12 digits")
    private String cardNumber;

    @Schema(
            description = "Type of the card",
            example = "Credit Card"
    )
    @NotEmpty(message = "Card type can not be a null or empty")
    private String cardType;

    @Schema(
            description = "Total amount limit available against a card",
            example = "100000"
    )
    @Positive(message = "Total card limit should be greater than zero")
    private int totalLimit;

    @Schema(
            description = "Total amount used by a Customer",
            example = "1000"
    )
    @PositiveOrZero(message = "Total amount used should be equal or greater than zero")
    private int amountUsed;

    @Schema(
            description = "Total available amount against a card",
            example = "90000"
    )
    @PositiveOrZero(message = "Total available amount should be equal or greater than zero")
    private int availableAmount;
}

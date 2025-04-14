package on.ssgdeal.user_service.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import on.ssgdeal.user_service.exception.destination.DestinationException;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    @Column(name = "address", nullable = false)
    private String address;

    public Address(final String address) {
        validate(address);
        this.address = address;
    }

    public static Address valueOf(String address) {
        return new Address(address);
    }

    private void validate(final String address) {
        if (Objects.isNull(address) || address.isBlank()) {
            throw new DestinationException.DestinationAddressIsNullException();
        }
    }

    @Override
    public String toString() {
        return address;
    }
}
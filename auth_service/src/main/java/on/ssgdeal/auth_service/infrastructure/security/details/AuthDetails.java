package on.ssgdeal.auth_service.infrastructure.security.details;

import java.io.Serializable;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthDetails extends UserDetails, Serializable {

}

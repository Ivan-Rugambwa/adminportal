package almroth.kim.seat_api.error.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorResponse extends ErrorResponse {
    private ArrayList<String> errors = new ArrayList<>();

}

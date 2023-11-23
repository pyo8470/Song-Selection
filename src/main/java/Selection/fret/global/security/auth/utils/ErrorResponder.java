package Selection.fret.global.security.auth.utils;

import Selection.fret.global.response.ErrorResponse;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class ErrorResponder {
    public static void sendErrorResponse(HttpServletResponse response, HttpStatus status) throws IOException{
        Gson gson = new Gson();
        ErrorResponse errorResponse = ErrorResponse.of(status);
    }
}
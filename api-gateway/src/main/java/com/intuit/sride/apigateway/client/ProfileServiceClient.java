package com.intuit.sride.apigateway.client;

import com.intuit.sride.apigateway.api.response.AuthorizationResponse;
import feign.Param;
import feign.RequestLine;
import jakarta.ws.rs.ext.ParamConverter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

//@ClientMetadata(serviceName = "profile-service")
//@FeignClient(name = "reference-data", url = "${apigee.url}", configuration = ApigeeFeignConfiguration.class)
@FeignClient(name = "PROFILE-SERVICE/profile")
public interface ProfileServiceClient {

    @RequestMapping(method = RequestMethod.GET , value = "/users/{userId}/authorised") // Define the appropriate endpoint and HTTP method
    AuthorizationResponse isAuthorised(@PathVariable("userId") String userId, @RequestParam("resource") String resource, @RequestParam("username") String username);
}

package com.proton.controller.resources.auth.permissions;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("protoon/funcionario")
@Tag(name = "Funcionario")
public class FuncionarioPermissions {


    @Operation(
            description = "Get endpoint for funcionario",
            summary = "This is a summary for funcionario get endpoint",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    )
            }

    )
    @GetMapping
    public String get() {
        return "GET:: funcionario controller";
    }
    @PostMapping
    public String post() {
        return "POST:: funcionario_ controller";
    }
    @PutMapping
    public String put() {
        return "PUT:: funcionario controller";
    }
    @DeleteMapping
    public String delete() {
        return "DELETE:: funcionario controller";
    }
}

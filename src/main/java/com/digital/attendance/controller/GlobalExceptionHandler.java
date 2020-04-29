/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digital.attendance.controller;

import com.digital.attendance.model.User;
import com.digital.attendance.response.ApiResponse;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 *
 * @author oreoluwa
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{
    
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ApiResponse<String> springHandleNotFound(HttpServletResponse response, Exception ex) throws IOException {
        ApiResponse<String> error = new ApiResponse<>();
        error.setResponse(ex.getMessage());
        error.setStatus(HttpStatus.NOT_FOUND);
        error.setError("Error Found");
        error.setResponsecode("99");
        return error;
    }
    
}

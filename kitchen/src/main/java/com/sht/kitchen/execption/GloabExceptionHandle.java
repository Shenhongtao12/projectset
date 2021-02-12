package com.sht.kitchen.execption;

import com.sht.kitchen.common.RestResponse;
import com.sht.kitchen.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * @author Aaron
 * @date 2020/11/26 19:59
 */
@RestControllerAdvice
public class GloabExceptionHandle extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GloabExceptionHandle.class);

    /**
     * 处理请求参数格式错误 @RequestBody上validate失败后抛出的异常
     *
     * @param e MethodArgumentNotValidException
     * @return
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public RestResponse handleParamException(Exception e) {
        LOGGER.error(e.getMessage());

        MethodArgumentNotValidException exception = (MethodArgumentNotValidException) e;
        BindingResult bindingResult = exception.getBindingResult();
        if (bindingResult.hasErrors() && bindingResult.getAllErrors().size() > 0) {
            String defaultMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return ERROR(400, defaultMessage);
        } else {
            return ERROR(400, "参数不合法");
        }

    }

    /**
     * 处理Get请求中 使用@Valid 验证路径中请求实体校验失败后抛出的异常，详情继续往下看代码
     *
     * @param e BindException
     * @return
     */
    @ExceptionHandler(BindException.class)
    public RestResponse BindExceptionHandler(BindException e) {
        String result = "参数不合法";
        LOGGER.error(e.getMessage());
        if (e.getAllErrors() != null && !e.getAllErrors().isEmpty()) {
            result = e.getAllErrors().get(0).getDefaultMessage();
        }
        return ERROR(400, result);
    }

    /**
     * 处理请求参数格式错误 @RequestParam上validate失败后抛出的异常
     *
     * @param e ConstraintViolationException
     * @return must not be null
     */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = ConstraintViolationException.class)
    public RestResponse handleUnauthorized(ConstraintViolationException e) {
        LOGGER.error(e.getMessage());
        String result = "参数不合法.";
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        if (!constraintViolations.isEmpty()) {
            result = constraintViolations.stream().findFirst().get().getMessage();
        }
        return ERROR(400, result);
    }

    /**
     * 参数类型转换错误
     *
     * @param e
     * @return
     */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {ConversionFailedException.class, MethodArgumentTypeMismatchException.class})
    public RestResponse handleUnauthorized(Exception e) {
        LOGGER.error(e.getMessage(), e);
        String result = " 参数类型不正确.";
        return ERROR(400, result);
    }


    @ExceptionHandler(value = Exception.class)
    @ResponseStatus()
    public RestResponse handleException(Exception e) {
        LOGGER.error(e.getMessage(), e);
        RestResponse restResponse = new RestResponse();
        return ERROR("服务器异常，请稍后再试.");
    }
}

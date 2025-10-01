package com.bitejiuyeke.bitecommonsecurity.handler;

import com.bitejiuyeke.bitecommondomain.constants.CommonConstants;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.domain.ResultCode;
import com.bitejiuyeke.bitecommondomain.exception.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.stream.Collectors;

// @RestControllerAdvice = @ResponseBody + @ControllerAdvice
// @ControllerAdvice 是 Spring 官方定义的 AOP 策略，其切点是全部的 Controller，通知是类中的 @ExceptionHandler 修饰的方法
// 但是其实现的原理与 @Aspect 的 AOP 策略不同，这一点需加以区分
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 取异常错误码的前三位设置响应http报文的状态码
     *
     * @param response  http响应报文对象
     * @param errorCode 异常错误码（六位）
     */
    private void setResponseCode(HttpServletResponse response, Integer errorCode) {
        int httpCode = Integer.parseInt(String.valueOf(errorCode).substring(0, 3));
        response.setStatus(httpCode);
    }

    /**
     * 请求方式不支持
     *
     * @param e        异常信息
     * @param request  请求 http 报文
     * @param response 响应 http 报文
     * @return 异常结果
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public R<?> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        log.error("请求地址：{}，不支持请求：{}", requestURI, e.getMethod());

        setResponseCode(response, ResultCode.REQUEST_METNHOD_NOT_SUPPORTED.getCode());
        return R.fail(ResultCode.REQUEST_METNHOD_NOT_SUPPORTED.getCode(),
                ResultCode.REQUEST_METNHOD_NOT_SUPPORTED.getMsg());

    }

    /**
     * 请求参数类型匹配不一致
     *
     * @param e        异常信息
     * @param request  请求 http 报文
     * @param response 响应 http 报文
     * @return 异常结果
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public R<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e,
                                                          HttpServletRequest request,
                                                          HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        log.error("请求地址：{}，类型匹配不一致：", requestURI, e);

        setResponseCode(response, ResultCode.PARA_TYPE_MISMATCH.getCode());
        return R.fail(ResultCode.PARA_TYPE_MISMATCH.getCode(), ResultCode.PARA_TYPE_MISMATCH.getMsg());

    }

    /**
     * 资源不存在异常（url 不存在）
     *
     * @param e        异常信息
     * @param request  请求 http 报文
     * @param response 响应 http 报文
     * @return 异常结果
     */
    @ExceptionHandler({NoResourceFoundException.class})
    public R<?> handlerNoResourceFoundException(NoResourceFoundException e,
                                                HttpServletRequest request,
                                                HttpServletResponse response) {

        String requestURI = request.getRequestURI();
        log.error("请求地址：{}，url 不存在：", requestURI, e);

        setResponseCode(response, ResultCode.URL_NOT_FOUND.getCode());
        return R.fail(ResultCode.URL_NOT_FOUND.getCode(), ResultCode.URL_NOT_FOUND.getMsg());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        log.error("请求地址：{}，方法参数不匹配：{}", requestURI, e.getMessage());

        String retMsg = joinMessage(e);
        setResponseCode(response, ResultCode.INVALID_PARA.getCode());
        return R.fail(ResultCode.INVALID_PARA.getCode(),
                !retMsg.isBlank() ? retMsg : ResultCode.INVALID_PARA.getMsg());
    }

    private String joinMessage(MethodArgumentNotValidException e) {
        List<ObjectError> allErrors = e.getAllErrors();
        if (CollectionUtils.isEmpty(allErrors)) {
            return "";
        }
        return allErrors.stream().map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(CommonConstants.DEFAULT_DELIMITER));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public R<?> handleConstraintViolationException(ConstraintViolationException e,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        log.error("请求地址：{}，方法参数不匹配：{}", requestURI, e.getMessage());

        setResponseCode(response, ResultCode.INVALID_PARA.getCode());
        String retMsg = e.getMessage();
        return R.fail(ResultCode.INVALID_PARA.getCode(),
                !retMsg.isBlank() ? retMsg : ResultCode.INVALID_PARA.getMsg());
    }

    @ExceptionHandler({ServiceException.class})
    public R<?> handlerServiceException(ServiceException e,
                                        HttpServletRequest request,
                                        HttpServletResponse response) {
        Integer code = e.getCode() == null ? ResultCode.ERROR.getCode() : e.getCode();
        String msg = e.getMsg() == null ? ResultCode.ERROR.getMsg() : e.getMsg();
        setResponseCode(response, code);
        return R.fail(code, msg);
    }

    /**
     * 拦截运行时异常，为运行时异常进行兜底
     *
     * @param e        异常信息
     * @param request  请求 http 报文
     * @param response 响应 http 报文
     * @return 异常结果
     */
    @ExceptionHandler({RuntimeException.class})
    public R<?> handleRuntimeException(RuntimeException e,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {

        String requestURI = request.getRequestURI();
        log.error("请求地址：{}，运行时异常：", requestURI, e);

        setResponseCode(response, ResultCode.SERVICE_NOT_FOUND.getCode());
        return R.fail();
    }

    /**
     * 拦截除上述以外的所有异常，最后的兜底拦截
     *
     * @param e        异常信息
     * @param request  请求 http 报文
     * @param response 响应 http 报文
     * @return 异常结果
     */
    @ExceptionHandler({Exception.class})
    public R<?> handleException(Exception e,
                                HttpServletRequest request,
                                HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        log.error("请求地址：{}，异常：", requestURI, e);

        setResponseCode(response, ResultCode.ERROR.getCode());
        return R.fail();
    }


}

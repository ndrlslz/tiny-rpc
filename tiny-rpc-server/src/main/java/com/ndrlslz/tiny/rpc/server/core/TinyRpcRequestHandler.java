package com.ndrlslz.tiny.rpc.server.core;

import com.ndrlslz.tiny.rpc.core.exception.TinyRpcException;
import com.ndrlslz.tiny.rpc.core.protocol.TinyRpcRequest;
import com.ndrlslz.tiny.rpc.core.protocol.TinyRpcResponse;
import com.ndrlslz.tiny.rpc.core.utils.ReflectUtils;
import io.netty.channel.ChannelHandlerContext;
import io.reactivex.Single;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static java.util.Objects.isNull;

public class TinyRpcRequestHandler implements RequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(TinyRpcRequestHandler.class);
    private Object serviceImpl;

    TinyRpcRequestHandler(Object serviceImpl) {
        this.serviceImpl = serviceImpl;
    }

    public void handle(ChannelHandlerContext ctx, TinyRpcRequest msg) {
        Single
                .just(msg)
                .subscribe((tinyRpcRequest, throwable) -> handleTinyRpcRequest(ctx, msg));
    }

    private void handleTinyRpcRequest(ChannelHandlerContext ctx, TinyRpcRequest msg) {
        Object response = invokeMethod(msg);

        TinyRpcResponse tinyRpcResponse = new TinyRpcResponse();
        tinyRpcResponse.setResponseValue(response);
        tinyRpcResponse.setMethodName(msg.getMethodName());

        Class<?> responseType = isNull(response) ? null : response.getClass();
        tinyRpcResponse.setResponseType(responseType);
        tinyRpcResponse.setCorrelationId(msg.getCorrelationId());

        ctx.writeAndFlush(tinyRpcResponse);
    }

    private Object invokeMethod(TinyRpcRequest tinyRpcRequest) {
        try {
            Object response = MethodUtils.invokeMethod(serviceImpl, tinyRpcRequest.getMethodName(),
                    tinyRpcRequest.getArgumentsValue());

            if (isAsyncMethod(response)) {
                return getAsyncResult((Future) response);
            } else {
                return response;
            }
        } catch (NoSuchMethodException | IllegalAccessException exception) {
            LOGGER.error(exception.getMessage(), exception);

            return new TinyRpcException(exception.getMessage(), exception);
        } catch (InvocationTargetException exception) {
            LOGGER.error(exception.getMessage(), exception);

            Throwable targetException = exception.getTargetException();
            return determineException(targetException);
        }
    }

    private boolean isAsyncMethod(Object response) {
        return response instanceof Future;
    }

    private Object getAsyncResult(Future response) {
        try {
            return response.get();
        } catch (InterruptedException exception) {
            LOGGER.error(exception.getMessage(), exception);

            return exception;
        } catch (ExecutionException exception) {
            LOGGER.error(exception.getMessage(), exception);

            Throwable cause = exception.getCause();
            return determineException(cause);
        }
    }

    private Throwable determineException(Throwable targetException) {
        if (isRuntimeException(targetException)) {
            return targetException;
        }

        if (isCheckedException(targetException)) {
            return targetException;
        }

        if (isJavaException(targetException)) {
            return targetException;
        }

        if (InterfaceAndExceptionInSameJar(serviceImpl, targetException)) {
            return targetException;
        }

        return new TinyRpcException(targetException.getMessage(), targetException);
    }

    private boolean InterfaceAndExceptionInSameJar(Object serviceImpl, Throwable targetException) {
        Class<?> interfaceClass = serviceImpl.getClass().getInterfaces()[0];
        String interfaceCodeBase = ReflectUtils.getCodeBase(interfaceClass);
        String exceptionCodeBase = ReflectUtils.getCodeBase(targetException.getClass());

        return interfaceCodeBase == null || exceptionCodeBase == null || interfaceCodeBase.equals(exceptionCodeBase);
    }

    private boolean isJavaException(Throwable targetException) {
        String className = targetException.getClass().getName();
        return className.startsWith("java.") || className.startsWith("javax.");
    }

    private boolean isCheckedException(Throwable targetException) {
        return targetException instanceof Exception && !(targetException instanceof RuntimeException);
    }

    private boolean isRuntimeException(Throwable targetException) {
        return targetException.getClass() == RuntimeException.class;
    }
}
package fr.florian.juroszek.interceptor;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.Instant;

/**
 * Interceptor which logs information about request, response and processing time
 */
@Component
public class LoggerInterceptor implements HandlerInterceptor {

    private final static Logger logger = Logger.getLogger(LoggerInterceptor.class);

    private Instant startProcessingTime;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        // Starting processing time counter
        startProcessingTime = Instant.now();
        logger.info("Pre Handle method is Calling");

        // Log request information
        if (request != null) {
            HttpServletRequest requestCacheWrapperObject = new ContentCachingRequestWrapper(request);
            logger.info("Method type : " + requestCacheWrapperObject.getMethod());
            logger.info("Request URI : " + requestCacheWrapperObject.getRequestURI());
            for (String key : requestCacheWrapperObject.getParameterMap().keySet()) {
                logger.info("Parameter : " + key + " = " + requestCacheWrapperObject.getParameter(key));
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        logger.info("Post Handle method is Calling");
    }

    @Override
    public void afterCompletion
            (HttpServletRequest request, HttpServletResponse response, Object handler,
             Exception exception) throws Exception {
        // If response is not null, log some information
        if (response != null) {
            HttpServletResponse responseCacheWrapperObject = new ContentCachingResponseWrapper(response);
            logger.info("Response status : " + responseCacheWrapperObject.getStatus());
            logger.info("[Request] " + request + ", [Response] " + response);
        }

        // Stop processing time counter and log final duration
        Instant endProcessingTime = Instant.now();
        long processingTime = Duration.between(startProcessingTime, endProcessingTime).toMillis();
        logger.info("Processing time : " + processingTime + "ms");
    }
}

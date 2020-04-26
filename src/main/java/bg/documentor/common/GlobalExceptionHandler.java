package bg.documentor.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * GlobalExceptionHandler
 */
@Slf4j @ControllerAdvice public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * Default exception handler for {@link Exception}
	 *
	 * @param ex      {@link Throwable}
	 * @param request {@link WebRequest}
	 * @return {@link ResponseEntity} with {@link HttpStatus#INTERNAL_SERVER_ERROR} and {@link ApiError}
	 */
	@ExceptionHandler(Throwable.class) public ResponseEntity<ApiError> globalExceptionHandler(Throwable ex,
			WebRequest request) {
		log.error(ex.getMessage(), ex);
		return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, List.of(ex.getLocalizedMessage()), request);
	}

	/**
	 * Default exception handler for {@link IllegalArgumentException}, {@link IllegalStateException}
	 *
	 * @param ex      {@link Exception}
	 * @param request {@link WebRequest}
	 * @return {@link ResponseEntity} with {@link HttpStatus#CONFLICT} and {@link ApiError}
	 */
	@ExceptionHandler({ IllegalArgumentException.class,
			IllegalStateException.class }) public ResponseEntity<ApiError> entityAlreadyExistHandler(Exception ex,
			WebRequest request) {
		log.error(ex.getMessage(), ex);
		return buildResponseEntity(HttpStatus.BAD_REQUEST, List.of(ex.getLocalizedMessage()), request);
	}

	/**
	 * Default ex handler for handling NOT_FOUND
	 *
	 * @param ex      {@link Exception}
	 * @param request {@link WebRequest}
	 * @return {@link ResponseEntity} with {@link HttpStatus#NOT_FOUND} and {@link ApiError}
	 */
	@ExceptionHandler({ UsernameNotFoundException.class, NoSuchElementException.class,
			EntityNotFoundException.class }) public ResponseEntity<ApiError> entityNotFoundHandler(Exception ex,
			WebRequest request) {
		log.warn(ex.getMessage(), ex);
		return buildResponseEntity(HttpStatus.NOT_FOUND, List.of(ex.getLocalizedMessage()), request);
	}

	//400
	@ExceptionHandler({
			MethodArgumentTypeMismatchException.class }) public ResponseEntity<ApiError> handleMethodArgumentTypeMismatch(
			MethodArgumentTypeMismatchException ex, WebRequest request) {
		String requiredType = Optional.ofNullable(ex.getRequiredType())
				.map(clazz -> ex.getRequiredType().getSimpleName()).orElse("");

		String error = ex.getName() + " should be of type " + requiredType;

		return buildResponseEntity(HttpStatus.BAD_REQUEST, List.of(error), request);
	}

	@ExceptionHandler({ ConstraintViolationException.class }) public ResponseEntity<ApiError> handleConstraintViolation(
			ConstraintViolationException ex, WebRequest request) {
		logger.info(ex.getClass().getName());

		List<String> errors = ex.getConstraintViolations().stream()
				.map(violation -> violation.getRootBeanClass().getName() + " " + violation
						.getPropertyPath() + ": " + violation.getMessage()).collect(toList());

		return buildResponseEntity(HttpStatus.BAD_REQUEST, errors, request);
	}

	@Override protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		logger.info(ex.getClass().getName());

		String error = ex.getValue() + " value for " + ex.getPropertyName() + " should be of type " + ex
				.getRequiredType();
		ApiError apiError = buildApplicationException(request, List.of(error));

		return handleExceptionInternal(ex, apiError, headers, HttpStatus.BAD_REQUEST, request);
	}

	@Override protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		logger.info(ex.getClass().getName());

		String error = ex.getRequestPartName() + " part is missing";
		ApiError apiError = buildApplicationException(request, List.of(error));

		return handleExceptionInternal(ex, apiError, headers, HttpStatus.BAD_REQUEST, request);
	}

	@Override protected ResponseEntity<Object> handleMissingServletRequestParameter(
			MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		logger.info(ex.getClass().getName());

		String error = ex.getParameterName() + " parameter is missing";
		ApiError apiError = buildApplicationException(request, List.of(error));

		return handleExceptionInternal(ex, apiError, headers, HttpStatus.BAD_REQUEST, request);
	}

	@Override protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		List<String> errors = extractErrorMessages(ex.getBindingResult());
		ApiError apiError = buildApplicationException(request, errors);

		return handleExceptionInternal(ex, apiError, headers, HttpStatus.BAD_REQUEST, request);
	}

	@Override protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		logger.info(ex.getClass().getName());

		List<String> errors = extractErrorMessages(ex.getBindingResult());
		ApiError apiError = buildApplicationException(request, errors);

		return handleExceptionInternal(ex, apiError, headers, HttpStatus.BAD_REQUEST, request);
	}

	// 404
	@Override protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		logger.info(ex.getClass().getName());
		String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();
		ApiError apiError = buildApplicationException(request, List.of(error));

		return handleExceptionInternal(ex, apiError, headers, HttpStatus.NOT_FOUND, request);
	}

	// 405
	@Override protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
			HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		logger.info(ex.getClass().getName());

		String str = ex.getMethod() + " method is not supported for this request. Supported methods are ";
		StringBuilder sb = new StringBuilder(str);

		Optional.ofNullable(ex.getSupportedHttpMethods())
				.ifPresent(httpMethods -> httpMethods.forEach(t -> sb.append(t).append(" ")));

		ApiError apiError = buildApplicationException(request, List.of(sb.toString()));
		return handleExceptionInternal(ex, apiError, headers, HttpStatus.METHOD_NOT_ALLOWED, request);
	}

	// 415
	@Override protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		logger.info(ex.getClass().getName());

		String str = ex.getContentType() + " media type is not supported. Supported media types are ";
		StringBuilder sb = new StringBuilder(str);

		ex.getSupportedMediaTypes().forEach(t -> sb.append(t).append(" "));

		ApiError apiError = buildApplicationException(request, List.of(sb.substring(0, sb.length() - 2)));

		return handleExceptionInternal(ex, apiError, headers, HttpStatus.UNSUPPORTED_MEDIA_TYPE, request);
	}

	/**
	 * Get field and global errors from bindingResult
	 *
	 * @param bindingResult {@link BindingResult}
	 * @return list of error messages
	 */
	private List<String> extractErrorMessages(BindingResult bindingResult) {

		List<String> errors = bindingResult.getFieldErrors().stream()
				.map(e -> e.getField() + ": " + e.getDefaultMessage()).collect(toList());

		for (ObjectError error : bindingResult.getGlobalErrors()) {
			errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
		}

		return errors;
	}

	/**
	 * Builds ResponseEntity with given {@link HttpStatus} and {@link ApiError}.
	 * See also {@link #buildApplicationException(WebRequest, List)}.
	 *
	 * @param httpStatus    {@link HttpStatus}
	 * @param errorMessages {@link Throwable}
	 * @param request       {@link WebRequest}
	 * @return {@link ResponseEntity}
	 */
	private ResponseEntity<ApiError> buildResponseEntity(HttpStatus httpStatus, List<String> errorMessages,
			WebRequest request) {
		return ResponseEntity.status(httpStatus).body(buildApplicationException(request, errorMessages));
	}

	/**
	 * Build exception details.
	 *
	 * @param request       {@link WebRequest}
	 * @param errorMessages the error messages
	 * @return the exception details
	 */
	private ApiError buildApplicationException(WebRequest request, List<String> errorMessages) {
		return ApiError.builder().timestamp(LocalDateTime.now()).errorMessages(errorMessages)
				.path(request.getDescription(false)).build();
	}
}

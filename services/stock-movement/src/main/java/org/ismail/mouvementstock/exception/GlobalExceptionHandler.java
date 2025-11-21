package org.ismail.mouvementstock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    
    @ExceptionHandler(MouvementStockNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMouvementStockNotFoundException(
            MouvementStockNotFoundException ex, WebRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Mouvement Stock Not Found",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(InvalidMouvementException.class)
    public ResponseEntity<ErrorResponse> handleInvalidMouvementException(
            InvalidMouvementException ex, WebRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Mouvement",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(StockInsuffisantException.class)
    public ResponseEntity<ErrorResponse> handleStockInsuffisantException(
            StockInsuffisantException ex, WebRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Insufficient Stock",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(ProduitServiceException.class)
    public ResponseEntity<ErrorResponse> handleProduitServiceException(
            ProduitServiceException ex, WebRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "Produit Service Error",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex, WebRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Une erreur inattendue s'est produite: " + ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


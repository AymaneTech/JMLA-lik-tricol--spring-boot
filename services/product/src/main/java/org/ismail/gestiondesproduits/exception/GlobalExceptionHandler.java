package org.ismail.gestiondesproduits.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidQuantityException.class)
    public ResponseEntity<ErrorResponse> handleInvalidQuantityException(
            InvalidQuantityException ex, WebRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Quantity",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(MouvementStockException.class)
    public ResponseEntity<ErrorResponse> handleMouvementStockException(
            MouvementStockException ex, WebRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "Mouvement Stock Service Error",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }
    
    @ExceptionHandler(ProduitSaveException.class)
    public ResponseEntity<ErrorResponse> handleProduitSaveException(
            ProduitSaveException ex, WebRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Produit Save Error",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
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

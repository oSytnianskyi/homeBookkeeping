package com.bookkeeping.resource;

import com.bookkeeping.dto.CreateTransactionDto;
import com.bookkeeping.dto.CreatedEntityIdDto;
import com.bookkeeping.dto.TransactionDto;
import com.bookkeeping.service.TransactionService;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import lombok.RequiredArgsConstructor;

import static com.bookkeeping.config.Endpoints.CATEGORIES;

@Path(CATEGORIES)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class TransactionResource {
  private final TransactionService transactionService;

  @GET
  @Path("/{categoryId}/transactions")
  public List<TransactionDto> getTransactions(@PathParam("categoryId") Long categoryId) {
    return transactionService.getTransactions(categoryId);
  }

  @POST
  @Path("/{categoryId}/transactions")
  public CreatedEntityIdDto createTransaction(@PathParam("categoryId") Long categoryId, CreateTransactionDto transactionDto) {
    return transactionService.createTransaction(categoryId, transactionDto);
  }
}
package com.bookkeeping.service.impl;

import com.bookkeeping.dto.CreateTransactionDto;
import com.bookkeeping.dto.CreatedEntityIdDto;
import com.bookkeeping.dto.TransactionDto;
import com.bookkeeping.entity.Account;
import com.bookkeeping.entity.Category;
import com.bookkeeping.entity.Transaction;
import com.bookkeeping.entity.User;
import com.bookkeeping.exception.BookkeepingResourceNotFoundException;
import com.bookkeeping.repository.AccountRepository;
import com.bookkeeping.repository.CategoryRepository;
import com.bookkeeping.repository.TransactionRepository;
import com.bookkeeping.service.InternalUserService;
import com.bookkeeping.service.TransactionService;
import com.bookkeeping.util.MapperUtil;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
  private final TransactionRepository transactionRepository;
  private final InternalUserService internalUserService;
  private final CategoryRepository categoryRepository;
  private final AccountRepository accountRepository;

  @Override
  public List<TransactionDto> getTransactions(Long categoryId) {
    Category category = getCategory(categoryId);
    List<Transaction> transactions = transactionRepository.findAllByCategoryId(categoryId);

    return transactions.stream()
      .map(transaction -> MapperUtil.TRANSACTION_MAPPER.toDto(transaction, category))
      .collect(Collectors.toList());
  }

  @Override
  public CreatedEntityIdDto createTransaction(Long accountId, Long categoryId, CreateTransactionDto transactionDto) {
    User user = internalUserService.getCurrentUser();
    Account account = getAccount(accountId);
    Category category = getCategory(categoryId);

    Transaction transaction = MapperUtil.TRANSACTION_MAPPER.toEntity(transactionDto, category, user, account);
    Transaction createdTransaction = transactionRepository.save(transaction);

    Long transactionId = createdTransaction.getId();
    return new CreatedEntityIdDto(transactionId);
  }

  private Account getAccount(Long accountId) {
    String email = internalUserService.getCurrentUserEmail();
    return accountRepository.findByIdAndUserEmail(accountId, email)
      .orElseThrow(() -> new BookkeepingResourceNotFoundException(String.format("Account by id: %s, not found", accountId)));
  }

  private Category getCategory(Long categoryId) {
    String email = internalUserService.getCurrentUserEmail();
    return categoryRepository.findByIdAndUserEmail(categoryId, email)
      .orElseThrow(() -> new BookkeepingResourceNotFoundException(String.format("Category by id: %s, not found", categoryId)));
  }
}

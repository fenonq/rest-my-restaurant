package com.spring.myrestaurant.service.impl;

import com.spring.myrestaurant.dto.ReceiptDto;
import com.spring.myrestaurant.exception.EntityNotFoundException;
import com.spring.myrestaurant.mapper.ReceiptMapper;
import com.spring.myrestaurant.model.*;
import com.spring.myrestaurant.repository.ReceiptRepository;
import com.spring.myrestaurant.repository.StatusRepository;
import com.spring.myrestaurant.repository.UserRepository;
import com.spring.myrestaurant.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReceiptServiceImpl implements ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final UserRepository userRepository;
    private final StatusRepository statusRepository;

    @Override
    public List<ReceiptDto> findAll() {
        log.info("find all receipts");
        return receiptRepository.findAll()
                .stream()
                .map(ReceiptMapper.INSTANCE::mapReceiptToReceiptDto)
                .collect(Collectors.toList());
    }

    @Override
    public ReceiptDto findById(Long id) {
        log.info("find receipt with id {}", id);
        Receipt receipt = receiptRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return ReceiptMapper.INSTANCE.mapReceiptToReceiptDto(receipt);
    }

    @Override
    public ReceiptDto save(ReceiptDto receiptDto) {
        log.info("save receipt");
        Receipt receipt = ReceiptMapper.INSTANCE.mapReceiptDtoToReceipt(receiptDto);
        receipt = receiptRepository.save(receipt);
        return ReceiptMapper.INSTANCE.mapReceiptToReceiptDto(receipt);
    }

    @Override
    @Transactional
    public ReceiptDto update(Long id, ReceiptDto receiptDto) {
        log.info("update receipt with id {}", id);
        receiptRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        Receipt receipt = ReceiptMapper.INSTANCE.mapReceiptDtoToReceipt(receiptDto);
        receipt = receiptRepository.save(receipt);
        return ReceiptMapper.INSTANCE.mapReceiptToReceiptDto(receipt);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info("delete receipt with id {}", id);
        Receipt receipt = receiptRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        receiptRepository.delete(receipt);
    }

    @Override
    @Transactional
    public ReceiptDto makeOrder(String username) {
        log.info("make an order");
        User user = userRepository.findByUsername(username);

        if (user.getCart().isEmpty()) {
            throw new EntityNotFoundException("User cart is empty!");
        }

        int totalPrice =
                user.getCart()
                        .stream()
                        .mapToInt(Dish::getPrice)
                        .sum();
        Receipt receipt =
                Receipt
                        .builder()
                        .customer(user)
                        .status(Status
                                .builder()
                                .id(1L)
                                .name("New")
                                .build())
                        .createDate(LocalDateTime.now())
                        .totalPrice(totalPrice)
                        .dishes(new ArrayList<>(user.getCart()))
                        .build();

        user.getCart().clear();
        receiptRepository.save(receipt);
        return ReceiptMapper.INSTANCE.mapReceiptToReceiptDto(receipt);
    }

    @Override
    @Transactional
    public ReceiptDto nextStatus(Long receiptId, String username) {
        log.info("change status in receipt with id {}", receiptId);

        User manager = userRepository.findByUsername(username);

        Receipt receipt = receiptRepository.findById(receiptId).orElseThrow(EntityNotFoundException::new);
        Status statusDone = statusRepository.findByName("Done");
        Status statusCanceled = statusRepository.findByName("Canceled");

        if (receipt.getManager() == null) {
            receipt.setManager(manager);
        }

        if (!receipt.getStatus().getId().equals(statusDone.getId()) &&
                !receipt.getStatus().getId().equals(statusCanceled.getId()) &&
                receipt.getManager().getId().equals(manager.getId())) {

            receipt.setStatus(statusRepository.findById(receipt.getStatus().getId() + 1)
                    .orElseThrow(EntityNotFoundException::new));

            receiptRepository.save(receipt);
        }
        return ReceiptMapper.INSTANCE.mapReceiptToReceiptDto(receipt);
    }

    @Override
    @Transactional
    public ReceiptDto cancelOrRenewReceipt(Long receiptId, String username) {
        log.info("cancel/renew receipt with id {}", receiptId);

        User manager = userRepository.findByUsername(username);

        Receipt receipt = receiptRepository.findById(receiptId).orElseThrow(EntityNotFoundException::new);
        Status accepted = statusRepository.findByName("Accepted");
        Status canceled = statusRepository.findByName("Canceled");

        if (receipt.getManager() == null) {
            receipt.setManager(manager);
        }

        if (receipt.getManager().getId().equals(manager.getId())) {
            if (!receipt.getStatus().getId().equals(canceled.getId())) {
                receipt.setStatus(canceled);
            } else {
                receipt.setStatus(accepted);
            }
        }

        receiptRepository.save(receipt);
        return ReceiptMapper.INSTANCE.mapReceiptToReceiptDto(receipt);
    }

    @Override
    public List<ReceiptDto> findByCustomerUsername(String username) {
        log.info("find all receipts by customer username {}", username);
        return receiptRepository.findByCustomerUsername(username)
                .stream()
                .map(ReceiptMapper.INSTANCE::mapReceiptToReceiptDto)
                .collect(Collectors.toList());
    }

}

package com.spring.myrestaurant.service.impl;

import com.spring.myrestaurant.dto.ReceiptDto;
import com.spring.myrestaurant.exception.EntityNotFoundException;
import com.spring.myrestaurant.mapper.DishMapper;
import com.spring.myrestaurant.mapper.UserMapper;
import com.spring.myrestaurant.model.Dish;
import com.spring.myrestaurant.model.Receipt;
import com.spring.myrestaurant.model.Status;
import com.spring.myrestaurant.model.User;
import com.spring.myrestaurant.repository.ReceiptRepository;
import com.spring.myrestaurant.repository.StatusRepository;
import com.spring.myrestaurant.repository.UserRepository;
import com.spring.myrestaurant.test.util.TestDataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReceiptServiceImplTest {

    @InjectMocks
    private ReceiptServiceImpl receiptService;

    @Mock
    private ReceiptRepository receiptRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StatusRepository statusRepository;

    @Test
    void findAllTest() {
        List<Receipt> receipts = List.of(
                TestDataUtil.createReceipt(),
                TestDataUtil.createReceipt()
        );
        when(receiptRepository.findAll()).thenReturn(receipts);

        List<ReceiptDto> returnedReceipts = receiptService.findAll();

        verify(receiptRepository).findAll();
        assertThat(returnedReceipts, hasSize(receipts.size()));
    }

    @Test
    void findByIdTest() {
        Receipt receipt = TestDataUtil.createReceipt();
        when(receiptRepository.findById(anyLong())).thenReturn(Optional.of(receipt));

        ReceiptDto returnedReceipt = receiptService.findById(anyLong());

        verify(receiptRepository).findById(anyLong());
        assertThat(returnedReceipt, allOf(
                hasProperty("id", equalTo(receipt.getId())),
                hasProperty("createDate", equalTo(receipt.getCreateDate()))
        ));
    }

    @Test
    void findByIdNotFoundTest() {
        when(receiptRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> receiptService.findById(anyLong()));
    }

    @Test
    void saveTest() {
        Receipt receipt = TestDataUtil.createReceipt();
        ReceiptDto receiptDto = TestDataUtil.createReceiptDto();
        when(receiptRepository.save(any())).thenReturn(receipt);

        ReceiptDto returnedReceipt = receiptService.save(receiptDto);

        assertThat(returnedReceipt, allOf(
                hasProperty("id", equalTo(receiptDto.getId())),
                hasProperty("customer", equalTo(receiptDto.getCustomer()))
        ));
    }

    @Test
    void updateTest() {
        Receipt receipt = TestDataUtil.createReceipt();
        ReceiptDto receiptDto = TestDataUtil.createReceiptDto();
        when(receiptRepository.findById(anyLong())).thenReturn(Optional.of(receipt));
        when(receiptRepository.save(any())).thenReturn(receipt);

        ReceiptDto returnedReceipt = receiptService.update(receipt.getId(), receiptDto);

        assertThat(returnedReceipt, allOf(
                hasProperty("id", equalTo(receiptDto.getId())),
                hasProperty("customer", equalTo(receiptDto.getCustomer()))
        ));
    }

    @Test
    void updateNotFoundTest() {
        ReceiptDto receiptDto = TestDataUtil.createReceiptDto();

        when(receiptRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> receiptService.update(receiptDto.getId(), receiptDto));
    }

    @Test
    void deleteByIdTest() {
        Receipt receipt = TestDataUtil.createReceipt();
        when(receiptRepository.findById(anyLong())).thenReturn(Optional.of(receipt));

        receiptService.deleteById(anyLong());

        verify(receiptRepository).delete(receipt);
    }

    @Test
    void deleteByIdNotFoundTest() {
        when(receiptRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> receiptService.deleteById(anyLong()));
    }

    @Test
    void makeOrderTest() {
        User customer = TestDataUtil.createUserCustomer();
        Dish dish = TestDataUtil.createDish();
        customer.getCart().add(dish);
        when(userRepository.findByUsername(anyString())).thenReturn(customer);

        ReceiptDto returnedReceipt = receiptService.makeOrder(customer.getUsername());

        verify(receiptRepository).save(any());
        assertThat(returnedReceipt, allOf(
                hasProperty("dishes", hasItem(DishMapper.INSTANCE.mapDishDto(dish))),
                hasProperty("customer", equalTo(UserMapper.INSTANCE.mapUserDto(customer)))
        ));
    }

    @Test
    void makeOrderEmptyCartTest() {
        User customer = TestDataUtil.createUserCustomer();

        when(userRepository.findByUsername(anyString())).thenReturn(customer);

        assertThrows(EntityNotFoundException.class,
                () -> receiptService.makeOrder(customer.getUsername()));
    }

    @Test
    void nextStatusTest() {
        User manager = TestDataUtil.createUserManager();
        Receipt receipt = TestDataUtil.createReceipt();
        Status nextStatus = TestDataUtil.createStatus(2L, "Accepted");
        Status statusDone = TestDataUtil.createStatus(3L, "Done");

        when(userRepository.findByUsername(anyString())).thenReturn(manager);
        when(receiptRepository.findById(anyLong())).thenReturn(Optional.of(receipt));
        when(statusRepository.findByName(anyString())).thenReturn(statusDone);
        when(statusRepository.findById(anyLong())).thenReturn(Optional.of(nextStatus));

        assertNotEquals(nextStatus, receipt.getStatus());

        receiptService.nextStatus(receipt.getId(), manager.getUsername());

        verify(receiptRepository).save(any());
        assertThat(receipt, hasProperty("status", equalTo(nextStatus)));
    }

    @Test
    void nextStatusManagerNotFoundTest() {
        Receipt receipt = TestDataUtil.createReceipt();

        when(userRepository.findByUsername(anyString())).thenReturn(null);

        assertThrows(EntityNotFoundException.class,
                () -> receiptService.nextStatus(receipt.getId(), anyString()));
    }

    @Test
    void nextStatusReceiptNotFoundTest() {
        User manager = TestDataUtil.createUserManager();

        when(userRepository.findByUsername(anyString())).thenReturn(manager);
        when(receiptRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> receiptService.nextStatus(anyLong(), manager.getUsername()));
    }


    @Test
    void nextStatusUserNotManagerTest() {
        User user = TestDataUtil.createUserCustomer();

        when(userRepository.findByUsername(anyString())).thenReturn(user);

        assertThrows(EntityNotFoundException.class,
                () -> receiptService.nextStatus(anyLong(), user.getUsername()));
    }

    @Test
    void cancelOrRenewReceiptTest() {
        String accepted = "Accepted";
        String canceled = "Canceled";

        User manager = TestDataUtil.createUserManager();
        Receipt receipt = TestDataUtil.createReceipt();
        Status statusAccepted = TestDataUtil.createStatus(2L, accepted);
        Status statusCanceled = TestDataUtil.createStatus(3L, canceled);

        when(userRepository.findByUsername(anyString())).thenReturn(manager);
        when(receiptRepository.findById(anyLong())).thenReturn(Optional.of(receipt));
        when(statusRepository.findByName(accepted)).thenReturn(statusAccepted);
        when(statusRepository.findByName(canceled)).thenReturn(statusCanceled);

        receiptService.cancelOrRenewReceipt(receipt.getId(), manager.getUsername());
        assertThat(receipt, hasProperty("status", equalTo(statusCanceled)));

        receiptService.cancelOrRenewReceipt(receipt.getId(), manager.getUsername());
        assertThat(receipt, hasProperty("status", equalTo(statusAccepted)));
    }

    @Test
    void cancelOrRenewReceiptManagerNotFoundTest() {
        Receipt receipt = TestDataUtil.createReceipt();

        when(userRepository.findByUsername(anyString())).thenReturn(null);

        assertThrows(EntityNotFoundException.class,
                () -> receiptService.cancelOrRenewReceipt(receipt.getId(), anyString()));
    }

    @Test
    void cancelOrRenewReceiptReceiptNotFoundTest() {
        User manager = TestDataUtil.createUserManager();

        when(userRepository.findByUsername(anyString())).thenReturn(manager);
        when(receiptRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> receiptService.cancelOrRenewReceipt(anyLong(), manager.getUsername()));
    }

    @Test
    void cancelOrRenewReceiptUserNotManagerTest() {
        User user = TestDataUtil.createUserCustomer();

        when(userRepository.findByUsername(anyString())).thenReturn(user);

        assertThrows(EntityNotFoundException.class,
                () -> receiptService.cancelOrRenewReceipt(anyLong(), user.getUsername()));
    }

    @Test
    void findByCustomerUsername() {
        List<Receipt> receipts = List.of(
                TestDataUtil.createReceipt(),
                TestDataUtil.createReceipt()
        );
        when(receiptRepository.findByCustomerUsername(anyString())).thenReturn(receipts);

        List<ReceiptDto> returnedReceipts =
                receiptService.findByCustomerUsername(TestDataUtil.USERNAME);

        verify(receiptRepository).findByCustomerUsername(anyString());
        assertThat(returnedReceipts, hasSize(receipts.size()));
    }

}

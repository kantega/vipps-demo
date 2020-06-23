package no.kantega.vippsdemo;

import no.kantega.vipps.TransactionStatus;
import no.kantega.vipps.dto.CallbackTransactionInfoDTO;
import no.kantega.vipps.dto.PaymentCallbackInfoDTO;
import no.kantega.vippsdemo.service.PaymentService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class VippsPaymentStatusListenerTests {

    private final PaymentService paymentService;
    private final VippsPaymentStatusListener listener;

    public VippsPaymentStatusListenerTests() {
        paymentService = mock(PaymentService.class);
        listener = new VippsPaymentStatusListener(paymentService);
    }

    @Test
    public void setPaymentStatus() {
        PaymentCallbackInfoDTO paymentStatus = mock(PaymentCallbackInfoDTO.class);
        CallbackTransactionInfoDTO transactionInfo = mock(CallbackTransactionInfoDTO.class);

        when(paymentStatus.getTransactionInfo()).thenReturn(transactionInfo);
        when(transactionInfo.getStatus()).thenReturn(TransactionStatus.RESERVED);

        listener.setPaymentStatus("some order id", paymentStatus, "some auth token");

        verify(paymentService, times(1)).updateOrderStatus("some order id",
                "RESERVED", "some auth token");
    }
}

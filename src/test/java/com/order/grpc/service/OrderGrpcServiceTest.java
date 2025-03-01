package com.order.grpc.service;

import com.order.grpc.CreateOrderRequest;
import com.order.grpc.CreateOrderResponse;
import com.order.grpc.OrderServiceGrpc;
import com.order.service.OrderService;
import io.grpc.StatusRuntimeException;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import org.junit.Rule;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class OrderGrpcServiceTest {

    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    @Mock private OrderService orderService;
    private OrderGrpcService orderGrpcService;
    private OrderServiceGrpc.OrderServiceBlockingStub blockingStub;

    @BeforeEach
    void setUp() throws Exception {
        orderGrpcService = new OrderGrpcService(null, null, null, null, orderService);

        // Create an in-process gRPC server
        String serverName = InProcessServerBuilder.generateName();
        grpcCleanup.register(
                InProcessServerBuilder.forName(serverName)
                        .directExecutor()
                        .addService(orderGrpcService)
                        .build()
                        .start()
        );

        blockingStub = OrderServiceGrpc.newBlockingStub(
                grpcCleanup.register(InProcessChannelBuilder.forName(serverName).directExecutor().build())
        );
    }

    @Test
    void createOrder_ShouldReturnOrderId_WhenOrderIsCreated() {
        when(orderService.createOrder(any(CreateOrderRequest.class))).thenReturn(1L);

        CreateOrderRequest request = CreateOrderRequest.newBuilder()
                .setGrandTotal(100.0)
                .setCurrency("USD")
                .setUserId(101L)
                .setRestaurantId(202L)
                .build();

        CreateOrderResponse response = blockingStub.createOrder(request);

        assertNotNull(response);
        assertEquals(1L, response.getOrderId());

        verify(orderService, times(1)).createOrder(any(CreateOrderRequest.class));
    }

    @Test
    void createOrder_ShouldHandleExceptions() {
        // Simulate failure in orderService
        when(orderService.createOrder(any(CreateOrderRequest.class))).thenThrow(new RuntimeException("Order creation failed"));

        // Create request
        CreateOrderRequest request = CreateOrderRequest.newBuilder()
                .setGrandTotal(100.0)
                .setCurrency("USD")
                .setUserId(101L)
                .setRestaurantId(202L)
                .build();

        // Expect exception
        StatusRuntimeException exception = assertThrows(StatusRuntimeException.class, () -> {
            blockingStub.createOrder(request);
        });

        // Verify gRPC error code
        assertEquals(io.grpc.Status.UNKNOWN.getCode(), exception.getStatus().getCode());

        // Verify orderService was called
        verify(orderService, times(1)).createOrder(any(CreateOrderRequest.class));
    }
}

package com.github.barbodh.madgridapi.registry;

import com.github.barbodh.madgridapi.registry.dao.PlayerRegistryDao;
import com.github.barbodh.madgridapi.registry.service.PlayerRegistryServiceImpl;
import com.google.api.core.ApiFutures;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlayerRegistryServiceImplTest {
    private final String playerId = "123";

    @Mock
    private Firestore firestore;
    @Mock
    private Transaction transaction;
    @Mock
    private PlayerRegistryDao playerRegistryDao;
    @InjectMocks
    private PlayerRegistryServiceImpl playerRegistryServiceImpl;

    @BeforeEach
    public void setup() {
        when(firestore.runTransaction(any())).thenAnswer(invocation -> {
            Transaction.Function<Boolean> function = invocation.getArgument(0);
            return ApiFutures.immediateFuture(function.updateCallback(transaction));
        });
    }

    @Test
    public void testUpdate() {
        playerRegistryServiceImpl.update(playerId);

        verify(playerRegistryDao).update(transaction, playerId);
    }

    @Test
    public void testExists() {
        playerRegistryServiceImpl.exists(playerId);

        verify(playerRegistryDao).exists(transaction, playerId);
    }

    @Test
    public void testDelete() {
        playerRegistryServiceImpl.delete(playerId);

        verify(playerRegistryDao).delete(transaction, playerId);
    }
}

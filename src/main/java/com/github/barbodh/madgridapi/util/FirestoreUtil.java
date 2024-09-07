package com.github.barbodh.madgridapi.util;

import com.github.barbodh.madgridapi.exception.FirestoreOperationException;
import com.github.barbodh.madgridapi.exception.FirestoreTransactionException;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Transaction;

import java.util.concurrent.ExecutionException;

public class FirestoreUtil {
    public static <T> T awaitCompletion(ApiFuture<T> future) {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException exception) {
            if (exception instanceof InterruptedException) Thread.currentThread().interrupt();
            throw new FirestoreOperationException(exception);
        }
    }

    public static <T> T runTransaction(Firestore firestore, Transaction.Function<T> function) {
        try {
            return firestore.runTransaction(function).get();
        } catch (InterruptedException | ExecutionException exception) {
            throw new FirestoreTransactionException(exception);
        }
    }
}

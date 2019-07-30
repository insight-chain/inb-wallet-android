package org.web3j.utils;

import java.math.BigInteger;

import rx.Observable;
import rx.Subscriber;

/**
 * Observable utility functions.
 */
public class Observables {

    public static Observable<BigInteger> range(
            final BigInteger startValue, final BigInteger endValue) {
        return range(startValue, endValue, true);
    }

    /**
     * Simple Observable implementation to emit a range of BigInteger values.
     *
     * @param startValue first value to emit in range
     * @param endValue final value to emit in range
     * @param ascending direction to iterate through range
     * @return Observable to emit this range of values
     */
    public static Observable<BigInteger> range(
            final BigInteger startValue, final BigInteger endValue, final boolean ascending) {
        if (startValue.compareTo(BigInteger.ZERO) == -1) {
            throw new IllegalArgumentException("Negative start index cannot be used");
        } else if (startValue.compareTo(endValue) > 0) {
            throw new IllegalArgumentException(
                    "Negative start index cannot be greater then end index");
        }

        if (ascending) {
            return Observable.create(new Observable.OnSubscribe<BigInteger>() {
                @Override
                public void call(Subscriber<? super BigInteger> subscriber) {
                    for (BigInteger i = startValue;
                         i.compareTo(endValue) < 1
                                 && !subscriber.isUnsubscribed();
                         i = i.add(BigInteger.ONE)) {
                        subscriber.onNext(i);
                    }

                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onCompleted();
                    }
                }
            });
        } else {
            return Observable.create(new Observable.OnSubscribe<BigInteger>() {
                @Override
                public void call(Subscriber<? super BigInteger> subscriber) {
                    for (BigInteger i = endValue;
                         i.compareTo(startValue) > -1
                                 && !subscriber.isUnsubscribed();
                         i = i.subtract(BigInteger.ONE)) {
                        subscriber.onNext(i);
                    }

                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onCompleted();
                    }
                }
            });
        }
    }
}

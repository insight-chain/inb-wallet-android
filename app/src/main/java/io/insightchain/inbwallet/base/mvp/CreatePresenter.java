package io.insightchain.inbwallet.base.mvp;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * create by NiPengyuan
 * time:2019/3/27
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CreatePresenter {
    Class<?>[] presenter() default {};
}

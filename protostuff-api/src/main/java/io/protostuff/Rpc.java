package io.protostuff;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marker annotation, indicates that an annotated method is a rpc service method.
 * Other annotations can be used for request/response qualifiers customization -
 * {@link Request}, {@link Response}.
 *
 * @author Kostiantyn Shchepanovskyi
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface Rpc
{

}

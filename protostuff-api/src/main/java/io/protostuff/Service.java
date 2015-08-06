package io.protostuff;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Rpc service annotation, specifies service namespace.
 *
 * https://github.com/protostuff/protostuff/wiki/Rpc-Services
 *
 * <p>
 * Consider this service definition:
 * <pre>
 *     package foo;
 *     service Bar {
 *         rpc DoWork(Request) returns(Response);
 *     }
 * </pre>
 * <p>
 * By default, service namespace is formed as a package + '.' + service name.
 * In the example above, service namespace is "foo.Bar".
 * <p>
 * Request and response qualifiers are generated using rpc method name.
 * Request qualifier is formed as service namespace + '/' + method name + 'Request'.
 * Response qualifier is formed as service namespace + '/' + method name + 'Response'.
 * In the example above, request/response qualifiers are "foo.Bar/DoWorkRequest" and
 * "foo.Bar/DoWorkResponse".
 *
 * @author Kostiantyn Shchepanovskyi
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface Service
{

    /**
     * Service namespace.
     */
    String value();
}

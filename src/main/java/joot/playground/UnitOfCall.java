package joot.playground;

/**
 *
 * @author witoldsz
 */
@FunctionalInterface
public interface UnitOfCall<T> {

    T run() throws Exception;
}

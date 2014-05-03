package joot.playground;

/**
 *
 * @author witoldsz
 */
@FunctionalInterface
public interface UnitOfWork {

    void run() throws Exception;
}

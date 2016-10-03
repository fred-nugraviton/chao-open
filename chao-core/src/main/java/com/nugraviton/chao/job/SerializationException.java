package com.nugraviton.chao.job;

/**
 * <p>Exception thrown when the Serialization process fails.</p>
 *
 * <p>The original error is wrapped within this one.</p>
 *
 * <p>#NotThreadSafe# because Throwable is not thread-safe</p>
 * @since 1.0
 * @version $Id: SerializationException.java 1436768 2013-01-22 07:07:42Z ggregory $
 */
public class SerializationException extends RuntimeException {

    /**
     * Required for serialization support.
     * 
     * @see java.io.Serializable
     */
    private static final long serialVersionUID = 4029025366392702726L;

    /**
     * <p>Constructs a new {@code SerializationException} without specified
     * detail message.</p>
     */
    public SerializationException() {
        super();
    }

    /**
     * <p>Constructs a new {@code SerializationException} with specified
     * detail message.</p>
     *
     * @param msg  The error message.
     */
    public SerializationException(final String msg) {
        super(msg);
    }

    /**
     * <p>Constructs a new {@code SerializationException} with specified
     * nested {@code Throwable}.</p>
     *
     * @param cause  The {@code Exception} or {@code Error}
     *  that caused this exception to be thrown.
     */
    public SerializationException(final Throwable cause) {
        super(cause);
    }

    /**
     * <p>Constructs a new {@code SerializationException} with specified
     * detail message and nested {@code Throwable}.</p>
     *
     * @param msg    The error message.
     * @param cause  The {@code Exception} or {@code Error}
     *  that caused this exception to be thrown.
     */
    public SerializationException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

}

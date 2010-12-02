package com.akiban.cserver.api;

import com.akiban.cserver.InvalidOperationException;

/**
 * Wrapper for an InvalidOperationException. The sole purpose of this exception type is so that if you want to
 * explicitly list a few InvalidOperationException subclasses in your "throws" clause, but you also have to account for
 * a generic InvalidOperationException (for legacy reasons), you can use this instead of the plain
 * InvalidOperationException. Otherwise, that InvalidOperationException throws declaration makes the subclasses
 * redundant. 
 */
public final class GenericInvalidOperationException extends InvalidOperationException {
    private final InvalidOperationException cause;

    GenericInvalidOperationException(InvalidOperationException e) {
        super(e.getCode(), e.getMessage(), e);
        this.cause = e;
    }

    GenericInvalidOperationException(Throwable t) {
        super(t);
        this.cause = (t instanceof InvalidOperationException)
                ? (InvalidOperationException) t
                : new InvalidOperationException(t);
    }

    @Override
    public InvalidOperationException getCause() {
        return cause;
    }

    
}

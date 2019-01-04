package io.protostuff;

/**
 * A builder that can build a {@link Message message}.
 *
 * @author nethibernate
 * @created Jan 4, 2019
 **/
public interface Builder<T extends Message> {
	
	T build();
}

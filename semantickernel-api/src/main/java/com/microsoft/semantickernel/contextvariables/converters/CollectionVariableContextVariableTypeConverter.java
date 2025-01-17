// Copyright (c) Microsoft. All rights reserved.
package com.microsoft.semantickernel.contextvariables.converters;

import static com.microsoft.semantickernel.contextvariables.ContextVariableTypes.convert;

import com.microsoft.semantickernel.contextvariables.ContextVariable;
import com.microsoft.semantickernel.contextvariables.ContextVariableType;
import com.microsoft.semantickernel.contextvariables.ContextVariableTypeConverter;
import com.microsoft.semantickernel.contextvariables.ContextVariableTypes;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * A {@link ContextVariableTypeConverter} for {@code java.util.Collection} variables. Use
 * {@code ContextVariableTypes.getGlobalVariableTypeForClass(String.class)} to get an instance of
 * this class.
 * @see ContextVariableTypes#getGlobalVariableTypeForClass(Class)
 */
public class CollectionVariableContextVariableTypeConverter extends
    ContextVariableTypeConverter<Collection> {

    /**
     * Creates a new instance of the {@link CollectionVariableContextVariableTypeConverter} class.
     * @param delimiter The delimiter to use joining elements of the collection.
     */
    @SuppressFBWarnings("CT_CONSTRUCTOR_THROW")
    public CollectionVariableContextVariableTypeConverter(String delimiter) {
        super(
            Collection.class,
            s -> convert(s, Collection.class),
            getString(delimiter),
            s -> {
                throw new UnsupportedOperationException();
            });
    }

    /**
     * Creates a new instance of the {@link CollectionVariableContextVariableTypeConverter} class.
     */
    @SuppressFBWarnings("CT_CONSTRUCTOR_THROW")
    public CollectionVariableContextVariableTypeConverter() {
        this(",");
    }

    /**
     * Gets a function that converts a collection to a string.
     * @param delimiter The delimiter to use joining elements of the collection.
     * @return  A function that converts a collection to a string.
     */
    @SuppressWarnings("NullAway")
    public static ToPromptStringFunction<Collection> getString(String delimiter) {
        return (types, collection) -> {
            String formatted = (String) collection
                .stream()
                .map(t -> getString(types, t))
                .collect(Collectors.joining(delimiter));

            return escapeXmlString(formatted);
        };
    }

    private static String getString(ContextVariableTypes types, Object t) {
        if (t instanceof ContextVariable) {
            return ((ContextVariable<?>) t).toPromptString(types);
        } else {
            ContextVariableType converter = types.getVariableTypeForClass(
                t.getClass());

            if (converter != null) {
                return converter.getConverter().toPromptString(types, t);
            }
            return t.toString();
        }
    }
}

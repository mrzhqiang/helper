package com.github.mrzhqiang.helper.data.cassandra;

import com.github.mrzhqiang.helper.data.DataAccessException;
import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import java.util.*;

final class FindByIdQuery {

    @Nullable
    private final String idProperty;
    private final List<Object> idCollection;

    private FindByIdQuery(@Nullable String idProperty, List<Object> idCollection) {
        this.idProperty = idProperty;
        this.idCollection = idCollection;
    }

    static FindByIdQuery forIds(Iterable<?> ids) {
        Preconditions.checkArgument(Objects.nonNull(ids), "The given Iterable of ids must not be null");

        List<Object> idCollection = new ArrayList<>();
        String idField = null;

        for (Object id : ids) {
            if (id instanceof MapId) {
                MapId mapId = (MapId) id;
                Iterator<String> iterator = mapId.keySet().iterator();

                if (mapId.size() > 1) {
                    throw new DataAccessException("MapId with multiple keys are not supported");
                }

                if (!iterator.hasNext()) {
                    throw new DataAccessException("MapId is empty");
                } else {
                    idField = iterator.next();
                    idCollection.add(mapId.get(idField));
                }
            } else {
                idCollection.add(id);
            }
        }

        return new FindByIdQuery(idField, idCollection);
    }

    static boolean hasCompositeKeys(Iterable<?> ids) {
        Preconditions.checkArgument(Objects.nonNull(ids),  "The given Iterable of ids must not be null");

        for (Object id : ids) {
            if (id instanceof MapId) {
                MapId mapId = (MapId) id;
                if (mapId.size() > 1) {
                    return true;
                }
            }
        }

        return false;
    }

    @Nullable
    String getIdProperty() {
        return idProperty;
    }

    List<Object> getIdCollection() {
        return idCollection;
    }
}

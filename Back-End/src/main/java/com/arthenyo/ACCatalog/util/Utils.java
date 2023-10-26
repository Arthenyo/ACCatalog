package com.arthenyo.ACCatalog.util;

import com.arthenyo.ACCatalog.entities.Product;
import com.arthenyo.ACCatalog.projections.IdProjection;
import com.arthenyo.ACCatalog.projections.ProductProjection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

    public static <ID> List<? extends IdProjection<ID>> replace(List<? extends IdProjection<ID>> ordered, List<? extends IdProjection<ID>> unordered) {

        Map<ID, IdProjection<ID>> map = new HashMap<>();
        for(IdProjection<ID> obj : unordered){
            map.put(obj.getId(), obj);
        }
        List<IdProjection<ID>> result = new ArrayList<>();
        for(IdProjection<ID> obj : ordered){
            result.add(map.get(obj.getId()));
        }

        return result;
    }
}

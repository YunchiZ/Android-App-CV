package com.team13.whatsmissing.data.repository;

import java.util.List;

public interface AssociationsRepository {

    List<String> getAssociations(String word);

}

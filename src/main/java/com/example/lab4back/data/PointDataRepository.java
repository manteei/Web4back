package com.example.lab4back.data;

import com.example.lab4back.beans.PointData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface PointDataRepository extends CrudRepository<PointData, Long> {
    List<PointData> findAllByUsername(String username);
    @Transactional
    void deleteByUsername(String username);
}

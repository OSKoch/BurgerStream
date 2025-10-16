package com.burgerstream.backend.repository.menu;

import com.burgerstream.backend.model.menu.Side;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SideRepository extends JpaRepository<Side, Long> {

    List<Side> findByIsShareableTrue();
}
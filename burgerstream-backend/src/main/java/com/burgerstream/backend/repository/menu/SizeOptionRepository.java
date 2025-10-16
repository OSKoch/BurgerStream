package com.burgerstream.backend.repository.menu;

import com.burgerstream.backend.model.menu.SizeOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SizeOptionRepository  extends JpaRepository<SizeOption, Long> {
}
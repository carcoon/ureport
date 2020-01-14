package com.bstek.ureport.report.repository;

import com.bstek.ureport.domain.UreportFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UreportFileRepo extends JpaRepository<UreportFile,Long> {
    UreportFile findTopByName(String name);
    Page<UreportFile> findAllByNameLikeOrTitleLike(String name, String title, Pageable pageable);
}

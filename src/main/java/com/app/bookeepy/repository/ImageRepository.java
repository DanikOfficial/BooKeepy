package com.app.bookeepy.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.bookeepy.entity.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

	@Query(value = "DELETE FROM images WHERE id IN (:ids)", nativeQuery = true)
	@Transactional
	@Modifying
	public void deleteSome(@Param("ids") Integer[] ids);

	@Query(value = "DELETE FROM images WHERE book_id = :id", nativeQuery = true)
	@Transactional
	@Modifying
	public void deleteImages(@Param("id") Long id);
}

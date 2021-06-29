package com.devsuperior.movieflix.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.projections.MovieByGenreProjection;

public interface MovieRepository extends JpaRepository<Movie, Long>{

	@Query("SELECT obj "
			+ "FROM Movie obj "
			+ "WHERE "
			+ "(COALESCE(:genre) IS NULL OR obj.genre = :genre) "
			+ "GROUP BY obj.title")
	Page<MovieByGenreProjection> findByGenre(Genre genre, Pageable pageable);
}
